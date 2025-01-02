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
package io.github.fishlikewater.raiden.redis.core;

import io.github.fishlikewater.raiden.redis.core.enums.ServerPattern;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.redisson.client.codec.Codec;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.MasterSlaveServersConfig;
import org.redisson.config.ReplicatedServersConfig;
import org.redisson.config.SentinelServersConfig;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * {@code RedissonCfg}
 * Redisson配置类
 * </p>
 *
 * @author fishlikewater@126.com
 * @version 1.0.0
 * @since 2024年05月13日 20:35
 **/
@Data
public class RedissonPatternCfg implements Serializable {

    @Serial
    private static final long serialVersionUID = 8090491648279726067L;

    protected ServerPattern serverPattern;

    protected Cluster cluster;

    protected MasterSlave masterSlave;

    protected Sentinel sentinel;

    protected Replicated replicated;

    private Single single;

    /**
     * Redisson序列化编码
     */
    private Codec codec;

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class Cluster extends ClusterServersConfig implements Serializable {

        @Serial
        private static final long serialVersionUID = 3585907187838736297L;
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class MasterSlave extends MasterSlaveServersConfig implements Serializable {

        @Serial
        private static final long serialVersionUID = -8491781193081842558L;
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class Sentinel extends SentinelServersConfig implements Serializable {

        @Serial
        private static final long serialVersionUID = -5453222334364343368L;
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class Replicated extends ReplicatedServersConfig implements Serializable {

        @Serial
        private static final long serialVersionUID = -8755746104156339625L;
    }

    @Data
    public static class Single implements Serializable {

        private String address;

        private String password;

        private String username;

        private int database = 0;

        private int subscriptionConnectionMinimumIdleSize = 1;

        private int subscriptionConnectionPoolSize = 50;

        private int connectionMinimumIdleSize = 24;

        private int connectionPoolSize = 64;

        private long dnsMonitoringInterval = 5000;

        private int idleConnectionTimeout = 10000;

        private int connectTimeout = 10000;

        private int timeout = 3000;

        private int subscriptionTimeout = 7500;

        private int retryAttempts = 3;

        private int retryInterval = 1500;
    }
}
