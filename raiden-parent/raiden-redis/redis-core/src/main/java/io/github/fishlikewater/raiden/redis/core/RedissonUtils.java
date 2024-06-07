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
package io.github.fishlikewater.raiden.redis.core;

import cn.hutool.core.bean.BeanUtil;
import io.github.fishlikewater.raiden.core.ObjectUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;

import java.util.function.BiConsumer;

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

    public static RedissonClient redissonClient(RedissonPatternCfg cfg, BiConsumer<RedissonConfig, RedissonPatternCfg> fx) {
        RedissonConfig config = new RedissonConfig();
        final SingleServerConfig singleServerConfig = new Config().useSingleServer();
        BeanUtil.copyProperties(cfg.getSingle(), singleServerConfig, true);
        config.setSingleServerConfig(singleServerConfig);

        fx.accept(config, cfg);
        return Redisson.create(config);
    }

    public static RedissonClient redissonClient(RedissonPatternCfg cfg) {
        return switch (cfg.getServerPattern()) {
            case CLUSTER -> redissonClientCluster(cfg, RedissonUtils::codec);
            case MASTER_SLAVE -> redissonClientMasterSlave(cfg, RedissonUtils::codec);
            case REPLICATED -> redissonClientReplicated(cfg, RedissonUtils::codec);
            case SENTINEL -> redissonClientSentinel(cfg, RedissonUtils::codec);
            default -> redissonClient(cfg, RedissonUtils::codec);
        };
    }

    private static RedissonClient redissonClientSentinel(RedissonPatternCfg cfg, BiConsumer<RedissonConfig, RedissonPatternCfg> fx) {
        RedissonConfig config = new RedissonConfig();
        RedissonPatternCfg.Sentinel sentinel = cfg.getSentinel();
        if (ObjectUtils.isNullOrEmpty(sentinel)) {
            throw new IllegalArgumentException("RedissonConfig.sentinelServerConfig is null");
        }
        config.setSentinelServersConfig(sentinel);

        fx.accept(config, cfg);
        return Redisson.create(config);
    }

    private static RedissonClient redissonClientReplicated(RedissonPatternCfg cfg, BiConsumer<RedissonConfig, RedissonPatternCfg> fx) {
        RedissonConfig config = new RedissonConfig();
        RedissonPatternCfg.Replicated replicated = cfg.getReplicated();
        if (ObjectUtils.isNullOrEmpty(replicated)) {
            throw new IllegalArgumentException("RedissonConfig.replicatedServerConfig is null");
        }
        config.setReplicatedServersConfig(replicated);

        fx.accept(config, cfg);
        return Redisson.create(config);
    }

    private static RedissonClient redissonClientMasterSlave(RedissonPatternCfg cfg, BiConsumer<RedissonConfig, RedissonPatternCfg> fx) {
        RedissonConfig config = new RedissonConfig();
        RedissonPatternCfg.MasterSlave masterSlave = cfg.getMasterSlave();
        if (ObjectUtils.isNullOrEmpty(masterSlave)) {
            throw new IllegalArgumentException("RedissonConfig.masterSlaveServerConfig is null");
        }
        config.setMasterSlaveServersConfig(masterSlave);

        fx.accept(config, cfg);
        return Redisson.create(config);
    }

    private static RedissonClient redissonClientCluster(RedissonPatternCfg cfg, BiConsumer<RedissonConfig, RedissonPatternCfg> fx) {
        RedissonConfig config = new RedissonConfig();
        RedissonPatternCfg.Cluster cluster = cfg.getCluster();
        if (ObjectUtils.isNullOrEmpty(cluster)) {
            throw new IllegalArgumentException("RedissonConfig.clusterServerConfig is null");
        }
        config.setClusterServersConfig(cluster);

        fx.accept(config, cfg);
        return Redisson.create(config);
    }

    private static void codec(RedissonConfig config, RedissonPatternCfg cfg) {
        if (ObjectUtils.isNullOrEmpty(cfg.getCodec())) {
            config.setCodec(new JsonJacksonCodec());
        } else {
            config.setCodec(cfg.getCodec());
        }
    }
}
