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
package io.github.fishlikewater.raiden.http.core.remote;

import io.github.fishlikewater.raiden.http.core.MultipartData;
import io.github.fishlikewater.raiden.http.core.annotation.GET;
import io.github.fishlikewater.raiden.http.core.annotation.HttpServer;
import io.github.fishlikewater.raiden.http.core.annotation.POST;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

/**
 * {@code DemoFile}
 * 测试文件
 *
 * @author fishlikewater@126.com
 * @version 1.0.0
 * @since 2024/03/20
 */
@HttpServer(url = "http://127.0.0.1:9000")
public interface DemoFile {

    /**
     * 上传文件
     *
     * @param data 文件路径
     * @return 文件上传结果
     */
    @POST("/upload")
    String uploadFile(MultipartData data);

    /**
     * 上传文件
     *
     * @param data 文件路径
     * @return 文件上传结果
     */
    @POST("/upload2")
    String uploadFile2(MultipartData data);

    /**
     * 下载文件
     *
     * @return 文件下载结果
     */
    @GET("/download")
    InputStream download();

    /**
     * 下载文件
     *
     * @return 文件下载结果
     */
    @GET("/download")
    CompletableFuture<InputStream> downloadAsync();

    /**
     * 下载文件
     *
     * @param data 文件路径
     * @return 文件下载结果
     */
    @GET(value = "https://p.qqan.com/up/2024-1/17042617506341657.jpg")
    Path download(MultipartData data);

    /**
     * 下载文件
     *
     * @return 文件
     */
    @POST("https://p.qqan.com/up/2024-1/17042617506341657.jpg")
    CompletableFuture<byte[]> download2();
}
