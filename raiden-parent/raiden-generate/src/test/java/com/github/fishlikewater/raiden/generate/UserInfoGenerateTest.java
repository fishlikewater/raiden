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
package com.github.fishlikewater.raiden.generate;

import org.junit.Test;

/**
 * {@code UserInfoGenerateTest}
 *
 * @author zhangxiang
 * @version 1.0.0
 * @since 2024/05/31
 */
public class UserInfoGenerateTest {

    @Test
    public void testGenerateUserInfo() {
        for (int i = 0; i < 10; i++) {
            System.out.println(GenerateUtils.USER_NAME.generate());
        }
    }

    @Test
    public void testGenerateAge() {
        for (int i = 0; i < 10; i++) {
            System.out.println(GenerateUtils.AGE.generate());
        }
    }

    @Test
    public void testGenerateNation() {
        for (int i = 0; i < 10; i++) {
            System.out.println(GenerateUtils.NATION.generate());
        }
    }

    @Test
    public void testGeneratePhoneNumber() {
        System.out.println(GenerateUtils.TELEPHONE.generate());
        System.out.println(GenerateUtils.TELEPHONE_AREA.generate());
        System.out.println(GenerateUtils.MOBILEPHONE.generate());
    }

    @Test
    public void testGenerateEmail() {
        for (int i = 0; i < 10; i++) {
            System.out.println(GenerateUtils.EMAIL.generate());
        }
    }
}
