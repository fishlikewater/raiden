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
package io.github.fishlikewater.raiden.core;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Pattern;

/**
 * {@code NetUtils}
 * 网络工具类
 *
 * @author zhangxiang
 * @since 2025/10/27
 */
public class NetUtils {

    public static final Pattern IPV4 = Pattern.compile("^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)$");

    /**
     * 判断是否为IPv4地址
     *
     * @param ip IP地址
     * @return 是否为IPv4地址
     */
    public static boolean isIPv4(String ip) {
        return IPV4.matcher(ip).matches();
    }

    /**
     * 判断是否为IPv6地址
     *
     * @param ip IP地址
     * @return 是否为IPv6地址
     */
    public static boolean isIPv6(String ip) {
        try {
            return InetAddress.getByName(ip) instanceof Inet6Address;
        } catch (UnknownHostException e) {
            return false;
        }
    }

    /**
     * 判断IPv6地址是否为内网地址
     * <br><br>
     * 以下地址将归类为本地地址，如有业务场景有需要，请根据需求自行处理：
     * <pre>
     * 通配符地址 0:0:0:0:0:0:0:0
     * 链路本地地址 fe80::/10
     * 唯一本地地址 fec0::/10
     * 环回地址 ::1
     * </pre>
     *
     * @param ip IP地址
     * @return 是否为内网地址
     */
    public static boolean isInnerIPv6(String ip) {
        try {
            // 判断是否为IPv6地址
            if (InetAddress.getByName(ip) instanceof Inet6Address inet6Address) {
                // isAnyLocalAddress 判断是否为通配符地址，通常不会将其视为内网地址，根据业务场景自行处理判断
                // isLinkLocalAddress 判断是否为链路本地地址，通常不算内网地址，是否划分归属于内网需要根据业务场景自行处理判断
                // isLoopbackAddress 判断是否为环回地址，与IPv4的 127.0.0.1 同理，用于表示本机
                // isSiteLocalAddress 判断是否为本地站点地址，IPv6唯一本地地址（Unique Local Addresses，简称ULA）
                if (inet6Address.isAnyLocalAddress()
                        || inet6Address.isLinkLocalAddress()
                        || inet6Address.isLoopbackAddress()
                        || inet6Address.isSiteLocalAddress()) {
                    return true;
                }
            }
        } catch (UnknownHostException e) {
            // 注意，isInnerIPv6方法和isIPv6方法的适用范围不同，所以此处不能忽略其异常信息。
            throw new IllegalArgumentException("Invalid IPv6 address!", e);
        }
        return false;
    }

    /**
     * 判断IPv4地址是否为内网地址
     *
     * @param ip IP地址
     * @return 是否为内网地址
     */
    public static boolean isInnerIP(String ip) {
        if (!isIPv4(ip)) {
            return false;
        }

        String[] parts = ip.split("\\.");
        int firstPart = Integer.parseInt(parts[0]);
        int secondPart = Integer.parseInt(parts[1]);

        // 10.0.0.0/8
        if (firstPart == 10) {
            return true;
        }

        // 172.16.0.0/12
        if (firstPart == 172 && secondPart >= 16 && secondPart <= 31) {
            return true;
        }

        // 192.168.0.0/16
        if (firstPart == 192 && secondPart == 168) {
            return true;
        }

        // 127.0.0.0/8 (回环地址)
        if (firstPart == 127) {
            return true;
        }

        // 169.254.0.0/16 (链路本地地址)
        return firstPart == 169 && secondPart == 254;
    }
}
