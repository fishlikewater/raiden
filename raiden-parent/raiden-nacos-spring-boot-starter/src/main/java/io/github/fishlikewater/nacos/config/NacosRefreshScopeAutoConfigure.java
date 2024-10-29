package io.github.fishlikewater.nacos.config;

import io.github.fishlikewater.nacos.scope.NacosRefreshScope;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * {@code NacosRefreshScopeAutoConfigure}
 *
 * @author zhangxiang
 * @version 1.0.7
 * @since 2024/10/29
 */
@AutoConfiguration
public class NacosRefreshScopeAutoConfigure {

    @Bean
    public NacosRefreshScope nacosRefreshScope() {
        return new NacosRefreshScope();
    }
}
