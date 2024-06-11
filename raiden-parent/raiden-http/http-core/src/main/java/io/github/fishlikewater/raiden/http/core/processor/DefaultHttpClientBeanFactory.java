/*
 * Copyright (c) 2024 zhangxiang (fishlikewater@126.com)
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
package io.github.fishlikewater.raiden.http.core.processor;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.TypeUtil;
import io.github.fishlikewater.raiden.core.ObjectUtils;
import io.github.fishlikewater.raiden.core.StringUtils;
import io.github.fishlikewater.raiden.http.core.MethodArgsBean;
import io.github.fishlikewater.raiden.http.core.annotation.*;
import io.github.fishlikewater.raiden.http.core.constant.HttpConstants;
import io.github.fishlikewater.raiden.http.core.enums.HttpMethod;
import io.github.fishlikewater.raiden.http.core.interceptor.HttpClientInterceptor;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 *
 * </p>
 *
 * @author fishlikewater@126.com
 * @version 1.0.0
 * @since 2023年09月23日 20:39
 **/
@Slf4j
public class DefaultHttpClientBeanFactory implements HttpClientBeanFactory {

    ConcurrentHashMap<String, MethodArgsBean> methodCache = new ConcurrentHashMap<>();
    ConcurrentHashMap<String, Object> proxyCache = new ConcurrentHashMap<>();
    ConcurrentHashMap<String, HttpClientInterceptor> interceptorCache = new ConcurrentHashMap<>();

    @Override
    public MethodArgsBean getMethodArgsBean(String methodName) {
        return methodCache.get(methodName);
    }

    @Override
    public void cacheMethod(Method method) {
        HttpMethod requestMethodType = null;
        boolean isForm = false;
        String path = "";
        final Annotation[] annotations = method.getAnnotations();

        for (Annotation annotation : annotations) {
            if (annotation instanceof Form) {
                isForm = true;
            }
            if (annotation instanceof RequireLine requireLine) {
                requestMethodType = requireLine.method();
                path = requireLine.path();
            }
            if (annotation instanceof GET get) {
                requestMethodType = HttpMethod.GET;
                path = get.value();
            }
            if (annotation instanceof POST post) {
                requestMethodType = HttpMethod.POST;
                path = post.value();
            }
            if (annotation instanceof PUT put) {
                requestMethodType = HttpMethod.PUT;
                path = put.value();
            }
            if (annotation instanceof PATCH patch) {
                requestMethodType = HttpMethod.PATCH;
                path = patch.value();
            }
            if (annotation instanceof DELETE delete) {
                requestMethodType = HttpMethod.DELETE;
                path = delete.value();
            }
        }
        if (Objects.isNull(requestMethodType)) {
            return;
        }
        final Interceptor interceptor = method.getDeclaringClass().getAnnotation(Interceptor.class);
        HttpServer httpServer = method.getDeclaringClass().getAnnotation(HttpServer.class);
        String serverName = httpServer.serverName();
        String interceptorClassName = ObjectUtils.isNotNullOrEmpty(interceptor) ? interceptor.getClass().getName() : null;
        final Class<?> returnType = method.getReturnType();
        final Type returnType1 = TypeUtil.getReturnType(method);
        final Type typeArgument = TypeUtil.getTypeArgument(returnType1);
        Parameter[] parameters = method.getParameters();
        Heads heads = method.getAnnotation(Heads.class);
        Map<String, String> headMap = MapUtil.newHashMap();
        if (Objects.nonNull(heads)) {
            Arrays.stream(heads.value()).map(h -> h.split(":", 2)).forEach(s -> headMap.put(s[0], s[1]));
        }
        final String requestUrl = path.startsWith("http") ? path : getUrl(httpServer.protocol(), httpServer.url(), path);
        final String className = method.getDeclaringClass().getName();
        String name = method.toGenericString();
        MethodArgsBean methodArgsBean = new MethodArgsBean(
                className,
                method.getName(),
                serverName,
                httpServer.sourceHttpClient(),
                interceptorClassName,
                requestMethodType,
                isForm,
                headMap,
                requestUrl,
                parameters,
                returnType,
                typeArgument);
        methodCache.put(name, methodArgsBean);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getProxyObject(Class<T> tClass) {
        return (T) proxyCache.get(tClass.getName());
    }

    @Override
    public void cacheProxyObject(String className, Object proxyObject) {
        proxyCache.put(className, proxyObject);
    }

    @Override
    public HttpClientInterceptor getInterceptor(String interceptorName) {
        return interceptorCache.get(interceptorName);
    }

    @Override
    public void setHttpClientInterceptor(HttpClientInterceptor httpClientInterceptor) {
        interceptorCache.put(httpClientInterceptor.getClass().getName(), httpClientInterceptor);
    }

    private String getUrl(String protocol, String url, String path) {
        String requestUrl;
        if (!url.isBlank() && !url.endsWith(HttpConstants.URL_SPLIT)) {
            url += HttpConstants.URL_SPLIT;
        }
        if (url.startsWith(HttpConstants.HTTP)) {
            requestUrl = url;
        } else {
            requestUrl = StringUtils.format("{}://{}", protocol, url);
        }
        if (path.startsWith(HttpConstants.URL_SPLIT)) {
            path = path.substring(1);
        }
        requestUrl += path;
        return requestUrl;
    }
}