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
package io.github.fishlikewater;

import io.github.fishlikewater.raiden.core.Assert;
import io.github.fishlikewater.raiden.core.RandomUtils;
import org.junit.Test;

/**
 * {@code RandomTest}
 *
 * @author zhangxiang
 * @version 1.0.0
 * @since 2024/05/07
 */
public class RandomTest {

    @Test
    public void testRandomNum() {
        String randNum = RandomUtils.randNum(6);
        Assert.isTrue(randNum.length() == 6, "随机数长度不为6");
    }

    @Test
    public void testRandomNumAndAlphabet() {
        String randNum = RandomUtils.randomNumberAndAlphabet(6);
        Assert.isTrue(randNum.length() == 6, "随机数长度不为6");
    }

    @Test
    public void testRandomNumWithCount() {
        String randNum = RandomUtils.randomInt(0, 100, true);
        Assert.isTrue(randNum.length() == 3, "随机数长度不为6");
    }
}
