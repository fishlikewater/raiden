package com.github.fishlikewater.raiden.redis.core;

import com.github.fishlikewater.raiden.core.func.LambdaFunction;

import java.io.Serializable;

/**
 * {@code RedisClientProxy}
 * redis 操作接口定义
 *
 * @author zhangxiang
 * @version 1.0.0
 * @since 2024/05/16
 */
public interface RedisClientProxy {

    /**
     * 缓存
     *
     * @param key   key
     * @param value value
     * @param <T>   value type
     */
    <T extends Serializable> void put(String key, T value);

    /**
     * 缓存
     *
     * @param keyFunc keyFunc
     * @param value   value
     * @param <T>     value type
     */
    <R, T extends Serializable> void put(LambdaFunction<R, ?> keyFunc, T value);
}
