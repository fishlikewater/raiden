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
package io.github.fishlikewater.nacos.annotation;

import io.github.fishlikewater.nacos.constant.Constants;
import io.github.fishlikewater.nacos.registry.AbstractNacosConfigRegister;
import org.springframework.context.annotation.Scope;

import java.lang.annotation.*;

/**
 * NacosPreScan
 * 配置文件上下文刷新前预加载路径（该配置需标注在启动i类上）
 *
 * @author zhangxiang
 * @version 1.0.9
 * @since 2024/12/2
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Scope(value = Constants.NACOS_REFRESH_SCOPE)
public @interface NacosPreScan {

    Class<? extends AbstractNacosConfigRegister>[] baseClasses() default {};

    String[] basePackages() default {};
}
