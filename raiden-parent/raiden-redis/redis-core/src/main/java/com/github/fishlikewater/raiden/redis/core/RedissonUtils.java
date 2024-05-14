/*
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
package com.github.fishlikewater.raiden.redis.core;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;

/**
 * <p>
 * {@code RedissonUtils}
 *
 * </p>
 *
 * @author fishlikewater@126.com
 * @version 1.0.0
 * @since 2024年05月13日 20:38
 **/
public class RedissonUtils {

    public static RedissonClient redissonClient(RedissonCfg cfg) {
        Config config = new Config();
        config.useSingleServer()
                .setAddress(cfg.getUrl())
                .setPassword(cfg.getPassWord())
                .setConnectionMinimumIdleSize(cfg.getConnectionMinimumIdleSize())
                .setConnectionPoolSize(cfg.getConnectionPoolSize())
                .setDatabase(cfg.getDataBase());
        config.setCodec(new JsonJacksonCodec());
        return Redisson.create(config);
    }
}
