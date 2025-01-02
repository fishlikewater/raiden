/*
 * Copyright (c) 2025 zhangxiang (fishlikewater@126.com)
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
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * {@code RequestUtils}
 *
 * @author zhangxiang
 * @version 1.0.5
 * @since 2024/07/24
 */
public class RequestUtils {

    private RequestUtils() {
        throw new RaidenException("not.support.newInstance");
    }

    /**
     * 获取当前请求
     *
     * @return HttpServletRequest
     */
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        Objects.requireNonNull(requestAttributes);
        return requestAttributes.getRequest();
    }

    /**
     * 获取当前响应
     *
     * @return HttpServletResponse
     */
    public static HttpServletResponse getResponse() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        Objects.requireNonNull(requestAttributes);
        HttpServletResponse response = requestAttributes.getResponse();
        assert response != null;
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        return response;
    }

    /**
     * 重置请求
     *
     * @param request HttpServletRequest
     */
    public static void resetRequest(HttpServletRequest request) {
        ServletRequestAttributes requestAttributes = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(requestAttributes);
    }

    /**
     * 重置请求
     *
     * @param request HttpServletRequest
     * @param headers HttpHeaders
     */
    public static void resetRequest(HttpServletRequest request, HttpHeaders headers) {
        HeaderHttpServletRequestWrapper requestWrapper = new HeaderHttpServletRequestWrapper(request, headers);
        ServletRequestAttributes requestAttributes = new ServletRequestAttributes(requestWrapper);
        RequestContextHolder.setRequestAttributes(requestAttributes);
    }
}
