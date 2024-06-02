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

import io.github.fishlikewater.raiden.core.func.LambdaFunction;

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
