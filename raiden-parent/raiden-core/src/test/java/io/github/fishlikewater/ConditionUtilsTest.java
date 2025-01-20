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
package io.github.fishlikewater;

import io.github.fishlikewater.raiden.core.ConditionUtils;
import org.junit.Test;

/**
 * {@code ConditionUtilsTest}
 * 条件工具测试
 *
 * @author zhangxiang
 * @version 1.1.3
 * @since 2025/01/20
 */
public class ConditionUtilsTest {

    @Test
    public void testConditionUtils() {
        ConditionUtils.of(() -> true)
                .isTrue(() -> false)
                .orElse(() -> true)
                .and(() -> true)
                .or(() -> true)
                .isTrue(() -> true)
                .isTrue(() -> System.out.println("11111"));
    }

    @Test
    @SuppressWarnings("all")
    public void testConditionUtils2() {
        String name = "fishlikewater";
        ConditionUtils.of(() -> name.startsWith("fish"))
                .isTrue(() -> {
                    System.out.println("isTrue");
                    System.out.println("fish");
                    return true;
                })
                .orElse(() -> {
                    System.out.println("orElse");
                    System.out.println("not fish");
                    return false;
                })
                .and(() -> {
                    System.out.println("and");
                    return name.length() > 10;
                })
                .or(() -> {
                    System.out.println("or");
                    return name.endsWith("water");
                })
                .isTrue(() -> {
                    System.out.println("isTrue");
                    return true;
                })
                .isTrue(() -> System.out.println("11111"));
    }

    @Test
    @SuppressWarnings("all")
    public void testConditionUtils3() {
        String name = "fishlikewater";
        ConditionUtils.Condition condition = ConditionUtils.of(() -> name.startsWith("zfish"));
        System.out.println(condition.and(() -> false).or(() -> true));
        condition = condition
                .isTrue(() -> {
                    System.out.println("istrue");
                    return false;
                })
                .orElse(() -> {
                    System.out.println("orElse");
                    return false;
                });
        condition.isFalse(() -> {System.out.println("isfalse");});
    }
}
