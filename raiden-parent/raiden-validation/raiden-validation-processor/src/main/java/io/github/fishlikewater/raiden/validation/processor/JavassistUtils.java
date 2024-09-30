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
package io.github.fishlikewater.raiden.validation.processor;

import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * JavassistUtils
 *
 * @author zhangx
 * @version 1.0.0
 * @since 2024/09/30
 **/
public class JavassistUtils {

    public static List<String> getMethodParameterNames(CtMethod method) throws NotFoundException {
        CodeAttribute codeAttribute = method.getMethodInfo().getCodeAttribute();
        LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);

        if (attr == null) {
            throw new RuntimeException("LocalVariableTable attribute is not found in the method");
        }

        List<String> parameterTypes = Arrays.stream(method.getParameterTypes()).map(CtClass::getName).toList();
        List<String> paramNames = new ArrayList<>();
        // 方法参数在局部变量表中从索引 1 开始，索引 0 是 this
        int pos = Modifier.isStatic(method.getModifiers()) ? 0 : 1;
        for (int i = 0; i < parameterTypes.size(); i++) {
            paramNames.add(attr.variableName(i + pos));
        }
        return paramNames;
    }
}
