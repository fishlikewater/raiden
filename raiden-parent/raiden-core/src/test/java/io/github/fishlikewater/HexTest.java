package io.github.fishlikewater;

import io.github.fishlikewater.raiden.core.Hex;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

/**
 * {@code HexTest}
 * Hex测试
 *
 * @author zhangxiang
 * @version 1.0.0
 * @since 2024/12/25
 */
public class HexTest {

    @Test
    public void test() {
        String testStr = "hello world";
        String encodeHexStr = Hex.encodeHexStr(testStr.getBytes());
        System.out.println(encodeHexStr);
        System.out.println(Hex.decodeHexStr(encodeHexStr, StandardCharsets.UTF_8));
    }
}
