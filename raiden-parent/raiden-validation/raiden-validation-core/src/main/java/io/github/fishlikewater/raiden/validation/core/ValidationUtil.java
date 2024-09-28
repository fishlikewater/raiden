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

    public <T> void validate(Collection<T> ts, Class<?>... groups) {
        ts.forEach((t) -> {
            Set<ConstraintViolation<T>> violations;
            if (ObjectUtils.isNullOrEmpty(groups)) {
                violations = getValidator().validate(t);
            } else {
                violations = getValidator().validate(t, groups);
            }
            if (!CollectionUtils.isEmpty(violations)) {
                throw new ConstraintViolationException(violations);
            }
        });
    }
}
