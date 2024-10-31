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

import com.alibaba.nacos.api.config.ConfigService;

import java.util.Collection;

/**
 * {@code DefaultDynamicNacosConfigListener}
 * 默认监听器
 *
 * @author zhangxiang
 * @version 1.0.7
 * @since 2024/10/31
 */
public class DefaultDynamicNacosConfigListener extends AbstractDynamicNacosConfigListener {

    @Override
    public void registerListener(Collection<ConfigService> configServices) {

    }
}
