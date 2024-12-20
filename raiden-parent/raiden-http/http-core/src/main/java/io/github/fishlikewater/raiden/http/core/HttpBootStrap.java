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
package io.github.fishlikewater.raiden.http.core;

import cn.hutool.core.lang.Assert;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import io.github.fishlikewater.raiden.core.ObjectUtils;
import io.github.fishlikewater.raiden.http.core.annotation.HttpServer;
import io.github.fishlikewater.raiden.http.core.annotation.Interceptor;
import io.github.fishlikewater.raiden.http.core.client.HttpRequestClient;
import io.github.fishlikewater.raiden.http.core.constant.HttpConstants;
import io.github.fishlikewater.raiden.http.core.factory.DefaultHttpClientBeanFactory;
import io.github.fishlikewater.raiden.http.core.interceptor.HttpClientInterceptor;
import io.github.fishlikewater.raiden.http.core.interceptor.PredRequestInterceptor;
import io.github.fishlikewater.raiden.http.core.processor.DefaultHttpClientProcessor;
import io.github.fishlikewater.raiden.http.core.processor.ExceptionProcessor;
import io.github.fishlikewater.raiden.http.core.proxy.JdkInterfaceProxy;
import io.github.fishlikewater.raiden.http.core.source.SourceHttpClientRegistry;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.net.http.HttpClient;
import java.time.Duration;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 配置入口类
 * </p>
 *
 * @author fishlikewater@126.com
 * @version 1.0.0
 * @since 2023年09月24日 10:14
 **/
@Slf4j
@Accessors(chain = true)
public class HttpBootStrap {

    @Getter
    private static final HttpConfig config = new HttpConfig();

    public static void registryPredRequestInterceptor(PredRequestInterceptor predRequestInterceptor) {
        config.setPredRequestInterceptor(predRequestInterceptor);
    }

    public static void registryHttpClientInterceptor(HttpClientInterceptor interceptor) {
        config.getHttpClientBeanFactory().registerHttpClientInterceptor(interceptor);
    }

    public static HttpClient getHttpClient(String className) {
        return config.getSourceHttpClientRegistry().get(className);
    }

    public static void setSelfManager(boolean selfManager) {
        config.setSelfManager(selfManager);
    }

    public static void registerHttpClient(String name, HttpClient httpClient) {
        Assert.notNull(config.getSourceHttpClientRegistry(), "not Initialization...");
        config.getSourceHttpClientRegistry().register(name, httpClient);
    }

    public static <T> T getProxy(Class<T> tClass) {
        return config.getHttpClientBeanFactory().getProxyObject(tClass);
    }

    public static void registerDefaultHttpClient() {
        SourceHttpClientRegistry sourceHttpClientRegistry = new SourceHttpClientRegistry(List.of(registry -> {
            final HttpClient defaultClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(60)).version(HttpClient.Version.HTTP_1_1).build();
            registry.register(HttpConstants.DEFAULT, defaultClient);
        }));
        config.setSourceHttpClientRegistry(sourceHttpClientRegistry);
    }

    /**
     * 单独使用httpRequestClient
     */
    public static void init() {
        registerDefaultHttpClient();
        config.getSourceHttpClientRegistry().init();
        if (Objects.isNull(config.getHttpClient())) {
            config.setHttpClient(new HttpRequestClient());
        }
    }

    /**
     * 接口代理使用
     *
     * @param packages 扫描包
     */
    public static void init(String... packages) throws ClassNotFoundException {
        log.info("httpClient Initialization begin...");
        if (config.isSelfManager()) {
            registerDefaultHttpClient();
            config.getSourceHttpClientRegistry().init();
        }
        if (Objects.isNull(config.getHttpClient())) {
            config.setHttpClient(new HttpRequestClient());
        }
        if (Objects.isNull(config.getHttpClientBeanFactory())) {
            config.setHttpClientBeanFactory(new DefaultHttpClientBeanFactory());
        }

        if (Objects.isNull(config.getHttpClientProcessor())) {
            config.setHttpClientProcessor(new DefaultHttpClientProcessor());
        }
        if (Objects.isNull(config.getInterfaceProxy())) {
            buildProxy();
        }
        final ClassGraph classGraph = new ClassGraph();
        try (ScanResult scan = classGraph.enableAllInfo().acceptPackages(packages).scan()) {
            final ClassInfoList allClasses = scan.getClassesWithAnnotation(HttpServer.class);
            for (ClassInfo allClass : allClasses) {
                Class<?> clazz = Class.forName(allClass.getName());
                Method[] methods = clazz.getDeclaredMethods();
                cache(methods, clazz);
            }
        }
        log.info("httpClient Initialization complete...");
    }

    private static void buildProxy() {
        final JdkInterfaceProxy jdkInterfaceProxy = new JdkInterfaceProxy();
        jdkInterfaceProxy.setHttpClientBeanFactory(config.getHttpClientBeanFactory());
        jdkInterfaceProxy.setHttpClientProcessor(config.getHttpClientProcessor());
        config.setInterfaceProxy(jdkInterfaceProxy);
    }

    private static void cache(Method[] methods, Class<?> clazz) {
        Interceptor interceptorAnnotation = clazz.getAnnotation(Interceptor.class);
        HttpServer httpServer = clazz.getAnnotation(HttpServer.class);
        if (config.isSelfManager()) {
            cacheProxyClass(clazz);
            cacheInterceptor(interceptorAnnotation);
            cacheExceptionProcessor(httpServer);
        }
        for (Method method : methods) {
            config.getHttpClientBeanFactory().cacheMethod(method, httpServer, interceptorAnnotation);
        }
    }

    private static void cacheExceptionProcessor(HttpServer httpServer) {
        Class<? extends ExceptionProcessor> processorClass = httpServer.exceptionProcessor();
        ExceptionProcessor processor = config.getHttpClientBeanFactory().getExceptionProcessor(processorClass.getName());
        if (ObjectUtils.isNullOrEmpty(processor)) {
            config.getHttpClientBeanFactory().registerExceptionProcessor(getExceptionProcessor(processorClass));
        }
    }

    private static void cacheInterceptor(Interceptor interceptorAnnotation) {
        if (ObjectUtils.isNotNullOrEmpty(interceptorAnnotation)) {
            Class<? extends HttpClientInterceptor>[] classes = interceptorAnnotation.value();
            for (Class<? extends HttpClientInterceptor> aClass : classes) {
                HttpClientInterceptor interceptor = config.getHttpClientBeanFactory().getInterceptor(aClass.getName());
                if (ObjectUtils.isNotNullOrEmpty(interceptor)) {
                    config.getHttpClientBeanFactory().registerHttpClientInterceptor(getInterceptor(aClass));
                }
            }
        }
    }

    private static void cacheProxyClass(Class<?> clazz) {
        final Object instance = config.getInterfaceProxy().getInstance(clazz);
        config.getHttpClientBeanFactory().cacheProxyObject(clazz.getName(), instance);
    }

    @SneakyThrows
    private static HttpClientInterceptor getInterceptor(Class<? extends HttpClientInterceptor> iClass) {
        return iClass.getDeclaredConstructor().newInstance();
    }

    @SneakyThrows
    private static ExceptionProcessor getExceptionProcessor(Class<? extends ExceptionProcessor> processorClass) {
        return processorClass.getDeclaredConstructor().newInstance();
    }
}
