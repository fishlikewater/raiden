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
package io.github.fishlikewater.raiden.timer.autoconfigure;

import io.github.fishlikewater.raiden.timer.core.BaseTimerTask;
import io.github.fishlikewater.raiden.timer.core.timer.TimerLauncher;
import io.github.fishlikewater.spring.boot.raiden.core.processor.BeanProcessor;
import lombok.NonNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * <p>
 * {@code TimerBeanProcessor}
 * </p>
 * 注入定时任务
 *
 * @author fishlikewater@126.com
 * @version 1.0.2
 * @since 2024年06月10日 11:55
 **/
public class TimerBeanProcessor implements BeanProcessor {

    protected ConfigurableListableBeanFactory beanFactory;

    @Override
    public void postProcessBeanFactory(@NonNull ConfigurableListableBeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
        if (bean instanceof BaseTimerTask task) {
            final TimerLauncher timerLauncher = this.beanFactory.getBean(TimerLauncher.class);
            timerLauncher.add(task);
        }
        return bean;
    }
}
