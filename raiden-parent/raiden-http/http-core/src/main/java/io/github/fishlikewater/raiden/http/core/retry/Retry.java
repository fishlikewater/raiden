package io.github.fishlikewater.raiden.http.core.retry;

import io.github.fishlikewater.raiden.http.core.RequestWrap;

import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

/**
 * {@code Retry}
 * 重试处理器
 *
 * @author zhangxiang
 * @version 1.0.8
 * @since 2024/11/13
 */
public interface Retry {

    /**
     * 重试
     *
     * @param response    响应
     * @param requestWrap 请求
     * @param ex          异常
     * @param <T>         实际需要的类型
     * @return 响应
     */
    <T> T retrySync(HttpResponse<T> response, RequestWrap requestWrap, Throwable ex);

    <T> CompletableFuture<T> retryAsync(HttpResponse<T> response, RequestWrap requestWrap, Throwable ex);
}
