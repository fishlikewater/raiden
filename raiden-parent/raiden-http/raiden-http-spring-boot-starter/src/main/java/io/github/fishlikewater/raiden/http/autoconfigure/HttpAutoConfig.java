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
package io.github.fishlikewater.raiden.http.autoconfigure;

import io.github.fishlikewater.raiden.core.ObjectUtils;
import io.github.fishlikewater.raiden.http.core.HttpBootStrap;
import io.github.fishlikewater.raiden.http.core.constant.DefaultConstants;
import io.github.fishlikewater.raiden.http.core.constant.HttpConstants;
import io.github.fishlikewater.raiden.http.core.degrade.resilience4j.CircuitBreakerConfigRegister;
import io.github.fishlikewater.raiden.http.core.degrade.resilience4j.CircuitBreakerConfigRegistry;
import io.github.fishlikewater.raiden.http.core.degrade.resilience4j.GlobalBreakerConfigRegister;
import io.github.fishlikewater.raiden.http.core.degrade.sentinel.GlobalSentinelConfigRegister;
import io.github.fishlikewater.raiden.http.core.degrade.sentinel.SentinelDegradeRuleRegister;
import io.github.fishlikewater.raiden.http.core.degrade.sentinel.SentinelDegradeRuleRegistry;
import io.github.fishlikewater.raiden.http.core.processor.DefaultExceptionProcessor;
import io.github.fishlikewater.raiden.http.core.processor.ExceptionProcessor;
import io.github.fishlikewater.raiden.http.core.source.SourceHttpClientRegister;
import io.github.fishlikewater.raiden.http.core.source.SourceHttpClientRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Role;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author fishlikewater@126.com
 * @version 1.0.0
 * @since 2023年09月24日 12:37
 **/
@Slf4j
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@AutoConfiguration
@EnableConfigurationProperties(HttpConfigProperties.class)
public class HttpAutoConfig {

    @Bean
    public HttpContextRefreshedEventListener httpContextRefreshedEventListener() {
        return new HttpContextRefreshedEventListener();
    }

    @Bean
    public HttpBeanProcessor httpBeanProcessor() {
        return new HttpBeanProcessor();
    }

    @Bean
    public ExceptionProcessor exceptionProcessor() {
        return new DefaultExceptionProcessor();
    }

    @Bean
    public SourceHttpClientRegister sourceHttpClientRegister() {
        return (registry) -> {
            final HttpClient defaultClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(60)).version(HttpClient.Version.HTTP_1_1).build();
            registry.register(HttpConstants.DEFAULT, defaultClient);
        };
    }

    @Bean
    @ConditionalOnMissingBean
    public ServiceInstanceChooser serviceInstanceChooser() {
        return new ServiceInstanceChooser.NoValidServiceInstanceChooser();
    }

    @Bean
    @ConditionalOnMissingBean
    public ServiceChoose serviceChoose(ServiceInstanceChooser serviceInstanceChooser) {
        final ServiceChoose serviceChoose = new ServiceChoose(serviceInstanceChooser);
        HttpBootStrap.registryPredRequestInterceptor(serviceChoose);
        return serviceChoose;
    }

    @Bean
    @ConditionalOnMissingBean
    public SourceHttpClientRegistry sourceOkHttpClientRegistry(
            @Autowired(required = false) List<SourceHttpClientRegister> sourceOkHttpClientRegistrars) {
        final SourceHttpClientRegistry sourceHttpClientRegistry = new SourceHttpClientRegistry(sourceOkHttpClientRegistrars);
        sourceHttpClientRegistry.init();
        HttpBootStrap.getConfig().setSourceHttpClientRegistry(sourceHttpClientRegistry);
        return sourceHttpClientRegistry;
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(CircuitBreakerConfigRegister.class)
    public CircuitBreakerConfigRegistry circuitBreakerConfigRegistry(
            @Autowired(required = false) GlobalBreakerConfigRegister globalBreakerConfigRegister,
            @Autowired(required = false) List<CircuitBreakerConfigRegister> circuitBreakerConfigRegisters) {
        CircuitBreakerConfigRegistry registry = new CircuitBreakerConfigRegistry(circuitBreakerConfigRegisters);
        if (ObjectUtils.isNotNullOrEmpty(globalBreakerConfigRegister)) {
            registry.register(DefaultConstants.GLOBAL_CIRCUIT_BREAKER_CONFIG, globalBreakerConfigRegister.get());
        }
        HttpBootStrap.getConfig().setBreakerConfigRegistry(registry);
        return registry;
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(GlobalBreakerConfigRegister.class)
    public CircuitBreakerConfigRegistry circuitBreakerConfigRegistry2(GlobalBreakerConfigRegister globalBreakerConfigRegister) {
        CircuitBreakerConfigRegistry registry = new CircuitBreakerConfigRegistry(new ArrayList<>());
        registry.register(DefaultConstants.GLOBAL_CIRCUIT_BREAKER_CONFIG, globalBreakerConfigRegister.get());
        HttpBootStrap.getConfig().setBreakerConfigRegistry(registry);
        return registry;
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(SentinelDegradeRuleRegister.class)
    public SentinelDegradeRuleRegistry sentinelDegradeRuleRegistry(
            @Autowired(required = false) GlobalSentinelConfigRegister globalSentinelConfigRegister,
            @Autowired(required = false) List<SentinelDegradeRuleRegister> sentinelDegradeRuleRegisters) {
        SentinelDegradeRuleRegistry registry = new SentinelDegradeRuleRegistry(sentinelDegradeRuleRegisters);
        if (ObjectUtils.isNotNullOrEmpty(globalSentinelConfigRegister)) {
            registry.register(DefaultConstants.GLOBAL_CIRCUIT_BREAKER_CONFIG, globalSentinelConfigRegister.get());
        }
        HttpBootStrap.getConfig().setSentDegradeRuleRegistry(registry);
        return registry;
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(GlobalSentinelConfigRegister.class)
    public SentinelDegradeRuleRegistry sentinelDegradeRuleRegistry2(GlobalSentinelConfigRegister globalSentinelConfigRegister) {
        SentinelDegradeRuleRegistry registry = new SentinelDegradeRuleRegistry(new ArrayList<>());
        registry.register(DefaultConstants.GLOBAL_CIRCUIT_BREAKER_CONFIG, globalSentinelConfigRegister.get());
        HttpBootStrap.getConfig().setSentDegradeRuleRegistry(registry);
        return registry;
    }
}
