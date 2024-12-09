package io.github.fishlikewater.raiden.http.core.interceptor;

import io.github.fishlikewater.raiden.core.ObjectUtils;
import io.github.fishlikewater.raiden.http.core.RequestWrap;
import io.github.fishlikewater.raiden.http.core.Response;

import java.io.IOException;
import java.util.List;

/**
 * {@code RealInterceptorChain}
 *
 * @author zhangxiang
 * @version 1.1.0
 * @since 2024/12/09
 */
public class RealInterceptorChain implements HttpInterceptor.Chain {

    private final List<HttpInterceptor> interceptors;
    private RequestWrap requestWrap;
    private int index;

    public RealInterceptorChain(RequestWrap requestWrap, List<HttpInterceptor> interceptors) {
        this.requestWrap = requestWrap;
        this.interceptors = interceptors;
    }

    @Override
    public RequestWrap requestWrap() {
        return this.requestWrap;
    }

    @Override
    public Response<?> proceed(RequestWrap requestWrap) throws IOException, InterruptedException {
        if (this.index >= this.interceptors.size()) {
            throw new AssertionError();
        }
        HttpInterceptor interceptor = this.interceptors.get(index);
        this.incrementIndex();
        this.updateRequestWrap(requestWrap);
        Response<?> response = interceptor.intercept(this);
        if (ObjectUtils.isNullOrEmpty(response)) {
            throw new NullPointerException("interceptor " + interceptor + " returned null");
        }
        return response;
    }

    private void updateRequestWrap(RequestWrap requestWrap) {
        this.requestWrap = requestWrap;
    }

    private void incrementIndex() {
        this.index++;
    }
}
