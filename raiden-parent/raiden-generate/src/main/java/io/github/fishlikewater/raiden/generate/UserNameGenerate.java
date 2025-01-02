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

import cn.hutool.json.JSONObject;
import io.github.fishlikewater.raiden.core.ObjectUtils;
import io.github.fishlikewater.raiden.core.RandomUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * {@code UserNameGenerate}
 * 用户名生成器
 *
 * @author zhangxiang
 * @version 1.0.0
 * @since 2024/05/31
 */
@Slf4j
public class UserNameGenerate extends AbstractGenerate<String> {

    private List<String> lastName = new ArrayList<>();

    private List<String> name = new ArrayList<>();

    @Override
    public String generate() {
        this.init();
        StringBuilder sb = new StringBuilder();
        int nameLen = RandomUtils.randomInt(1, 3);
        sb.append(lastName.get(RandomUtils.randomInt(0, lastName.size())));
        for (int i = 0; i < nameLen; i++) {
            sb.append(name.get(RandomUtils.randomInt(0, name.size())));
        }
        return sb.toString();
    }

    private void init() {
        if (this.determineIsNotInitialized()) {
            JSONObject jsonObject = this.readFileAsJsonObject("userName.json");
            lastName = jsonObject.getBeanList("lastName", String.class);
            name = jsonObject.getBeanList("name", String.class);
        }
    }

    private boolean determineIsNotInitialized() {
        return ObjectUtils.isNullOrEmpty(lastName) || ObjectUtils.isNullOrEmpty(name);
    }
}
