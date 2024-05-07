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
package com.github.fishlikewater.raiden.http.core;

import com.github.fishlikewater.raiden.http.core.enums.HttpMethod;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * <p>
 *
 * </p>
 *
 * @author fishlikewater@126.com
 * @since 2023年09月24日 9:37
 * @version 1.0.0
 **/
@Data
@AllArgsConstructor
public class MethodArgsBean {

    private String className;

    private String methodName;

    private String serverName;

    private String sourceHttpClientName;

    private String interceptorClassName;

    private HttpMethod requestMethod;

    private boolean isForm;

    private Map<String, String> headMap;

    private String url;

    private Parameter[] urlParameters;

    private Class<?> returnType;

    private Type typeArgument;
}
