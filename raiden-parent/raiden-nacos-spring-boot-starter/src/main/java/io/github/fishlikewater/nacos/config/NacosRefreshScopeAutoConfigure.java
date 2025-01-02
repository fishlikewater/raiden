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

import io.github.fishlikewater.nacos.bind.PropertiesBinder;
import io.github.fishlikewater.nacos.listener.GlobalNacosConfigListener;
import io.github.fishlikewater.nacos.registry.NacosConfigRegister;
import io.github.fishlikewater.nacos.scope.NacosRefreshScope;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;

/**
 * {@code NacosRefreshScopeAutoConfigure}
 *
 * @author zhangxiang
 * @version 1.0.7
 * @since 2024/10/29
 */
@AutoConfiguration
public class NacosRefreshScopeAutoConfigure {

    @Bean
    public NacosRefreshScope nacosRefreshScope() {
        return new NacosRefreshScope();
    }

    @Bean
    public PropertiesBinder propertiesBinder() {
        return new PropertiesBinder();
    }

    @Bean
    @Lazy
    @ConditionalOnMissingBean(NacosConfigRegister.class)
    public GlobalNacosConfigListener globalNacosConfigListener() {
        return new GlobalNacosConfigListener();
    }
}
