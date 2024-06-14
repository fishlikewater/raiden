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
package io.github.fishlikewater.raiden.processor;

import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Names;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;


/**
 * {@code AbstractProcessor}
 * 编译时注解处理器
 *
 * @author zhangxiang
 * @version 1.0.2
 * @since 2024/06/13
 */
@SuppressWarnings("all")
public abstract class AbstractAnnotationProcessor extends AbstractProcessor {

    private Messager messager;

    private JavacTrees tree;

    private Context context;

    private TreeMaker treeMaker;

    private Names names;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        this.messager = processingEnv.getMessager();
        this.tree = JavacTrees.instance(processingEnv);
        this.context = ((JavacProcessingEnvironment) processingEnv).getContext();
        this.treeMaker = TreeMaker.instance(context);
        this.names = Names.instance(context);
    }

    /**
     * 获取变量名
     *
     * @param name 变量名
     * @return 变量名
     */
    protected Name getNameFromString(String name) {
        return this.names.fromString(name);
    }

    /**
     * 创建变量
     * <pre>
     *     String a = "123"
     *     JCTree.JCVariableDecl var = makeVarDef(treeMaker.Modifiers(0), "a", memberAccess("java.lang.String"), treeMaker.Literal("123"));
     * </pre>
     *
     * @param modifiers 修饰符
     * @param name      变量名
     * @param vartype   变量类型
     * @param init      初始化值
     * @return 变量
     */
    protected JCTree.JCVariableDecl makeVarDecl(JCTree.JCModifiers modifiers, String name, JCTree.JCExpression vartype, JCTree.JCExpression init) {
        return this.treeMaker.VarDef(modifiers, this.getNameFromString(name), vartype, init);
    }

    /**
     * 创建 域/方法 的多级访问, 方法的标识只能是最后一个
     *
     * @param components 组件
     * @return 成员变量
     */
    protected JCTree.JCExpression memberAccess(String components) {
        String[] componentArray = components.split("\\.");
        JCTree.JCExpression expr = this.treeMaker.Ident(getNameFromString(componentArray[0]));
        for (int i = 1; i < componentArray.length; i++) {
            expr = this.treeMaker.Select(expr, this.getNameFromString(componentArray[i]));
        }
        return expr;
    }

    /**
     * 创建赋值语句
     * <pre>
     *     makeAssignment(treeMaker.Ident(getNameFromString("a")), treeMaker.Literal("assignment test"));
     *     //a = "assignment test";
     * </pre>
     *
     * @param lhs 左值
     * @param rhs 右值
     * @return 赋值语句
     */
    private JCTree.JCExpressionStatement makeAssignment(JCTree.JCExpression lhs, JCTree.JCExpression rhs) {
        return this.treeMaker.Exec(treeMaker.Assign(lhs, rhs));
    }
}
