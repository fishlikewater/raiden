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
package io.github.fishlikewater;

import io.github.fishlikewater.raiden.core.FileUtils;
import io.github.fishlikewater.raiden.core.enums.FileMagicNumberEnum;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * {@code FileUtilsTest}
 *
 * @author zhangxiang
 * @version 1.0.3
 * @since 2024/07/02
 */
public class FileUtilsTest {

    @Test
    public void testFileUtils() {
        File file = FileUtils.file("test.png");
        InputStream inputStream = FileUtils.readFile(file);
        FileMagicNumberEnum read = FileUtils.readMagicNum(inputStream);
        Assert.assertNotNull(read);
    }

    @Test
    public void readMagic() {
        File file = FileUtils.file("test.png");
        FileMagicNumberEnum read = FileUtils.readMagicNum(file);
        Assert.assertNotNull(read);
    }

    @Test
    public void readFile() {
        File file = FileUtils.file("classpath:test.txt");
        String readFileUtf8 = FileUtils.readFileUtf8(file);
        Assert.assertEquals("123", readFileUtf8);
    }

    @Test
    public void getFileSuffix() {
        String fileSuffix = FileUtils.getFileSuffix("test.txt");
        Assert.assertEquals("txt", fileSuffix);
    }

    @Test
    public void getFileName() {
        String fileName = FileUtils.getFileName("test.txt");
        Assert.assertEquals("test", fileName);
    }

    @Test
    public void readFile2() {
        byte[] bytes = FileUtils.readFile("classpath:test.txt");
        Assert.assertEquals("123", new String(bytes, StandardCharsets.UTF_8));
    }

    @Test
    public void readLines() {
        File file = FileUtils.file("test.txt");
        List<String> lines = FileUtils.readLinesUtf8(file);
        Assert.assertEquals("123", lines.getFirst());
    }

    @Test
    public void readLines2() {
        File file = FileUtils.file("test.txt");
        List<String> lines = FileUtils.readLines(file, StandardCharsets.ISO_8859_1);
        Assert.assertEquals("123", lines.getFirst());
    }

    @Test
    public void readLine() {
        File file = FileUtils.file("test.txt");
        FileUtils.readLine(file, System.out::println);
    }

    @Test
    public void readLine2() {
        File file = FileUtils.file("test.txt");
        FileUtils.readLine(file, Charset.forName("gbk"), System.out::println);
    }
}
