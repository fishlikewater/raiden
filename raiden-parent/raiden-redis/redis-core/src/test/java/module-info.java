/**
 * {@code module-info}
 *
 * @author zhangxiang
 * @since 2024/07/03
 */
module raiden.redis.core.test {
    requires redisson;
    requires raiden.redis.core;
    requires junit;
    requires raiden.core;
    requires static lombok;

    exports io.github.fishlikewater.raiden.redis.test;
}