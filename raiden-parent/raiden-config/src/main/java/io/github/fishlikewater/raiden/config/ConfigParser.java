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
package io.github.fishlikewater.raiden.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import io.github.fishlikewater.raiden.config.ini.Ini;
import io.github.fishlikewater.raiden.config.ini.Section;
import io.github.fishlikewater.raiden.core.StringUtils;
import io.github.fishlikewater.raiden.json.core.JSONUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * {@code ConfigParser}
 * 配置文件读取
 *
 * @author zhangxiang
 * @version 1.0.3
 * @since 2024/07/16
 */
public class ConfigParser {

    private static final Pattern SECTION = Pattern.compile("\\[(.*?)]");

    private static final Pattern PROPERTY = Pattern.compile("(\\w+)\\s*=\\s*(.*?)");

    /**
     * 读取配置文件
     *
     * @param file 配置文件
     * @return 配置文件
     * @throws IOException 读取配置文件异常
     */
    public Ini readIni(File file) throws IOException {
        Ini ini = new Ini();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            String strLine;
            String currentSection = "";
            String currentKey = "";
            String currentValue = "";
            while ((strLine = bufferedReader.readLine()) != null) {
                boolean isInSection = false;
                strLine = strLine.trim();

                // 是否为section
                Matcher matcher = SECTION.matcher((strLine));
                if (matcher.matches()) {
                    isInSection = true;
                }

                if (isInSection) {
                    this.handPreElement(currentSection, currentKey, currentValue, ini);
                    currentKey = "";
                    currentValue = "";
                    currentSection = strLine.substring(1, strLine.length() - 1);
                    Section section = new Section(currentSection, new HashMap<>(8));
                    ini.add(section);
                    continue;
                }

                // 是否为key
                Matcher matcherKey = PROPERTY.matcher(strLine);
                if (matcherKey.matches()) {
                    this.handPreElement(currentSection, currentKey, currentValue, ini);
                    currentKey = matcherKey.group(1);
                    currentValue = matcherKey.group(2);
                    if (StringUtils.isNotBlank(currentSection)) {
                        Section section = ini.getSection(currentSection);
                        section.set(currentKey, currentValue);
                    } else {
                        ini.add(currentKey, currentValue);
                    }
                } else {
                    currentValue += strLine;
                    if (StringUtils.isNotBlank(currentSection)) {
                        Section section = ini.getSection(currentSection);
                        section.set(currentKey, currentValue);
                    } else {
                        ini.add(currentKey, currentValue);
                    }
                }

            }
            this.handPreElement(currentSection, currentKey, currentValue, ini);
        }
        return ini;
    }

    @SuppressWarnings("all")
    private void handPreElement(String oldSection, String currentKey, String currentValue, Ini ini) throws JsonProcessingException {
        if (StringUtils.isNotBlank(oldSection)) {
            if (StringUtils.isBlank(currentKey)) {
                return;
            }
            Section section = ini.getSection(oldSection);
            if (StringUtils.endWith(currentValue, "]") && StringUtils.startWith(currentValue, "[")) {
                List<String> list = JSONUtils.JACKSON.readValue(currentValue, new TypeReference<List<String>>() {});
                section.set(currentKey, list);
            }
            if (StringUtils.endWith(currentValue, "}") && StringUtils.startWith(currentValue, "{")) {
                Map<String, Object> subMap = JSONUtils.JACKSON.readValue(currentValue, new TypeReference<Map<String, Object>>() {});
                section.set(currentKey, subMap);
            }
        }
    }
}
