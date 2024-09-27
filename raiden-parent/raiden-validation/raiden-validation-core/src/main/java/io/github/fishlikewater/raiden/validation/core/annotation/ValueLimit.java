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
package io.github.fishlikewater.raiden.validation.core.annotation;

import io.github.fishlikewater.raiden.validation.core.BaseEnum;
import io.github.fishlikewater.raiden.validation.core.ValueLimitValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * <p>
 * {@code ValueLimit}
 * 值限定注解
 * </p>
 * <ol>
 * <li>|- 优先使用enumValues 判定</li>
 * <li>|- 如果注解的是数值型 且 enumValues没有设置的情况下，使用intValues</li>
 * <li>|- 如果注解的是字符串 且 enumValues没有设置的情况下，使用stringValues</li>
 * </ol>
 *
 * @author fishlikewater@126.com
 * @version 1.0.2
 * @since 2024年06月06日 22:54
 **/
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = ValueLimitValidator.class
)
public @interface ValueLimit {

    String message() default "values that are not allowed to be entered";

    int[] intValues() default {};

    String[] stringValues() default {};

    Class<? extends BaseEnum<?>>[] enumClass() default {};

    boolean allowNull() default false;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
