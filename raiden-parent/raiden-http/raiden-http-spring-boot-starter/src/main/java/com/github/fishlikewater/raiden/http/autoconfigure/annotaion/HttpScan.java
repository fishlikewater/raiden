/*
 * Copyright © 2024 zhangxiang (fishlikewater@126.com)
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
package com.github.fishlikewater.raiden.http.autoconfigure.annotaion;

import com.github.fishlikewater.raiden.http.autoconfigure.HttpServerScannerRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 包扫描
 *
 * @author fishlikewater@126.com
 * @since 2023年09月22日 12:32
 * @version 1.0.0
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(HttpServerScannerRegistrar.class)
@Inherited
public @interface HttpScan {

    /**
     * Scan package path
     * Same meaning as basePackages
     *
     * @return basePackages
     */
    String[] value() default {};

    /**
     * Scan package path
     *
     * @return basePackages
     */
    String[] basePackages() default {};

    /**
     * Scan package classes
     *
     * @return Scan package classes
     */
    Class<?>[] basePackageClasses() default {};
}
