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

import cn.hutool.core.io.resource.ResourceUtil;
import io.github.fishlikewater.raiden.core.constant.CommonConstants;
import io.github.fishlikewater.raiden.core.enums.FileMagicNumberEnum;
import io.github.fishlikewater.raiden.core.exception.RaidenExceptionCheck;

import java.io.*;
import java.nio.charset.Charset;

/**
 * {@code FileUtils}
 * 文件工具类
 *
 * @author zhangxiang
 * @version 1.0.3
 * @since 2024/06/28
 */
public class FileUtils {

    /**
     * 获取文件名
     *
     * @param fileName 文件名
     * @return 文件名 不带后缀
     */
    public static String getFileName(String fileName) {
        if (fileName == null) {
            return null;
        }
        int index = fileName.lastIndexOf(".");
        return index == -1 ? null : fileName.substring(0, index);
    }

    /**
     * 获取文件后缀
     *
     * @param fileName 文件名
     * @return 文件后缀
     */
    public static String getFileSuffix(String fileName) {
        if (fileName == null) {
            return null;
        }
        int index = fileName.lastIndexOf(".");
        return index == -1 ? null : fileName.substring(index + 1);
    }

    /**
     * 获取文件输入流
     *
     * @param path 文件路径
     * @return 文件输入流
     */
    public static InputStream getInputStream(String path) {
        return ResourceUtil.getStream(path);
    }

    /**
     * 获取文件输入流
     *
     * @param file 文件
     * @return 文件输入流
     */
    public static InputStream getInputStream(File file) {
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            return RaidenExceptionCheck.INSTANCE.throwUnchecked("not.found.file", e);
        }
    }

    /**
     * 获取文件缓冲输入流
     *
     * @param path    文件路径
     * @param charset 字符集
     * @return 文件缓冲输入流
     */
    public static BufferedReader getReader(String path, Charset charset) {
        return ResourceUtil.getReader(path, charset);
    }

    /**
     * 获取文件字节数组
     *
     * @param path 文件路径
     * @return 文件字节数组
     */
    public static byte[] getBytes(String path) {
        return ResourceUtil.readBytes(path);
    }

    /**
     * 获取文件字符串
     *
     * @param path 文件路径
     * @return 文件字符串
     */
    public static String readString(String path) {
        return ResourceUtil.readUtf8Str(path);
    }

    /**
     * 获取文件字符串
     *
     * @param path    文件路径
     * @param charset 字符集
     * @return 文件字符串
     */
    public static String readString(String path, Charset charset) {
        return ResourceUtil.readStr(path, charset);
    }

    /**
     * 获取文件缓冲输入流
     *
     * @param file 文件
     * @return 文件缓冲输入流
     */
    public static BufferedInputStream getBufferInputStream(File file) {
        return new BufferedInputStream(getInputStream(file));
    }

    /**
     * 读取文件头 魔数
     *
     * @param fileName    文件名
     * @param inputStream 文件输入流
     * @return 文件头
     */
    public static FileMagicNumberEnum read(String fileName, InputStream inputStream) {
        try {
            byte[] head = new byte[CommonConstants.FILE_HEADER_LENGTH];
            int ignored = inputStream.read(head, 0, CommonConstants.FILE_HEADER_LENGTH);
            String header = Hex.encodeHexStr(head);
            String suffix = getFileSuffix(fileName);

            return FileMagicNumberEnum.codeOf(header, suffix);
        } catch (Exception e) {
            return RaidenExceptionCheck.INSTANCE.throwUnchecked(e);
        }
    }
}
