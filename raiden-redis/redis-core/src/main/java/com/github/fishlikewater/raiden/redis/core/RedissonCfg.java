package com.github.fishlikewater.raiden.redis.core;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * {@code RedissonCfg}
 * Redisson配置类
 * </p>
 *
 * @author fishlikewater@126.com
 * @version 1.0.0
 * @since 2024年05月13日 20:35
 **/
@Data
public class RedissonCfg implements Serializable {

    private String url;

    private String passWord;

    private int dataBase = 1;

    private int connectionMinimumIdleSize = 1;

    private int connectionPoolSize = 2;
}
