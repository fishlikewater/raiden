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
package com.github.fishlikewater.raiden.http.autoconfigure;

import com.github.fishlikewater.raiden.http.autoconfigure.annotaion.HttpScan;
import com.github.fishlikewater.raiden.http.core.HttpBootStrap;
import com.github.fishlikewater.raiden.http.core.LogConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author fishlikewater@126.com
 * @since 2021年12月26日 13:28
 * @version 1.0.0
 **/
@Slf4j
public class HttpServerScannerRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware, BeanClassLoaderAware, EnvironmentAware {

    private ResourceLoader resourceLoader;

    private ClassLoader classLoader;

    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) throws BeansException {
        this.environment = environment;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, @NonNull BeanDefinitionRegistry registry) {
        AnnotationAttributes attributes = AnnotationAttributes
                .fromMap(metadata.getAnnotationAttributes(HttpScan.class.getName()));
        if (attributes == null) {
            return;
        }
        final Boolean enableLog = environment.getProperty("com.raiden.http.enable-log", boolean.class, false);
        final LogConfig.LogLevel logLevel = environment.getProperty("com.raiden.http.log-level", LogConfig.LogLevel.class, LogConfig.LogLevel.BASIC);
        // Specify the base package for scanning
        String[] basePackages = getPackagesToScan(attributes);
        try {
            HttpBootStrap.init(basePackages);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        if (enableLog) {
            HttpBootStrap.getLogConfig().setEnableLog(true);
            HttpBootStrap.getLogConfig().setLogLevel(logLevel);
        }
        ClassPathRemoteServerScanner scanner = new ClassPathRemoteServerScanner(registry, classLoader);

        if (resourceLoader != null) {
            scanner.setResourceLoader(resourceLoader);
        }
        log.info("Scan the @HttpServer annotated interface using the @RemoteScan configuration. packages={}", Arrays.toString(basePackages));
        scanner.registerFilters();
        // Scan and register to BeanDefinition
        scanner.doScan(basePackages);
    }

    private String[] getPackagesToScan(AnnotationAttributes attributes) {
        String[] value = attributes.getStringArray("value");
        String[] basePackages = attributes.getStringArray("basePackages");
        Class<?>[] basePackageClasses = attributes.getClassArray("basePackageClasses");
        if (!ObjectUtils.isEmpty(value)) {
            Assert.state(ObjectUtils.isEmpty(basePackages),
                    "@HttpScan basePackages and value attributes are mutually exclusive");
        }
        Set<String> packagesToScan = new LinkedHashSet<>();
        packagesToScan.addAll(Arrays.asList(value));
        packagesToScan.addAll(Arrays.asList(basePackages));
        for (Class<?> basePackageClass : basePackageClasses) {
            packagesToScan.add(ClassUtils.getPackageName(basePackageClass));
        }
        return packagesToScan.toArray(new String[0]);
    }

    @Override
    public void setResourceLoader(@NonNull ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void setBeanClassLoader(@NonNull ClassLoader classLoader) {
        this.classLoader = classLoader;
    }
}
