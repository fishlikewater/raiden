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
package io.github.fishlikewater.raiden.generate;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

/**
 * {@code FileConfigRead}
 * 读取文件
 *
 * @author zhangxiang
 * @version 1.0.3
 * @since 2024/07/18
 */
public interface FileConfigRead {

    /**
     * 读取文件
     *
     * @param path 文件路径
     * @return JSONObject
     */

    default JSONObject readFileAsJsonObject(String path) {
        return JSONUtil.parseObj(ResourceUtil.readUtf8Str(path));
    }

    /**
     * 读取文件
     *
     * @param path 文件路径
     * @return JSONArray
     */
    default JSONArray readFileAsJsonArray(String path) {
        return JSONUtil.parseArray(ResourceUtil.readUtf8Str(path));
    }
}
