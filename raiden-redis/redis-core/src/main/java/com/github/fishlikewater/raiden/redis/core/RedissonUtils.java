package com.github.fishlikewater.raiden.redis.core;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;

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

    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress(configProperties.getRedissonCfg().getUrl())
                .setPassword(configProperties.getRedissonCfg().getPassWord())
                .setConnectionMinimumIdleSize(configProperties.getRedissonCfg().getConnectionMinimumIdleSize())
                .setConnectionPoolSize(configProperties.getRedissonCfg().getConnectionPoolSize())
                .setDatabase(configProperties.getRedissonCfg().getDataBase());
        return Redisson.create(config);
    }
}
