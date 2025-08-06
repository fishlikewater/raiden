package io.github.fishlikewater;

import io.github.fishlikewater.raiden.core.future.RobustFuture;
import org.junit.Test;

/**
 * {@code RobustFutureTest}
 *
 * @author zhangxiang
 * @since 2025/8/6
 */
public class RobustFutureTest {

    @Test
    public void test() throws InterruptedException {
        RobustFuture<String> future = new RobustFuture<>();
        future.addListener(f -> {
            if (f.isSuccess()) {
                System.out.println(f.getNow());
            }
        });
        future.setSuccess("hello world");
        //Thread.sleep(1000);
    }
}
