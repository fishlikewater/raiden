/*
 * Copyright (c) 2023-2025 zhangxiang (fishlikewater@126.com)
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
package io.github.fishlikewater;

import io.github.fishlikewater.raiden.core.model.SmartMap;
import org.junit.Test;

import java.util.HashMap;

/**
 * {@code SmartMapTest}
 * SmartMap测试
 *
 * @author zhangxiang
 * @version 1.1.3
 * @since 2025/02/08
 */
public class SmartMapTest {

    @Test
    public void test() {
        SmartMap<String, Object> map = new SmartMap<>();
        map.put("a", "1");
        map.put("b", 2);
        map.put("c", true);
        map.put("d", 3.0);
        HashMap<Object, Object> hashMap = new HashMap<>();
        hashMap.put("e", "4");
        hashMap.put("f", 5);
        map.put("g", hashMap);
        System.out.println(map.getString("a"));
        System.out.println(map.getInteger("b"));
        System.out.println(map.getBoolean("c"));
        System.out.println(map.getDouble("d"));
        System.out.println(map.getMap("g").getString("e"));
        System.out.println(map.getMap("g").getInteger("f"));
    }
}
