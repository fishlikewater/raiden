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
package io.github.fishlikewater.raiden.http.core.proxy;

import io.github.fishlikewater.raiden.core.ObjectUtils;
import io.github.fishlikewater.raiden.core.StringUtils;
import io.github.fishlikewater.raiden.core.TypeUtils;
import io.github.fishlikewater.raiden.http.core.*;
import io.github.fishlikewater.raiden.http.core.annotation.Body;
import io.github.fishlikewater.raiden.http.core.annotation.Heads;
import io.github.fishlikewater.raiden.http.core.annotation.Param;
import io.github.fishlikewater.raiden.http.core.annotation.PathParam;
import io.github.fishlikewater.raiden.http.core.enums.HttpMethod;
import io.github.fishlikewater.raiden.http.core.factory.HttpClientBeanFactory;
import io.github.fishlikewater.raiden.http.core.processor.HttpClientProcessor;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.net.http.HttpClient;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author fishlikewater@126.com
 * @version 1.0.0
 * @since 2023年09月23日 18:30
 **/
public interface InterfaceProxy {

    /**
     * 处理请求
     *
     * @param method                方法
     * @param args                  参数
     * @param httpClientProcessor   {@code HttpClientProcessor}
     * @param httpClientBeanFactory {@code HttpClientBeanFactory}
     * @return Object
     */
    default Object handler(Method method, Object[] args, HttpClientProcessor httpClientProcessor, HttpClientBeanFactory httpClientBeanFactory) {
        String name = method.toGenericString();
        MethodArgsBean methodArgsBean = httpClientBeanFactory.getMethodArgsBean(name);
        if (Objects.nonNull(HttpBootStrap.getConfig().getPredRequestInterceptor())) {
            HttpBootStrap.getConfig().getPredRequestInterceptor().handler(methodArgsBean);
        }
        HttpMethod httpMethod = methodArgsBean.getRequestMethod();
        boolean form = methodArgsBean.isForm();
        Parameter[] parameters = methodArgsBean.getUrlParameters();
        String url = methodArgsBean.getUrl();
        Class<?> returnType = methodArgsBean.getReturnType();
        Type typeArgument = methodArgsBean.getTypeArgument();
        Map<String, String> headMap = methodArgsBean.getHeadMap();

        HttpClient httpClient = HttpBootStrap.getHttpClient(methodArgsBean.getSourceHttpClientName());
        RequestWrap requestWrap = RequestWrap.builder()
                .method(method)
                .args(args)
                .degrade(methodArgsBean.isDegrade())
                .degradeType(methodArgsBean.getDegradeType())
                .circuitBreakerConfigName(methodArgsBean.getCircuitBreakerConfigName())
                .httpMethod(httpMethod)
                .returnType(returnType)
                .typeArgumentClass(TypeUtils.getClass(typeArgument))
                .form(form)
                .sync(methodArgsBean.isSync())
                .url(url)
                .httpClient(httpClient)
                .headMap(headMap)
                .build();
        List<String> interceptorNames = methodArgsBean.getInterceptorNames();
        String exceptionProcessorName = methodArgsBean.getExceptionProcessorName();
        String fallbackFactoryName = methodArgsBean.getFallbackFactoryName();
        if (ObjectUtils.isNotNullOrEmpty(interceptorNames)) {
            requestWrap.setInterceptors(httpClientBeanFactory.getInterceptors(interceptorNames));
        }
        if (ObjectUtils.isNotNullOrEmpty(exceptionProcessorName)) {
            requestWrap.setExceptionProcessor(httpClientBeanFactory.getExceptionProcessor(exceptionProcessorName));
        }
        if (ObjectUtils.isNotNullOrEmpty(fallbackFactoryName)) {
            requestWrap.setFallbackFactory(httpClientBeanFactory.getFallbackFactory(fallbackFactoryName));
        }
        /* 构建请求参数*/
        if (ObjectUtils.isNotNullOrEmpty(parameters)) {
            this.buildParams(requestWrap, parameters, args);
        }

        /* 设置重试次数*/
        requestWrap.setRetryCount(HttpBootStrap.getConfig().getMaxRetryCount());

        return httpClientProcessor.handler(requestWrap);
    }

    /**
     * 处理参数
     *
     * @param requestWrap 参数包装
     * @param parameters  方法参数
     * @param args        参数
     */
    default void buildParams(RequestWrap requestWrap, Parameter[] parameters, Object[] args) {
        Map<String, String> paramMap = new HashMap<>();
        Map<String, String> paramPath = new HashMap<>();
        Object bodyObject = null;
        MultipartData multipartData = null;
        for (int i = 0; i < parameters.length; i++) {
            Param param = parameters[i].getAnnotation(Param.class);
            if (ObjectUtils.isNotNullOrEmpty(param)) {
                this.handleParam(paramMap, param, args[i]);
                continue;
            }
            PathParam pathParam = parameters[i].getAnnotation(PathParam.class);
            if (ObjectUtils.isNotNullOrEmpty(pathParam)) {
                paramPath.put(pathParam.value(), (String) args[i]);
                continue;
            }
            Body body = parameters[i].getAnnotation(Body.class);
            if (ObjectUtils.isNotNullOrEmpty(body)) {
                bodyObject = args[i];
                continue;
            }
            Heads heads = parameters[i].getAnnotation(Heads.class);
            if (ObjectUtils.isNotNullOrEmpty(heads) && args[i] instanceof HeadWrap headWrap) {
                Map<String, String> headMap = requestWrap.getHeadMap();
                headWrap.getHeads().forEach(head -> headMap.put(head.getKey(), head.getValue()));
            }
            if (args[i] instanceof MultipartData mData) {
                multipartData = mData;
            }
        }
        if (!paramPath.isEmpty()) {
            String url = StringUtils.format(requestWrap.getUrl(), paramPath, true);
            requestWrap.setUrl(url);
        }
        requestWrap.setParamMap(paramMap);
        requestWrap.setBodyObject(bodyObject);
        requestWrap.setMultipartData(multipartData);
    }

    /**
     * 处理参数
     *
     * @param paramMap 参数
     * @param param    参数注解
     * @param arg      参数
     */
    default void handleParam(Map<String, String> paramMap, Param param, Object arg) {
        if (arg instanceof String || arg instanceof Number) {
            paramMap.put(param.value(), arg.toString());
        } else {
            Map<String, Object> map = ObjectUtils.beanToMap(arg, true);
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                paramMap.put(entry.getKey(), entry.getValue().toString());
            }
        }
    }

    /**
     * 获取代理对象
     *
     * @param interfaceClass 接口
     * @return T
     */
    <T> T getInstance(Class<T> interfaceClass);
}
