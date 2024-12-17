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
package io.github.fishlikewater.raiden.core;

/**
 * {@code SystemPropertyUtil}
 * 系统属性获取工具类
 *
 * @author zhangxiang
 * @version 1.1.0
 * @since 2024/12/17
 */
public class SystemPropertyUtil {

    /**
     * 获取可用处理器数量
     *
     * @return 数量
     */
    public static int getCores() {
        return Runtime.getRuntime().availableProcessors();
    }

    /**
     * 获取用户主目录
     *
     * @return 路径
     */
    public static String getUserHome() {
        return System.getProperty("user.home");
    }

    /**
     * 获取用户当前目录
     *
     * @return 路径
     */
    public static String getUserDir() {
        return System.getProperty("user.dir");
    }

    /**
     * 获取Java版本
     *
     * @return Java版本
     */
    public static String getJavaVersion() {
        return System.getProperty("java.version");
    }

    /**
     * 获取Java供应商
     *
     * @return Java供应商
     */
    public static String getJavaVendor() {
        return System.getProperty("java.vendor");
    }

    /**
     * 获取操作系统名称
     *
     * @return 操作系统名称
     */
    public static String getOsName() {
        return System.getProperty("os.name");
    }

    /**
     * 获取系统属性
     *
     * @param key 属性名
     * @return 属性值
     */
    public static String getProperty(String key) {
        return System.getProperty(key);
    }
}
