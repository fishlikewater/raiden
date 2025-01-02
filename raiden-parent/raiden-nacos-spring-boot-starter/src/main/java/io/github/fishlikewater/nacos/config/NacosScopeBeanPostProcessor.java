/*
 * Copyright (c) 2025 zhangxiang (fishlikewater@126.com)
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
package io.github.fishlikewater.nacos.config;

import io.github.fishlikewater.nacos.annotation.NacosRefreshScope;
import io.github.fishlikewater.raiden.core.ObjectUtils;
import lombok.NonNull;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.annotation.AnnotationUtils;

/**
 * {@code NacosScopeBeanPostProcessor}
 *
 * @author zhangxiang
 * @version 1.0.7
 * @since 2024/11/01
 */
@AutoConfiguration
public class NacosScopeBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, @NonNull String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        NacosRefreshScope annotation = AnnotationUtils.findAnnotation(beanClass, NacosRefreshScope.class);
        if (ObjectUtils.isNotNullOrEmpty(annotation) && annotation.proxyMode() != ScopedProxyMode.NO) {
            return this.createProxy(bean, beanName, annotation.proxyMode());
        }

        return bean;
    }

    private Object createProxy(Object bean, @NonNull String beanName, ScopedProxyMode scopedProxyMode) {
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setTarget(proxyFactory);
        if (scopedProxyMode == ScopedProxyMode.TARGET_CLASS) {
            proxyFactory.setProxyTargetClass(true);
        } else if (scopedProxyMode == ScopedProxyMode.INTERFACES) {
            proxyFactory.setProxyTargetClass(false);
        }
        return proxyFactory.getProxy();
    }
}
