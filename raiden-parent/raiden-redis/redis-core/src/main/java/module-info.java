/**
 * {@code module-info}
 *
 * @author zhangxiang
 * @since 2024/06/17
 */
module raiden.redis.core {
    requires static lombok;
    requires redisson;
    requires raiden.core;
    requires com.fasterxml.jackson.core;
    requires raiden.json.core;
    requires cn.hutool.core;

    exports io.github.fishlikewater.raiden.redis.core;
    exports io.github.fishlikewater.raiden.redis.core.annotation;
    exports io.github.fishlikewater.raiden.redis.core.delay;
}