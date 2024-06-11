package io.github.fishlikewater.raiden.http.core.exception;

import io.github.fishlikewater.raiden.core.exception.ExceptionCheck;

/**
 * <p>
 * {@code HttpExceptionCheck}
 * </p>
 *
 * @author fishlikewater@126.com
 * @version 1.0.2
 * @since 2024年06月11日 22:30
 **/
public class HttpExceptionCheck extends ExceptionCheck {

    static {
        HttpExceptionCheck.e = new RaidenHttpException();
    }
}
