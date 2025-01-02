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

import io.github.fishlikewater.raiden.core.tuple.Tuple;
import io.github.fishlikewater.raiden.core.tuple.Tuple2;
import org.junit.Assert;
import org.junit.Test;

/**
 * {@code TupleTest}
 *
 * @author zhangxiang
 * @version 1.0.8
 * @since 2024/11/29
 */
public class TupleTest {

    @Test
    public void testTuple() {
        Tuple tuple = Tuple.newInstance(2);
        tuple.setField("1", 0);
        tuple.setField(1, 1);
        int field = tuple.getField(1);
        Assert.assertEquals(1, field);
    }

    @Test
    public void testTuple2() {
        Tuple2<Integer, String> tuple2 = Tuple2.of(1, "zs");
        Assert.assertEquals(1, (int) tuple2.f0);
    }
}
