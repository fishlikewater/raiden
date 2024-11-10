package io.github.fishlikewater.raiden.http.core.processor;

import io.github.fishlikewater.raiden.http.core.exception.RaidenHttpException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * DefaultExceptionProcessor
 *
 * @author zhangxiang
 * @version 1.0.8
 * @since 2024/11/10
 **/
@Slf4j
public class DefaultExceptionProcessor implements ExceptionProcessor {

    @Override
    public <T> void invalidRespHandle(HttpRequest request, HttpResponse<T> response) {
        log.error("request failed, response status code: {}", response.statusCode());
    }

    @Override
    public void ioExceptionHandle(HttpRequest request, IOException cause) {
        log.error("request failed, request address url: {}", request.uri(), cause);
        throw new RaidenHttpException("request failed, request address url: {}", request.uri(), cause);
    }

    @Override
    public void otherExceptionHandle(HttpRequest request, Throwable cause) {
        log.error("request failed, request address url: {}", request.uri(), cause);
        throw new RaidenHttpException("request failed, request address url: {}", request.uri(), cause);
    }
}