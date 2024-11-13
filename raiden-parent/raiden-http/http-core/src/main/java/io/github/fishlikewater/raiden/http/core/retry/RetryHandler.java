package io.github.fishlikewater.raiden.http.core.retry;

import io.github.fishlikewater.raiden.core.ObjectUtils;
import io.github.fishlikewater.raiden.http.core.HttpBootStrap;
import io.github.fishlikewater.raiden.http.core.RequestWrap;
import lombok.extern.slf4j.Slf4j;

import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

/**
 * {@code RetryHandler}
 *
 * @author zhangxiang
 * @since 2024/11/11
 */
@Slf4j
public class RetryHandler implements Retry {

    @Override

    public <T> T retrySync(HttpResponse<T> response, RequestWrap requestWrap, Throwable ex) {
        if (ObjectUtils.isNullOrEmpty(response) && ObjectUtils.isNotNullOrEmpty(ex)) {
            if (HttpBootStrap.getConfig().getMaxRetryCount() > 0 && requestWrap.getRetryCount() > 0) {
                return toRetrySync(requestWrap);
            }
            return null;
        }
        if (response.statusCode() >= 200 && response.statusCode() < 300) {
            return response.body();
        }
        if (HttpBootStrap.getConfig().getMaxRetryCount() > 0 && requestWrap.getRetryCount() > 0) {
            return toRetrySync(requestWrap);
        }
        return null;
    }

    @Override
    public <T> CompletableFuture<T> retryAsync(HttpResponse<T> response, RequestWrap requestWrap, Throwable ex) {
        if (ObjectUtils.isNullOrEmpty(response) && ObjectUtils.isNotNullOrEmpty(ex)) {
            if (HttpBootStrap.getConfig().getMaxRetryCount() > 0 && requestWrap.getRetryCount() > 0) {
                return toRetryAsync(requestWrap);
            }
            return null;
        }
        if (response.statusCode() >= 200 && response.statusCode() < 300) {
            return CompletableFuture.completedFuture(response.body());
        }
        if (HttpBootStrap.getConfig().getMaxRetryCount() > 0 && requestWrap.getRetryCount() > 0) {
            return toRetryAsync(requestWrap);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private <T> T toRetrySync(RequestWrap requestWrap) {
        requestWrap.setRetryCount(requestWrap.getRetryCount() - 1);
        log.info("begin sync retry, count:[{}] ", HttpBootStrap.getConfig().getMaxRetryCount() - requestWrap.getRetryCount());
        return (T) HttpBootStrap.getConfig().getHttpClientProcessor().handler(requestWrap);
    }

    @SuppressWarnings("unchecked")
    private <T> CompletableFuture<T> toRetryAsync(RequestWrap requestWrap) {
        requestWrap.setRetryCount(requestWrap.getRetryCount() - 1);
        log.info("begin async retry, count:[{}] ", HttpBootStrap.getConfig().getMaxRetryCount() - requestWrap.getRetryCount());
        return (CompletableFuture<T>) HttpBootStrap.getConfig().getHttpClientProcessor().handler(requestWrap);
    }
}
