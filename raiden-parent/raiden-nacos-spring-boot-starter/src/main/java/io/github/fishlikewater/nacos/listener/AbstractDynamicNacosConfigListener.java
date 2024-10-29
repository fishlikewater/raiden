/*
 * Copyright (c) 2023 Uphicoo Co., Ltd. All rights reserved.
 *
 * This software is owned by Uphicoo Co., Ltd.
 * Without the official authorization of Uphicoo Co., Ltd.,
 * no enterprise or individual can obtain, read, install,
 * or disseminate any content protected by intellectual
 * property rights involved in this software.
 *
 * The website of uphicoo, please see <https://uphicoo.com>
 */
package io.github.fishlikewater.nacos.listener;

import com.alibaba.nacos.api.config.ConfigChangeEvent;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.client.config.listener.impl.AbstractConfigChangeListener;
import com.alibaba.nacos.spring.context.event.config.NacosConfigReceivedEvent;
import com.alibaba.nacos.spring.factory.NacosServiceFactory;
import com.alibaba.nacos.spring.util.NacosBeanUtils;
import io.github.fishlikewater.nacos.model.ConfigMeta;
import io.github.fishlikewater.nacos.registry.DynamicNacosConfigListenerRegistry;
import io.github.fishlikewater.nacos.scope.NacosRefreshScope;
import io.github.fishlikewater.raiden.core.StringUtils;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * {@code AbstractDynamicNacosConfigListener}
 *
 * @author zhangxiang
 * @version 1.0.7
 * @since 2024/10/29
 */
public abstract class AbstractDynamicNacosConfigListener extends AbstractConfigChangeListener implements
        ApplicationListener<NacosConfigReceivedEvent>, DynamicNacosConfigListenerRegistry, InitializingBean, BeanFactoryAware {

    protected static final Logger log = LoggerFactory.getLogger(AbstractDynamicNacosConfigListener.class);

    protected static final String DEFAULT_GROUP = "DEFAULT_GROUP";
    protected static final String DEFAULT_CONFIG_TYPE = "yaml";
    protected static final String APP_KEY = "spring.application.name";

    protected final Set<ConfigMeta> configMetas = new HashSet<>();
    protected final Set<String> configDataIds = new HashSet<>();

    private BeanFactory beanFactory;

    @Override
    public void setBeanFactory(@NonNull BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        NacosServiceFactory factory = NacosBeanUtils.getNacosServiceFactoryBean();
        Collection<ConfigService> configServices = factory.getConfigServices();

        this.registerListener(configServices);
    }

    @Override
    public void onApplicationEvent(NacosConfigReceivedEvent event) {
        log.info("Dynamic.nacos: on.spring.nacos.refresh.listener.received.config.changed.event:[{}:{}:{}]",
                event.getGroupId(), event.getDataId(), event.getType());

        if (!this.configMetas.isEmpty()) {
            if (!this.configDataIds.contains(event.getDataId())) {
                return;
            }
        }

        // 执行两次: 确保在 {@code #receiveConfigChange 被调用前报错的时候还能正常的刷新
        this.onEvent(event);
    }

    @Override
    public void receiveConfigChange(ConfigChangeEvent event) {
        log.info("Dynamic.nacos: on.nacos.refresh.listener.received.config.changed.event");

        this.onEvent(event);
    }

    // ----------------------------------------------------------------

    private void onEvent(NacosConfigReceivedEvent event) {
        NacosRefreshScope refresher = this.beanFactory.getBean(NacosRefreshScope.class);

        this.preRefresh(event);
        refresher.refreshAll();
        this.posRefresh(event);
    }

    private void onEvent(ConfigChangeEvent event) {
        NacosRefreshScope refresher = this.beanFactory.getBean(NacosRefreshScope.class);

        this.preRefresh(event);
        refresher.refreshAll();
        this.posRefresh(event);
    }

    // ----------------------------------------------------------------

    @Override
    public void registerConfigMeta(ConfigMeta meta) {
        this.configMetas.add(meta);
        this.configDataIds.add(meta.getDataId());
    }

    @Override
    public Set<ConfigMeta> tryAcquireConfigMetas() {
        return Collections.unmodifiableSet(this.configMetas);
    }

    // ----------------------------------------------------------------

    public void preRefresh(ConfigChangeEvent event) {
        // do nothing
    }

    public void posRefresh(ConfigChangeEvent event) {
        // do nothing
    }

    // ----------------------------------------------------------------

    public void preRefresh(NacosConfigReceivedEvent event) {
        // do nothing
    }

    public void posRefresh(NacosConfigReceivedEvent event) {
        // do nothing
    }

    // ----------------------------------------------------------------

    /**
     * 注册监听器
     *
     * @param configServices {@link ConfigService}
     */
    public abstract void registerListener(Collection<ConfigService> configServices);

    public void addTemplateListener(ConfigService configService, String template) {
        this.addTemplateListener(configService, DEFAULT_GROUP, template);
    }

    public void addTemplateListener(ConfigService configService, String groupId, String template) {
        Environment environment = this.beanFactory.getBean(Environment.class);
        String app = environment.getProperty(APP_KEY);
        String dataId = StringUtils.format(template, app);

        this.addListener(configService, groupId, dataId);
        this.doRegisterMeta(groupId, dataId);
    }

    private void doRegisterMeta(String groupId, String dataId) {
        ConfigMeta meta = ConfigMeta.builder()
                .groupId(groupId)
                .dataId(dataId)
                .type(DEFAULT_CONFIG_TYPE)
                .build();

        this.registerConfigMeta(meta);
    }

    public void addListener(ConfigService configService, String dataId) {
        this.addListener(configService, DEFAULT_GROUP, dataId);
    }

    public void addListener(ConfigService configService, String group, String dataId) {
        try {
            configService.addListener(dataId, group, this);
            log.info("Add dynamic nacos.refresh.listener for config dataId:[{}]", dataId);
        } catch (Exception e) {
            log.error("Add dynamic nacos.refresh.listener for config dataId:[{}] failed", dataId, e);
            throw new RuntimeException(StringUtils.format("添加 dataId:[{}] 失败", dataId), e);
        }
    }
}
