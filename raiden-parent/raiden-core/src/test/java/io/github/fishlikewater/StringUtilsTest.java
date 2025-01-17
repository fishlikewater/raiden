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
package io.github.fishlikewater;

import io.github.fishlikewater.raiden.core.Assert;
import io.github.fishlikewater.raiden.core.StringUtils;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * {@code StringUtilsTest}
 *
 * @author zhangxiang
 * @version 1.0.0
 * @since 2024/05/30
 */
public class StringUtilsTest {

    @Test
    public void testMapToStr() {
        Map<String, Object> map = new HashMap<>();
        map.put("a", 1);
        map.put("b", 2);
        String str = StringUtils.toStr(map);
        Assert.isTrue(str.equals("a=1&b=2"), "map to str error");
        String str1 = StringUtils.toStr(map, "|");
        Assert.isTrue(str1.equals("a=1|b=2"), "map to str error");

        String str2 = StringUtils.format("{a} and {b}", map, true);
        Assert.isTrue(str2.equals("1 and 2"), "map to str error");
    }
}
