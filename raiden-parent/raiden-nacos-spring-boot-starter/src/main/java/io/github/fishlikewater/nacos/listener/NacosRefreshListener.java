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
package io.github.fishlikewater.nacos.listener;

import com.alibaba.nacos.api.config.ConfigChangeItem;

import java.util.Collection;

/**
 * {@code NacosRefreshListener}
 * 单独dateId刷新监听器
 *
 * @author zhangxiang
 * @version 1.1.1
 * @since 2025/01/02
 */
public interface NacosRefreshListener {

    /**
     * 支持监听的groupId 与 dataId
     *
     * @return groupId + ":" + dataId
     */
    String support();

    /**
     * 监听处理
     *
     * @param changeItems 监听到的变更内容
     */
    void handle(Collection<ConfigChangeItem> changeItems);
}
