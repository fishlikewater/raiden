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
package io.github.fishlikewater.raiden.core.func;

import cn.hutool.core.util.StrUtil;
import io.github.fishlikewater.raiden.core.constant.CommonConstants;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.util.Locale;

/**
 * {@code LambdaMeta}
 *
 * @author zhangxiang
 * @version 1.0.0
 * @since 2024/04/30
 */
public interface LambdaMeta {
    /**
     * 解析lambda表达式，把表达式转换成字段名
     *
     * @param fx lambda表达式
     * @return 字段名
     */
    static <T, R> String resolve(LambdaFunction<T, R> fx) {
        try {
            Method method = fx.getClass().getDeclaredMethod(CommonConstants.LAMBDA_FUNCTION_NAME);
            method.setAccessible(Boolean.TRUE);
            SerializedLambda serializedLambda = (SerializedLambda) method.invoke(fx);
            String methodName = serializedLambda.getImplMethodName();
            return methodToProperty(methodName);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 把方法名转换成字段名
     *
     * @param name 方法名
     * @return 字段名
     */
    static String methodToProperty(String name) {
        if (name.startsWith(CommonConstants.BOOLEAN_FIELD_START_WITH)) {
            name = name.substring(2);
        } else if (name.startsWith(CommonConstants.GET_METHOD_START_WITH) || name.startsWith(CommonConstants.SET_METHOD_START_WITH)) {
            name = name.substring(3);
        }

        if (StrUtil.isNotBlank(name) && !Character.isUpperCase(name.charAt(1))) {
            name = name.substring(0, 1).toLowerCase(Locale.ENGLISH) + name.substring(1);
        }

        return name;
    }
}
