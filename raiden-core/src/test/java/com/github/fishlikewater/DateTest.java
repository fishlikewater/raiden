package com.github.fishlikewater;

import com.github.fishlikewater.raiden.core.DateUtils;
import org.junit.Test;

import java.time.LocalDateTime;

/**
 * {@code DateTest}
 *
 * @author zhangxiang
 * @version 1.0.0
 * @since 2024/05/07
 */
public class DateTest {

    @Test
    public void testDate() {
        System.out.println(DateUtils.transfer(LocalDateTime.now()));
    }
}
