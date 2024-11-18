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
package io.github.fishlikewater.raiden.docs.autoconfig.handle;

import io.github.fishlikewater.raiden.core.LambdaUtils;
import io.github.fishlikewater.raiden.core.ObjectUtils;
import io.github.fishlikewater.raiden.core.constant.CommonConstants;
import io.github.fishlikewater.raiden.docs.autoconfig.DocProperties;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Paths;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.Serial;
import java.util.Set;

/**
 * {@code DocOpenApiCustomizer}
 *
 * @author zhangxiang
 * @version 1.0.8
 * @since 2024/11/15
 */
@RequiredArgsConstructor
public class DocOpenApiCustomizer implements GlobalOpenApiCustomizer {

    private final ServerProperties serverProperties;
    private final DocProperties docProperties;

    @Override
    public void customise(OpenAPI openApi) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (ObjectUtils.isNotNullOrEmpty(attributes)) {
            String url = attributes.getRequest().getRequestURI();
            DocProperties.GroupConfig group = this.tryAcquireGroup(url);
            if (ObjectUtils.isNotNullOrEmpty(group)) {

            }
        }

        this.toAddContextPath(openApi);
    }

    private DocProperties.GroupConfig tryAcquireGroup(String url) {
        String[] split = url.split(CommonConstants.Symbol.SYMBOL_PATH);
        if (split.length > 0) {
            String groupName = split[split.length - 1];
            Set<DocProperties.GroupConfig> groupConfigs = docProperties.getGroupConfigs();
            return LambdaUtils.findFirst(groupConfigs, groupConfig -> groupConfig.getGroup().equals(groupName));
        }
        return null;
    }

    private void toAddContextPath(OpenAPI openApi) {
        String contextPath = serverProperties.getServlet().getContextPath();
        String finalContextPath;
        if ((contextPath == null || contextPath.trim().isEmpty()) || "/".equals(contextPath)) {
            finalContextPath = "";
        } else {
            finalContextPath = contextPath;
        }
        Paths oldPaths = openApi.getPaths();
        if (oldPaths instanceof CustomerPaths) {
            return;
        }
        CustomerPaths newPaths = new CustomerPaths();
        oldPaths.forEach((k, v) -> newPaths.addPathItem(finalContextPath + k, v));
        openApi.setPaths(newPaths);
    }

    /**
     * 单独使用一个类便于判断 解决springdoc路径拼接重复问题
     */
    static class CustomerPaths extends Paths {

        @Serial
        private static final long serialVersionUID = 978599468583092579L;

        public CustomerPaths() {
            super();
        }
    }
}
