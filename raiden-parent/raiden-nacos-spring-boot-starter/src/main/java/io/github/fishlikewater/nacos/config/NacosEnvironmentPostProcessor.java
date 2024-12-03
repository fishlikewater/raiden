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
package io.github.fishlikewater.nacos.config;

import com.alibaba.boot.nacos.config.autoconfigure.NacosBootConfigException;
import com.alibaba.boot.nacos.config.properties.NacosConfigProperties;
import com.alibaba.boot.nacos.config.util.NacosConfigLoader;
import com.alibaba.boot.nacos.config.util.NacosConfigLoaderFactory;
import com.alibaba.boot.nacos.config.util.NacosConfigPropertiesUtils;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.ConfigType;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.spring.factory.CacheableEventPublishingNacosServiceFactory;
import com.alibaba.nacos.spring.util.NacosUtils;
import io.github.fishlikewater.nacos.annotation.NacosPreScan;
import io.github.fishlikewater.nacos.model.ConfigMeta;
import io.github.fishlikewater.nacos.registry.AbstractNacosConfigRegister;
import io.github.fishlikewater.raiden.core.LambdaUtils;
import io.github.fishlikewater.raiden.core.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.type.filter.AssignableTypeFilter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.function.Function;

/**
 * NacosEnvironmentPostProcessor
 *
 * @author zhangxiang
 * @version 1.0.8
 * @since 2024/11/23
 **/
@Slf4j
public class NacosEnvironmentPostProcessor implements EnvironmentPostProcessor {

    private NacosConfigProperties nacosConfigProperties;

    private final CacheableEventPublishingNacosServiceFactory singleton = CacheableEventPublishingNacosServiceFactory.getSingleton();
    private final Map<String, ConfigService> serviceCache = new HashMap<>(8);

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        Class<?> mainApplicationClass = application.getMainApplicationClass();
        if (ObjectUtils.isNullOrEmpty(mainApplicationClass)) {
            return;
        }
        // 没有注解 或者注解没有指定包或类 则扫描主类包
        NacosPreScan preScan = mainApplicationClass.getAnnotation(NacosPreScan.class);
        if (ObjectUtils.isNullOrEmpty(preScan) || this.determineNotScan(preScan)) {
            this.sanPackage(mainApplicationClass.getPackageName(), environment);
            return;
        }
        // 扫描指定的包
        String[] basePackages = preScan.basePackages();
        if (ObjectUtils.isNotNullOrEmpty(basePackages)) {
            for (String basePackage : basePackages) {
                this.sanPackage(basePackage, environment);
            }
        }
        // 扫描指定的类
        Class<? extends AbstractNacosConfigRegister>[] classes = preScan.baseClasses();
        if (ObjectUtils.isNotNullOrEmpty(classes)) {
            this.scanClasses(classes, environment);
        }
    }

    private void scanClasses(Class<? extends AbstractNacosConfigRegister>[] classes, ConfigurableEnvironment environment) {
        for (Class<? extends AbstractNacosConfigRegister> clazz : classes) {
            this.loadClassConfig(environment, clazz);
        }
    }

    private boolean determineNotScan(NacosPreScan preScan) {
        return ObjectUtils.isNullOrEmpty(preScan.baseClasses()) && ObjectUtils.isNullOrEmpty(preScan.basePackages());
    }

    private void sanPackage(String packageName, ConfigurableEnvironment environment) {
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AssignableTypeFilter(AbstractNacosConfigRegister.class));
        for (BeanDefinition beanDefinition : provider.findCandidateComponents(packageName)) {
            try {
                Class<?> clazz = Class.forName(beanDefinition.getBeanClassName());
                this.loadClassConfig(environment, clazz);
            } catch (Exception e) {
                log.error("load class exception:", e);
            }
        }
    }

    private void loadClassConfig(ConfigurableEnvironment environment, Class<?> clazz) {
        try {
            AbstractNacosConfigRegister register = (AbstractNacosConfigRegister) clazz.getConstructor().newInstance();
            List<ConfigMeta> configMeta = register.getConfigMeta();
            LambdaUtils.handle(configMeta, meta -> this.refreshEnvironment(meta, environment));
        } catch (Exception e) {
            log.error("load class exception:", e);
        }
    }

    private void refreshEnvironment(ConfigMeta meta, ConfigurableEnvironment environment) {
        if (ObjectUtils.isNullOrEmpty(nacosConfigProperties)) {
            nacosConfigProperties = NacosConfigPropertiesUtils.buildNacosConfigProperties(environment);
        }
        NacosConfigLoader configLoader = NacosConfigLoaderFactory.getSingleton(this.builder);
        nacosConfigProperties.setDataIds(meta.getDataId());
        nacosConfigProperties.setGroup(meta.getGroupId());
        nacosConfigProperties.setType(ConfigType.valueOf(meta.getType().toUpperCase()));
        nacosConfigProperties.setAutoRefresh(false);
        configLoader.loadConfig(environment, this.nacosConfigProperties);
    }

    private final Function<Properties, ConfigService> builder = properties -> {
        try {
            final String key = NacosUtils.identify(properties);
            if (serviceCache.containsKey(key)) {
                return serviceCache.get(key);
            }
            final ConfigService configService = NacosFactory.createConfigService(properties);
            serviceCache.put(key, configService);
            return singleton.deferCreateService(configService, properties);
        } catch (NacosException e) {
            throw new NacosBootConfigException(
                    "ConfigService can't be created with properties : " + properties, e);
        }
    };
}
