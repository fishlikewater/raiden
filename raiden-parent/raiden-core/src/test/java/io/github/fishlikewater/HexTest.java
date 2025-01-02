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

import io.github.fishlikewater.raiden.core.Hex;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

/**
 * {@code HexTest}
 * Hex测试
 *
 * @author zhangxiang
 * @version 1.0.0
 * @since 2024/12/25
 */
public class HexTest {

    @Test
    public void test() {
        String testStr = "hello world, ni hao,I'm come";
        String encodeHexStr = Hex.encodeHexStr(testStr.getBytes());
        System.out.println(encodeHexStr);
        System.out.println(Hex.decodeHexStr(encodeHexStr.toCharArray(), StandardCharsets.UTF_8));
    }
}
