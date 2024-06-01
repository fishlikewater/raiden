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
package com.github.fishlikewater.raiden.redis.processor;

import com.github.fishlikewater.raiden.redis.core.annotation.RedisCache;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.*;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.SimpleTypeVisitor8;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>
 * {@code DemoProcessor}
 *
 * </p>
 *
 * @author fishlikewater@126.com
 * @version 1.0.0
 * @since 2024年05月16日 23:26
 **/
@AutoService(Processor.class)
public class DemoProcessor extends AbstractProcessor {

    private static int flag = 1;

    /* ======================================================= */
    /* Fields                                                  */
    /* ======================================================= */

    /**
     * 用于将创建的类写入到文件
     */
    private Filer mFiler;


    /* ======================================================= */
    /* Override/Implements Methods                             */
    /* ======================================================= */

    @Override
    public synchronized void init(ProcessingEnvironment environment) {
        super.init(environment);
        mFiler = environment.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment environment) {
        // 获取所有被 @DemoAnnotation 注解的类
        Set<? extends Element> elements = environment.getElementsAnnotatedWith(RedisCache.class);
        if (elements.isEmpty()) {
            return false;
        }
        // 创建一个方法，返回 Set<Class>
        MethodSpec method = createMethodWithElements(elements);

        // 创建一个类
        TypeSpec clazz = createClassWithMethod(method);

        // 将这个类写入文件
        writeClassToFile(clazz);

        return false;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        // 这个方法返回当前处理器 能处理哪些注解，这里我们只返回 DemoAnnotation
        return Collections.singleton(RedisCache.class.getCanonicalName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        // 这个方法返回当前处理器 支持的代码版本
        return SourceVersion.latestSupported();
    }

    /**
     * 创建一个方法，这个方法返回 elements 中的所有类信息。
     */
    private MethodSpec createMethodWithElements(Set<? extends Element> elements) {

        // "getAllClasses" 是生成的方法的名称
        MethodSpec.Builder builder = MethodSpec.methodBuilder("getAllClasses");

        // 为这个方法加上 "public static" 的修饰符
        builder.addModifiers(Modifier.PUBLIC, Modifier.STATIC);

        // 定义返回值类型为 Set<Class>
        ParameterizedTypeName returnType = ParameterizedTypeName.get(
                ClassName.get(Set.class),
                ClassName.get(Class.class)
        );
        builder.returns(returnType);

        // 经过上面的步骤，
        // 我们得到了 public static Set<Class> getAllClasses() {} 这个方法,
        // 接下来我们实现它的方法体：

        // 方法中的第一行: Set<Class> set = new HashSet<>();
        builder.addStatement("$T<$T> set = new $T<>();", Set.class, Class.class, HashSet.class);

        // 上面的 "$T" 是占位符，代表一个类型，可以自动 import 包。其它占位符：
        // $L: 字符(Literals)、 $S: 字符串(String)、 $N: 命名(Names)

        // 遍历 elements, 添加代码行
        for (Element element : elements) {

            TypeMirror elementType = element.asType();
            ClassName className = ClassName.get(elementType.accept(new SimpleTypeVisitor8<TypeElement, Void>() {
                @Override
                public TypeElement visitDeclared(DeclaredType t, Void p) {
                    return (TypeElement) t.asElement();
                }
            }, null));

            builder.addStatement("set.add($T.class)", className);
        }

        // 经过上面的 for 循环，我们就把所有添加了注解的类加入到 set 变量中了，
        // 最后，只需要把这个 set 作为返回值 return 就好了：
        builder.addStatement("return set");

        return builder.build();
    }

    /**
     * 创建一个类，并把参数中的方法加入到这个类中
     */
    private TypeSpec createClassWithMethod(MethodSpec method) {
        // 定义一个名字叫 OurClass 的类
        TypeSpec.Builder ourClass = TypeSpec.classBuilder("CustomerProcessor" + flag++);

        // 声明为 public
        ourClass.addModifiers(Modifier.PUBLIC);

        // 为这个类加入一段注释
        ourClass.addJavadoc("这个类是自动创建的哦~\n\n @author ZhengHaiPeng");

        // 为这个类新增一个方法
        ourClass.addMethod(method);

        return ourClass.build();
    }

    /**
     * 将一个创建好的类写入到文件中参与编译
     */
    private void writeClassToFile(TypeSpec clazz) {
        // 声明一个文件在 "me.moolv.apt" 下
        JavaFile file = JavaFile.builder("me.moolv.apt", clazz).build();
        this.processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "创建file");
        // 写入文件
        try {
            file.writeTo(mFiler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
