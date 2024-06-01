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
package com.github.fishlikewater.raiden.generate;

import com.github.fishlikewater.raiden.core.RandomUtils;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;

/**
 * <p>
 * {@code EmailGenerate}
 * 邮件地址生成器
 * </p>
 *
 * @author fishlikewater@126.com
 * @version 1.0.0
 * @since 2024年06月01日 9:56
 **/
@Slf4j
public class EmailGenerate extends AbstractGenerate<String> {

    private final String[] suffix = {"@qq.com", "@163.com", "@gmail.com", "@outlook.com", "@sina.com", "@yahoo.com", "@hotmail.com", "@yandex.com", "@aol.com", "@126.com"};

    @Override
    public String generate() {
        final String userName = GenerateUtils.USER_NAME.generate();
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        final char[] charArray = userName.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (char c : charArray) {
            try {
                String[] array = PinyinHelper.toHanyuPinyinStringArray(c, format);
                sb.append(array[0]);
            } catch (Exception e) {
                log.error("generate pinyin failed, target: {}", userName, e);
            }
        }
        sb.append(suffix[RandomUtils.randomInt(0, suffix.length)]);
        return sb.toString();
    }
}
