/*
 * Copyright (c) 2024 zhangxiang (fishlikewater@126.com)
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
package io.github.fishlikewater.raiden.generate;

import io.github.fishlikewater.raiden.core.RandomUtils;
import io.github.fishlikewater.raiden.core.StringUtils;

/**
 * <p>
 * {@code PhoneNumberGenerate}
 * 手机号码生成器
 * </p>
 *
 * @author fishlikewater@126.com
 * @version 1.0.0
 * @since 2024年06月01日 9:28
 **/
public class PhoneNumberGenerate {


    public static class Telephone extends AbstractGenerate<String> {

        private boolean areaCode;

        public Telephone() {

        }

        public Telephone(boolean areaCode) {
            this.areaCode = areaCode;
        }

        private static final String[] prefix = {
                "010", "020", "021", "022", "023", "024", "025", "027", "028", "029",
                "030", "031", "032", "033", "034", "035", "036", "037", "038", "039",
                "040", "041", "042", "043", "044", "045", "046", "047", "048", "049",
                "050", "051", "052", "053", "054", "055", "056", "057", "058", "059",
        };

        @Override
        public String generate() {
            if (areaCode) {
                return StringUtils.format("{}{}", prefix[RandomUtils.randomInt(0, prefix.length)], RandomUtils.randNum(8));
            }
            return RandomUtils.randNum(8);
        }
    }

    public static class Mobilephone extends AbstractGenerate<String> {

        private static final String[] prefix = {
                "130", "131", "132", "133", "134", "135", "136", "137", "138", "139",
                "147", "150", "151", "152", "153", "155", "156", "157", "158", "159",
                "170", "171", "172", "173", "174", "175", "176", "177", "178"
        };

        @Override
        public String generate() {
            return StringUtils.format("{}{}", prefix[RandomUtils.randomInt(0, prefix.length)], RandomUtils.randNum(8));
        }
    }
}
