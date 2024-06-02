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
package io.github.fishlikewater.raiden.core;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

/**
 * {@code CollectionUtils}
 *
 * @author zhangxiang
 * @version 1.0.0
 * @since 2024/04/30
 */
public final class CollectionUtils {

    /**
     * 判断对象是否为空
     *
     * @param object 对象
     * @return 是否为空
     */
    public static boolean isEmpty(Object object) {
        switch (object) {
            case null -> {
                return true;
            }
            case Collection<?> coll -> {
                return coll.isEmpty();
            }
            case Iterable<?> iterable -> {
                return !iterable.iterator().hasNext();
            }
            case Map<?, ?> map -> {
                return map.isEmpty();
            }
            case Object[] objects -> {
                return objects.length == 0;
            }
            case Iterator<?> iterator -> {
                return !iterator.hasNext();
            }
            case Enumeration<?> enumeration -> {
                return !enumeration.hasMoreElements();
            }
            default -> {
                try {
                    return Array.getLength(object) == 0;
                } catch (IllegalArgumentException var2) {
                    throw new IllegalArgumentException(STR."Unsupported object type: \{object.getClass().getName()}");
                }
            }
        }
    }
}
