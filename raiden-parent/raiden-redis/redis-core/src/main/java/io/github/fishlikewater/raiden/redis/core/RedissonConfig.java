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

import org.redisson.config.*;

/**
 * {@code RedissonConfig}
 *
 * @author zhangxiang
 * @version 1.0.0
 * @since 2024/05/15
 */
public class RedissonConfig extends Config {

    @Override
    public void setSingleServerConfig(SingleServerConfig config) {
        super.setSingleServerConfig(config);
    }

    @Override
    public void setClusterServersConfig(ClusterServersConfig config) {
        super.setClusterServersConfig(config);
    }

    @Override
    public void setSentinelServersConfig(SentinelServersConfig config) {
        super.setSentinelServersConfig(config);
    }

    @Override
    public void setMasterSlaveServersConfig(MasterSlaveServersConfig config) {
        super.setMasterSlaveServersConfig(config);
    }

    @Override
    public void setReplicatedServersConfig(ReplicatedServersConfig config) {
        super.setReplicatedServersConfig(config);
    }
}
