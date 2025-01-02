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
package io.github.fishlikewater.raiden.crypto;

import io.github.fishlikewater.raiden.core.Hex;
import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

/**
 * <p>
 * {@code HexTest}
 * </p>
 *
 * @author fishlikewater@126.com
 * @version 1.0.2
 * @since 2024年06月08日 12:10
 **/
public class HexTest {

    @Test
    public void testHex16() {
        String s = "0FF123456";
        final boolean hexNumber = Hex.isHexNumber(s);
        Assert.assertTrue(hexNumber);

        String string = "testHex";
        final String hexStr = Hex.encodeHexStr(string);
        System.out.println(hexStr);

        final byte[] bytes = Hex.decodeHex(hexStr);
        System.out.println(new String(bytes, StandardCharsets.UTF_8));
    }
}
