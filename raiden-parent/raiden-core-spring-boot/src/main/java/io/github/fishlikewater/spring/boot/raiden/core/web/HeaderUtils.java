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
package io.github.fishlikewater.spring.boot.raiden.core.web;

import io.github.fishlikewater.raiden.core.exception.RaidenException;
import jakarta.servlet.http.HttpServletRequest;

/**
 * {@code HeaderUtils}
 *
 * @author zhangxiang
 * @version 1.0.5
 * @since 2024/07/24
 */
public class HeaderUtils {

    public HeaderUtils() {
        throw new RaidenException("not.support.newInstance");
    }

    /**
     * 获取请求头
     *
     * @param header 请求头
     * @return 请求头
     */
    public static String header(String header) {
        HttpServletRequest request = RequestUtils.getRequest();
        return request.getHeader(header);
    }
}
