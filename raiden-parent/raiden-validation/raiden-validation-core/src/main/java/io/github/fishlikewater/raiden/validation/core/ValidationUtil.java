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
package io.github.fishlikewater.raiden.validation.core;

import io.github.fishlikewater.raiden.core.CollectionUtils;
import io.github.fishlikewater.raiden.core.ObjectUtils;
import jakarta.validation.*;

import java.util.Collection;
import java.util.Set;

/**
 * ValidationUtil
 *
 * @author zhangxiang
 * @version 1.0.6
 * @since 2024/9/28
 **/
public class ValidationUtil {

    public static Validator validator;

    public static void setValidator(Validator validator) {
        ValidationUtil.validator = validator;
    }

    public static Validator getValidator() {
        if (ObjectUtils.isNullOrEmpty(validator)) {
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            validator = factory.getValidator();
        }
        return validator;
    }

    public static <T> void validate(T t) {
        validate(t, (Class<?>) null);
    }

    public static <T> void validate(T t, Class<?>... groups) {
        Set<ConstraintViolation<T>> violations;
        if (ObjectUtils.isNullOrEmpty(groups)) {
            violations = getValidator().validate(t);
        } else {
            violations = getValidator().validate(t, groups);
        }
        if (!CollectionUtils.isEmpty(violations)) {
            throw new ConstraintViolationException(violations);
        }
    }

    public static <T> void validate(Collection<T> ts, Class<?>... groups) {
        ts.forEach((t) -> {
            ValidationUtil.validate(t, groups);
        });
    }
}
