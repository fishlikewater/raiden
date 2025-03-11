package io.github.fishlikewater.spring.boot.raiden.core;


import io.github.fishlikewater.raiden.core.ObjectUtils;
import io.github.fishlikewater.raiden.core.exception.RaidenExceptionCheck;
import io.github.fishlikewater.raiden.core.handler.Pipeline;
import io.github.fishlikewater.raiden.core.handler.PipelineContext;
import io.github.fishlikewater.raiden.core.handler.PipelineHandler;

import java.util.function.Function;

/**
 * {@code PipelineChainHelper}
 * 处理链构建器
 *
 * @author zhangxiang
 * @since 1.1.4
 */
public class PipelineChainHelper {

    public static <T> PipelineChain<T> newChain() {
        return new PipelineChain<>();
    }


    public static class PipelineChain<T> {

        private final Pipeline<T> pipeline;

        public PipelineChain() {
            this.pipeline = new Pipeline<>();
        }

        public PipelineChain<T> addHandler(Class<? extends PipelineHandler<T>> clazz) {
            PipelineHandler<T> handler = SpringUtils.getBean(clazz);
            if (ObjectUtils.isNullOrEmpty(handler)) {
                RaidenExceptionCheck.INSTANCE.throwUnchecked("this bean not exist!!!, className: {}", clazz.getSimpleName());
            }
            this.pipeline.addHandler(handler);
            return this;
        }

        public PipelineChain<T> addFirstHandler(Class<? extends PipelineHandler<T>> clazz) {
            PipelineHandler<T> handler = SpringUtils.getBean(clazz);
            if (ObjectUtils.isNullOrEmpty(handler)) {
                RaidenExceptionCheck.INSTANCE.throwUnchecked("this bean not exist!!!, className: {}", clazz.getSimpleName());
            }
            this.pipeline.addFirstHandler(handler);
            return this;
        }

        public PipelineChain<T> addLastHandler(Class<? extends PipelineHandler<T>> clazz) {
            PipelineHandler<T> handler = SpringUtils.getBean(clazz);
            if (ObjectUtils.isNullOrEmpty(handler)) {
                RaidenExceptionCheck.INSTANCE.throwUnchecked("this bean not exist!!!, className: {}", clazz.getSimpleName());
            }
            this.pipeline.addLastHandler(handler);
            return this;
        }

        public void start(T t) {
            this.pipeline.start(t);
        }

        public <K> K start(T t, Function<PipelineContext, K> function) {
            return this.pipeline.start(t, function);
        }
    }
}
