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
package com.github.fishlikewater.raiden.http.core.remote;

import com.github.fishlikewater.raiden.http.core.HeadWrap;
import com.github.fishlikewater.raiden.http.core.annotation.*;

import java.util.Map;

/**
 * {@code DemoLocal}
 * 测试接口
 *
 * @author fishlikewater@126.com
 * @since 2024/03/20
 * @version 1.0.0
 */

@HttpServer(url = "http://127.0.0.1:8080")
public interface DemoLocal {


    /**
     * 测试本地接口
     *
     * @param map {@code Map}
     * @return {@code String}
     */
    @POST("/demo")
    @Heads({
            "customer: xxx"
    })
    String demo(@Body Map<String, Object> map);

    /**
     * 测试本地接口 请求头为变量时 放在参数中时 参数类型只能为 {@code Map<String, String>} 或者 {@link HeadWrap}
     *
     * @param map {@code Map}
     * @param headMap {@code Map}
     * @return {@code String}
     */
    @POST("/demo")
    String demo(@Body Map<String, Object> map, @Heads Map<String, String> headMap);

    /**
     * 测试本地接口 请求头为变量时 放在参数中时 参数类型只能为 {@code Map<String, String>} 或者 {@link HeadWrap}
     *
     * @param map {@code Map}
     * @param headWrap {@code HeadWrap}
     * @return {@code String}
     */
    @POST("/demo")
    String demo(@Body Map<String, Object> map, @Heads HeadWrap headWrap);

    /**
     * 测试form请求
     *
     * @param map {@code Map}
     * @return {@code String}
     */
    @POST("/form")
    @Form
    String form(@Body Map<String, Object> map);


}
