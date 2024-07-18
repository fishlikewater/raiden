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
package io.github.fishlikewater.raiden.config.ini;

import io.github.fishlikewater.raiden.core.LambdaUtils;
import io.github.fishlikewater.raiden.core.ObjectUtils;
import lombok.NonNull;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * {@code Ini}
 * ini文件对象
 *
 * @author zhangxiang
 * @version 1.0.3
 * @since 2024/07/16
 */
@SuppressWarnings("all")
public class Ini implements Serializable {

    @Serial
    private static final long serialVersionUID = 4502090835088587726L;

    private Map<String, Section> sectionMap;

    public Ini() {
        this.sectionMap = new HashMap<>();
    }

    public Section getSection(@NonNull String sectionName) {
        return this.sectionMap.get(sectionName);
    }

    public <T> T get(@NonNull String key) {
        return (T) this.getSection("default").get(key);
    }

    public <T> T get(@NonNull String section, @NonNull String key) {
        return (T) this.sectionMap.get(section).get(key);
    }

    public void add(Section section) {
        this.sectionMap.put(section.getSectionName(), section);
    }

    public <T> void add(@NonNull String key, @NonNull T value) {
        Section section = this.getSection("default");
        if (ObjectUtils.isNullOrEmpty(section)) {
            section = new Section("default", new HashMap<>());
            this.add(section);
        }
        section.set(key, value);
    }

    public List<Section> getSections() {
        return LambdaUtils.toList(this.sectionMap.values(), section -> section);
    }
}
