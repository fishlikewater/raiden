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
package com.github.fishlikewater.raiden.json.autoconfig;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fishlikewater.raiden.json.core.JSONUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

/**
 * {@code JacksonConfig}
 * <p>
 * json 配置
 * </p>
 *
 * @author fishlikewater@126.com
 * @since 2024年05月11日 20:43
 **/
@Slf4j
public class JacksonConfig {

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        return JSONUtils.JACKSON;
    }

}
