package io.github.fishlikewater.raiden.validation.core;

import io.github.fishlikewater.raiden.core.ObjectUtils;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

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
}
