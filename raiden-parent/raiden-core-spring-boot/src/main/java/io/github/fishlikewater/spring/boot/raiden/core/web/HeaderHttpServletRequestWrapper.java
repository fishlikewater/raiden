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

import io.github.fishlikewater.raiden.core.ObjectUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.springframework.http.HttpHeaders;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;

/**
 * {@code HeaderHttpServletRequestWrapper}
 *
 * @author zhangxiang
 * @version 1.0.5
 * @since 2024/07/24
 */
public class HeaderHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private final HttpHeaders headers;

    public HeaderHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
        this.headers = new HttpHeaders();
    }

    public HeaderHttpServletRequestWrapper(HttpServletRequest request, HttpHeaders headers) {
        super(request);
        this.headers = headers;
    }

    public void setHeader(String name, String value) {
        headers.add(name, value);
    }

    @Override
    public String getHeader(String name) {
        String headerValue = this.headers.getFirst(name);
        if (headerValue != null) {
            return headerValue;
        }

        return this.tryHttpServletRequest().getHeader(name);
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        if (this.headers.containsKey(name)) {
            return Collections.enumeration(Collections.singletonList(this.headers.getFirst(name)));
        }

        return this.tryHttpServletRequest().getHeaders(name);
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        HashMap<String, String> allHeaders = new HashMap<>();
        if (ObjectUtils.isNotNullOrEmpty(this.headers)) {
            this.headers.forEach((key, value) -> allHeaders.put(key, value.get(0)));
        }

        Enumeration<String> originalHeaders = this.tryHttpServletRequest().getHeaderNames();
        while (originalHeaders.hasMoreElements()) {
            String headerName = originalHeaders.nextElement();
            allHeaders.putIfAbsent(headerName, this.tryHttpServletRequest().getHeader(headerName));
        }

        return Collections.enumeration(allHeaders.keySet());
    }

    private HttpServletRequest tryHttpServletRequest() {
        return (HttpServletRequest) this.getRequest();
    }
}
