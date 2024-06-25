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
package io.github.fishlikewater.raiden.redis.autoconfig;

import io.github.fishlikewater.raiden.redis.core.RedissonPatternCfg;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serial;
import java.io.Serializable;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * {@code RedisProperties}
 *
 * @author zhangxiang
 * @version 1.0.0
 * @since 2024/05/15
 */
@Data
@ConfigurationProperties(prefix = "raiden.redis")
@EqualsAndHashCode(callSuper = true)
public class RedisProperties extends RedissonPatternCfg {

    @Serial
    private static final long serialVersionUID = 2232525714538349168L;

    private boolean enabled;

    private Delay delay;

    private Cache cache;

    @Data
    public static class Cache implements Serializable {

        @Serial
        private static final long serialVersionUID = -5450845588631494332L;

        private boolean enabled;

        private String prefix;

        private Duration expirationTime;

        private TimeUnit timeUnit;
    }

    @Data
    public static class Delay implements Serializable {

        @Serial
        private static final long serialVersionUID = 612468000501891604L;

        private boolean enabled;

        private String topic;
    }
}
