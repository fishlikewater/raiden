/**
 * {@code module-info}
 *
 * @author zhangxiang
 * @since 2024/06/17
 */
module raiden.crypto {
    requires static lombok;
    requires raiden.core;
    requires org.bouncycastle.provider;
    exports io.github.fishlikewater.raiden.crypto;
}