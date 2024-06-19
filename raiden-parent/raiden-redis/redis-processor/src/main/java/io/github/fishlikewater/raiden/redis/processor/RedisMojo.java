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

import com.github.javaparser.ParseResult;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.utils.SourceRoot;
import io.github.fishlikewater.raiden.redis.core.annotation.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.apache.maven.api.plugin.annotations.Mojo;
import org.apache.maven.api.plugin.annotations.Parameter;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * {@code RedisMojo}
 *
 * @author zhangxiang
 * @version 1.0.2
 * @since 2024/06/19
 */
@Slf4j
@Mojo(name = "rdc")
public class RedisMojo extends AbstractMojo {

    @Parameter(property = "项目目录", defaultValue = "${project.basedir}/src/main/java")
    private File path;

    @Parameter(property = "生成后保存路径", defaultValue = "${project.build.directory}/generated-sources")
    private File out;

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;

    @Override
    public void execute() throws MojoExecutionException {
        log.info("begin.generate.redis.cache.code");
        this.test();
        log.info("complete.generate.redis.cache.code");
    }

    public static void main(String[] args) throws IOException {
        SourceRoot sourceRoot = new SourceRoot(Path.of("E:\\IdeaProjects\\demo"));
        MethodVisitor methodVisitor = new MethodVisitor();
        List<ParseResult<CompilationUnit>> parseResults = sourceRoot.tryToParse();
        for (ParseResult<CompilationUnit> parseResult : parseResults) {
            Optional<CompilationUnit> compilationUnit = parseResult.getResult();
            if (compilationUnit.isPresent()) {
                CompilationUnit cu = compilationUnit.get();
                List<MethodDeclaration> methodDeclarations = cu.findAll(MethodDeclaration.class);
                for (MethodDeclaration methodDeclaration : methodDeclarations) {
                    Optional<AnnotationExpr> annotationByClass = methodDeclaration.getAnnotationByClass(RedisCache.class);
                    if (annotationByClass.isPresent()) {
                        annotationByClass.get().remove();
                        methodVisitor.visit(methodDeclaration, null);
                        System.out.println(methodDeclaration);

                    }
                }
            }
        }
    }

    private void test() throws MojoExecutionException {
        ParserConfiguration configuration = new ParserConfiguration();
        configuration.setCharacterEncoding(StandardCharsets.UTF_8);
        StaticJavaParser.setConfiguration(configuration);

        try (Stream<Path> walk = Files.walk(Paths.get(path.toURI()))) {
            walk
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".java"))
                    .forEach(path -> {
                        try {
                            processJavaFile(path.toFile());
                        } catch (IOException e) {
                            log.error("Error processing file: " + path, e);
                        }
                    });
            project.addCompileSourceRoot(out.getPath());
        } catch (IOException e) {
            throw new MojoExecutionException("Error reading source directory", e);
        }
    }

    private void processJavaFile(File file) throws IOException {
        CompilationUnit cu = StaticJavaParser.parse(file);
        List<MethodDeclaration> methodDeclarations = cu.findAll(MethodDeclaration.class);
        for (MethodDeclaration methodDeclaration : methodDeclarations) {
            Optional<AnnotationExpr> annotationByClass = methodDeclaration.getAnnotationByClass(RedisCache.class);
            if (annotationByClass.isEmpty()) {
                return;
            }
            methodDeclaration.getBody().ifPresent(body -> body.addStatement(0, StaticJavaParser.parseStatement("System.out.println(\"Method executed\");")));
            saveCompilationUnit(cu, file);
        }
    }

    private void saveCompilationUnit(CompilationUnit cu, File originalFile) throws IOException {
        String formattedCode = cu.toString();

        Path relativePath = path.toPath().relativize(originalFile.toPath());
        File outputFile = new File(out, relativePath.toString());

        boolean ignore = outputFile.getParentFile().mkdirs();

        try (FileWriter writer = new FileWriter(outputFile, StandardCharsets.UTF_8)) {
            writer.write(formattedCode);
        }
    }

    private static class MethodVisitor extends VoidVisitorAdapter<Void> {
        @Override
        public void visit(MethodDeclaration md, Void arg) {
            ExpressionStmt expressionStmt = new ExpressionStmt(new NameExpr("System.out.println(\"Before executing method: " + md.getName() + "\")"));
            Optional<BlockStmt> body = md.getBody();
            body.ifPresent(blockStmt -> blockStmt.addStatement(0, expressionStmt));
            super.visit(md, arg);
        }
    }
}
