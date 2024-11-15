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

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Paths;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.boot.autoconfigure.web.ServerProperties;

import java.io.Serial;

/**
 * {@code DocOpenApiCustomizer}
 *
 * @author zhangxiang
 * @version 1.0.8
 * @since 2024/11/15
 */
@RequiredArgsConstructor
public class DocOpenApiCustomizer implements OpenApiCustomizer {

    private final ServerProperties serverProperties;

    @Override
    public void customise(OpenAPI openApi) {
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
