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
import io.github.fishlikewater.raiden.core.constant.CommonConstants;
import io.github.fishlikewater.raiden.core.enums.SortEnum;
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
    public void testMapMapToStr() {
        Map<String, Object> map = new HashMap<>();
        map.put("a", 1);
        map.put("b", 2);
        String str = StringUtils.mapToStr(map);
        Assert.isTrue(str.equals("a=1&b=2"), "map to str error");
        String str1 = StringUtils.mapToStr(map, "|");
        Assert.isTrue(str1.equals("a=1|b=2"), "map to str error");

        String str2 = StringUtils.format("{a} and {b}", map, true);
        Assert.isTrue(str2.equals("1 and 2"), "map to str error");
    }

    @Test
    public void testMapSortToStr() {
        Map<String, Object> map = new HashMap<>();
        map.put("c", 3);
        map.put("a", 1);
        map.put("b", 2);
        String str = StringUtils.mapSortToStr(map, SortEnum.DESC);
        String str2 = StringUtils.mapSortToStr(map, CommonConstants.Symbol.SYMBOL_AT, SortEnum.ASC);
        Assert.isTrue(str.equals("c=3&b=2&a=1"), "map to str error");
        Assert.isTrue(str2.equals("a=1@b=2@c=3"), "map to str error");
    }

    @Test
    public void testLowerFirst() {
        String str = "ABC";
        String lowerFirst = StringUtils.lowerFirst(str);
        Assert.isTrue(StringUtils.equals(lowerFirst, "aBC"), "lowerFirst with error");
    }

    @Test
    public void testEndWithAny() {
        String str = "abc";
        Assert.isTrue(StringUtils.endWithAny(str, "c", "b"), "endWithAny with error");
    }

    @Test
    public void testStartWithAny() {
        String str = "abc";
        Assert.isTrue(StringUtils.startWithAny(str, "a", "b"), "startWithAny with error");
    }

    @Test
    public void testEndWithIgnoreCase() {
        String str = "abc";
        Assert.isTrue(StringUtils.endWithIgnoreCase(str, "C"), "endWithIgnoreCase with error");
    }
}
