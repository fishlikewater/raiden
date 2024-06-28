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

import org.junit.Test;

/**
 * {@code MD5Test}
 * MD5 测试
 *
 * @author zhangxiang
 * @version 1.0.3
 * @since 2024/06/28
 */
public class MD5Test {

    @Test
    public void testMD5() {
        String testString = "这是一段测试字符串";
        MD5 md5 = new MD5();
        String hex = md5.digestHex(testString);
        System.out.println(hex);
    }
}
