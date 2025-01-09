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
package io.github.fishlikewater.nacos.annotation;

import io.github.fishlikewater.nacos.constant.Constants;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * {@code NacosRefresh}
 * 支持nacos 刷新标记的bean
 *
 * @author zhangxiang
 * @version 1.0.7
 * @since 2024/10/29
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Scope(value = Constants.NACOS_REFRESH_SCOPE)
public @interface NacosRefreshScope {

    @AliasFor(annotation = Scope.class, attribute = "proxyMode")
    ScopedProxyMode proxyMode() default ScopedProxyMode.TARGET_CLASS;
}
