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
import io.github.fishlikewater.raiden.docs.autoconfig.annotation.DocTag;
import io.swagger.v3.oas.models.Operation;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.customizers.GlobalOperationCustomizer;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.util.UriUtils;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Set;

/**
 * DocOperationCustomizer
 *
 * @author zhangxiang
 * @version 1.0.8
 * @since 2024/11/18
 **/
@RequiredArgsConstructor
public class DocOperationCustomizer implements GlobalOperationCustomizer {

    private final DocProperties docProperties;

    @Override
    public Operation customize(Operation operation, HandlerMethod handlerMethod) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (ObjectUtils.isNullOrEmpty(attributes)) {
            return operation;
        }

        String url = attributes.getRequest().getRequestURI();
        DocProperties.GroupConfig group = this.tryAcquireGroup(url);
        if (ObjectUtils.isNullOrEmpty(group)) {
            return operation;
        }

        List<String> tags = group.getTag();
        if (ObjectUtils.isNullOrEmpty(tags)) {
            return operation;
        }

        DocTag classDocTag = handlerMethod.getBeanType().getDeclaredAnnotation(DocTag.class);
        if (ObjectUtils.isNotNullOrEmpty(classDocTag)) {
            String[] classTags = classDocTag.value();
            for (String classTag : classTags) {
                if (tags.contains(classTag)) {
                    return operation;
                }
            }
        }

        DocTag docTag = handlerMethod.getMethod().getDeclaredAnnotation(DocTag.class);
        if (ObjectUtils.isNotNullOrEmpty(docTag)) {
            String[] methodTags = docTag.value();
            for (String methodTag : methodTags) {
                if (tags.contains(methodTag)) {
                    return operation;
                }
            }
        }

        return null;
    }

    private DocProperties.GroupConfig tryAcquireGroup(String url) {
        String[] split = url.split(CommonConstants.Symbol.SYMBOL_PATH);
        if (split.length > 0) {
            String groupName = split[split.length - 1];
            // 解码
            String decodeName = UriUtils.decode(groupName, Charset.defaultCharset());
            Set<DocProperties.GroupConfig> groupConfigs = docProperties.getGroupConfigs();
            return LambdaUtils.findFirst(groupConfigs, groupConfig -> groupConfig.getGroup().equals(decodeName));
        }
        return null;
    }
}
