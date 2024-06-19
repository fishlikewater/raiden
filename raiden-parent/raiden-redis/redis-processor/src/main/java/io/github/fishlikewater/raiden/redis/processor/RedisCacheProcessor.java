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
package io.github.fishlikewater.raiden.redis.processor;

import com.github.javaparser.ParseProblemException;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import io.github.fishlikewater.raiden.redis.core.annotation.RedisCache;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.Set;

/**
 * <p>
 * {@code RedisCacheProcessor}
 * </p>
 * redis 缓存注解代码生成
 *
 * @author fishlikewater@126.com
 * @version 1.0.2
 * @since 2024年06月16日 20:22
 **/
@SupportedAnnotationTypes("io.github.fishlikewater.raiden.redis.core.annotation.RedisCache")
@SupportedSourceVersion(SourceVersion.RELEASE_21)
public class RedisCacheProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(RedisCache.class)) {
            if (element.getKind() == ElementKind.METHOD) {
                ExecutableElement methodElement = (ExecutableElement) element;
                modifyMethod(methodElement);
            }
        }
        return true;
    }

    private void modifyMethod(ExecutableElement methodElement) {
        TypeElement enclosingElement = (TypeElement) methodElement.getEnclosingElement();

        try {
            // Parse the source file using JavaParser
            CompilationUnit cu = StaticJavaParser.parse(enclosingElement.getQualifiedName().toString());

            // Visit and modify the AST
            new MethodVisitor().visit(cu, null);

        } catch (ParseProblemException e) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Failed to parse or modify method: " + e.getMessage());
        }
    }

    private static class MethodVisitor extends VoidVisitorAdapter<Void> {
        @Override
        public void visit(MethodDeclaration md, Void arg) {
            // Modify the method AST here
            super.visit(md, arg);
            ExpressionStmt expressionStmt = new ExpressionStmt(new NameExpr("System.out.println(\"Before executing method: " + md.getName() + "\");"));
            // Example: Inserting a print statement at the beginning of the method
            md.getBody().get().addStatement(0, expressionStmt);
        }
    }
}
