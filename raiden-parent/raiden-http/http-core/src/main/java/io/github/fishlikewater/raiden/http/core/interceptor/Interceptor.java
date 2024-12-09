package io.github.fishlikewater.raiden.http.core.interceptor;

import io.github.fishlikewater.raiden.http.core.RequestWrap;
import io.github.fishlikewater.raiden.http.core.Response;

import java.io.IOException;

/**
 * {@code Interceptor}
 * 拦截器
 *
 * @author zhangxiang
 * @version 1.1.0
 * @since 2024/12/09
 */
public interface Interceptor {

    Response<?> intercept(Chain chain) throws IOException, InterruptedException;

    int order();

    interface Chain {

        RequestWrap requestWrap();

        Response<?> proceed(RequestWrap requestWrap) throws IOException, InterruptedException;
    }
}
