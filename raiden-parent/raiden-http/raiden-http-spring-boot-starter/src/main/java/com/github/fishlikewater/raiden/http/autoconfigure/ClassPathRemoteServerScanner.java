/*
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

import com.github.fishlikewater.raiden.http.autoconfigure.proxy.SpringJdkInterfaceProxy;
import com.github.fishlikewater.raiden.http.core.HttpBootStrap;
import com.github.fishlikewater.raiden.http.core.annotation.HttpServer;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;

import java.util.Arrays;
import java.util.Set;

/**
 * @author fishlikewater@126.com
 * @since 2023年09月22日 12:32
 * @version 1.0.0
 */
@Slf4j
public class ClassPathRemoteServerScanner extends ClassPathBeanDefinitionScanner {

    private final ClassLoader classLoader;


    public ClassPathRemoteServerScanner(BeanDefinitionRegistry registry, ClassLoader classLoader) {
        super(registry, false);
        this.classLoader = classLoader;
    }

    public void registerFilters() {
        AnnotationTypeFilter annotationTypeFilter = new AnnotationTypeFilter(HttpServer.class);
        this.addIncludeFilter(annotationTypeFilter);
    }

    @NonNull
    @Override
    protected Set<BeanDefinitionHolder> doScan(@NonNull String @NonNull ... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);
        if (beanDefinitions.isEmpty()) {
            log.warn(STR."No was found in '\{Arrays.toString(basePackages)}' package. Please check your configuration.");
        } else {
            processBeanDefinitions(beanDefinitions);
        }
        return beanDefinitions;
    }

    @Override
    protected boolean isCandidateComponent(
            AnnotatedBeanDefinition beanDefinition) {
        if (beanDefinition.getMetadata().isInterface()) {
            try {
                Class<?> target = ClassUtils.forName(
                        beanDefinition.getMetadata().getClassName(),
                        classLoader);
                return !target.isAnnotation();
            } catch (Exception ex) {
                log.error("load class exception:", ex);
            }
        }
        return false;
    }

    private void processBeanDefinitions(Set<BeanDefinitionHolder> beanDefinitions) {
        GenericBeanDefinition definition;
        for (BeanDefinitionHolder holder : beanDefinitions) {
            definition = (GenericBeanDefinition) holder.getBeanDefinition();
            if (log.isDebugEnabled()) {
                log.debug(STR."Creating RemoteServerBean with name '\{holder.getBeanName()}' and '\{definition.getBeanClassName()}' Interface");
            }
            definition.getPropertyValues().add("interfaceClass", definition.getBeanClassName());
            definition.getPropertyValues().add("httpClientProcessor", HttpBootStrap.getHttpClientProcessor());
            definition.getPropertyValues().add("httpClientBeanFactory", HttpBootStrap.getHttpClientBeanFactory());
            definition.setBeanClass(SpringJdkInterfaceProxy.class);
            definition.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
            definition.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_TYPE);
        }
    }
}
