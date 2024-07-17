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
package io.github.fishlikewater.raiden.generate;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import io.github.fishlikewater.raiden.json.core.JSONUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 * {@code AbstractGenerate}
 *
 * @author zhangxiang
 * @version 1.0.0
 * @since 2024/05/31
 */
public abstract class AbstractGenerate<T> implements Generate<T> {

    protected JSONObject readFileAsJsonObject(String path) {
        File file = FileUtil.file(path);
        return JSONUtils.HutoolJSON.readJSONObject(file, StandardCharsets.UTF_8);
    }

    protected JSONArray readFileAsJsonArray(String path) {
        File file = FileUtil.file(path);
        return JSONUtils.HutoolJSON.readJSONArray(file, StandardCharsets.UTF_8);
    }
}
