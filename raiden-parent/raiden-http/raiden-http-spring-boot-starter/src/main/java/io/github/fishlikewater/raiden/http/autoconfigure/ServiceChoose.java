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

import io.github.fishlikewater.raiden.core.StringUtils;
import io.github.fishlikewater.raiden.core.constant.CommonConstants;
import io.github.fishlikewater.raiden.http.core.MethodArgsBean;
import io.github.fishlikewater.raiden.http.core.interceptor.PredRequestInterceptor;
import lombok.RequiredArgsConstructor;

import java.net.URI;

/**
 * 注册服务名处理
 *
 * @author fishlikewater@126.com
 * @version 1.0.0
 * @since 2023年09月26日 14:44
 **/
@RequiredArgsConstructor
public class ServiceChoose implements PredRequestInterceptor {

    private final ServiceInstanceChooser serviceInstanceChooser;

    @Override
    public void handler(MethodArgsBean methodArgsBean) {
        final String serverName = methodArgsBean.getServerName();
        if (StringUtils.isNotBlank(serverName)) {
            final URI uri = serviceInstanceChooser.choose(serverName);
            String path = methodArgsBean.getPath();
            if (path.startsWith(CommonConstants.Symbol.SYMBOL_PATH)) {
                path = path.substring(1);
            }
            methodArgsBean.setUrl(StringUtils.format("{}://{}:{}/{}", uri.getScheme(), uri.getHost(), uri.getPort(), path));
        }
    }
}
