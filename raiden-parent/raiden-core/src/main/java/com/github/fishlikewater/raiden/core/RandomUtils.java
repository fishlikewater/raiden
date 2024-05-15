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
package com.github.fishlikewater.raiden.core;

import java.security.SecureRandom;
import java.util.UUID;

/**
 * {@code RandomUtils}
 * 随机数生成工具类
 *
 * @author zhangxiang
 * @version 1.0.0
 * @since 2024/05/07
 */
@SuppressWarnings("unused")
public class RandomUtils {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    public static final char[] NUMBER_ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    private RandomUtils() {
        throw new IllegalAccessError("RandomUtils class");
    }

    /**
     * 封装JDK自带的UUID, 长度32, 中间无-分割.
     *
     * @return 一个随机码, 形如:5ec24ed3ff1a41c18d23a37af006bbb3
     */
    public static String uuid() {
        return randomStringByUuid().replaceAll("-", "");
    }

    /**
     * 使用SecureRandom随机生成Long.
     *
     * @return the long
     */
    public static long randomLong() {
        long number = SECURE_RANDOM.nextLong();
        if (Long.MIN_VALUE == number) {
            return Long.MAX_VALUE;
        } else {
            return Math.abs(number);
        }
    }

    /**
     * 使用SecureRandom随机生成int.
     *
     * @return the int
     */
    public static int randomInt() {
        int number = SECURE_RANDOM.nextInt();
        if (Integer.MIN_VALUE == number) {
            return Integer.MAX_VALUE;
        } else {
            return Math.abs(number);
        }
    }

    /**
     * 范围内随机数字
     *
     * @param from 开始
     * @param to   结束
     * @return long
     */
    public static long randomLong(long from, long to) {
        return from + SECURE_RANDOM.nextLong(to - from);
    }

    /**
     * 范围内随机数字
     *
     * @param from 开始
     * @param to   结束
     * @return int int
     */
    public static int randomInt(int from, int to) {
        return from + SECURE_RANDOM.nextInt(to - from);
    }

    /**
     * 指定位数数字
     *
     * @param charCount 位数
     * @return String rand num
     */
    public static String randNum(int charCount) {
        StringBuilder charValue = new StringBuilder();
        for (int i = 0; i < charCount; i++) {
            char c = (char) (randomInt(0, 10) + '0');
            charValue.append(c);
        }
        return charValue.toString();
    }

    /**
     * 指定位数数字和字母
     *
     * @param charCount 位数
     * @return String rand num
     */
    public static String randomNumberAndAlphabet(int charCount) {
        StringBuilder charValue = new StringBuilder();
        for (int i = 0; i < charCount; i++) {
            char c = NUMBER_ALPHABET[randomInt(0, NUMBER_ALPHABET.length)];
            charValue.append(c);
        }
        return charValue.toString();
    }

    /**
     * 生成UUID
     *
     * @return string
     */
    private static String randomStringByUuid() {
        return UUID.randomUUID().toString();
    }
}