package io.github.fishlikewater.raiden.redis.processor;

import io.github.fishlikewater.raiden.redis.core.annotation.RedisCache;
import lombok.Data;

/**
 * {@code TestRedisCache}
 *
 * @author zhangxiang
 * @version 1.0.2
 * @since 2024/06/20
 */
@Data
public class TestRedisCache {

    private String name;

    private String age;

    @RedisCache(key = "#{id}", expire = 1000)
    public void sayHi(String id) {
        System.out.println("hi");
    }
}
