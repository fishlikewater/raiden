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
 *
 * @author zhangxiang
 * @since 2024/06/17
 */
module raiden.core.spring.boot {
    requires spring.beans;
    requires static lombok;
    requires spring.context;
    requires spring.core;
    requires raiden.core;
    requires spring.boot;
    requires spring.expression;

    exports io.github.fishlikewater.spring.boot.raiden.core;
    exports io.github.fishlikewater.spring.boot.raiden.core.engine;
    exports io.github.fishlikewater.spring.boot.raiden.core.getter;
    exports io.github.fishlikewater.spring.boot.raiden.core.i18n;
    exports io.github.fishlikewater.spring.boot.raiden.core.property;
    exports io.github.fishlikewater.spring.boot.raiden.core.processor;
}