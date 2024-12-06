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
package io.github.fishlikewater.raiden.http.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.Accessors;

import java.io.File;
import java.io.InputStream;
import java.nio.file.OpenOption;
import java.nio.file.Path;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.WRITE;

/**
 * 文件数据
 *
 * @author fishlikewater@126.com
 * @version 1.0.0
 * @since 2023年09月26日 16:13
 **/
@Data
@Accessors(chain = true)
public class MultipartData {

    private String[] paths;

    private File[] files;

    private Path path;

    private FileStream fileStream;

    private boolean fileDownload;

    private OpenOption[] openOptions = new OpenOption[]{CREATE, WRITE};

    private MultipartData() {
    }

    private MultipartData(String[] paths) {
        this.paths = paths;
    }

    private MultipartData(File[] files) {
        this.files = files;
    }

    private MultipartData(Path path, OpenOption... openOptions) {
        this.path = path;
        this.openOptions = openOptions;
    }

    public static MultipartData ofFileUpload(String... paths) {
        return new MultipartData(paths);
    }

    public static MultipartData ofFileUpload(File... files) {
        return new MultipartData(files);
    }

    public static MultipartData ofFileUpload(InputStream inputStream, @NonNull String fileName, long size) {

        FileStream fileStream = new FileStream(inputStream, fileName, size);
        MultipartData multipartData = new MultipartData();
        multipartData.setFileStream(fileStream);
        return multipartData;
    }

    public static MultipartData ofFileDownload(Path path, OpenOption... openOptions) {
        MultipartData multipartData = new MultipartData(path, openOptions);
        multipartData.setFileDownload(true);
        return multipartData;
    }

    @Data
    @Accessors(chain = true)
    @AllArgsConstructor
    public static class FileStream {

        private InputStream inputStream;

        private String fileName;

        private long size;
    }
}
