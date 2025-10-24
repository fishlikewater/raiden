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
        String str3 = StringUtils.mapSortToStr(map, CommonConstants.Symbol.SYMBOL_AT, "", SortEnum.ASC, v -> v);
        String str4 = StringUtils.mapSortToStr(map, "", "", SortEnum.ASC, v -> v);
        Assert.isTrue(str.equals("c=3&b=2&a=1"), "map to str error");
        Assert.isTrue(str2.equals("a=1@b=2@c=3"), "map to str error");
        Assert.isTrue(str3.equals("a1@b2@c3"), "map to str error");
        Assert.isTrue(str4.equals("a1b2c3"), "map to str error");
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

    @Test
    public void testUrlJoinParam() {
        String url = "https://www.baidu.com";
        String url1 = StringUtils.urlJoinParam(url, Map.of("a", "1"));
        Assert.isTrue(StringUtils.equals(url1, "https://www.baidu.com?a=1"), "urlJoinParam with error");
    }

    @Test
    public void anyEquals() {
        Assert.isTrue(StringUtils.anyEquals("1", "1", "2"), "anyEquals with error");
    }

    @Test
    public void anyNotEquals() {
        Assert.isTrue(StringUtils.allNotEquals("0", "1", "2"), "anyEquals with error");
    }
}
