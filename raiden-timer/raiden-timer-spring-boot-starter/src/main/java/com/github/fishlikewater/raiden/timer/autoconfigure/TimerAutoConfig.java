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
package com.github.fishlikewater.raiden.timer.autoconfigure;

import com.github.fishlikewater.raiden.timer.core.timer.TimerLauncher;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * {@code TimerAutoConfig}
 * 定时器启动配置
 *
 * @author zhangxiang
 * @version 1.0.0
 * @since 2024/04/07
 */

@AutoConfiguration
@EnableConfigurationProperties(TimerConfigProperties.class)
public class TimerAutoConfig {

    /**
     * 定时器启动器
     *
     * @param timerConfig 定时器配置
     * @return TimerLauncher
     */
    @Bean
    public TimerLauncher timerLauncher(TimerConfigProperties timerConfig) {
        return new TimerLauncher(timerConfig);
    }

}
