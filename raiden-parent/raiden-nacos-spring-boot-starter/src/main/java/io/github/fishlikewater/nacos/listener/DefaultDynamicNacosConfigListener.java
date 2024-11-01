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

import com.alibaba.nacos.spring.core.env.NacosPropertySource;
import io.github.fishlikewater.nacos.model.ConfigMeta;
import io.github.fishlikewater.raiden.core.StringUtils;
import lombok.NonNull;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;

/**
 * {@code DefaultDynamicNacosConfigListener}
 * 默认监听器
 *
 * @author zhangxiang
 * @version 1.0.7
 * @since 2024/10/31
 */
public class DefaultDynamicNacosConfigListener extends AbstractDynamicNacosConfigListener implements EnvironmentAware {

    private final ConfigMeta configMeta;

    private ConfigurableEnvironment environment;

    public DefaultDynamicNacosConfigListener(ConfigMeta configMeta) {
        this.configMeta = configMeta;
    }

    @Override
    public void receiveConfigInfo(String config) {
        for (PropertySource<?> propertySource : environment.getPropertySources()) {
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
                    MutablePropertySources propertySources = environment.getPropertySources();
                    // replace NacosPropertySource
                    propertySources.replace(name, newNacosPropertySource);
                }
            }
        }
    }

    @Override
    public void setEnvironment(@NonNull Environment environment) {
        this.environment = (ConfigurableEnvironment) environment;
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
