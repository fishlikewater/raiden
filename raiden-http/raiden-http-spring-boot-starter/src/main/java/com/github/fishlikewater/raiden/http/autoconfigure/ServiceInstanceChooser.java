/*
 * Copyright © 2024 zhangxiang (fishlikewater@126.com)
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
package com.github.fishlikewater.raiden.http.autoconfigure;

import java.net.URI;

/**
 * 注册服务选择
 *
 * @author fishlikewater@126.com
 * @since 2023年09月26日 14:33
 * @version 1.0.0
 **/
@FunctionalInterface
public interface ServiceInstanceChooser {

    /**
     * 选择服务实例
     *
     * @param serviceId 服务id
     * @return 服务实例
     */
    URI choose(String serviceId);

    class NoValidServiceInstanceChooser implements ServiceInstanceChooser {

        @Override
        public URI choose(String serviceId) {
            throw new RuntimeException("没有配置服务选择实现类，请配置它");
        }
    }
}
