/*
 * Copyright © 2024 zhangxiang (fishlikewater@126.com)
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
package com.github.fishlikewater.raiden.http.core.processor;

import com.github.fishlikewater.raiden.http.core.HeadWrap;
import com.github.fishlikewater.raiden.http.core.annotation.Heads;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * {@code CheckProcessor}
 * 编译检查
 *
 * @author zhangxiang
 * @since 2024/03/19
 * @version 1.0.0
 */
public class CheckProcessor extends AbstractProcessor {

    @Override
    public synchronized void init(ProcessingEnvironment env) {
        super.init(env);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> eleStrSet = roundEnv.getElementsAnnotatedWith(Heads.class);
        for (Element element : eleStrSet) {
            if (element.getKind() == ElementKind.PARAMETER) {
                VariableElement param = (VariableElement) element;
                TypeMirror typeMirror = param.asType();
                return this.valid(typeMirror, element);
            }
        }
        return true;
    }

    private boolean valid(TypeMirror typeMirror, Element element) {
        if (typeMirror instanceof DeclaredType declaredType) {
            List<? extends TypeMirror> typeArguments = declaredType.getTypeArguments();
            if (declaredType.asElement().toString().equals(Map.class.getName())) {
                return this.handleMap(element, typeArguments);
            }
            return declaredType.asElement().toString().equals(HeadWrap.class.getName());
        }
        return false;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> strings = new TreeSet<>();
        strings.add("com.github.fishlikewater.raidencore.annotation.Heads");
        return strings;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    private boolean handleMap(Element element, List<? extends TypeMirror> typeArguments) {
        TypeMirror keyType = typeArguments.get(0);
        TypeMirror valueType = typeArguments.get(1);

        boolean isKeyTypeString = keyType.toString().equals(String.class.getName());
        boolean isValueTypeObject = valueType.toString().equals(String.class.getName());
        if (isKeyTypeString && isValueTypeObject) {
            return true;
        } else {
            this.processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "只支持Map<String, String>与HeadWrap参数类型", element);
            return false;
        }
    }
}
