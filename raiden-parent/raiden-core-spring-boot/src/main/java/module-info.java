/**
 * {@code module-info}
 *
 * @author zhangxiang
 * @since 2024/06/17
 */
module raiden.core.spring.boot {
    requires spring.beans;
    requires static lombok;
    requires spring.context;
    requires spring.core;
    requires raiden.core;
    requires spring.boot;
    requires cn.hutool.core;

    exports io.github.fishlikewater.spring.boot.raiden.core;
    exports io.github.fishlikewater.spring.boot.raiden.core.engine;
    exports io.github.fishlikewater.spring.boot.raiden.core.getter;
    exports io.github.fishlikewater.spring.boot.raiden.core.i18n;
    exports io.github.fishlikewater.spring.boot.raiden.core.property;
}