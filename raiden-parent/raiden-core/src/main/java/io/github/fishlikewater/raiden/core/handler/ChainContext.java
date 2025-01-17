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
package io.github.fishlikewater.raiden.core.handler;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * ChainContext
 *
 * @author zhangxiang
 * @version 1.1.2
 * @since 2025/1/17
 **/
public class ChainContext {

    private final Map<String, Object> context = new HashMap<>(8);

    public static ChainContext getInstance() {
        return new ChainContext();
    }

    private ChainContext() {
    }

    public ChainContext addProperty(String key, Object value) {
        context.put(key, value);
        return this;
    }

    public Object getProperty(String key) {
        return context.get(key);
    }

    public <T> T getProperty(String key, Function<Object, T> function) {
        return function.apply(context.get(key));
    }

    public void clear() {
        context.clear();
    }

    public ChainContext removeProperty(String key) {
        context.remove(key);
        return this;
    }
}
