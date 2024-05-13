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

import com.github.fishlikewater.raiden.http.core.HttpBootStrap;
import com.github.fishlikewater.raiden.http.core.source.SourceHttpClientRegister;
import com.github.fishlikewater.raiden.http.core.source.SourceHttpClientRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Role;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author fishlikewater@126.com
 * @since 2023年09月24日 12:37
 * @version 1.0.0
 **/
@Slf4j
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@AutoConfiguration
@EnableConfigurationProperties(HttpConfigProperties.class)
public class HttpAutoConfig {

    @Bean
    public SourceHttpClientRegister sourceHttpClientRegister(){
        return (registry) -> {
            final HttpClient defaultClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(60)).version(HttpClient.Version.HTTP_1_1).build();
            registry.register("default", defaultClient);
        };
    }

    @Bean
    @ConditionalOnMissingBean
    public ServiceInstanceChooser retrofitServiceInstanceChooser() {
        return new ServiceInstanceChooser.NoValidServiceInstanceChooser();
    }

    @Bean
    @ConditionalOnMissingBean
    public ServiceChoose serviceChoose(ServiceInstanceChooser serviceInstanceChooser){
        final ServiceChoose serviceChoose = new ServiceChoose(serviceInstanceChooser);
        HttpBootStrap.setPredRequest(serviceChoose);
        return serviceChoose;
    }

    @Bean
    @ConditionalOnMissingBean
    public SourceHttpClientRegistry sourceOkHttpClientRegistry(
            @Autowired(required = false) List<SourceHttpClientRegister> sourceOkHttpClientRegistrars) {
        final SourceHttpClientRegistry sourceHttpClientRegistry = new SourceHttpClientRegistry(sourceOkHttpClientRegistrars);
        sourceHttpClientRegistry.init();
        HttpBootStrap.setSourceHttpClientRegistry(sourceHttpClientRegistry);
        return sourceHttpClientRegistry;
    }
}
