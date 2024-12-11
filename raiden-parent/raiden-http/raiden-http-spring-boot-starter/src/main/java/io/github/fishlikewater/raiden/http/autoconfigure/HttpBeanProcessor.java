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
package io.github.fishlikewater.raiden.http.autoconfigure;

import io.github.fishlikewater.raiden.http.core.HttpBootStrap;
import io.github.fishlikewater.raiden.http.core.degrade.FallbackFactory;
import io.github.fishlikewater.raiden.http.core.interceptor.HttpInterceptor;
import io.github.fishlikewater.raiden.http.core.processor.ExceptionProcessor;
import lombok.NonNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * <p>
 * {@code HttpBeanProcessor}
 * </p>
 * 相关类自动注入管理容器
 *
 * @author fishlikewater@126.com
 * @version 1.0.2
 * @since 2024年06月12日 20:55
 **/
public class HttpBeanProcessor implements BeanPostProcessor, BeanFactoryPostProcessor {

    protected ConfigurableListableBeanFactory beanFactory;

    @Override
    public void postProcessBeanFactory(@NonNull ConfigurableListableBeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
        if (bean instanceof HttpInterceptor interceptor) {
            HttpBootStrap.getConfig().getHttpClientBeanFactory().registerHttpClientInterceptor(interceptor);
        }
        if (bean instanceof ExceptionProcessor exceptionProcessor) {
            HttpBootStrap.getConfig().getHttpClientBeanFactory().registerExceptionProcessor(exceptionProcessor);
        }
        if (bean instanceof FallbackFactory<?> fallback) {
            HttpBootStrap.getConfig().getHttpClientBeanFactory().registerFallbackFactory(fallback);
        }
        return bean;
    }
}
