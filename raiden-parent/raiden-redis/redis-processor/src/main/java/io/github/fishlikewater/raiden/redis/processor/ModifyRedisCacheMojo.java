package io.github.fishlikewater.raiden.redis.processor;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import io.github.fishlikewater.raiden.core.StringUtils;
import io.github.fishlikewater.raiden.core.constant.CommonConstants;
import io.github.fishlikewater.raiden.redis.core.annotation.RedisCache;
import javassist.*;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.annotation.Annotation;
import lombok.extern.slf4j.Slf4j;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * {@code ModifyRedisCacheMojo}
 * 修改字节码
 *
 * @author zhangxiang
 * @version 1.0.2
 * @since 2024/06/20
 */
@Slf4j
@Mojo(name = "addCache", defaultPhase = LifecyclePhase.PROCESS_CLASSES)
public class ModifyRedisCacheMojo extends AbstractMojo {

    @Parameter(property = "扫描包")
    private String packages;

    @Parameter(property = "生成后保存路径", defaultValue = "${project.build.directory}/classes")
    private File out;

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;

    @Override
    public void execute() {
        this.handle();
    }

    private void handle() {
        ClassPool pool = ClassPool.getDefault();
        final ClassGraph classGraph = new ClassGraph();
        String[] packageArr = packages.split(",");
        try (ScanResult scan = classGraph.enableAllInfo().acceptPackages(packageArr).scan()) {
            final ClassInfoList allClasses = scan.getAllClasses();
            for (ClassInfo aClass : allClasses) {
                CtClass ctClass = pool.get(aClass.getName());
                this.handleTargetClass(ctClass);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void handleTargetClass(CtClass ctClass) throws Exception {
        boolean classModify = false;
        CtMethod[] methods = ctClass.getDeclaredMethods();
        for (CtMethod method : methods) {
            boolean b = method.hasAnnotation(RedisCache.class);
            if (b) {
                classModify = true;
                // 1. 清出该注解
                AnnotationsAttribute visibleAnnotations = (AnnotationsAttribute) method.getMethodInfo().getAttribute(AnnotationsAttribute.invisibleTag);
                RedisCache redisCache = (RedisCache) method.getAnnotation(RedisCache.class);
                Annotation[] annotations = visibleAnnotations.getAnnotations();
                Annotation[] newAnnotations = Arrays.stream(annotations)
                        .filter(annotation -> !annotation.getTypeName().equals(RedisCache.class.getName()))
                        .toArray(Annotation[]::new);
                if (annotations.length == 0) {
                    method.getMethodInfo().removeAttribute(AnnotationsAttribute.invisibleTag);
                } else {
                    // 更新方法的注解属性
                    visibleAnnotations.setAnnotations(newAnnotations);
                    method.getMethodInfo().addAttribute(visibleAnnotations);
                }
                // 解析该注解
                this.handleMethod(ctClass, method, redisCache);
            }
        }
        if (classModify) {
            this.addRedisson(ctClass);
            ctClass.writeFile(out.getPath());
            // 或者，使用 ctClass.toClass() 方法将修改后的类加载到当前 JVM 中
            // ctClass.toClass();
        }
    }

    private void addRedisson(CtClass ctClass) throws Exception {
        ClassPool pool = ClassPool.getDefault();
        CtClass redissonClient = pool.get("org.redisson.api.RedissonClient");
        CtField ctField = new CtField(redissonClient, "redissonClient", ctClass);
        ctField.setModifiers(Modifier.PRIVATE | Modifier.FINAL);
        ctClass.addField(ctField);
        // 获取默认构造函数
        CtConstructor defaultConstructor = ctClass.getConstructor("()V");
        if (defaultConstructor != null) {
            // 删除默认构造函数
            ctClass.removeConstructor(defaultConstructor);
        }
        // 创建新的构造函数，添加自定义类型参数
        CtConstructor newConstructor = new CtConstructor(new CtClass[]{redissonClient}, ctClass);
        newConstructor.setBody("{ this.redissonClient = $1; }");
        // 将新的构造函数添加到类中
        ctClass.addConstructor(newConstructor);
    }

    private void handleMethod(CtClass ctClass, CtMethod method, RedisCache redisCache) throws Exception {
        this.moveOldMethod(ctClass, method);
        String key = redisCache.key();
        if (StringUtils.startWith(key, CommonConstants.SYMBOL_EXPRESSION)) {
            StringBuilder sb = new StringBuilder();
            sb.append("{\n");
            List<String> methodParameterNames = Utils.getMethodParameterNames(method);
            sb.append("     org.springframework.expression.ExpressionParser parser = new org.springframework.expression.spel.standard.SpelExpressionParser();\n");
            sb.append("     org.springframework.expression.spel.support.StandardEvaluationContext context = new org.springframework.expression.spel.support.StandardEvaluationContext();\n");
            for (String name : methodParameterNames) {
                String param = StringUtils.format("     context.setVariable(\"{}\", {});\n", name, name);
                sb.append(param);
            }
            sb.append("     java.lang.String key = parser.parseExpression(\"")
                    .append(key)
                    .append("\").getValue(context, java.lang.String.class);\n");
            sb.append("}\n");
            method.insertBefore(sb.toString());
        }


    }

    private void moveOldMethod(CtClass ctClass, CtMethod method) throws Exception {
        String name = method.getName();
        String methodName = name + "Old";
        // 创建新方法
        CtMethod newMethod = new CtMethod(
                method.getReturnType(),
                methodName,
                method.getParameterTypes(),
                ctClass
        );
        // 复制方法体
        newMethod.setModifiers(Modifier.PRIVATE);
        newMethod.setBody(method, null);
        newMethod.setName(methodName);
        // 将新方法添加到类中
        ctClass.addMethod(newMethod);
    }
}
