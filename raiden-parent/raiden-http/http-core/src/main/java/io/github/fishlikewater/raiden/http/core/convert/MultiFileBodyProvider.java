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
package io.github.fishlikewater.raiden.http.core.convert;

import io.github.fishlikewater.raiden.core.ObjectUtils;
import io.github.fishlikewater.raiden.core.StringUtils;
import io.github.fishlikewater.raiden.http.core.MultipartData;
import io.github.fishlikewater.raiden.http.core.constant.HttpConstants;
import io.github.fishlikewater.raiden.http.core.exception.HttpExceptionCheck;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.http.HttpRequest;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;
import java.util.stream.Collectors;

/**
 * 文件上传处理器
 *
 * @author fishlikewater@126.com
 * @version 1.0.0
 * @since 2023年09月27日 9:38
 **/
public class MultiFileBodyProvider implements HttpRequest.BodyPublisher {

    private long contentLength = 0L;
    private List<Path> paths;
    private byte[] paramByte;
    private MultipartData.FileStream fileStream;
    private final byte[] endBytes;
    private final List<byte[]> fileParams = new ArrayList<>();
    private final String boundary;

    public MultiFileBodyProvider(MultipartData multipartData, Object paramObj, String boundaryString) {
        this.boundary = boundaryString;

        this.handleParam(paramObj);

        MultipartData.FileStream fileStream = multipartData.getFileStream();
        if (ObjectUtils.isNotNullOrEmpty(fileStream)) {
            this.handleFileStream(fileStream);
        } else {
            this.handlePath(multipartData);
        }
        endBytes = (StringUtils.format("\r\n--{}--", boundary)).getBytes();
        contentLength += endBytes.length;
    }

    @Override
    public long contentLength() {
        return contentLength;
    }

    @Override
    public void subscribe(Flow.Subscriber<? super ByteBuffer> subscriber) {
        final SubmissionPublisher<ByteBuffer> submissionPublisher = new SubmissionPublisher<>();
        submissionPublisher.subscribe(subscriber);
        final ByteBuffer paramBuffer = copy2(paramByte, paramByte.length);
        submissionPublisher.submit(paramBuffer);

        if (ObjectUtils.isNullOrEmpty(this.paths)) {
            this.subStream(submissionPublisher);
        } else {
            this.subPaths(submissionPublisher);
        }

        final ByteBuffer endBuffer = copy2(endBytes, endBytes.length);
        submissionPublisher.submit(endBuffer);
        submissionPublisher.close();
    }

    private void subStream(SubmissionPublisher<ByteBuffer> submissionPublisher) {
        final byte[] bytes = fileParams.getFirst();
        submissionPublisher.submit(copy2(bytes, bytes.length));
        try (InputStream inputStream = this.fileStream.getInputStream()) {
            int readCount;
            byte[] readByte = new byte[HttpConstants.DEFAULT_READ_LIMIT];
            while ((readCount = inputStream.read(readByte)) != -1) {
                submissionPublisher.submit(copy2(readByte, readCount));
            }
        } catch (Exception e) {
            HttpExceptionCheck.INSTANCE.throwUnchecked(e);
        }
    }

    private void subPaths(SubmissionPublisher<ByteBuffer> submissionPublisher) {
        for (int i = 0; i < paths.size(); i++) {
            final byte[] bytes = fileParams.get(i);
            submissionPublisher.submit(copy2(bytes, bytes.length));
            final File file = paths.get(i).toFile();
            try (FileInputStream fileInputStream = new FileInputStream(file)) {
                int readCount;
                byte[] readByte = new byte[HttpConstants.DEFAULT_READ_LIMIT];
                while ((readCount = fileInputStream.read(readByte)) != -1) {
                    submissionPublisher.submit(copy2(readByte, readCount));
                }
            } catch (Exception e) {
                HttpExceptionCheck.INSTANCE.throwUnchecked(e);
            }
        }
    }

    private ByteBuffer copy2(byte[] content, int length) {
        ByteBuffer b = ByteBuffer.allocate(length);
        b.put(content, 0, length);
        b.flip();
        return b;
    }

    private void handlePath(MultipartData multipartData) {
        if (ObjectUtils.isNotNullOrEmpty(multipartData.getPaths())) {
            paths = Arrays.stream(multipartData.getPaths()).map(Path::of).collect(Collectors.toList());
        } else {
            paths = Arrays.stream(multipartData.getFiles()).map(file -> Path.of(file.getPath())).collect(Collectors.toList());
        }
        for (int i = 0; i < paths.size(); i++) {
            try {
                final File file = paths.get(i).toFile();
                String fileData = StringUtils.format("{}--{}\r\nContent-Disposition: form-data; name=\"file\"; filename=\"{}\"\r\nContent-Type: application/octet-stream\r\n\r\n", i > 0 ? "\r\n" : "", boundary, file.getName());
                final byte[] bytes = fileData.getBytes();
                fileParams.add(bytes);
                contentLength += bytes.length;
                contentLength += file.length();
            } catch (Exception e) {
                HttpExceptionCheck.INSTANCE.throwUnchecked(e, "build file data error");
            }
        }
    }

    private void handleFileStream(MultipartData.FileStream fileStream) {
        this.fileStream = fileStream;
        String fileData = StringUtils.format("--{}\r\nContent-Disposition: form-data; name=\"file\"; filename=\"{}\"\r\nContent-Type: application/octet-stream\r\n\r\n", boundary, fileStream.getFileName());
        final byte[] bytes = fileData.getBytes();
        fileParams.add(bytes);
        contentLength += bytes.length;
        contentLength += fileStream.getSize();
    }

    private void handleParam(Object paramObj) {
        StringBuilder paramData = new StringBuilder();
        if (Objects.nonNull(paramObj)) {
            Map<String, Object> paramMap = ObjectUtils.beanToMap(paramObj, true);
            paramMap.forEach((k, v) -> {
                paramData.append("--").append(boundary).append("\r\n");
                paramData.append("Content-Disposition: form-data; name=\"").append(k).append("\"\r\n\r\n").append(v).append("\r\n");
            });
        }
        this.paramByte = paramData.toString().getBytes();
        this.contentLength += this.paramByte.length;
    }
}
