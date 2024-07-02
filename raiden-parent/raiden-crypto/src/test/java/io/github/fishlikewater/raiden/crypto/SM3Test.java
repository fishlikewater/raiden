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
package io.github.fishlikewater.raiden.crypto;

import io.github.fishlikewater.raiden.crypto.digest.SM3;
import org.junit.Test;

/**
 * {@code SM3Test}
 *
 * @author zhangxiang
 * @version 1.0.3
 * @since 2024/07/02
 */
public class SM3Test {

    @Test
    public void testSM3() {
        String testString = "这是一段测试字符串";
        SM3 sm3 = new SM3();
        String hex = sm3.digestHex(testString);
        System.out.println(hex);
    }
}
