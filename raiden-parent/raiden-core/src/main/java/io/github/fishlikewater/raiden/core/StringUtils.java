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

import io.github.fishlikewater.raiden.core.constant.CommonConstants;
import jakarta.annotation.Nullable;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * {@code StringUtils}
 * 字符串工具类
 *
 * @author zhangxiang
 * @version 1.0.0
 * @since 2024/05/30
 */
public final class StringUtils {

    private static final String[] EMPTY_STRING_ARRAY = {};

    /**
     * <p>判断字符串是否为空</p>
     * <p>例:</p>
     * <ol>
     *     <li>StringUtils.isBlank(null) // true</li>
     *     <li>StringUtils.isBlank("") // true</li>
     *     <li>StringUtils.isBlank(" ") // true</li>
     *     <li>StringUtils.isBlank(" a") // false</li>
     * </ol>
     *
     * @param str 字符串
     * @return 是否为空
     */
    public static boolean isBlank(CharSequence str) {
        return null == str || str.isEmpty();
    }

    /**
     * <p>判断字符串是否不为空</p>
     * <p>例:</p>
     * <ol>
     *     <li>StringUtils.isNotBlank(null) // false</li>
     *     <li>StringUtils.isNotBlank("") // false</li>
     *     <li>StringUtils.isNotBlank(" ") // false</li>
     *     <li>StringUtils.isNotBlank(" a") // true</li>
     * </ol>
     *
     * @param str 字符串
     * @return 是否不为空
     */
    public static boolean isNotBlank(CharSequence str) {
        return !isBlank(str);
    }

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
     * <p>字符串替换</p>
     * <p>例:</p>
     * <pre>
     *     StringUtils.replace("a=1&b=2", "&", "|") // a=1|b=2
     * </pre>
     *
     * @param value       字符串
     * @param searchStr   要替换的字符串
     * @param replacement 替换的字符串
     * @return 替换后的字符串
     */
    public static String replace(String value, String searchStr, String replacement) {
        return value.replace(searchStr, replacement);
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
    @SuppressWarnings("all")
    public static String[] toStringArray(@Nullable Collection<String> collection) {
        return (CollectionUtils.isNotEmpty(collection) ? collection.toArray(EMPTY_STRING_ARRAY) : EMPTY_STRING_ARRAY);
    }

    /**
     * 清理空白字符
     *
     * @param str 被清理的字符串
     * @return 清理后的字符串
     */
    public static String cleanBlank(CharSequence str) {
        int len = str.length();
        final StringBuilder sb = new StringBuilder(len);
        char c;
        for (int i = 0; i < len; i++) {
            c = str.charAt(i);
            if (!isBlankChar(c)) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 是否空白符<br>
     * 空白符包括空格、制表符、全角空格和不间断空格<br>
     *
     * @param c 字符
     * @return 是否空白符
     */
    public static boolean isBlankChar(int c) {
        return Character.isWhitespace(c)
                || Character.isSpaceChar(c)
                || c == '\ufeff'
                || c == '\u202a'
                || c == '\u0000'
                || c == 'ㅤ'
                || c == '⠀'
                || c == '\u180e';
    }

    /**
     * <p>将byte[]转换为字符串</p>
     * <p>例:</p>
     * <pre>
     *     byte[] key = "123456".getBytes();
     *     String str = StringUtils.utf8Str(key); // 123456
     * </pre>
     *
     * @param key byte[]
     * @return 字符串
     */
    public static String utf8Str(byte[] key) {
        return new String(key, StandardCharsets.UTF_8);
    }

    /**
     * <p>将字符串转换为byte[]</p>
     * <p>例:</p>
     * <pre>
     *     String str = "123456";
     *     byte[] key = StringUtils.bytes(str); // [49,50,51,52,53,54]
     * </pre>
     *
     * @param data 字符串
     * @return byte[]
     */
    public static byte[] bytes(String data, Charset charset) {
        return data.getBytes(charset);
    }

    /**
     * 首字母小写
     *
     * @param string 原始字符串
     * @return 首字母小写字符串
     */
    public static String lowerFirst(String string) {
        if (StringUtils.isNotBlank(string)) {
            if (string.length() > 1) {
                return Character.toLowerCase(string.charAt(0)) + string.substring(1);
            } else {
                return string.toLowerCase();
            }
        }
        return string;
    }

    /**
     * 比较两个字符串是否相等
     *
     * @param str1 字符串1
     * @param str2 字符串2
     * @return 是否相等
     */
    public static boolean equals(String str1, String str2) {
        return Objects.equals(str1, str2);
    }

    /**
     * 判断字符串是否以指定字符串结尾
     *
     * @param currentValue 当前值
     * @param endValue     结尾值
     * @return 结果
     */
    public static boolean endWith(String currentValue, String endValue) {
        return StringUtils.isNotBlank(currentValue)
                && StringUtils.isNotBlank(endValue)
                && currentValue.endsWith(endValue);
    }

    /**
     * 判断字符串是否以指定字符串结尾，忽略大小写
     *
     * @param str   字符串
     * @param match 匹配字符串
     * @return 结果
     */
    public static boolean endWithIgnoreCase(String str, String match) {
        if (null == str) {
            return false;
        }
        if (str.length() < match.length()) {
            return false;
        }
        int strOffset = str.length() - match.length();
        return str.regionMatches(true, strOffset, match, 0, match.length());
    }

    /**
     * 判断字符串是否以指定字符串开头
     *
     * @param currentValue 当前值
     * @param startValue   开始值
     * @return 结果
     */
    public static boolean startWith(String currentValue, String startValue) {
        return StringUtils.isNotBlank(currentValue)
                && StringUtils.isNotBlank(startValue)
                && currentValue.startsWith(startValue);
    }

    /**
     * 判断字符串是否以指定字符串开头，忽略大小写
     *
     * @param str   字符串
     * @param match 匹配字符串
     * @return 结果
     */
    public static boolean startWithIgnoreCase(String str, String match) {
        if (null == str) {
            return false;
        }
        if (str.length() < match.length()) {
            return false;
        }
        return str.regionMatches(true, 0, match, 0, match.length());
    }

    /**
     * 格式化文本，使用 {varName} 占位<br>
     * map = {a: "aValue", b: "bValue"}; format("{a} and {b}", map);    ---->    aValue and bValue
     *
     * @param template 文本模板，被替换的部分用 {key} 表示
     * @param map      参数值对
     * @return 格式化后的文本
     */
    public static String format(CharSequence template, Map<?, ?> map, boolean ignoreNull) {
        if (null == template) {
            return null;
        }
        if (null == map || map.isEmpty()) {
            return template.toString();
        }

        String template2 = template.toString();
        String value;
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            value = entry.getValue().toString();
            if (null == value && ignoreNull) {
                continue;
            }
            template2 = replace(template2, "{" + entry.getKey() + "}", value);
        }
        return template2;
    }
}
