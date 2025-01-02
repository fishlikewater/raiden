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
package io.github.fishlikewater.raiden.generate;

import org.junit.Test;

/**
 * {@code IdGenerateTest}
 *
 * @author zhangxiang
 * @version 1.0.0
 * @since 2024/05/30
 */
public class IdGenerateTest {

    @Test
    public void testIdGenerate() {
        IdGenerate idGenerate = new IdGenerate();
        System.out.println(idGenerate.generate());
        System.out.println(idGenerate.generateString());
    }

}
