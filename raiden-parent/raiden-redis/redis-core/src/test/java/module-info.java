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
 * @since 2024/07/03
 */
module raiden.redis.core.test {
    requires redisson;
    requires raiden.redis.core;
    requires junit;
    requires raiden.core;
    requires static lombok;

    exports io.github.fishlikewater.raiden.redis.test;
}