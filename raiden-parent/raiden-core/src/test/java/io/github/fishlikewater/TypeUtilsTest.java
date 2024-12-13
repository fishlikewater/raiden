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
package io.github.fishlikewater;

import io.github.fishlikewater.raiden.core.Assert;
import io.github.fishlikewater.raiden.core.TypeUtils;
import org.junit.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TypeUtilsTest
 *
 * @author zhangxiang
 * @version 1.1.0
 * @since 2024/12/13
 **/
public class TypeUtilsTest {

    @Test
    public void test() throws NoSuchMethodException {
        Class<MyClass> clazz = MyClass.class;
        Method method = clazz.getMethod("myMethod");
        Type returnType = TypeUtils.getReturnType(method);
        Assert.isTrue(returnType.getTypeName().equals("java.util.List<java.lang.String>"), "returnType is not a list");
        Class<?> genericType = TypeUtils.getGenericType(returnType);
        Assert.isTrue(genericType.isAssignableFrom(String.class), "genericType is not String");
    }

    @Test
    public void test2() throws NoSuchMethodException {
        Class<MyClass> clazz = MyClass.class;
        Method method = clazz.getMethod("myMethod2");
        Type returnType = TypeUtils.getReturnType(method);
        Assert.isTrue(returnType.getTypeName().equals("java.util.Map<java.lang.String, java.lang.Object>"), "returnType is not a Map");
        Class<?> genericType = TypeUtils.getGenericType(returnType, 1);
        Assert.isTrue(genericType.isAssignableFrom(Object.class), "genericType is not Object");
    }


    static class MyClass {
        public List<String> myMethod() {
            return new ArrayList<>();
        }

        public Map<String, Object> myMethod2() {
            return new HashMap<>();
        }
    }
}
