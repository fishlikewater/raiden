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

import cn.hutool.core.util.StrUtil;
import com.github.fishlikewater.raiden.http.core.MethodArgsBean;
import com.github.fishlikewater.raiden.http.core.interceptor.PredRequest;
import lombok.RequiredArgsConstructor;

import java.net.URI;

/**
 * 注册服务名处理
 *
 * @author fishlikewater@126.com
 * @since 2023年09月26日 14:44
 * @version 1.0.0
 **/
@RequiredArgsConstructor
public class ServiceChoose implements PredRequest {

    private final ServiceInstanceChooser serviceInstanceChooser;

    @Override
    public void handler(MethodArgsBean methodArgsBean) {
        if (StrUtil.isBlank(methodArgsBean.getUrl())){
            final String serverName = methodArgsBean.getServerName();
            if (StrUtil.isBlank(serverName)){
                throw new RuntimeException("not config request");
            }
            final URI uri = serviceInstanceChooser.choose(serverName);
            methodArgsBean.setUrl(uri.toString());
        }
    }
}
