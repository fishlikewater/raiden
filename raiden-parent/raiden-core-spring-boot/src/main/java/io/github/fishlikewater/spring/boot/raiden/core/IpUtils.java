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
package io.github.fishlikewater.spring.boot.raiden.core;

import io.github.fishlikewater.raiden.core.constant.CommonConstants;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;

/**
 * {@code IpUtils}
 * IP工具类
 *
 * @author zhangxiang
 * @version 1.0.5
 * @since 2024/07/24
 */
@Slf4j
public class IpUtils {

    private static final String X_FORWARDED_FOR = "x-forwarded-for";
    private static final String PROXY_CLIENT_IP = "Proxy-Client-IP";
    private static final String WL_PROXY_CLIENT_IP = "WL-Proxy-Client-IP";
    private static final String HTTP_CLIENT_IP = "HTTP_CLIENT_IP";
    private static final String HTTP_X_FORWARDED_FOR = "HTTP_X_FORWARDED_FOR";
    private static final String LOCAL_IP = "localhost,127.0.0.1,0:0:0:0:0:0:0:1";
    private static final String UNKNOWN = "unknown";
    private static final int IP_LEN = 15;

    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader(X_FORWARDED_FOR);
        if (isNull(ip)) {
            ip = request.getHeader(PROXY_CLIENT_IP);
        }
        if (isNull(ip)) {
            ip = request.getHeader(WL_PROXY_CLIENT_IP);
        }
        if (isNull(ip)) {
            ip = request.getHeader(HTTP_CLIENT_IP);
        }
        if (isNull(ip)) {
            ip = request.getHeader(HTTP_X_FORWARDED_FOR);
        }
        if (isNull(ip)) {
            ip = request.getRemoteAddr();
        }
        // 本机访问
        if (LOCAL_IP.contains(ip)) {
            // 根据网卡取本机配置的IP
            InetAddress inet;
            try {
                inet = InetAddress.getLocalHost();
                ip = inet.getHostAddress();
            } catch (UnknownHostException e) {
                log.error("acquire.host.error", e);
            }
        }
        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (null != ip && ip.length() > IP_LEN) {
            if (ip.indexOf(CommonConstants.Symbol.SYMBOL_COMMA) > IP_LEN) {
                ip = ip.substring(0, ip.indexOf(","));
            }
        }
        return ip;
    }

    /**
     * 判断ip是否为空
     *
     * @param ip ip
     * @return boolean
     */
    public static boolean isNull(String ip) {
        return ip == null || ip.isEmpty() || UNKNOWN.equalsIgnoreCase(ip);
    }

    /**
     * 获取mac地址
     */
    public static String getMacAddress() throws Exception {
        // 取mac地址
        byte[] macAddressBytes = NetworkInterface.getByInetAddress(InetAddress.getLocalHost()).getHardwareAddress();
        // 下面代码是把mac地址拼装成String
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < macAddressBytes.length; i++) {
            if (i != 0) {
                sb.append(CommonConstants.Symbol.SYMBOL_DASH);
            }
            // mac[i] & 0xFF 是为了把byte转化为正整数
            String s = Integer.toHexString(macAddressBytes[i] & 0xFF);
            sb.append(s.length() == 1 ? 0 + s : s);
        }
        return sb.toString().trim().toUpperCase();
    }
}
