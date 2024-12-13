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
package io.github.fishlikewater.raiden.http.core.factory;

import io.github.fishlikewater.raiden.core.ObjectUtils;
import io.github.fishlikewater.raiden.core.StringUtils;
import io.github.fishlikewater.raiden.core.TypeUtils;
import io.github.fishlikewater.raiden.http.core.MethodArgsBean;
import io.github.fishlikewater.raiden.http.core.annotation.*;
import io.github.fishlikewater.raiden.http.core.constant.HttpConstants;
import io.github.fishlikewater.raiden.http.core.degrade.FallbackFactory;
import io.github.fishlikewater.raiden.http.core.enums.HttpMethod;
import io.github.fishlikewater.raiden.http.core.interceptor.HttpInterceptor;
import io.github.fishlikewater.raiden.http.core.processor.ExceptionProcessor;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

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
    ConcurrentHashMap<String, HttpInterceptor> interceptorCache = new ConcurrentHashMap<>();

    ConcurrentHashMap<String, ExceptionProcessor> exceptionProcessorCache = new ConcurrentHashMap<>();
    ConcurrentHashMap<String, FallbackFactory<?>> fallbackFactoryCache = new ConcurrentHashMap<>();

    @Override
    public MethodArgsBean getMethodArgsBean(String methodName) {
        return methodCache.get(methodName);
    }

    @Override
    public void cacheMethod(Method method, HttpServer httpServer, Interceptor interceptor, Degrade degrade) {
        MethodArgsBean argsBean = this.handleMethodAnnotation(method);
        if (Objects.isNull(argsBean.getRequestMethod())) {
            return;
        }
        String path = argsBean.getUrl();
        String serverName = httpServer.serverName();
        final Class<?> returnType = method.getReturnType();
        final Type returnType1 = TypeUtils.getReturnType(method);
        final Type typeArgument = TypeUtils.getGenericType(returnType1);
        Parameter[] parameters = method.getParameters();

        Heads heads = method.getAnnotation(Heads.class);
        Map<String, String> headMap = new HashMap<>(8);
        if (Objects.nonNull(heads)) {
            Arrays.stream(heads.value())
                    .map(h -> h.split(HttpConstants.HEAD_SPLIT_SYMBOL, 2))
                    .forEach(s -> {
                        String key = s[0].trim();
                        String value = s[1].trim();
                        headMap.put(key, value);
                    });
        }

        Degrade methodDegrade = method.getAnnotation(Degrade.class);
        if (ObjectUtils.isNotNullOrEmpty(methodDegrade)) {
            argsBean.setDegrade(true);
            argsBean.setFallbackFactoryName(methodDegrade.fallback().getName());
            argsBean.setDegradeType(methodDegrade.type());
            argsBean.setCircuitBreakerConfigName(methodDegrade.circuitBreakerConfigName());
        } else if (ObjectUtils.isNotNullOrEmpty(degrade)) {
            argsBean.setDegrade(true);
            argsBean.setFallbackFactoryName(degrade.fallback().getName());
            argsBean.setDegradeType(degrade.type());
            argsBean.setCircuitBreakerConfigName(degrade.circuitBreakerConfigName());
        }

        final String className = method.getDeclaringClass().getName();
        final String requestUrl = path.startsWith(HttpConstants.HTTP) ? path : getUrl(httpServer.protocol(), httpServer.url(), path);
        Class<? extends HttpInterceptor>[] interceptors = ObjectUtils.isNotNullOrEmpty(interceptor) ? interceptor.value() : null;
        if (ObjectUtils.isNotNullOrEmpty(interceptors)) {
            for (Class<? extends HttpInterceptor> aClass : interceptors) {
                argsBean.addInterceptorName(aClass.getName());
            }
        }

        String exceptionProcessorClassName = ObjectUtils.isNotNullOrEmpty(httpServer.exceptionProcessor()) ? httpServer.exceptionProcessor().getName() : null;
        argsBean.setClassName(className)
                .setServerName(serverName)
                .setProtocol(httpServer.protocol())
                .setUrlPrefix(httpServer.url())
                .setPath(path)
                .setExceptionProcessorName(exceptionProcessorClassName)
                .setUrl(requestUrl)
                .setHeadMap(headMap)
                .setUrlParameters(parameters)
                .setReturnType(returnType)
                .setTypeArgument(typeArgument)
                .setSync(!returnType.isAssignableFrom(CompletableFuture.class))
                .setSourceHttpClientName(httpServer.sourceHttpClient());

        String name = method.toGenericString();
        methodCache.put(name, argsBean);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getProxyObject(Class<T> tClass) {
        return (T) this.proxyCache.get(tClass.getName());
    }

    @Override
    public void cacheProxyObject(String className, Object proxyObject) {
        this.proxyCache.put(className, proxyObject);
    }

    @Override
    public HttpInterceptor getInterceptor(String interceptorName) {
        if (ObjectUtils.isNullOrEmpty(interceptorName)) {
            return null;
        }
        return this.interceptorCache.get(interceptorName);
    }

    @Override
    public List<HttpInterceptor> getInterceptors(List<String> interceptorNames) {
        if (ObjectUtils.isNotNullOrEmpty(interceptorNames)) {
            return interceptorNames
                    .stream()
                    .map(this::getInterceptor)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }
        return List.of();
    }

    @Override
    public void registerHttpClientInterceptor(HttpInterceptor httpInterceptor) {
        this.interceptorCache.put(httpInterceptor.getClass().getName(), httpInterceptor);
    }

    @Override
    public ExceptionProcessor getExceptionProcessor(String name) {
        if (ObjectUtils.isNullOrEmpty(name)) {
            return null;
        }
        return this.exceptionProcessorCache.get(name);
    }

    @Override
    public void registerExceptionProcessor(ExceptionProcessor exceptionProcessor) {
        this.exceptionProcessorCache.put(exceptionProcessor.getClass().getName(), exceptionProcessor);
    }

    @Override
    public FallbackFactory<?> getFallbackFactory(String name) {
        return this.fallbackFactoryCache.get(name);
    }

    @Override
    public void registerFallbackFactory(FallbackFactory<?> fallbackFactory) {
        this.fallbackFactoryCache.put(fallbackFactory.getClass().getName(), fallbackFactory);
    }

    private MethodArgsBean handleMethodAnnotation(Method method) {
        MethodArgsBean.MethodArgsBeanBuilder builder = MethodArgsBean.builder();
        final Annotation[] annotations = method.getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation instanceof Form) {
                builder.isForm(true);
            }
            if (annotation instanceof RequireLine requireLine) {
                builder.requestMethod(requireLine.method());
                builder.url(requireLine.path());
            }
            if (annotation instanceof GET get) {
                builder.requestMethod(HttpMethod.GET);
                builder.url(get.value());
            }
            if (annotation instanceof POST post) {
                builder.requestMethod(HttpMethod.POST);
                builder.url(post.value());
            }
            if (annotation instanceof PUT put) {
                builder.requestMethod(HttpMethod.PUT);
                builder.url(put.value());
            }
            if (annotation instanceof PATCH patch) {
                builder.requestMethod(HttpMethod.PATCH);
                builder.url(patch.value());
            }
            if (annotation instanceof DELETE delete) {
                builder.requestMethod(HttpMethod.DELETE);
                builder.url(delete.value());
            }
        }
        return builder.build();
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
