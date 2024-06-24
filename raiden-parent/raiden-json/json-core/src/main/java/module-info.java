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
/**
 * {@code module-info}
 * json工具依赖模块管理
 *
 * @author zhangxiang
 * @since 2024/06/17
 */
module raiden.json.core {
    requires com.fasterxml.jackson.databind;
    requires cn.hutool.core;
    requires transitive org.slf4j;
    requires static lombok;
    requires com.google.gson;
    requires cn.hutool.json;
    requires com.fasterxml.jackson.datatype.jsr310;

    exports io.github.fishlikewater.raiden.json.core;
    exports io.github.fishlikewater.raiden.json.core.annotation;
    exports io.github.fishlikewater.raiden.json.core.jackson;
}