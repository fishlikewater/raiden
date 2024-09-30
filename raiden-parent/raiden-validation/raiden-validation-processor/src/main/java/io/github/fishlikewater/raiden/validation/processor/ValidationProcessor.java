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

import com.google.auto.service.AutoService;
import io.github.fishlikewater.raiden.core.ObjectUtils;
import io.github.fishlikewater.raiden.core.StringUtils;
import io.github.fishlikewater.raiden.validation.core.annotation.Validation;
import javassist.*;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * {@code ValidationProcessor}
 *
 * @author zhangxiang
 * @version 1.0.6
 * @since 2024/09/30
 */
@AutoService(Processor.class)
@SupportedAnnotationTypes("io.github.fishlikewater.raiden.validation.core.annotation.Validation")
@SupportedSourceVersion(SourceVersion.RELEASE_21)
public class ValidationProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (TypeElement annotation : annotations) {
            Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(annotation);
            for (Element element : annotatedElements) {
                if (element.getEnclosingElement() instanceof ExecutableElement methodElement) {
                    TypeElement typeElement = (TypeElement) methodElement.getEnclosingElement();
                    try {
                        //this.modifyMethod(typeElement, methodElement, annotation);
                        this.modifyClass(typeElement, methodElement);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return true;
    }

    private void modifyClass(TypeElement typeElement, ExecutableElement methodElement) throws Exception {
        String className = typeElement.getQualifiedName().toString();
        String methodName = methodElement.getSimpleName().toString();

        File outputDir = new File(processingEnv.getFiler().createResource(
                javax.tools.StandardLocation.CLASS_OUTPUT, "", "dummy.txt").toUri()).getParentFile();
        ClassPool pool = ClassPool.getDefault();
        pool.appendClassPath(new LoaderClassPath(Thread.currentThread().getContextClassLoader()));
        CtClass ctClass = pool.get(className);
        CtMethod ctMethod = ctClass.getDeclaredMethod(methodName);

        // 添加前置处理
        ctMethod.insertBefore("{ System.out.println(\"Pre-process: Method called\"); }");

        // 保存修改后的类
        ctClass.writeFile(outputDir.getAbsolutePath());

        // 清理
        ctClass.detach();
    }

    private void modifyMethod(TypeElement classElement, ExecutableElement methodElement, TypeElement annotation) throws Exception {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, classElement.getQualifiedName().toString());
        ClassPool pool = ClassPool.getDefault();
        CtClass ctClass = pool.get(classElement.getQualifiedName().toString());
        this.handleTargetClass(ctClass);
    }

    private void handleTargetClass(CtClass ctClass) throws Exception {
        boolean classModify = false;
        CtMethod[] methods = ctClass.getDeclaredMethods();
        for (CtMethod method : methods) {
            List<Integer> list = new ArrayList<>();
            List<Validation> annotations = new ArrayList<>();
            Object[][] parameterAnnotations = method.getParameterAnnotations();
            for (int i = 0; i < parameterAnnotations.length; i++) {
                Object[] parameterAnnotation = parameterAnnotations[i];
                for (Object o : parameterAnnotation) {
                    if (o instanceof Validation validation) {
                        list.add(i);
                        annotations.add(validation);
                        classModify = true;
                        break;
                    }
                }
                this.methodAddValidation(method, list, annotations);
            }
        }
        if (classModify) {
            //ctClass.writeFile(this.out.getPath());
            // 或者，使用 ctClass.toClass() 方法将修改后的类加载到当前 JVM 中
            ctClass.toClass();
        }
    }

    private void methodAddValidation(CtMethod method, List<Integer> list, List<Validation> annotations) throws CannotCompileException, NotFoundException {
        if (ObjectUtils.isNullOrEmpty(list)) {
            return;
        }
        List<String> methodParameterNames = JavassistUtils.getMethodParameterNames(method);
        for (int i = 0; i < list.size(); i++) {
            String paramName = methodParameterNames.get(list.get(i));
            Validation validation = annotations.get(i);
            Class<?>[] groups = validation.groups();
            StringBuilder sb = new StringBuilder();
            sb.append("{\n");
            sb.append("     ");
            if (groups.length == 0) {
                String validationStr = StringUtils.format("     io.github.fishlikewater.raiden.validation.core.ValidationUtil.validate({});\n", paramName);
                sb.append(validationStr);
            } else {
                List<String> groupStr = new ArrayList<>();
                for (Class<?> group : groups) {
                    groupStr.add(group.getCanonicalName() + ".class");
                }
                String group = StringUtils.format("     Class[] arr = {{}};\n", StringUtils.join(",", groupStr));
                sb.append(group);
                String validationStr = StringUtils.format("     io.github.fishlikewater.raiden.validation.core.ValidationUtil.validate({}, arr);\n", paramName);
                sb.append(validationStr);
            }
            sb.append("}\n");
            method.insertBefore(sb.toString());
        }
    }
}
