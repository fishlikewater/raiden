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
package io.github.fishlikewater.raiden.generate;

import cn.hutool.json.JSONArray;
import io.github.fishlikewater.raiden.core.ObjectUtils;
import io.github.fishlikewater.raiden.core.RandomUtils;
import io.github.fishlikewater.raiden.core.StringUtils;

/**
 * {@code IdCardGenerate}
 * 身份证生成器
 * <pre>
 * 前六位表示地址码，对应于身份证持有者的户籍所在地；
 * 接下来的八位是出生日期码，格式为yyyyMMdd；
 * 第15到17位是顺序码，奇数分配给男性，偶数分配给女性；
 * 最后一位是校验码，可能是0-9中的任何一个数字，或者是X（代表10）。
 * </pre>
 *
 * @author zhangxiang
 * @version 1.0.3
 * @since 2024/07/17
 */
public class IdCardGenerate extends AbstractGenerate<String> {

    private static final DateTimeGenerate.FullDateTimeGenerate GENERATE = new DateTimeGenerate.FullDateTimeGenerate();

    private JSONArray provinces;

    @Override
    public String generate() {
        this.init();
        String areaCode = GenerateUtils.ADDRESS.generate().areaCode();
        String date = GENERATE.generate("yyyyMMdd");
        String sexCode = RandomUtils.randomLong(0, 999, true);
        String lastCode = RandomUtils.randNum(1);
        return StringUtils.format("{}{}{}{}", areaCode, date, sexCode, lastCode);
    }

    private void init() {
        if (ObjectUtils.isNotNullOrEmpty(this.provinces)) {
            return;
        }
        this.provinces = this.readFileAsJsonArray("area-full.json");
    }
}
