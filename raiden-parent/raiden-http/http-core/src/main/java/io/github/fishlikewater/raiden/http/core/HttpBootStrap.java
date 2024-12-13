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

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import io.github.fishlikewater.raiden.core.Assert;
import io.github.fishlikewater.raiden.core.ObjectUtils;
import io.github.fishlikewater.raiden.http.core.annotation.Degrade;
import io.github.fishlikewater.raiden.http.core.annotation.HttpServer;
import io.github.fishlikewater.raiden.http.core.annotation.Interceptor;
import io.github.fishlikewater.raiden.http.core.client.HttpRequestClient;
import io.github.fishlikewater.raiden.http.core.constant.DefaultConstants;
import io.github.fishlikewater.raiden.http.core.constant.HttpConstants;
import io.github.fishlikewater.raiden.http.core.degrade.FallbackFactory;
import io.github.fishlikewater.raiden.http.core.degrade.resilience4j.CircuitBreakerConfigRegistry;
import io.github.fishlikewater.raiden.http.core.degrade.resilience4j.GlobalBreakerConfigRegister;
import io.github.fishlikewater.raiden.http.core.degrade.sentinel.SentinelDegradeRuleRegistry;
import io.github.fishlikewater.raiden.http.core.factory.DefaultHttpClientBeanFactory;
import io.github.fishlikewater.raiden.http.core.interceptor.HttpInterceptor;
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

    public static void registerGlobalBreakerConfig(GlobalBreakerConfigRegister configRegister) {
        config.getBreakerConfigRegistry().register(DefaultConstants.GLOBAL_CIRCUIT_BREAKER_CONFIG, configRegister.get());
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
            config.setBreakerConfigRegistry(new CircuitBreakerConfigRegistry(null));
            config.setSentDegradeRuleRegistry(new SentinelDegradeRuleRegistry(null));
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
        Interceptor interceptor = clazz.getAnnotation(Interceptor.class);
        HttpServer httpServer = clazz.getAnnotation(HttpServer.class);
        Degrade degrade = clazz.getAnnotation(Degrade.class);
        if (config.isSelfManager()) {
            cacheProxyClass(clazz);
            cacheInterceptor(interceptor);
            cacheExceptionProcessor(httpServer);
            cacheDegrade(degrade);
        }
        for (Method method : methods) {
            config.getHttpClientBeanFactory().cacheMethod(method, httpServer, interceptor, degrade);
        }
    }

    private static void cacheDegrade(Degrade degrade) {
        if (ObjectUtils.isNullOrEmpty(degrade)) {
            return;
        }
        Class<? extends FallbackFactory<?>> fallback = degrade.fallback();
        FallbackFactory<?> fallbackFactory = config.getHttpClientBeanFactory().getFallbackFactory(fallback.getName());
        if (ObjectUtils.isNullOrEmpty(fallbackFactory)) {
            config.getHttpClientBeanFactory().registerFallbackFactory(getFallbackFactory(fallback));
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
        if (ObjectUtils.isNullOrEmpty(interceptorAnnotation)) {
            return;
        }
        Class<? extends HttpInterceptor>[] classes = interceptorAnnotation.value();
        for (Class<? extends HttpInterceptor> aClass : classes) {
            HttpInterceptor interceptor = config.getHttpClientBeanFactory().getInterceptor(aClass.getName());
            if (ObjectUtils.isNullOrEmpty(interceptor)) {
                config.getHttpClientBeanFactory().registerHttpClientInterceptor(getInterceptor(aClass));
            }
        }
    }

    private static void cacheProxyClass(Class<?> clazz) {
        final Object instance = config.getInterfaceProxy().getInstance(clazz);
        config.getHttpClientBeanFactory().cacheProxyObject(clazz.getName(), instance);
    }

    @SneakyThrows
    private static HttpInterceptor getInterceptor(Class<? extends HttpInterceptor> iClass) {
        return iClass.getDeclaredConstructor().newInstance();
    }

    @SneakyThrows
    private static ExceptionProcessor getExceptionProcessor(Class<? extends ExceptionProcessor> processorClass) {
        return processorClass.getDeclaredConstructor().newInstance();
    }

    @SneakyThrows
    private static FallbackFactory<?> getFallbackFactory(Class<? extends FallbackFactory<?>> fallback) {
        return fallback.getDeclaredConstructor().newInstance();
    }
}
