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
package io.github.fishlikewater.nacos.listener;

import com.alibaba.nacos.api.config.ConfigChangeEvent;
import com.alibaba.nacos.client.config.listener.impl.AbstractConfigChangeListener;
import com.alibaba.nacos.spring.context.event.config.NacosConfigReceivedEvent;
import io.github.fishlikewater.nacos.context.NacosContextRefresher;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.ApplicationListener;

/**
 * {@code AbstractDynamicNacosConfigListener}
 *
 * @author zhangxiang
 * @version 1.0.7
 * @since 2024/10/29
 */
public abstract class AbstractDynamicNacosConfigListener extends AbstractConfigChangeListener implements
        ApplicationListener<NacosConfigReceivedEvent>, BeanFactoryAware {

    protected static final Logger log = LoggerFactory.getLogger(AbstractDynamicNacosConfigListener.class);

    private BeanFactory beanFactory;

    @Override
    public void setBeanFactory(@NonNull BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void onApplicationEvent(NacosConfigReceivedEvent event) {
        log.info("Dynamic.nacos: on.spring.nacos.refresh.listener.received.config.changed.event:[{}:{}:{}]",
                event.getGroupId(), event.getDataId(), event.getType());
    }

    @Override
    public void receiveConfigChange(ConfigChangeEvent event) {
        log.info("Dynamic.nacos: on.nacos.refresh.listener.received.config.changed.event");

        this.onEvent(event);
    }

    // ----------------------------------------------------------------

    private void onEvent(NacosConfigReceivedEvent event) {
        NacosContextRefresher refresher = this.beanFactory.getBean(NacosContextRefresher.class);

        this.preRefresh(event);
        refresher.refresh();
        this.posRefresh(event);
    }

    private void onEvent(ConfigChangeEvent event) {
        NacosContextRefresher refresher = this.beanFactory.getBean(NacosContextRefresher.class);

        this.preRefresh(event);
        refresher.refresh();
        this.posRefresh(event);
    }

    // ----------------------------------------------------------------

    public void preRefresh(ConfigChangeEvent event) {
        // do nothing
    }

    public void posRefresh(ConfigChangeEvent event) {
        // do nothing
    }

    // ----------------------------------------------------------------

    public void preRefresh(NacosConfigReceivedEvent event) {
        // do nothing
    }

    public void posRefresh(NacosConfigReceivedEvent event) {
        // do nothing
    }
}
