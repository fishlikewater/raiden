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
/**
 * {@code module-info}
 *
 * @author zhangxiang
 * @since 2024/06/17
 */
module raiden.redis.core {
    requires static lombok;
    requires redisson;
    requires org.slf4j;
    requires raiden.core;
    requires raiden.json.core;
    requires com.fasterxml.jackson.core;

    exports io.github.fishlikewater.raiden.redis.core;
    exports io.github.fishlikewater.raiden.redis.core.annotation;
    exports io.github.fishlikewater.raiden.redis.core.delay;
    exports io.github.fishlikewater.raiden.redis.core.enums;
}