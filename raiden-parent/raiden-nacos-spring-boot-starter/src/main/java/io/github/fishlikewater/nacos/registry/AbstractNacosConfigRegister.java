package io.github.fishlikewater.nacos.registry;

import com.alibaba.boot.nacos.config.autoconfigure.NacosBootConfigException;
import com.alibaba.boot.nacos.config.properties.NacosConfigProperties;
import com.alibaba.boot.nacos.config.util.NacosConfigPropertiesUtils;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.ConfigType;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.client.config.listener.impl.AbstractConfigChangeListener;
import com.alibaba.nacos.spring.factory.CacheableEventPublishingNacosServiceFactory;
import com.alibaba.nacos.spring.factory.NacosServiceFactory;
import com.alibaba.nacos.spring.util.NacosBeanUtils;
import io.github.fishlikewater.nacos.listener.DefaultDynamicNacosConfigListener;
import io.github.fishlikewater.nacos.model.ConfigMeta;
import io.github.fishlikewater.raiden.core.ObjectUtils;
import io.github.fishlikewater.raiden.core.StringUtils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;

import java.util.*;
import java.util.function.Function;

/**
 * {@code AbstractNacosConfigRegister}
 *
 * @author zhangxiang
 * @version 1.0.7
 * @since 2024/11/01
 */
@Slf4j
public abstract class AbstractNacosConfigRegister implements NacosConfigRegister, InitializingBean, EnvironmentAware {

    protected final Set<ConfigMeta> configMetas = new HashSet<>();

    protected final Set<String> configDataIds = new HashSet<>();

    private ConfigurableEnvironment environment;

    private NacosConfigProperties nacosConfigProperties;

    private final CacheableEventPublishingNacosServiceFactory singleton = CacheableEventPublishingNacosServiceFactory.getSingleton();

    public abstract List<ConfigMeta> getConfigMeta();

    public void register() {
        List<ConfigMeta> configMeta = this.getConfigMeta();
        if (ObjectUtils.isNullOrEmpty(configMeta)) {
            return;
        }
        for (ConfigMeta meta : configMeta) {
            DefaultDynamicNacosConfigListener listener = new DefaultDynamicNacosConfigListener(meta);
            this.register(meta, listener);
            this.registerConfigMeta(meta);
        }
    }

    @Override
    public void register(ConfigMeta configMeta, AbstractConfigChangeListener listener) {
        NacosServiceFactory factory = NacosBeanUtils.getNacosServiceFactoryBean();
        Collection<ConfigService> configServices = factory.getConfigServices();
        for (ConfigService configService : configServices) {
            try {
                configService.addListener(configMeta.getDataId(), configMeta.getGroupId(), listener);
                log.info("Add dynamic nacos.refresh.listener for config dataId:[{}]", configMeta.getDataId());
            } catch (Exception e) {
                log.error("Add dynamic nacos.refresh.listener for config dataId:[{}] failed", configMeta.getDataId(), e);
                throw new RuntimeException(StringUtils.format("添加 dataId:[{}] 失败", configMeta.getDataId()), e);
            }
        }
    }

    @Override
    public Set<ConfigMeta> tryAcquireConfigMetas() {
        return Collections.unmodifiableSet(this.configMetas);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.register();
    }

    @Override
    public void setEnvironment(@NonNull Environment environment) {
        this.environment = (ConfigurableEnvironment) environment;
    }


    // ----------------------------------------------------------------

    public void registerConfigMeta(ConfigMeta meta) {
        this.configMetas.add(meta);
        this.configDataIds.add(meta.getDataId());
    }

    private final Function<Properties, ConfigService> builder = properties -> {
        try {
            return singleton.createConfigService(properties);
        } catch (NacosException e) {
            throw new NacosBootConfigException(
                    "ConfigService can't be created with properties : " + properties, e);
        }
    };

    private void refreshEnvironment(ConfigMeta meta) {
        if (ObjectUtils.isNullOrEmpty(nacosConfigProperties)) {
            NacosConfigProperties nacosConfigProperties = NacosConfigPropertiesUtils.buildNacosConfigProperties(environment);
        }
        nacosConfigProperties.setDataIds(meta.getDataId());
        nacosConfigProperties.setGroup(meta.getGroupId());
        nacosConfigProperties.setType(ConfigType.valueOf(meta.getType().toUpperCase()));
    }

}