package io.github.fishlikewater.nacos.registry;

import io.github.fishlikewater.nacos.model.ConfigMeta;

import java.util.Set;

/**
 * {@code DynamicNacosConfigListenerRegistry}
 * 注册配置
 *
 * @author zhangxiang
 * @version 1.0.7
 * @since 2024/10/29
 */
public interface DynamicNacosConfigListenerRegistry {

    /**
     * 注册需要动态刷新的 {@code dataId}
     *
     * @param meta {@link  ConfigMeta}
     */
    void registerConfigMeta(ConfigMeta meta);

    /**
     * 获取注册的所有 {@code ConfigMeta} 列表
     *
     * @return {@link  ConfigMeta} 列表
     */
    Set<ConfigMeta> tryAcquireConfigMetas();
}
