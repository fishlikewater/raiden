package io.github.fishlikewater.raiden.http.core.processor;

import io.github.fishlikewater.raiden.http.core.RequestWrap;
import io.github.fishlikewater.raiden.http.core.exception.RaidenHttpException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
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
    public <T> void invalidRespHandle(RequestWrap requestWrap, HttpResponse<T> response) {
        log.error("request failed, response status code: {}", response.statusCode());
    }

    @Override
    public void ioExceptionHandle(RequestWrap requestWrap, IOException cause) {
        log.error("request failed, request address url: {}", requestWrap.getHttpRequest().uri(), cause);
        throw new RaidenHttpException("request failed, request address url: {}", requestWrap.getHttpRequest().uri(), cause);
    }

    @Override
    public void otherExceptionHandle(RequestWrap requestWrap, Throwable cause) {
        log.error("request failed, request address url: {}", requestWrap.getHttpRequest().uri(), cause);
        throw new RaidenHttpException("request failed, request address url: {}", requestWrap.getHttpRequest().uri(), cause);
    }
}