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

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;
import java.util.function.Function;

/**
 * {@code Section}
 *
 * @author zhangxiang
 * @version 1.0.3
 * @since 2024/07/16
 */
@Data
@Builder
@NoArgsConstructor
@SuppressWarnings("all")
public class Section implements Serializable {

    @Serial
    private static final long serialVersionUID = 7346968778301372686L;

    private String sectionName;

    private Map<String, String> pairs;

    public Section(String sectionName, Map<String, String> pairs) {
        this.sectionName = sectionName;
        this.pairs = pairs;
    }

    public <T> T get(String key, Function<String, T> fn) {
        return fn.apply(pairs.get(key));
    }

    public <T> void set(String key, T value) {
        this.pairs.put(key, value.toString());
    }
}
