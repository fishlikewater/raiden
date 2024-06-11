package io.github.fishlikewater.raiden.core.exception;

import io.github.fishlikewater.raiden.core.ObjectUtils;

/**
 * <p>
 * {@code ExceptionCheck}
 * </p>
 * 检查与抛出异常
 *
 * @author fishlikewater@126.com
 * @version 1.0.2
 * @since 2024年06月11日 21:05
 **/
public abstract class ExceptionCheck {

    protected static AbstractException e;

    public static <T> T isNull(Object object, String message, Object... args) {
        if (ObjectUtils.isNullOrEmpty(object)) {
            throw e.creat(message, args);
        }
        return null;
    }
}
