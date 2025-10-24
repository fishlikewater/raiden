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
package io.github.fishlikewater.raiden.core;

import io.github.fishlikewater.raiden.core.constant.CommonConstants;
import io.github.fishlikewater.raiden.core.enums.SortEnum;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;

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

    public static final String SPACE = " ";

    public static final String EMPTY = "";

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
     *     <li>StringUtils.isNotBlank("a") // true</li>
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
     *     String str = StringUtils.mapToStr(map); // a=1&b=2
     * </pre>
     *
     * @param map map
     * @return 字符串
     */
    public static <T> String mapToStr(Map<String, T> map) {
        return mapToStr(map, CommonConstants.Symbol.SYMBOL_AND);
    }

    /**
     * <p>将map转换为字符串 默认使用 symbol 分隔符</p>
     * <p>例:</p>
     * <pre>
     *     Map<String, Object> map = new HashMap<>();
     *     map.put("a", 1);
     *     map.put("b", 2);
     *     String str = StringUtils.mapToStr(map, "|"); // a=1|b=2
     * </pre>
     *
     * @param map    map
     * @param symbol 分隔符
     * @return 字符串
     */
    public static <T> String mapToStr(Map<String, T> map, String symbol) {
        return mapToStr(map, symbol, null);
    }

    /**
     * <p>将map转换为字符串 默认使用 symbol 分隔符</p>
     * <p>例:</p>
     * <pre>
     *     Map<String, Object> map = new HashMap<>();
     *     map.put("a", 1);
     *     map.put("b", 2);
     *     String str = StringUtils.mapToStr(map, "|", value -> value.toString()); // a=1|b=2
     * </pre>
     *
     * @param map       map
     * @param symbol    分隔符
     * @param valueFunc 值转换函数
     */
    public static <T, V> String mapToStr(Map<String, T> map, String symbol, Function<T, V> valueFunc) {
        return mapToStr(map, symbol, CommonConstants.Symbol.SYMBOL_EQUAL, valueFunc);
    }

    /**
     * <p>将map转换为字符串 默认使用 symbol 分隔符</p>
     * <p>例:</p>
     * <pre>
     *     Map<String, Object> map = new HashMap<>();
     *     map.put("a", 1);
     *     map.put("b", 2);
     *     String str = StringUtils.mapToStr(map, "|", "", value -> value.toString(), "=", "|"); // a1|b2
     * </pre>
     *
     * @param map           map
     * @param separator     分隔符
     * @param linkSeparator key value 连接符
     * @param valueFunc     值转换函数
     */
    public static <T, V> String mapToStr(Map<String, T> map, String separator, String linkSeparator, Function<T, V> valueFunc) {
        StringBuilder content = new StringBuilder();
        Set<String> keys = map.keySet();

        for (String key : keys) {
            T value = map.get(key);
            if (ObjectUtils.isNullOrEmpty(key) || ObjectUtils.isNullOrEmpty(value)) {
                continue;
            }

            if (StringUtils.isNotBlank(separator)) {
                content.append(separator);
            }

            content.append(key);

            if (StringUtils.isNotBlank(linkSeparator)) {
                content.append(linkSeparator);
            }

            if (ObjectUtils.isNullOrEmpty(valueFunc)) {
                content.append(value);
            } else {
                content.append(valueFunc.apply(value));
            }
        }
        if (StringUtils.isNotBlank(separator)) {
            content.delete(0, separator.length());
        }

        return content.toString();
    }

    /**
     * <p>将map按key排序转换为字符串</p>
     * <p>例:</p>
     * <pre>
     *     Map<String, Object> map = new HashMap<>();
     *     map.put("a", 1);
     *     map.put("b", 2);
     *     String str = StringUtils.mapSortToStr(map, true); // a=1|b=2
     * </pre>
     *
     * @param map  map
     * @param sort 排序方式
     */
    public static <T> String mapSortToStr(Map<String, T> map, SortEnum sort) {
        return mapSortToStr(map, CommonConstants.Symbol.SYMBOL_AND, sort);
    }

    /**
     * <p>将map按key排序转换为字符串</p>
     * <p>例:</p>
     * <pre>
     *     Map<String, Object> map = new HashMap<>();
     *     map.put("a", 1);
     *     map.put("b", 2);
     *     String str = StringUtils.mapSortToStr(map, "|", true); // a=1|b=2
     * </pre>
     *
     * @param map    map
     * @param symbol 分隔符
     * @param sort   排序方式
     */
    public static <T> String mapSortToStr(Map<String, T> map, String symbol, SortEnum sort) {
        return mapSortToStr(map, symbol, sort, null);
    }

    /**
     * <p>将map按key排序转换为字符串</p>
     * <p>例:</p>
     * <pre>
     *     Map<String, Object> map = new HashMap<>();
     *     map.put("a", 1);
     *     map.put("b", 2);
     *     String str = StringUtils.mapSortToStr(map, "|", true, value -> value.toString()); // a=1|b=2
     * </pre>
     *
     * @param map       map
     * @param symbol    分隔符
     * @param sort      排序方式
     * @param valueFunc 值转换函数
     */
    public static <T, V> String mapSortToStr(Map<String, T> map, String symbol, SortEnum sort, Function<T, V> valueFunc) {
        return mapSortToStr(map, symbol, CommonConstants.Symbol.SYMBOL_EQUAL, sort, valueFunc);
    }

    /**
     * <p>将map按key排序转换为字符串</p>
     * <p>例:</p>
     * <pre>
     *     Map<String, Object> map = new HashMap<>();
     *     map.put("a", 1);
     *     map.put("b", 2);
     *     String str = StringUtils.mapSortToStr(map, "|", "", SortEnum.ASC,  value -> value.toString()); // a1|b2
     * </pre>
     *
     * @param map           map
     * @param separator     分隔符
     * @param linkSeparator key value 连接符
     * @param sort          排序方式
     * @param valueFunc     值转换函数
     */
    public static <T, V> String mapSortToStr(Map<String, T> map, String separator, String linkSeparator, SortEnum sort, Function<T, V> valueFunc) {
        TreeMap<String, T> treeMap;
        if (sort == SortEnum.ASC) {
            treeMap = new TreeMap<>(map);
        } else {
            treeMap = new TreeMap<>(Comparator.reverseOrder());
            treeMap.putAll(map);
        }
        return mapToStr(treeMap, separator, linkSeparator, valueFunc);
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
    public static String[] toStringArray(Collection<String> collection) {
        return (CollectionUtils.isNotEmpty(collection) ? collection.toArray(EMPTY_STRING_ARRAY) : EMPTY_STRING_ARRAY);
    }

    /**
     * 将字符串切分为List
     *
     * @param str 字符串
     * @return 切分后的List
     */
    public static List<String> splitList(String str) {
        return splitList(str, CommonConstants.Symbol.SYMBOL_COMMA);
    }

    /**
     * 将字符串切分为List
     *
     * @param str       字符串
     * @param separator 分隔符
     * @return 切分后的List
     */
    public static List<String> splitList(String str, String separator) {
        if (str == null) {
            return Collections.emptyList();
        }
        String[] array = tokenizeToStringArray(str, separator, true, true);
        return Arrays.asList(array);
    }

    /**
     * 将字符串切分为List
     *
     * @param str    字符串
     * @param mapper 映射
     * @return 切分后的List
     */
    public static <T> List<T> splitList(String str, Function<String, T> mapper) {
        List<String> list = splitList(str);
        return LambdaUtils.toList(list, mapper);
    }

    /**
     * 将字符串切分为List
     *
     * @param str       字符串
     * @param separator 分隔符
     * @param mapper    映射
     * @return 切分后的List
     */
    public static <T> List<T> splitList(String str, String separator, Function<String, T> mapper) {
        List<String> list = splitList(str, separator);
        return LambdaUtils.toList(list, mapper);
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
     * 首字母大写
     *
     * @param string 原始字符串
     * @return 首字母小写字符串
     */
    public static String upperFirst(String string) {
        if (StringUtils.isNotBlank(string)) {
            if (string.length() > 1) {
                return Character.toUpperCase(string.charAt(0)) + string.substring(1);
            } else {
                return string.toUpperCase();
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
     * 比较两个字符串是否不相等
     *
     * @param str1 字符串1
     * @param str2 字符串2
     * @return 是否相等
     */
    public static boolean notEquals(String str1, String str2) {
        return !equals(str1, str2);
    }

    /**
     * 判断字符串是否相等
     *
     * @param source 源字符串
     * @param target 目标字符串
     * @return 是否相等
     */
    public static boolean anyEquals(String source, String... target) {
        for (String string : target) {
            if (equals(source, string)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 判断字符串是否不相等
     *
     * @param source 源字符串
     * @param target 目标字符串
     * @return 是否相等
     */
    public static boolean allNotEquals(String source, String... target) {
        return !anyEquals(source, target);
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
     * 判断字符串是否以指定任一字符串结尾
     *
     * @param currentValue 当前值
     * @param endValues    结尾值
     * @return 结果
     */
    public static boolean endWithAny(String currentValue, String... endValues) {
        if (StringUtils.isBlank(currentValue) || ObjectUtils.isNullOrEmpty(endValues)) {
            return false;
        }
        for (String endValue : endValues) {
            if (StringUtils.endWith(currentValue, endValue)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 判断字符串是否以指定字符串结尾，忽略大小写
     *
     * @param str   字符串
     * @param match 匹配字符串
     * @return 结果
     */
    public static boolean endWithIgnoreCase(String str, String match) {
        if (StringUtils.isBlank(str)) {
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
     * 判断字符串是否以指定任一字符串开头
     *
     * @param currentValue 当前值
     * @param startValues  开始值
     * @return 结果
     */
    public static boolean startWithAny(String currentValue, String... startValues) {
        if (StringUtils.isBlank(currentValue) || ObjectUtils.isNullOrEmpty(startValues)) {
            return false;
        }
        for (String startValue : startValues) {
            if (StringUtils.startWith(currentValue, startValue)) {
                return true;
            }
        }

        return false;
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
            template2 = replace(template2, CommonConstants.Symbol.SYMBOL_LEFT_BRACKET + entry.getKey() + CommonConstants.Symbol.SYMBOL_RIGHT_BRACKET, value);
        }
        return template2;
    }

    /**
     * 拼接URL参数
     *
     * @param url URL
     * @param map 参数
     * @return 拼接后的URL
     */
    public static String urlJoinParam(String url, Map<String, String> map) {
        if (Objects.isNull(map) || map.isEmpty()) {
            return url;
        }
        StringBuilder newUrl = new StringBuilder(url);
        if (!url.contains(CommonConstants.Symbol.URL_PARAMETER_SPLIT)) {
            newUrl.append(CommonConstants.Symbol.URL_PARAMETER_SPLIT);
        }
        for (Map.Entry<String, String> item : map.entrySet()) {
            char c = newUrl.charAt(newUrl.length() - 1);
            String param = StringUtils.format("{}{}={}",
                    c == '?' ? "" : CommonConstants.Symbol.SYMBOL_AND, item.getKey().trim(),
                    URLEncoder.encode(item.getValue().trim(), StandardCharsets.UTF_8));
            newUrl.append(param);
        }
        return newUrl.toString();
    }

    /**
     * 集合转字符串
     *
     * @param collection 集合
     * @return 集合转字符串
     */
    public static String join(Collection<String> collection) {
        return join(collection, CommonConstants.Symbol.SYMBOL_COMMA);
    }

    /**
     * 集合转字符串
     *
     * @param collection 集合
     * @param separator  分隔符
     * @return 字符串
     */
    public static String join(Collection<String> collection, String separator) {
        if (collection == null || collection.isEmpty()) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (String item : collection) {
            sb.append(item).append(separator);
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    /**
     * 集合转字符串
     *
     * @param collection 集合
     * @param function   转换函数
     * @param separator  分隔符
     * @return 集合转字符串
     */
    public static <T> String join(Collection<T> collection, Function<T, String> function, String separator) {
        if (collection == null || collection.isEmpty()) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (T item : collection) {
            sb.append(function.apply(item)).append(separator);
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
}
