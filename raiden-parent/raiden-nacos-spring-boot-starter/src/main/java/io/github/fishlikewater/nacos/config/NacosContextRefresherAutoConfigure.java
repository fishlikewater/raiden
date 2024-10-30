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
package io.github.fishlikewater.nacos.config;

import com.alibaba.boot.nacos.config.autoconfigure.NacosConfigAutoConfiguration;
import io.github.fishlikewater.nacos.context.NacosContextRefresher;
import io.github.fishlikewater.nacos.scope.NacosRefreshScope;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * {@code NacosContextRefresherAutoConfigure}
 *
 * @author zhangxiang
 * @version 1.0.7
 * @since 2024/10/30
 */
@AutoConfiguration(after = {
        NacosConfigAutoConfiguration.class,
        NacosRefreshScopeAutoConfigure.class,
})
public class NacosContextRefresherAutoConfigure {

    @Bean
    public NacosContextRefresher nacosContextRefresher(NacosRefreshScope scope) {
        return new NacosContextRefresher(scope);
    }
}
