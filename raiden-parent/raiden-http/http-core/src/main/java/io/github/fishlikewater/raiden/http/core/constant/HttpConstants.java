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
package io.github.fishlikewater.raiden.http.core.constant;

/**
 * {@code HttpConstants}
 * 一些常量
 *
 * @author zhangxiang
 * @version 1.0.2
 * @since 2024/06/11
 */
public interface HttpConstants {

    String DEFAULT = "default";

    String CONTENT_TYPE = "Content-Type";

    String CONTENT_TYPE_JSON = "application/json;charset=utf-8";

    String CONTENT_TYPE_FORM = "application/x-www-form-urlencoded;charset=utf-8";

    String URL_PARAMETER_SPLIT = "?";

    String URL_SPLIT = "/";

    String HEAD_SPLIT_SYMBOL = ":";

    String HTTP = "http";

    int HTTP_OK = 200;

    int DEFAULT_READ_LIMIT = 1024 * 1024;

}
