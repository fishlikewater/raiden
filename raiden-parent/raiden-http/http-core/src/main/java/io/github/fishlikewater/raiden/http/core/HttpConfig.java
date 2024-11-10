package io.github.fishlikewater.raiden.http.core;

import io.github.fishlikewater.raiden.http.core.client.AbstractHttpRequestClient;
import io.github.fishlikewater.raiden.http.core.factory.HttpClientBeanFactory;
import io.github.fishlikewater.raiden.http.core.interceptor.LogInterceptor;
import io.github.fishlikewater.raiden.http.core.interceptor.PredRequestInterceptor;
import io.github.fishlikewater.raiden.http.core.processor.HttpClientProcessor;
import io.github.fishlikewater.raiden.http.core.proxy.InterfaceProxy;
import io.github.fishlikewater.raiden.http.core.source.SourceHttpClientRegistry;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * HttpConfig
 *
 * @author zhangxiang
 * @version 1.0.8
 * @since 2024/11/10
 **/
@Data
@Accessors(chain = true)
public class HttpConfig {

    /**
     * 是否自行管理代理容器
     */
    private boolean selfManager;

    /**
     * 最大重试次数
     */
    private int maxRetryCount;

    /**
     * 是否开启日志
     */
    private boolean enableLog;

    /**
     * 接口代理
     */
    private InterfaceProxy interfaceProxy;

    /**
     * httpClient 请求客户端
     */
    private AbstractHttpRequestClient httpClient;

    /**
     * httpClient Bean处理器工厂
     */
    private HttpClientBeanFactory httpClientBeanFactory;

    /**
     * httpClient 处理器
     */
    private HttpClientProcessor httpClientProcessor;

    /**
     * httpClient 注册中心
     */
    private SourceHttpClientRegistry sourceHttpClientRegistry;

    /**
     * 日志级别
     */
    private LogConfig.LogLevel logLevel = LogConfig.LogLevel.BASIC;

    /**
     * 全局预请求拦截器
     */
    private PredRequestInterceptor predRequestInterceptor;

    /**
     * 全局日志拦截器
     */
    private LogInterceptor logInterceptor = new LogInterceptor();
}