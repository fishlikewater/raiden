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
package io.github.fishlikewater.raiden.core;

import io.github.fishlikewater.raiden.core.constant.CommonConstants;
import io.github.fishlikewater.raiden.core.enums.FileMagicNumberEnum;
import io.github.fishlikewater.raiden.core.exception.RaidenExceptionCheck;

import java.io.*;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.stream.Stream;

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

    /**
     * 替换jar 中的文件 生成新的jar
     *
     * @param oldJar        旧的jar
     * @param dir           修改后的类目录
     * @param classFullName 替换文件的 类全名
     */
    public static void replaceClassWithJar(File oldJar, String dir, String... classFullName) {
        if (ObjectUtils.isNullOrEmpty(dir)) {
            dir = oldJar.getAbsolutePath();
        }
        File newJar = new File(oldJar.getAbsolutePath().replace(".jar", "-new.jar"));
        List<String> list = Stream.of(classFullName)
                .map(item -> item.replace(".", "/") + ".class")
                .toList();
        try (JarFile jarFile = new JarFile(oldJar);
             JarOutputStream jarOutputStream = new JarOutputStream(new FileOutputStream(newJar))) {
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry jarEntry = entries.nextElement();
                try (InputStream inputStream = list.contains(jarEntry.getName())
                        ? new FileInputStream(new File(dir, jarEntry.getName()))
                        : jarFile.getInputStream(jarEntry)) {
                    jarOutputStream.putNextEntry(new JarEntry(jarEntry.getName()));
                    byte[] bytes = inputStream.readAllBytes();
                    jarOutputStream.write(bytes);
                    jarOutputStream.closeEntry();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
