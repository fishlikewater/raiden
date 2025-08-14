/*
 * Copyright (c) 2023-2025 zhangxiang (fishlikewater@126.com)
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

import io.github.fishlikewater.raiden.core.SystemPropertyUtils;
import io.github.fishlikewater.raiden.core.constant.CommonConstants;
import org.junit.Test;

/**
 * {@code SystemPropertyUtilTest}
 * 测试系统属性获取
 *
 * @author zhangxiang
 * @version 1.1.0
 * @since 2024/12/17
 */
public class SystemPropertyUtilsTest {

    @Test
    public void testNumOfCores() {
        System.out.println(SystemPropertyUtils.getCores());
    }

    @Test
    public void testUserHome() {
        System.out.println(SystemPropertyUtils.getUserHome());
    }

    @Test
    public void testUserDir() {
        System.out.println(SystemPropertyUtils.getUserDir());
    }

    @Test
    public void testJavaVersion() {
        System.out.println(SystemPropertyUtils.getJavaVersion());
    }

    @Test
    public void testJavaVendor() {
        System.out.println(SystemPropertyUtils.getJavaVendor());
    }

    @Test
    public void testOsName() {
        System.out.println(SystemPropertyUtils.getOsName());
    }

    @Test
    public void testMacAddress() {
        System.out.println(SystemPropertyUtils.getMacAddress(CommonConstants.Symbol.SYMBOL_COLON));
    }

    @Test
    public void testIpAddress() {
        System.out.println(SystemPropertyUtils.getIpAddress());
    }

    @Test
    public void testNetworkInterface() {
        System.out.println(SystemPropertyUtils.getNetworkInterface());
    }
}
