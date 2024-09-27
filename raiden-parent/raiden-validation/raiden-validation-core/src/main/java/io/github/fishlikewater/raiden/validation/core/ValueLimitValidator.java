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

import io.github.fishlikewater.raiden.core.ObjectUtils;
import io.github.fishlikewater.raiden.validation.core.annotation.ValueLimit;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


/**
 * <p>
 * {@code ValueLimitValidator}
 * </p>
 *
 * @author fishlikewater@126.com
 * @version 1.0.2
 * @since 2024年06月06日 23:00
 **/
public class ValueLimitValidator implements ConstraintValidator<ValueLimit, Object> {

    private Class<? extends BaseEnum<?>>[] enumClass;

    private boolean allowNull;

    private int[] intValues;

    private String[] stringValues;

    @Override
    public void initialize(ValueLimit valueLimit) {
        this.enumClass = valueLimit.enumClass();
        this.allowNull = valueLimit.allowNull();
        this.intValues = valueLimit.intValues();
        this.stringValues = valueLimit.stringValues();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        if (ObjectUtils.isNullOrEmpty(value)) {
            return this.allowNull;
        }
        if (ObjectUtils.isNotNullOrEmpty(this.enumClass)) {
            return this.handleEnumClass(this.enumClass, value);
        }
        if (value instanceof Integer intValue) {
            return this.handleIntValue(this.intValues, intValue);
        }
        if (value instanceof String stringValue) {
            return this.handleStringValue(this.stringValues, stringValue);
        }
        return false;
    }

    private boolean handleStringValue(String[] stringValues, String stringValue) {
        if (ObjectUtils.isNullOrEmpty(stringValues)) {
            return false;
        }
        for (String value : stringValues) {
            if (ObjectUtils.equals(stringValue, value)) {
                return true;
            }
        }
        return false;
    }

    private boolean handleIntValue(int[] intValues, Integer intValue) {
        if (ObjectUtils.isNullOrEmpty(intValues)) {
            return false;
        }
        for (int value : intValues) {
            if (ObjectUtils.equals(intValue, value)) {
                return true;
            }
        }
        return false;
    }

    private boolean handleEnumClass(Class<? extends BaseEnum<?>>[] enumClass, Object value) {
        for (Class<? extends BaseEnum<?>> eClass : enumClass) {
            BaseEnum<?>[] enums = eClass.getEnumConstants();
            for (BaseEnum<?> anEnum : enums) {
                final Object code = anEnum.getCode();
                if (ObjectUtils.equals(code, value)) {
                    return true;
                }
            }
        }
        return false;
    }
}
