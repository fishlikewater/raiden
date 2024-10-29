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
package io.github.fishlikewater.nacos.registry;

import io.github.fishlikewater.nacos.model.ConfigMeta;

import java.util.Set;

/**
 * {@code DynamicNacosConfigListenerRegistry}
 * 注册配置
 *
 * @author zhangxiang
 * @version 1.0.7
 * @since 2024/10/29
 */
public interface DynamicNacosConfigListenerRegistry {

    /**
     * 注册需要动态刷新的 {@code dataId}
     *
     * @param meta {@link  ConfigMeta}
     */
    void registerConfigMeta(ConfigMeta meta);

    /**
     * 获取注册的所有 {@code ConfigMeta} 列表
     *
     * @return {@link  ConfigMeta} 列表
     */
    Set<ConfigMeta> tryAcquireConfigMetas();
}
