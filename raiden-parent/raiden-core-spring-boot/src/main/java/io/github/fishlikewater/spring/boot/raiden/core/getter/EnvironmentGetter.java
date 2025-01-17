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
package io.github.fishlikewater.spring.boot.raiden.core.getter;

import io.github.fishlikewater.raiden.core.StringUtils;
import org.springframework.core.env.Environment;

/**
 * {@code EnvironmentGetter}
 *
 * @author zhangxiang
 * @version 1.0.0
 * @since 2024/04/30
 */
public interface EnvironmentGetter {

    String SPRING_PROFILES_ACTIVE_KEY = "spring.profiles.active";

    String SPRING_PROFILES_ACTIVE_DEV = "dev";
    String SPRING_PROFILES_ACTIVE_TEST = "test";
    String SPRING_PROFILES_ACTIVE_PRO = "pro";
    String SPRING_PROFILES_ACTIVE_PROD = "prod";

    /**
     * 获取当前环境
     *
     * @return 当前环境
     */
    default String determineCurrentProfilesActive() {
        return this.environment().getProperty(SPRING_PROFILES_ACTIVE_KEY);
    }

    /**
     * 是否是线下环境
     *
     * @param profiles 当前环境
     * @return 是否是线下环境
     */
    default boolean determineOffline(String profiles) {
        if (StringUtils.isBlank(profiles)) {
            return false;
        }

        return profiles.contains(SPRING_PROFILES_ACTIVE_DEV) || profiles.contains(SPRING_PROFILES_ACTIVE_TEST);
    }

    /**
     * 是否是线上环境
     *
     * @param profiles 当前环境
     * @return 是否是线上环境
     */
    default boolean determineIsLine(String profiles) {
        if (StringUtils.isBlank(profiles)) {
            return false;
        }
        return profiles.contains(SPRING_PROFILES_ACTIVE_PRO) || profiles.contains(SPRING_PROFILES_ACTIVE_PROD);
    }

    /**
     * 获取Environment
     *
     * @return Environment
     */
    Environment environment();
}
