/*
 * Copyright (c) 2025 zhangxiang (fishlikewater@126.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.fishlikewater.raiden.core.future;

import io.github.fishlikewater.raiden.core.LambdaUtils;
import io.github.fishlikewater.raiden.core.ObjectUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.function.Function;

/**
 * {@code RobustFuture}
 * 一个优雅且健壮的 Future 实现，灵感来源于 Netty Future
 *
 * @author zhangxiang
 * @since 2025/8/5
 */
@Slf4j
@SuppressWarnings("all")
public class RobustFuture<V> implements Future<V> {

    // ------------------------------------------------------------------------ 状态常量

    private static final int STATE_UNCOMPLETED = 0;

    private static final int STATE_SUCCESS = 1;

    private static final int STATE_FAILURE = 2;

    private static final int STATE_CANCELLED = 3;

    // ------------------------------------------------------------------------ 原子更新器 (避免锁，提升性能)

    private static final AtomicIntegerFieldUpdater<RobustFuture> STATE_UPDATER = AtomicIntegerFieldUpdater.newUpdater(RobustFuture.class, "state");

    private static final AtomicReferenceFieldUpdater<RobustFuture, Object> RESULT_UPDATER = AtomicReferenceFieldUpdater.newUpdater(RobustFuture.class, Object.class, "result");

    private static final AtomicReferenceFieldUpdater<RobustFuture, Throwable> CAUSE_UPDATER = AtomicReferenceFieldUpdater.newUpdater(RobustFuture.class, Throwable.class, "cause");

    private static final AtomicReferenceFieldUpdater<RobustFuture, ConcurrentLinkedQueue> LISTENER_UPDATER = AtomicReferenceFieldUpdater.newUpdater(RobustFuture.class, ConcurrentLinkedQueue.class, "listeners");

    // ------------------------------------------------------------------------  成员变量

    private volatile int state = STATE_UNCOMPLETED;

    /**
     * 存储成功结果
     */
    private volatile V result;

    /**
     * 存储失败原因
     */
    private volatile Throwable cause;

    /**
     * 存储 FutureListener 队列 (用于异步通知)
     */
    private volatile ConcurrentLinkedQueue<FutureListener<V>> listeners;

    /**
     * 用于 await() 阻塞
     */
    private final CountDownLatch latch = new CountDownLatch(1);

    /**
     * 取消状态 (更明确)
     */
    private final AtomicBoolean cancelled = new AtomicBoolean(false);

    /**
     * 线程池
     */
    private ExecutorService executor;

    // ------------------------------------------------------------------------ 构造函数

    public RobustFuture() {
        // 默认构造函数
    }

    public RobustFuture(ExecutorService executor) {
        this.executor = executor;
    }

    // ------------------------------------------------------------------------ 接口实现

    /**
     * 尝试取消任务的执行。
     * 如果任务已完成、已取消或由于其他原因无法取消，则此尝试将失败。
     * 如果成功，并且在调用 cancel 时任务尚未启动，则任务不应运行。
     * 如果任务已经开始，mayInterruptIfRunning 参数确定执行该任务的线程是否应被中断。
     *
     * @param mayInterruptIfRunning 如果任务已经开始，是否应中断执行该任务的线程
     * @return 如果此任务无法取消，通常是因为它已经正常完成；否则返回 true
     */
    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        if (isDone() || !cancelled.compareAndSet(false, true)) {
            return false;
        }
        if (STATE_UPDATER.compareAndSet(this, STATE_UNCOMPLETED, STATE_CANCELLED)) {
            return true;
        }
        // 竞态条件：可能在设置 cancelled 后，状态已被其他线程改变
        return false;
    }

    /**
     * 如果此任务在正常完成前被取消，则返回 true。
     *
     * @return 如果此任务在正常完成前被取消，则返回 true
     */
    @Override
    public boolean isCancelled() {
        return state == STATE_CANCELLED;
    }

    /**
     * 如果此任务已完成，则返回 true。
     * 完成可能是由于正常终止、异常或取消。
     *
     * @return 如果此任务已完成，则返回 true
     */
    @Override
    public boolean isDone() {
        return state != STATE_UNCOMPLETED;
    }

    /**
     * 获取此 Future 的结果，如果需要，等待直到计算完成，最多等待指定的时间。
     *
     * @param timeout 最长等待时间
     * @param unit    timeout 参数的时间单位
     * @return 计算的结果
     * @throws InterruptedException 如果等待时被中断
     * @throws ExecutionException   如果计算抛出异常
     * @throws TimeoutException     如果等待时间超过指定的超时时间
     */
    @Override
    public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        if (!latch.await(timeout, unit)) {
            throw new TimeoutException("Future get timed out after " + timeout + " " + unit);
        }
        return getNow(); // latch.await 成功，结果已就绪
    }

    /**
     * 获取此 Future 的结果，如果需要，等待计算完成。
     *
     * @return 计算的结果
     * @throws InterruptedException 如果等待时被中断
     * @throws ExecutionException   如果计算抛出异常
     */
    @Override
    public V get() throws InterruptedException, ExecutionException {
        latch.await();
        return getNow();
    }

    // ------------------------------------------------------------------------ 核心功能方法 (类似 Netty Future)

    /**
     * 设置此 Future 的成功结果。
     * 如果 Future 已经完成 (成功、失败或取消)，则此操作将失败。
     *
     * @param result 成功结果
     * @return 如果设置成功则返回 true，如果 Future 已完成则返回 false
     */
    public boolean setSuccess(V result) {
        if (STATE_UPDATER.compareAndSet(this, STATE_UNCOMPLETED, STATE_SUCCESS)) {
            RESULT_UPDATER.set(this, result);
            latch.countDown();
            notifyListeners();
            return true;
        }
        return false;
    }

    /**
     * 设置此 Future 的失败原因。
     * 如果 Future 已经完成 (成功、失败或取消)，则此操作将失败。
     *
     * @param cause 失败的异常
     * @return 如果设置成功则返回 true，如果 Future 已完成则返回 false
     */
    public boolean setFailure(Throwable cause) {
        if (STATE_UPDATER.compareAndSet(this, STATE_UNCOMPLETED, STATE_FAILURE)) {
            CAUSE_UPDATER.set(this, cause);
            latch.countDown();
            notifyListeners();
            return true;
        }
        return false;
    }

    /**
     * 添加一个监听器，当 Future 完成时 (无论成功、失败或取消) 异步调用。
     * 如果 Future 已经完成，监听器将被立即在一个新的线程中调用 (模拟异步)。
     * 监听器保证只会被调用一次。
     *
     * @param listener 要添加的监听器
     * @return this，支持链式调用
     */
    public RobustFuture<V> addListener(FutureListener<V> listener) {
        if (listener == null) {
            throw new NullPointerException("listener");
        }

        boolean notifyNow = isDone();
        if (!notifyNow) {
            // Future 尚未完成，添加到监听器队列
            if (LISTENER_UPDATER.compareAndSet(this, null, new ConcurrentLinkedQueue<>())) {
                // 成功初始化 listeners 队列
            }
            listeners.add(listener);
        }

        // 如果 Future 已完成，立即异步通知监听器
        if (notifyNow) {
            notifyListenerNow(listener);
        }
        return this;
    }

    /**
     * 移除一个监听器。
     * 注意：如果监听器正在被调用或即将被调用，此操作不能保证监听器不会被调用。
     *
     * @param listener 要移除的监听器
     * @return 如果监听器存在于队列中并被成功移除则返回 true
     */
    public boolean removeListener(FutureListener<V> listener) {
        if (listener == null || listeners == null) {
            return false;
        }
        return listeners.remove(listener);
    }

    /**
     * 等待此 Future 完成，最多等待指定的时间。
     *
     * @param timeout 最长等待时间
     * @param unit    timeout 参数的时间单位
     * @return 如果在超时前完成则返回 true，否则返回 false
     * @throws InterruptedException 如果等待时被中断
     */
    public boolean await(long timeout, TimeUnit unit) throws InterruptedException {
        return latch.await(timeout, unit);
    }

    /**
     * 等待此 Future 完成，不响应中断。
     * 注意：这可能会导致调用线程永久阻塞，如果 Future 永远不会完成。
     * 通常建议使用 awaitUninterruptibly(long, TimeUnit)。
     */
    public void awaitUninterruptibly() {
        boolean interrupted = false;
        while (!isDone()) {
            try {
                latch.await();
                break;
            } catch (InterruptedException e) {
                // 继续循环，等待完成
                interrupted = true;
            }
        }
        if (interrupted) {
            // 重新设置中断状态
            Thread.currentThread().interrupt();
        }
    }

    /**
     * 等待此 Future 完成，最多等待指定的时间，不响应中断。
     *
     * @param timeout 最长等待时间
     * @param unit    timeout 参数的时间单位
     * @return 如果在超时前完成则返回 true，否则返回 false
     */
    public boolean awaitUninterruptibly(long timeout, TimeUnit unit) {
        long startTime = System.nanoTime();
        long remainingNanos = unit.toNanos(timeout);
        boolean interrupted = false;

        try {
            while (!isDone() && remainingNanos > 0) {
                try {
                    return latch.await(remainingNanos, TimeUnit.NANOSECONDS);
                } catch (InterruptedException e) {
                    interrupted = true;
                    // 重新计算剩余时间
                    remainingNanos = unit.toNanos(timeout) - (System.nanoTime() - startTime);
                }
            }
            // 循环退出时检查状态
            return isDone();
        } finally {
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * 获取此 Future 的结果，如果可用，否则返回 null。
     * 不会阻塞。
     *
     * @return 结果，如果 Future 成功完成；如果失败则抛出异常；如果未完成或已取消则返回 null
     */
    @SuppressWarnings("unchecked")
    public V getNow() {
        switch (state) {
            case STATE_SUCCESS:
                return (V) result;
            case STATE_FAILURE:
                throw new RuntimeException(cause);
            case STATE_CANCELLED:
            case STATE_UNCOMPLETED:
            default:
                return null;
        }
    }

    /**
     * 判断此 Future 是否成功完成。
     *
     * @return 如果 Future 成功完成则返回 true
     */
    public boolean isSuccess() {
        return state == STATE_SUCCESS;
    }

    /**
     * 获取此 Future 失败时的异常原因。
     * 如果 Future 未失败，则返回 null。
     *
     * @return 失败原因，如果 Future 失败；否则返回 null
     */
    public Throwable cause() {
        if (state == STATE_FAILURE) {
            return cause;
        }
        return null;
    }

    // ---------------------------------------------------------------- 链式操作 (类似 Netty 的 Promise.thenXXX)

    /**
     * 创建一个新的 Future，当此 Future 成功完成时，使用此 Future 的结果作为输入执行给定的函数。
     * 如果此 Future 失败或被取消，新 Future 将以相同的失败或取消状态完成。
     *
     * @param fn  转换函数
     * @param <U> 新 Future 的结果类型
     * @return 新的 Future
     */
    public <U> RobustFuture<U> thenApply(Function<? super V, ? extends U> fn) {
        RobustFuture<U> newFuture = new RobustFuture<>();
        this.addListener(future -> {
            try {
                if (future.isSuccess()) {
                    U result = fn.apply(future.getNow());
                    newFuture.setSuccess(result);
                } else if (future.cause() != null) {
                    newFuture.setFailure(future.cause());
                } else if (future.isCancelled()) {
                    newFuture.cancel(false);
                }
            } catch (Throwable t) {
                newFuture.setFailure(t);
            }
        });
        return newFuture;
    }

    /**
     * 创建一个新的 Future，当此 Future 完成时 (无论成功、失败或取消)，执行给定的 Runnable。
     * 新 Future 的结果类型为 Void。Runnable 的执行结果不影响新 Future 的状态 (除非抛出异常)。
     *
     * @param action 要执行的操作
     * @return 新的 Future<Void>
     */
    public RobustFuture<Void> thenRun(Runnable action) {
        RobustFuture<Void> newFuture = new RobustFuture<>();
        this.addListener(future -> {
            try {
                action.run();
                // 根据原 Future 的状态设置新 Future 的状态
                if (future.isSuccess()) {
                    newFuture.setSuccess(null);
                } else if (future.cause() != null) {
                    newFuture.setFailure(future.cause());
                } else if (future.isCancelled()) {
                    newFuture.cancel(false);
                }
            } catch (Throwable t) {
                newFuture.setFailure(t);
            }
        });
        return newFuture;
    }

    /**
     * 创建一个新的 Future，当此 Future 成功完成时，执行给定的函数，该函数返回另一个 Future。
     * 新 Future 的完成状态由返回的 Future 决定。
     *
     * @param fn  返回 Future 的函数
     * @param <U> 新 Future 的结果类型
     * @return 新的 Future
     */
    public <U> RobustFuture<U> thenCompose(Function<? super V, ? extends RobustFuture<U>> fn) {
        RobustFuture<U> newFuture = new RobustFuture<>();
        this.addListener(future -> {
            if (future.isSuccess()) {
                try {
                    RobustFuture<U> nextFuture = fn.apply(future.getNow());
                    // 监听返回的 Future，将其状态传递给 newFuture
                    nextFuture.addListener(f -> {
                        if (f.isSuccess()) {
                            newFuture.setSuccess(f.get());
                        } else if (f.cause() != null) {
                            newFuture.setFailure(f.cause());
                        } else if (f.isCancelled()) {
                            newFuture.cancel(false);
                        }
                    });
                } catch (Exception e) {
                    newFuture.setFailure(e);
                }
            } else if (future.cause() != null) {
                newFuture.setFailure(future.cause());
            } else if (future.isCancelled()) {
                newFuture.cancel(false);
            }
        });
        return newFuture;
    }

    // ---------------------------------------------------------------- 私有方法

    /**
     * 通知所有注册的监听器。在 Future 完成时调用。
     */
    private void notifyListeners() {
        ConcurrentLinkedQueue<FutureListener<V>> listeners = this.listeners;
        if (listeners == null) {
            return;
        }

        // 将监听器队列置为 null，防止后续添加的监听器在此轮通知中被调用
        if (LISTENER_UPDATER.compareAndSet(this, listeners, null)) {
            LambdaUtils.handle(listeners, listener -> executor(listener));
        }
    }

    /**
     * 立即异步通知单个监听器。
     * 在一个独立的线程中执行。
     *
     * @param listener 要通知的监听器
     */
    private void notifyListenerNow(FutureListener<V> listener) {
        if (ObjectUtils.isNullOrEmpty(this.executor)) {
            CompletableFuture.runAsync(() -> {executor(listener);});
        } else {
            CompletableFuture.runAsync(() -> {executor(listener);}, this.executor);
        }
    }

    /**
     * 执行监听器。
     *
     * @param listener 要执行的监听器
     */
    private void executor(FutureListener<V> listener) {
        try {
            listener.operationComplete(this);
        } catch (Throwable t) {
            // 防止监听器内部异常影响其他监听器
            // 可以考虑记录日志
            log.error("Listener failed", t);
        }
    }
}
