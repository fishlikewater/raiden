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

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * {@code HeadWrap}
 * 自定义头包装
 *
 * @author zhangxiang
 * @version 1.0.0
 * @since 2024/03/19
 */
@Data
public class HeadWrap {

    private List<Head> heads;

    public HeadWrap of(String key, String value) {
        if (heads == null) {
            heads = new ArrayList<>();
        }
        heads.add(new Head(key, value));
        return this;
    }

    public HeadWrap of(Map<String, String> headMap) {
        if (heads == null) {
            heads = new ArrayList<>();
        }
        headMap.forEach((key, value) -> heads.add(new Head(key, value)));
        return this;
    }

    @Data
    public static class Head implements Serializable {
        @Serial
        private static final long serialVersionUID = 2787385130800187496L;

        private String key;

        private String value;

        public Head(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }
}
