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
package com.github.fishlikewater.spring.boot.raiden.core.processor;

import com.github.fishlikewater.spring.boot.raiden.core.BeanAware;
import com.github.fishlikewater.spring.boot.raiden.core.BeanInject;
import lombok.NonNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * {@code AbstractBeanProcessor}
 *
 * @author zhangxiang
 * @version 1.0.0
 * @since 2024/04/30
 */
public abstract class AbstractBeanProcessor<@NonNull V extends BeanInject, @NonNull T extends BeanAware> implements BeanProcessor {
    protected ConfigurableListableBeanFactory beanFactory;

    @Override
    public void postProcessBeanFactory(@NonNull ConfigurableListableBeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
        if (resolveAwareClass().isInstance(bean)) {
            V bean2 = this.beanFactory.getBean(this.resolveInjectClass());
            this.inject((T) bean, bean2);
        }
        return bean;
    }

    /**
     * 获取注入类
     *
     * @return Class
     */
    public abstract Class<V> resolveInjectClass();

    /**
     * 获取aware类
     *
     * @return Class
     */
    public abstract Class<T> resolveAwareClass();

    /**
     * 注入
     *
     * @param aware  {@code BeanAware}
     * @param inject {@code BeanInject}
     */
    public abstract void inject(T aware, V inject);
}
