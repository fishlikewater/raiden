/*
 * Copyright (c) 2023-2025 zhangxiang (fishlikewater@126.com)
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
package io.github.fishlikewater.raiden.core;

import lombok.Data;

import java.util.function.Supplier;

/**
 * {@code ConditionUtils}
 * 条件处理工具
 *
 * @author zhangxiang
 * @version 1.1.3
 * @since 2025/01/20
 */
public class ConditionUtils {

    public static Condition of(Supplier<Boolean> supplier) {
        return new Condition(supplier.get(), Status.INIT);
    }

    @Data
    public static class Condition {

        private boolean result;

        private Status status;

        private Condition(boolean result, Status status) {
            this.result = result;
            this.status = status;
        }

        public void isTrue(Runnable runnable) {
            if (this.result && (this.status == Status.INIT || this.status == Status.IS_EXECUTE)) {
                runnable.run();
            }
        }

        public Condition isTrue(Supplier<Boolean> supplier) {
            if (this.result && (this.status == Status.INIT || this.status == Status.IS_EXECUTE)) {
                this.status = Status.IS_EXECUTE;
                this.result = supplier.get();
            } else if (this.status != Status.INIT) {
                this.status = Status.NO_EXECUTE;
            }

            return this;
        }

        public void isFalse(Runnable runnable) {
            if (!this.result && (this.status == Status.INIT || this.status == Status.IS_EXECUTE)) {
                runnable.run();
            }
        }

        public Condition isFalse(Supplier<Boolean> supplier) {
            if (!this.result && (this.status == Status.INIT || this.status == Status.IS_EXECUTE)) {
                this.status = Status.IS_EXECUTE;
                this.result = supplier.get();
            } else if (this.status != Status.INIT) {
                this.status = Status.NO_EXECUTE;
            }

            return this;
        }

        public Condition orElse(Supplier<Boolean> supplier) {
            if (this.status == Status.NO_EXECUTE || this.status == Status.INIT) {
                this.result = supplier.get();
                this.status = Status.IS_EXECUTE;
            }

            return this;
        }

        public Condition or(Supplier<Boolean> supplier) {
            this.result = this.result || supplier.get();

            return this;
        }

        public Condition and(Supplier<Boolean> supplier) {
            this.result = this.result && supplier.get();

            return this;
        }
    }

    protected enum Status {

        /**
         * 初始状态
         */
        INIT,
        /**
         * 上一步执行状态
         */
        IS_EXECUTE,
        /**
         * 上一步未执行状态
         */
        NO_EXECUTE,
    }
}
