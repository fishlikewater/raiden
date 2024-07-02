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

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * {@code Hex}
 * </p>
 * 16进制转换
 *
 * @author fishlikewater@126.com
 * @version 1.0.2
 * @since 2024年06月08日 12:05
 **/

@SuppressWarnings("unused")
public class Hex {

    private static final Pattern PATTERN = Pattern.compile("^[0-9a-fA-F]+$", Pattern.CASE_INSENSITIVE);

    private static final char[] DIGITS_LOWER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private static final char[] DIGITS_UPPER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    /**
     * 判断是否是16进制数字
     *
     * @param value 待验证字符串
     * @return boolean
     */
    public static boolean isHexNumber(String value) {
        int index = 0;
        if (value.startsWith(CommonConstants.HEX_PREFIX, index) || value.startsWith(CommonConstants.HEX_PREFIX_UPPERCASE, index)) {
            index += 2;
        } else if (value.startsWith(CommonConstants.SYMBOL_EXPRESSION, index)) {
            index++;
        }
        final Matcher matcher = PATTERN.matcher(value);
        if (!matcher.matches()) {
            return false;
        }
        try {
            new BigInteger(value.substring(index), 16);
        } catch (final NumberFormatException e) {
            return false;
        }
        return true;
    }

    // ---------------------------------------------------------------- encode

    /**
     * 将字节数组转换为十六进制字符数组
     *
     * @param data byte[]
     * @return 十六进制char[]
     */
    public static char[] encodeHex(byte[] data) {
        return encodeHex(data, true);
    }

    /**
     * 将字节数组转换为十六进制字符数组
     *
     * @param data        byte[]
     * @param toLowerCase {@code true} 传换成小写格式 ， {@code false} 传换成大写格式
     * @return 十六进制char[]
     */
    public static char[] encodeHex(byte[] data, boolean toLowerCase) {
        if (toLowerCase) {
            return encodeHex(DIGITS_LOWER, data);
        }
        return encodeHex(DIGITS_UPPER, data);
    }

    /**
     * 将字节数组转换为十六进制字符串
     *
     * @param data byte[]
     * @return 十六进制String
     */
    public static String encodeHexStr(byte[] data) {
        return encodeHexStr(data, true);
    }

    /**
     * 将字符串转换为十六进制字符串，结果为小写，默认编码是UTF-8
     *
     * @param data 被编码的字符串
     * @return 十六进制String
     */
    public static String encodeHexStr(String data) {
        return encodeHexStr(data, StandardCharsets.UTF_8);
    }

    /**
     * 将字符串转换为十六进制字符串，结果为小写
     *
     * @param data    需要被编码的字符串
     * @param charset 编码
     * @return 十六进制String
     */
    public static String encodeHexStr(String data, Charset charset) {
        return encodeHexStr(StringUtils.bytes(data, charset), true);
    }

    /**
     * 将字节数组转换为十六进制字符串
     *
     * @param data        byte[]
     * @param toLowerCase {@code true} 传换成小写格式 ， {@code false} 传换成大写格式
     * @return 十六进制String
     */
    public static String encodeHexStr(byte[] data, boolean toLowerCase) {
        return new String(encodeHex(data, toLowerCase));
    }

    /**
     * 将字节数组转换为十六进制字符串
     *
     * @param data        byte[]
     * @param toLowerCase {@code true} 传换成小写格式 ， {@code false} 传换成大写格式
     * @return 十六进制String
     */
    public static String encodeHexStr(byte[] data, boolean toLowerCase, Charset charset) {
        return new String(encodeHex(data, toLowerCase));
    }


    private static char[] encodeHex(char[] alphabets, byte[] data) {
        final int len = data.length;
        final char[] out = new char[len << 1];
        for (int i = 0, j = 0; i < len; i++) {
            out[j++] = alphabets[(0xF0 & data[i]) >>> 4];
            out[j++] = alphabets[0x0F & data[i]];
        }
        return out;
    }

    // ---------------------------------------------------------------- decode

    /**
     * 将十六进制字符数组转换为字符串
     *
     * @param hexStr  十六进制String
     * @param charset 编码
     * @return 字符串
     */
    public static String decodeHexStr(String hexStr, Charset charset) {
        if (StringUtils.isEmpty(hexStr)) {
            return hexStr;
        }
        return StringUtils.str(decodeHex(hexStr), charset);
    }

    /**
     * 将十六进制字符数组转换为字符串
     *
     * @param hexData 十六进制char[]
     * @param charset 编码
     * @return 字符串
     */
    public static String decodeHexStr(char[] hexData, Charset charset) {
        return StringUtils.str(decodeHex(hexData), charset);
    }

    /**
     * 将十六进制字符数组转换为字节数组
     *
     * @param hexData 十六进制char[]
     * @return byte[]
     * @throws RuntimeException 如果源十六进制字符数组是一个奇怪的长度，将抛出运行时异常
     */
    public static byte[] decodeHex(char[] hexData) {
        return decodeHex(String.valueOf(hexData));
    }


    public static byte[] decodeHex(CharSequence hex) {
        isHexNumber(hex.toString());

        String encode = StringUtils.cleanBlank(hex);
        int length = encode.length();
        if (length % 2 != 0) {
            encode += "0";
            length += 1;
        }
        final byte[] out = new byte[length >> 1];
        int j = 0;
        for (int i = 0; i < length; i++) {
            final byte height = (byte) (encode.charAt(i) & 0x0f);
            i++;
            if (i >= length) {
                out[j] = (byte) ((0x0F & height) << 4);
            } else {
                final byte lower = (byte) (encode.charAt(i) & 0x0f);
                out[j] = (byte) (height << 4 | lower);
            }
            j++;
        }
        return out;
    }

}
