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

import io.github.fishlikewater.raiden.http.core.enums.DegradeType;
import io.github.fishlikewater.raiden.http.core.enums.LogLevel;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>
 *
 * </p>
 *
 * @author fishlikewater@126.com
 * @version 1.0.0
 * @since 2023年09月24日 12:35
 **/

@Data
@ConfigurationProperties(prefix = "raiden.http")
public class HttpConfigProperties {

    private boolean enableLog;

    private LogLevel logLevel;

    private int maxRetryCount;

    private long retryInterval;

    private boolean enableGlobalDegrade;

    private DegradeType degradeType;
}
