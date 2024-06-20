package io.github.fishlikewater.raiden.redis.processor;

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
 * {@code Utils}
 *
 * @author zhangxiang
 * @version 1.0.2
 * @since 2024/06/20
 */
public class Utils {

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
