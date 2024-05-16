package com.github.fishlikewater.raiden.redis.core;

import com.github.fishlikewater.raiden.core.func.LambdaFunction;
import com.github.fishlikewater.raiden.core.func.LambdaMeta;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

import java.io.Serializable;

/**
 * {@code DefaultRedisClientProxyImpl}
 *
 * @author zhangxiang
 * @version 1.0.0
 * @since 2024/05/16
 */
public class DefaultRedisClientProxyImpl implements RedisClientProxy {

    private final RedissonClient redissonClient;

    public DefaultRedisClientProxyImpl(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public <T extends Serializable> void put(String key, T value) {
        RBucket<T> bucket = this.redissonClient.getBucket(key);
        bucket.set(value);
    }

    @Override
    public <R, T extends Serializable> void put(LambdaFunction<R, ?> keyFunc, T value) {
        String key = LambdaMeta.resolve(keyFunc);
        this.put(key, value);
    }
}
