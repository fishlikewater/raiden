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
import io.github.fishlikewater.raiden.http.core.constant.HttpConstants;
import io.github.fishlikewater.raiden.http.core.factory.DefaultHttpClientBeanFactory;
import io.github.fishlikewater.raiden.http.core.factory.HttpClientBeanFactory;
import io.github.fishlikewater.raiden.http.core.interceptor.HttpClientInterceptor;
import io.github.fishlikewater.raiden.http.core.interceptor.PredRequestInterceptor;
import io.github.fishlikewater.raiden.http.core.processor.DefaultHttpClientProcessor;
import io.github.fishlikewater.raiden.http.core.processor.ExceptionProcessor;
import io.github.fishlikewater.raiden.http.core.processor.HttpClientProcessor;
import io.github.fishlikewater.raiden.http.core.proxy.InterfaceProxy;
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

    private static SourceHttpClientRegistry registry;

    private static InterfaceProxy interfaceProxy;

    @Getter
    private static final LogConfig logConfig = new LogConfig();

    @Getter
    private static PredRequestInterceptor predRequestInterceptor;

    @Getter
    private static HttpRequestClient httpRequestClient;

    @Getter
    private static boolean selfManager;

    @Getter
    private static HttpClientBeanFactory httpClientBeanFactory;

    @Getter
    private static HttpClientProcessor httpClientProcessor;

    public static void registryPredRequestInterceptor(PredRequestInterceptor predRequestInterceptor) {
        HttpBootStrap.predRequestInterceptor = predRequestInterceptor;
    }

    public static void registryHttpClientInterceptor(HttpClientInterceptor interceptor) {
        httpClientBeanFactory.registerHttpClientInterceptor(interceptor);
    }

    public static void setSourceHttpClientRegistry(SourceHttpClientRegistry registry) {
        HttpBootStrap.registry = registry;
    }

    public static HttpClient getHttpClient(String className) {
        return registry.get(className);
    }

    public static void setSelfManager(boolean selfManager) {
        HttpBootStrap.selfManager = selfManager;
    }

    public static void registerHttpClient(String name, HttpClient httpClient) {
        Assert.notNull(registry, "not Initialization...");
        registry.register(name, httpClient);
    }

    public static <T> T getProxy(Class<T> tClass) {
        return httpClientBeanFactory.getProxyObject(tClass);
    }

    public static void registerDefaultHttpClient() {
        registry = new SourceHttpClientRegistry(List.of(registry -> {
            final HttpClient defaultClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(60)).version(HttpClient.Version.HTTP_1_1).build();
            registry.register(HttpConstants.DEFAULT, defaultClient);
        }));
    }

    /**
     * 单独使用httpRequestClient
     */
    public static void init() {
        registerDefaultHttpClient();
        registry.init();
        if (Objects.isNull(httpRequestClient)) {
            httpRequestClient = new HttpRequestClient();
        }
    }

    /**
     * 接口代理使用
     *
     * @param packages 扫描包
     */
    public static void init(String... packages) throws ClassNotFoundException {
        log.info("httpClient Initialization begin...");
        if (selfManager) {
            registerDefaultHttpClient();
            registry.init();
        }
        if (Objects.isNull(httpRequestClient)) {
            httpRequestClient = new HttpRequestClient();
        }
        if (Objects.isNull(httpClientBeanFactory)) {
            httpClientBeanFactory = new DefaultHttpClientBeanFactory();
        }

        if (Objects.isNull(httpClientProcessor)) {
            httpClientProcessor = new DefaultHttpClientProcessor();
        }
        if (Objects.isNull(interfaceProxy)) {
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
        jdkInterfaceProxy.setHttpClientBeanFactory(httpClientBeanFactory);
        jdkInterfaceProxy.setHttpClientProcessor(httpClientProcessor);
        interfaceProxy = jdkInterfaceProxy;
    }

    private static void cache(Method[] methods, Class<?> clazz) {
        Interceptor interceptorAnnotation = clazz.getAnnotation(Interceptor.class);
        HttpServer httpServer = clazz.getAnnotation(HttpServer.class);
        if (selfManager) {
            cacheProxyClass(clazz);
            cacheInterceptor(interceptorAnnotation);
            cacheExceptionProcessor(httpServer);
        }
        for (Method method : methods) {
            httpClientBeanFactory.cacheMethod(method, httpServer, interceptorAnnotation);
        }
    }

    private static void cacheExceptionProcessor(HttpServer httpServer) {
        Class<? extends ExceptionProcessor> processorClass = httpServer.exceptionProcessor();
        ExceptionProcessor processor = httpClientBeanFactory.getExceptionProcessor(processorClass.getName());
        if (ObjectUtils.isNullOrEmpty(processor)) {
            httpClientBeanFactory.registerExceptionProcessor(getExceptionProcessor(processorClass));
        }
    }

    private static void cacheInterceptor(Interceptor interceptorAnnotation) {
        if (ObjectUtils.isNotNullOrEmpty(interceptorAnnotation)) {
            Class<? extends HttpClientInterceptor>[] classes = interceptorAnnotation.value();
            for (Class<? extends HttpClientInterceptor> aClass : classes) {
                HttpClientInterceptor interceptor = httpClientBeanFactory.getInterceptor(aClass.getName());
                if (ObjectUtils.isNotNullOrEmpty(interceptor)) {
                    httpClientBeanFactory.registerHttpClientInterceptor(getInterceptor(aClass));
                }
            }
        }
    }

    private static void cacheProxyClass(Class<?> clazz) {
        final Object instance = interfaceProxy.getInstance(clazz);
        httpClientBeanFactory.cacheProxyObject(clazz.getName(), instance);
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
