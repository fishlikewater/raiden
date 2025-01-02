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
package io.github.fishlikewater.spring.boot.raiden.core.codec;

import io.github.fishlikewater.spring.boot.raiden.core.annotation.Crypto;
import org.springframework.core.MethodParameter;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @author zhangxiang
 * @version 1.1.1
 * @since 2024/12/25
 **/
public class NeedCrypto {
    private NeedCrypto() {}

    /**
     * 是否需要对结果加密
     * 1.优先取方法上的注解
     * 2.方法没有注解再取类上的注解
     */
    static boolean needEncrypt(MethodParameter parameter) {
        Method method = parameter.getMethod();
        if (Objects.nonNull(method)) {
            boolean methodPresentAnno = method.isAnnotationPresent(Crypto.class);
            if (methodPresentAnno) {
                //方法上标注的是否需要加密
                return method.getAnnotation(Crypto.class).encrypt();
            }
        }

        Class<?> containingClass = parameter.getContainingClass();
        boolean classPresentAnno = containingClass.isAnnotationPresent(Crypto.class);
        if (classPresentAnno) {
            return containingClass.getAnnotation(Crypto.class).encrypt();
        }

        return false;
    }

    /**
     * 是否需要参数解密
     * 1.优先取方法上的注解
     * 2.方法没有注解再取类上的注解
     */
    static boolean needDecrypt(MethodParameter parameter) {
        Method method = parameter.getMethod();
        if (Objects.nonNull(method)) {
            boolean methodPresentAnno = method.isAnnotationPresent(Crypto.class);
            if (methodPresentAnno) {
                //方法上标注的是否需要加密
                return method.getAnnotation(Crypto.class).decrypt();
            }
        }

        Class<?> containingClass = parameter.getContainingClass();
        boolean classPresentAnno = containingClass.isAnnotationPresent(Crypto.class);
        if (classPresentAnno) {
            return containingClass.getAnnotation(Crypto.class).decrypt();
        }

        return false;
    }

    static Crypto getCryptoAnnotation(MethodParameter parameter) {
        Method method = parameter.getMethod();
        if (Objects.nonNull(method)) {
            boolean methodPresentAnno = method.isAnnotationPresent(Crypto.class);
            if (methodPresentAnno) {
                return method.getAnnotation(Crypto.class);
            }
        }

        Class<?> containingClass = parameter.getContainingClass();
        boolean classPresentAnno = containingClass.isAnnotationPresent(Crypto.class);
        if (classPresentAnno) {
            return containingClass.getAnnotation(Crypto.class);
        }

        return null;
    }
}
