/*
 * Copyright © 2024 zhangxiang (fishlikewater@126.com)
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
package com.github.fishlikewater.raiden.json.core;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.fasterxml.jackson.datatype.jsr310.PackageVersion;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.github.fishlikewater.raiden.json.core.fastjson.FastJSONUtils;
import com.github.fishlikewater.raiden.json.core.gson.GsonUtils;
import com.github.fishlikewater.raiden.json.core.hutool.HutoolJSONUtils;
import com.github.fishlikewater.raiden.json.core.jackson.BigNumberSerializer;
import com.github.fishlikewater.raiden.json.core.jackson.JackSonNotNullSerialize;
import com.github.fishlikewater.raiden.json.core.jackson.JacksonUtils;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * <p>
 * {@code JSONUtils}
 * 统一JSON工具类
 * </p>
 *
 * @author fishlikewater@126.com
 * @version 1.0.0
 * @since 2024年05月12日 11:20
 **/
@SuppressWarnings("unused")
public final class JSONUtils {

    public static final FastJSONUtils FAST_JSON = new FastJSONUtils();
    public static final GsonUtils GSON = new GsonUtils();
    public static final JacksonUtils JACKSON = new JacksonUtils();
    public static final HutoolJSONUtils HUTOOL_JSON = new HutoolJSONUtils();

    static {
        // 为mapper注册一个带有SerializerModifier的Factory，此modifier主要做的事情为：值为null时序列化为默认值
        JACKSON.setSerializerFactory(JACKSON.getSerializerFactory().withSerializerModifier(new NeedNotNullSerializerModifier()));
        // 设置时区
        JACKSON.setConfig(JACKSON.getSerializationConfig().with(TimeZone.getTimeZone(ZoneId.of("Asia/Shanghai"))));
        JACKSON.setConfig(JACKSON.getDeserializationConfig().with(TimeZone.getTimeZone(ZoneId.of("Asia/Shanghai"))));
        JACKSON.setConfig(JACKSON.getSerializationConfig().with(Locale.CHINA));
        JACKSON.setConfig(JACKSON.getDeserializationConfig().with(Locale.CHINA));
        JACKSON.registerModule(new JavaTimeModule());
    }

    private JSONUtils() {
    }

    public static class NeedNotNullSerializerModifier extends BeanSerializerModifier {

        @Override
        public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc, List<BeanPropertyWriter> beanProperties) {
            beanProperties.forEach(b -> b.assignNullSerializer(new JackSonNotNullSerialize(b)));
            return beanProperties;
        }
    }

    public static class JavaTimeModule extends SimpleModule {
        public JavaTimeModule() {
            super(PackageVersion.VERSION);
            this.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)));
            this.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(DatePattern.NORM_DATE_PATTERN)));
            this.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(DatePattern.NORM_TIME_PATTERN)));
            this.addSerializer(BigInteger.class, BigNumberSerializer.INSTANCE);

            this.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)));
            this.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(DatePattern.NORM_DATE_PATTERN)));
            this.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern(DatePattern.NORM_TIME_PATTERN)));
        }
    }
}
