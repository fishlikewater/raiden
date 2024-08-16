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
package io.github.fishlikewater.raiden.core;

import cn.hutool.core.util.StrUtil;
import io.github.fishlikewater.raiden.core.constant.CommonConstants;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

import java.util.Map;
import java.util.Set;

/**
 * {@code StringUtils}
 * 字符串工具类
 *
 * @author zhangxiang
 * @version 1.0.0
 * @since 2024/05/30
 */
public final class StringUtils extends StrUtil {

    /**
     * <p>格式化字符串</p>
     * <p>例:</p>
     * <ol>
     *     <li>StringUtils.format("a={},b={}", 1, 2) // a=1,b=2</li>
     * </ol>
     *
     * @param text 字符串
     * @param args 参数
     * @return 格式化后的字符串
     */
    public static String format(String text, Object... args) {
        assert null != text;
        FormattingTuple formattingTuple = MessageFormatter.arrayFormat(text, args);
        return formattingTuple.getMessage();
    }

    /**
     * <p>将map转换为字符串 默认使用 & 分隔符</p>
     * <p>例:</p>
     * <pre>
     *     Map<String, Object> map = new HashMap<>();
     *     map.put("a", 1);
     *     map.put("b", 2);
     *     String str = StringUtils.toStr(map); // a=1&b=2
     * </pre>
     *
     * @param map map
     * @return 字符串
     */
    public static <T> String toStr(Map<String, T> map) {
        return toStr(map, CommonConstants.Symbol.SYMBOL_AND);
    }

    /**
     * <p>将map转换为字符串 默认使用 symbol 分隔符</p>
     * <p>例:</p>
     * <pre>
     *     Map<String, Object> map = new HashMap<>();
     *     map.put("a", 1);
     *     map.put("b", 2);
     *     String str = StringUtils.toStr(map, "|"); // a=1|b=2
     * </pre>
     *
     * @param map    map
     * @param symbol 分隔符
     * @return 字符串
     */
    public static <T> String toStr(Map<String, T> map, String symbol) {
        StringBuilder content = new StringBuilder();
        Set<String> keys = map.keySet();

        for (String key : keys) {
            T value = map.get(key);
            if (ObjectUtils.isNullOrEmpty(key) || ObjectUtils.isNullOrEmpty(value)) {
                continue;
            }
            content.append(symbol)
                    .append(key)
                    .append("=")
                    .append(value);
        }
        content.delete(0, 1);
        return content.toString();
    }
}
