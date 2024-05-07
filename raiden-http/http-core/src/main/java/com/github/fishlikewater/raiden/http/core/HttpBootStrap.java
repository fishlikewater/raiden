/*
 * Copyright © 2024 zhangxiang (fishlikewater@126.com)
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
package com.github.fishlikewater.raiden.http.core;

import cn.hutool.core.lang.Assert;
import com.github.fishlikewater.raiden.http.core.annotation.HttpServer;
import com.github.fishlikewater.raiden.http.core.interceptor.HttpClientInterceptor;
import com.github.fishlikewater.raiden.http.core.interceptor.PredRequest;
import com.github.fishlikewater.raiden.http.core.processor.DefaultHttpClientBeanFactory;
import com.github.fishlikewater.raiden.http.core.processor.DefaultHttpClientProcessor;
import com.github.fishlikewater.raiden.http.core.processor.HttpClientBeanFactory;
import com.github.fishlikewater.raiden.http.core.processor.HttpClientProcessor;
import com.github.fishlikewater.raiden.http.core.proxy.InterfaceProxy;
import com.github.fishlikewater.raiden.http.core.proxy.JdkInterfaceProxy;
import com.github.fishlikewater.raiden.http.core.source.SourceHttpClientRegistry;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import lombok.Getter;
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
 * @since 2023年09月24日 10:14
 * @version 1.0.0
 **/
@Slf4j
@Accessors(chain = true)
public class HttpBootStrap {

    private static SourceHttpClientRegistry registry;

    @Getter
    private static PredRequest predRequest;

    @Getter
    private static HttpRequestClient httpRequestClient;

    @Getter
    private static boolean selfManager;

    @Getter
    private static LogConfig logConfig = new LogConfig();

    private static InterfaceProxy interfaceProxy;

    @Getter
    private static HttpClientBeanFactory httpClientBeanFactory;

    @Getter
    private static HttpClientProcessor httpClientProcessor;

    public static void setPredRequest(PredRequest predRequest) {
        HttpBootStrap.predRequest = predRequest;
    }

    public static void setHttpClientInterceptor(HttpClientInterceptor interceptor) {
        httpClientBeanFactory.setHttpClientInterceptor(interceptor);
    }

    public static void setSourceHttpClientRegistry(SourceHttpClientRegistry registry) {
        HttpBootStrap.registry = registry;
    }

    public static HttpClientInterceptor getHttpClientInterceptor(String className) {
        return httpClientBeanFactory.getInterceptor(className);
    }

    public static HttpClient getHttpClient(String className) {
        return registry.get(className);
    }

    public static void setSelfManager(boolean selfManager) {
        HttpBootStrap.selfManager = selfManager;
    }

    public static void registerHttpClient(String name, HttpClient httpClient) {
        Assert.notNull(registry, "请先调用init初始化...");
        registry.register(name, httpClient);
    }

    public static <T> T getProxy(Class<T> tClass) {
        return httpClientBeanFactory.getProxyObject(tClass);
    }

    public static void registerDefaultHttpClient() {
        registry = new SourceHttpClientRegistry(List.of(registry -> {
            final HttpClient defaultClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(60)).version(HttpClient.Version.HTTP_1_1).build();
            registry.register("default", defaultClient);
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

        log.info("httpClient 接口开始初始化....");
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
                cacheMethod(allClass, methods, clazz);
            }
        }
        log.info("httpClient 接口初始化完成....");
    }

    private static void buildProxy() {
        final JdkInterfaceProxy jdkInterfaceProxy = new JdkInterfaceProxy();
        jdkInterfaceProxy.setHttpClientBeanFactory(httpClientBeanFactory);
        jdkInterfaceProxy.setHttpClientProcessor(httpClientProcessor);
        interfaceProxy = jdkInterfaceProxy;
    }

    private static void cacheMethod(ClassInfo allClass, Method[] methods, Class<?> clazz) {
        for (Method method : methods) {
            if (selfManager) {
                final Object instance = interfaceProxy.getInstance(clazz);
                httpClientBeanFactory.cacheProxyObject(allClass.getName(), instance);
            }
            httpClientBeanFactory.cacheMethod(method);
        }
    }
}
