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
package io.github.fishlikewater.raiden.redis.core;

import lombok.Data;
import org.redisson.api.NameMapper;
import org.redisson.config.CommandMapper;
import org.redisson.config.SslProvider;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;
import java.net.URL;

/**
 * <p>
 * {@code SingleConfig}
 * </p>
 * 单机配置
 *
 * @author fishlikewater@126.com
 * @version 1.0.2
 * @since 2024年06月07日 20:00
 **/
@Data
public class SingleConfig {

    /**
     * Redis server address
     *
     */
    private String address;

    /**
     * Minimum idle subscription connection amount
     */
    private int subscriptionConnectionMinimumIdleSize = 1;

    /**
     * Redis subscription connection maximum pool size
     *
     */
    private int subscriptionConnectionPoolSize = 50;

    /**
     * Minimum idle Redis connection amount
     */
    private int connectionMinimumIdleSize = 24;

    /**
     * Redis connection maximum pool size
     */
    private int connectionPoolSize = 64;

    /**
     * Database index used for Redis connection
     */
    private int database = 0;

    /**
     * Interval in milliseconds to check DNS
     */
    private long dnsMonitoringInterval = 5000;

    private int idleConnectionTimeout = 10000;

    /**
     * Timeout during connecting to any Redis server.
     * Value in milliseconds.
     *
     */
    private int connectTimeout = 10000;

    /**
     * Redis server response timeout. Starts to countdown when Redis command was successfully sent.
     * Value in milliseconds.
     *
     */
    private int timeout = 3000;

    private int subscriptionTimeout = 7500;

    private int retryAttempts = 3;

    private int retryInterval = 1500;

    /**
     * Password for Redis authentication. Should be null if not needed
     */
    private String password;

    private String username;

    private String clientName;

    private boolean sslEnableEndpointIdentification = true;

    private String sslKeystoreType;

    private SslProvider sslProvider = SslProvider.JDK;

    private URL sslTruststore;

    private String sslTruststorePassword;

    private URL sslKeystore;

    private String sslKeystorePassword;

    private String[] sslProtocols;

    private String[] sslCiphers;

    private TrustManagerFactory sslTrustManagerFactory;

    private KeyManagerFactory sslKeyManagerFactory;

    private int pingConnectionInterval = 30000;

    private boolean keepAlive;

    private int tcpKeepAliveCount;

    private int tcpKeepAliveIdle;

    private int tcpKeepAliveInterval;

    private int tcpUserTimeout;

    private boolean tcpNoDelay = true;

    private NameMapper nameMapper = NameMapper.direct();

    private CommandMapper commandMapper = CommandMapper.direct();
}
