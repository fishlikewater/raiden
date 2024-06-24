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
 * @since 2024/06/24
 */
module raiden.docs.spring.boot.starter {
    requires org.slf4j;
    requires org.springdoc.openapi.common;
    requires spring.web;
    requires static lombok;
    requires io.swagger.v3.core;
    requires io.swagger.v3.oas.models;
    requires org.apache.commons.lang3;
    requires io.swagger.v3.oas.annotations;
    requires spring.context;
    requires spring.core;
    requires spring.boot;
    requires spring.boot.autoconfigure;

    exports io.github.fishlikewater.raiden.docs.autoconfig;
    exports io.github.fishlikewater.raiden.docs.autoconfig.handle;
}