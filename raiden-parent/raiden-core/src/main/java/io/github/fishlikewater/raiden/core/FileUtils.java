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

import io.github.fishlikewater.raiden.core.exception.RaidenExceptionCheck;

import java.io.*;

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
     * @param file 文件
     * @return 文件缓冲输入流
     */
    public static BufferedInputStream getBufferInputStream(File file) {
        return new BufferedInputStream(getInputStream(file));
    }

}
