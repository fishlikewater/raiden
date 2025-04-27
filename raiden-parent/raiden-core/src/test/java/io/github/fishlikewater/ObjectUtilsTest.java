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

import io.github.fishlikewater.raiden.core.ObjectUtils;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

/**
 * {@code ObjectUtilsTest}
 *
 * @author zhangxiang
 * @version 1.1.3
 * @since 2025/01/24
 */
public class ObjectUtilsTest {

    @Test
    public void test() {
        int integer = ObjectUtils.convert("1", int.class);
        long aLong = ObjectUtils.convert("1", Long.class);
        String helloWord = ObjectUtils.convert("hello word", String.class);
        Assert.assertEquals("hello word", helloWord);
        Assert.assertEquals(1, integer);
        Assert.assertEquals(1, aLong);

        TestBean testBean = new TestBean();
        testBean.setName("fishlikewater");
        boolean notNullOrEmpty = ObjectUtils.isNotNullOrEmpty(testBean, TestBean::getName);
        Assert.assertTrue(notNullOrEmpty);
    }


    @Data
    public static class TestBean {
        private String name;
        private Integer age;
    }
}
