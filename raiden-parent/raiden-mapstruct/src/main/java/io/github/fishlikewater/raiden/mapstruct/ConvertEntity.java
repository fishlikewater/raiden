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
package io.github.fishlikewater.raiden.mapstruct;

/**
 * <p>
 * 类型转换接口
 * </p>
 *
 * @author fishlikewater@126.com
 * @since 2024年05月11日 20:43
 **/
public interface ConvertEntity<S, T> {

    /**
     * @param source 被转换的类
     * @return T 转换后的类
     */
    T convertT(S source);

    /**
     * @param target 被转换的类
     * @return S 转换后的类
     */
    S convertS(T target);
}
