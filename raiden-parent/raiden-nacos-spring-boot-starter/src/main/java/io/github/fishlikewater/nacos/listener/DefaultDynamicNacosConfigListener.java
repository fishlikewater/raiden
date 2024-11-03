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
import com.alibaba.nacos.api.config.ConfigChangeItem;
import com.alibaba.nacos.spring.core.env.NacosPropertySource;
import io.github.fishlikewater.nacos.bind.PropertiesBinder;
import io.github.fishlikewater.nacos.context.NacosContextRefresher;
import io.github.fishlikewater.nacos.model.ConfigBinder;
import io.github.fishlikewater.nacos.model.ConfigMeta;
import io.github.fishlikewater.raiden.core.StringUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * {@code DefaultDynamicNacosConfigListener}
 * 默认监听器
 *
 * @author zhangxiang
 * @version 1.0.7
 * @since 2024/10/31
 */
public class DefaultDynamicNacosConfigListener extends AbstractDynamicNacosConfigListener {

    private final ConfigMeta configMeta;

    private final ConfigurableEnvironment environment;

    private final BeanFactory beanFactory;

    public DefaultDynamicNacosConfigListener(ConfigMeta configMeta, ConfigurableEnvironment environment, BeanFactory beanFactory) {
        this.configMeta = configMeta;
        this.environment = environment;
        this.beanFactory = beanFactory;
    }

    @Override
    public void receiveConfigChange(ConfigChangeEvent event) {
        log.info("Dynamic.nacos: on.nacos.refresh.listener.received.config.changed.event");
        NacosContextRefresher refresher = this.beanFactory.getBean(NacosContextRefresher.class);
        PropertiesBinder binder = this.beanFactory.getBean(PropertiesBinder.class);
        this.preRefresh(event);
        Set<String> set = new HashSet<>();

        Collection<ConfigChangeItem> changeItems = event.getChangeItems();
        for (ConfigChangeItem changeItem : changeItems) {
            String key = changeItem.getKey();
            for (ConfigBinder binderBinder : binder.getBinders()) {
                if (key.contains(binderBinder.getPrefix())) {
                    set.add(StringUtils.lowerFirst(binderBinder.getClazz().getSimpleName()));
                }
            }
        }
        for (String name : set) {
            refresher.refresh(name);
        }
        this.posRefresh(event);
    }

    @Override
    public void receiveConfigInfo(String config) {
        for (PropertySource<?> propertySource : this.environment.getPropertySources()) {
            if (propertySource instanceof NacosPropertySource nacosPropertySource) {
                if (StringUtils.equals(nacosPropertySource.getGroupId(), configMeta.getGroupId())
                        && StringUtils.equals(nacosPropertySource.getDataId(), configMeta.getDataId())) {
                    String name = nacosPropertySource.getName();
                    NacosPropertySource newNacosPropertySource = new NacosPropertySource(
                            this.configMeta.getDataId(),
                            this.configMeta.getGroupId(),
                            name,
                            config,
                            this.configMeta.getType());

                    this.copy(newNacosPropertySource, nacosPropertySource);
                    MutablePropertySources propertySources = this.environment.getPropertySources();
                    // replace NacosPropertySource
                    propertySources.replace(name, newNacosPropertySource);
                }
            }
        }
    }

    // ----------------------------------------------------------------

    protected void copy(NacosPropertySource target, NacosPropertySource original) {
        target.setGroupId(original.getGroupId());
        target.setDataId(original.getDataId());
        target.setType(original.getType());
        target.setAutoRefreshed(original.isAutoRefreshed());
        target.setFirst(original.isFirst());
        target.setBefore(original.getBefore());
        target.setAfter(original.getAfter());
        target.setProperties(original.getProperties());
        target.setAttributesMetadata(original.getAttributesMetadata());
        target.setOrigin(original.getOrigin());
        target.setBeanName(original.getBeanName());
        target.setBeanType(original.getBeanType());
    }
}
