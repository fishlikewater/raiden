package io.github.fishlikewater.nacos.registry;

import com.alibaba.nacos.client.config.listener.impl.AbstractConfigChangeListener;
import io.github.fishlikewater.nacos.model.ConfigMeta;

import java.util.Set;

/**
 * {@code NacosConfigRegister}
 * 注册
 *
 * @author zhangxiang
 * @version 1.0.7
 * @since 2024/11/01
 */
public interface NacosConfigRegister {

    void register(ConfigMeta configMeta, AbstractConfigChangeListener listener);

    Set<ConfigMeta> tryAcquireConfigMetas();
}
