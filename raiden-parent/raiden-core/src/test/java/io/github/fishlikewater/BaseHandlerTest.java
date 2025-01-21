/*
 * Copyright (c) 2025 zhangxiang (fishlikewater@126.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.fishlikewater;

import io.github.fishlikewater.raiden.core.handler.BaseHandler;
import io.github.fishlikewater.raiden.core.handler.ChainContext;
import io.github.fishlikewater.raiden.core.handler.Pipeline;
import org.junit.Test;

/**
 * BaseHandlerTest
 *
 * @author zhangxiang
 * @version 1.1.2
 * @since 2025/1/17
 **/
public class BaseHandlerTest {

    @Test
    public void test() {
        Pipeline<String> pipeline = new Pipeline<>();
        pipeline
                .addHandler(new TestHandler1())
                .addHandler(new TestHandler2())
                .addFirstHandler(new TestHandler3())
                .addLastHandler(new TestHandler3());

        pipeline.start("hello");
    }

    public static class TestHandler1 extends BaseHandler<String> {
        @Override
        public void doHandle(String string, ChainContext context) {
            System.out.println(string);
            context.addProperty("key", "hello world");
        }
    }

    public static class TestHandler2 extends BaseHandler<String> {
        @Override
        public void doHandle(String string, ChainContext context) {
            System.out.println(string);
            System.out.println(context.getProperty("key"));
            this.stop();
        }
    }

    public static class TestHandler3 extends BaseHandler<String> {
        @Override
        public void doHandle(String string, ChainContext context) {
            System.out.println("3333");
        }
    }
}
