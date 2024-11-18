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
import jakarta.annotation.Nullable;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

import java.util.*;

/**
 * {@code StringUtils}
 * 字符串工具类
 *
 * @author zhangxiang
 * @version 1.0.0
 * @since 2024/05/30
 */
public final class StringUtils extends StrUtil {

    private static final String[] EMPTY_STRING_ARRAY = {};

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

    /**
     * Copy from spring utils StringUtils<br/>
     * Tokenize the given {@code String} into a {@code String} array via a
     * {@link StringTokenizer}.
     *
     * @param str               the {@code String} to tokenize (potentially {@code null} or empty)
     * @param delimiters        the delimiter characters, assembled as a {@code String}
     *                          (each of the characters is individually considered as a delimiter)
     * @param trimTokens        trim the tokens via {@link String#trim()}
     * @param ignoreEmptyTokens omit empty tokens from the result array
     *                          (only applies to tokens that are empty after trimming; StringTokenizer
     *                          will not consider subsequent delimiters as token in the first place).
     * @return an array of the tokens
     * @see java.util.StringTokenizer
     * @see String#trim()
     */
    public static String[] tokenizeToStringArray(String str, String delimiters, boolean trimTokens, boolean ignoreEmptyTokens) {
        if (str == null) {
            return EMPTY_STRING_ARRAY;
        }

        StringTokenizer st = new StringTokenizer(str, delimiters);
        List<String> tokens = new ArrayList<>();
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if (trimTokens) {
                token = token.trim();
            }
            if (!ignoreEmptyTokens || !token.isEmpty()) {
                tokens.add(token);
            }
        }
        return toStringArray(tokens);
    }

    /**
     * Copy from spring utils StringUtils<br/>
     * Copy the given {@link Collection} into a {@code String} array.
     * <p>The {@code Collection} must contain {@code String} elements only.
     *
     * @param collection the {@code Collection} to copy
     *                   (potentially {@code null} or empty)
     * @return the resulting {@code String} array
     */
    public static String[] toStringArray(@Nullable Collection<String> collection) {
        return (CollectionUtils.isNotEmpty(collection) ? collection.toArray(EMPTY_STRING_ARRAY) : EMPTY_STRING_ARRAY);
    }
}
