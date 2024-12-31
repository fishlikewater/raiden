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
import io.github.fishlikewater.nacos.event.NacosRefreshFinishEvent;
import io.github.fishlikewater.nacos.model.ConfigMeta;
import io.github.fishlikewater.nacos.model.RefreshMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;


public abstract class AbstractDynamicNacosConfigListener extends AbstractConfigChangeListener {

    protected static final Logger log = LoggerFactory.getLogger(AbstractDynamicNacosConfigListener.class);

    // ----------------------------------------------------------------

    public void preRefresh(ConfigChangeEvent event, ConfigMeta configMeta, ApplicationContext applicationContext) {
        // do nothing
    }

    public void postRefresh(ConfigChangeEvent event, ConfigMeta configMeta, ApplicationContext applicationContext) {
        RefreshMeta refreshMeta = new RefreshMeta(configMeta.getGroupId(), configMeta.getDataId());
        NacosRefreshFinishEvent refreshFinishEvent = new NacosRefreshFinishEvent(refreshMeta);
        applicationContext.publishEvent(refreshFinishEvent);
    }
}
