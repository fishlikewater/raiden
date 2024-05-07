/*
 * Copyright © 2024 zhangxiang (fishlikewater@126.com)
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
package com.github.fishlikewater.test.controller;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import com.github.fishlikewater.test.domain.DemoPayload;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * {@code DemoController}
 * 测试controller
 *
 * @author fishlikewater@126.com
 * @since 2024/03/19
 * @version 1.0.0
 */
@RestController
public class DemoController {

    @PatchMapping("/patch")
    public String patch(@RequestBody DemoPayload payload) {
        System.out.println(payload);
        return "patch";
    }

    @PostMapping("/demo")
    public String demo(@RequestBody DemoPayload payload, @RequestHeader("customer") String customer) {
        System.out.println(payload);
        System.out.println(STR."接受到的请求头customer:\{customer}");
        return "ok";
    }

    @PostMapping(value = "/form", consumes = "application/x-www-form-urlencoded;charset=utf-8")
    public String form(DemoPayload payload) {
        System.out.println(payload);
        return "ok";
    }

    @GetMapping(value = "/form", consumes = "application/x-www-form-urlencoded;charset=utf-8")
    public String form2(DemoPayload payload) {
        System.out.println(payload);
        return "ok";
    }

    @PostMapping("/upload")
    public String upload(@RequestParam(value = "file") MultipartFile file) throws IOException {
        String fileName = IdUtil.getSnowflakeNextIdStr() + file.getOriginalFilename();
        saveFile(file, fileName);
        return "file";
    }

    private void saveFile(MultipartFile file, String fileName) throws IOException {
        final String localFileDir = "/file";
        final String yearAndMonth = DateUtil.format(LocalDateTime.now(), DatePattern.SIMPLE_MONTH_PATTERN);
        final File file1 = new File(localFileDir + File.separator + yearAndMonth + File.separator + fileName);
        if (!file1.exists()) {
            final File parentFile = file1.getParentFile();
            if (!parentFile.exists()) {
                boolean mkdir = parentFile.mkdirs();
                if (!mkdir) {
                    throw new RuntimeException("创建文件夹失败");
                }
            }
            final boolean newFile = file1.createNewFile();
            if (newFile) {
                FileUtil.writeBytes(file.getBytes(), file1);
            }
        }
    }
}
