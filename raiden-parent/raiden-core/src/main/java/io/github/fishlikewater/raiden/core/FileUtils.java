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
import io.github.fishlikewater.raiden.core.enums.FileMagicNumberEnum;
import io.github.fishlikewater.raiden.core.exception.RaidenExceptionCheck;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.List;
import java.util.function.Consumer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.regex.Pattern;
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

    private static final Pattern PATTERN_PATH_ABSOLUTE = Pattern.compile("^[a-zA-Z]:([/\\\\].*)?");

    public static File file(String path) {
        if (ObjectUtils.isNullOrEmpty(path)) {
            RaidenExceptionCheck.INSTANCE.throwUnchecked("path is null or empty");
        }
        if (path.startsWith(CommonConstants.Symbol.SYMBOL_PATH) || PATTERN_PATH_ABSOLUTE.matcher(path).matches()) {
            return new File(path);
        }

        // 检查是否为classpath路径
        if (path.startsWith(CommonConstants.CLASS_PATH)) {
            path = path.substring(CommonConstants.CLASS_PATH.length());
        }
        URL resource = FileUtils.class.getClassLoader().getResource(path);
        if (ObjectUtils.isNotNullOrEmpty(resource)) {
            return new File(resource.getFile());
        } else {
            return RaidenExceptionCheck.INSTANCE.throwUnchecked("Resource not found in classpath: {}", path);
        }
    }

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
    public static InputStream readFile(File file) {
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            return RaidenExceptionCheck.INSTANCE.throwUnchecked("not.found.file", e);
        }
    }

    /**
     * 读取classpath目录文件为字节流
     *
     * @param path 资源路径
     * @return 文件内容的字节数组
     */
    public static byte[] readFile(String path) {
        File file = file(path);
        try (InputStream inputStream = new FileInputStream(file)) {
            return inputStream.readAllBytes();
        } catch (IOException e) {
            return RaidenExceptionCheck.INSTANCE.throwUnchecked(e);
        }
    }

    /**
     * 读取文件为指定字符集的字符串
     *
     * @param file    文件
     * @param charset 字符集
     * @return 文件内容的字符串
     */
    public static String readFile(File file, Charset charset) {
        try {
            return Files.readString(Paths.get(file.getAbsolutePath()), charset);
        } catch (IOException e) {
            return RaidenExceptionCheck.INSTANCE.throwUnchecked("read.file.error", e);
        }
    }

    /**
     * 读取文件为UTF-8字符串
     *
     * @param file 文件
     * @return 文件内容的UTF-8字符串
     */
    public static String readFileUtf8(File file) {
        return readFile(file, StandardCharsets.UTF_8);
    }

    /**
     * 读取文件为指定字符集的行
     *
     * @param file    文件
     * @param charset 字符集
     * @return 文件内容的行
     */
    public static List<String> readLines(File file, Charset charset) {
        try {
            return Files.readAllLines(file.toPath(), charset);
        } catch (IOException e) {
            return RaidenExceptionCheck.INSTANCE.throwUnchecked("read.file.error", e);
        }
    }

    /**
     * @param file 文件
     * @return 文件内容的行(utf8编码)
     */
    public static List<String> readLinesUtf8(File file) {
        return readLines(file, StandardCharsets.UTF_8);
    }

    /**
     * 读取文件为指定字符集的行
     *
     * @param file     文件
     * @param consumer 每一行的处理
     */
    public static void readLine(File file, Consumer<String> consumer) {
        try (BufferedReader buffer = getBufferReader(file)) {
            String line;
            while ((line = buffer.readLine()) != null) {
                consumer.accept(line);
            }
        } catch (IOException e) {
            RaidenExceptionCheck.INSTANCE.throwUnchecked("read.file.error", e);
        }
    }

    /**
     * 读取文件为指定字符集的行
     *
     * @param file     文件
     * @param consumer 每一行的处理
     * @param charset  字符集
     */
    public static void readLine(File file, Charset charset, Consumer<String> consumer) {
        try (BufferedReader buffer = getBufferReader(file, charset)) {
            String line;
            while ((line = buffer.readLine()) != null) {
                consumer.accept(line);
            }
        } catch (IOException e) {
            RaidenExceptionCheck.INSTANCE.throwUnchecked("read.file.error", e);
        }
    }

    /**
     * 读取文件头 魔数
     *
     * @param inputStream 文件输入流
     * @return 文件头
     */
    public static FileMagicNumberEnum readMagicNum(InputStream inputStream) {
        try {
            byte[] head = new byte[CommonConstants.FILE_HEADER_LENGTH];
            int ignored = inputStream.read(head, 0, CommonConstants.FILE_HEADER_LENGTH);
            String header = Hex.encodeHexStr(head);

            return FileMagicNumberEnum.resolve(header);
        } catch (Exception e) {
            return RaidenExceptionCheck.INSTANCE.throwUnchecked(e);
        }
    }

    /**
     * 读取文件头 魔数
     *
     * @param file 文件
     * @return 文件头
     */
    public static FileMagicNumberEnum readMagicNum(File file) {
        return readMagicNum(readFile(file));
    }

    /**
     * 获取文件缓冲输入流
     *
     * @param file 文件
     * @return 文件缓冲输入流
     */
    public static BufferedInputStream getBufferInputStream(File file) {
        return new BufferedInputStream(readFile(file));
    }

    /**
     * 获取文件缓冲输入流
     *
     * @param file 文件
     * @return 文件缓冲输入流
     */
    public static BufferedReader getBufferReader(File file) {
        try {
            return new BufferedReader(new InputStreamReader(readFile(file), StandardCharsets.UTF_8));
        } catch (Exception e) {
            return RaidenExceptionCheck.INSTANCE.throwUnchecked(e);
        }
    }

    /**
     * 获取文件缓冲输入流
     *
     * @param file    文件
     * @param charset 编码
     * @return 文件缓冲输入流
     */
    public static BufferedReader getBufferReader(File file, Charset charset) {
        try {
            return new BufferedReader(new InputStreamReader(readFile(file), charset));
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
