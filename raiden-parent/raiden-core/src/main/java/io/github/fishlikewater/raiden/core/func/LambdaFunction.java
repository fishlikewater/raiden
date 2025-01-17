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
package io.github.fishlikewater.raiden.core.func;

import java.io.Serializable;
import java.util.function.Function;

/**
 * {@code LambdaFunction}
 *
 * @author zhangxiang
 * @version 1.0.0
 * @since 2024/04/30
 */
@FunctionalInterface
public interface LambdaFunction<T, R> extends Function<T, R>, Serializable {

}
