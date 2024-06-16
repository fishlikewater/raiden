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

import com.google.auto.service.AutoService;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.List;
import io.github.fishlikewater.raiden.processor.AbstractAnnotationProcessor;
import io.github.fishlikewater.raiden.redis.core.annotation.RedisCache;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
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
@AutoService(Processor.class)
@SupportedAnnotationTypes("io.github.fishlikewater.raiden.redis.core.annotation.RedisCache")
@SupportedSourceVersion(SourceVersion.RELEASE_21)
public class RedisCacheProcessor extends AbstractAnnotationProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        final Set<? extends Element> elementsAnnotatedWith = roundEnv.getElementsAnnotatedWith(RedisCache.class);
        for (Element element : elementsAnnotatedWith) {
            final JCTree.JCMethodDecl jcMethodDecl = (JCTree.JCMethodDecl) this.tree.getTree(element);
            treeMaker.pos = jcMethodDecl.pos;
            final JCTree.JCMethodInvocation apply = treeMaker.Apply(
                    List.of(this.memberAccess("java.lang.String")),
                    this.memberAccess("java.lang.System.out.println"),
                    List.of(treeMaker.Literal("xiao test zhen"))

            );
            final List<JCTree.JCStatement> jcStatements = List.of(
                    treeMaker.Exec(apply),
                    jcMethodDecl.body,
                    treeMaker.Exec(apply)
            );
            jcMethodDecl.body = treeMaker.Block(0, jcStatements);
        }
        return false;
    }
}
