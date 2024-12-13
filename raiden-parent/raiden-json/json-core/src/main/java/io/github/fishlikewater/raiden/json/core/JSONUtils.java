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
package io.github.fishlikewater.raiden.json.core;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationFeature;
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
import io.github.fishlikewater.raiden.core.DatePattern;
import io.github.fishlikewater.raiden.json.core.jackson.BigNumberSerializer;
import io.github.fishlikewater.raiden.json.core.jackson.JackSonNotNullSerialize;
import io.github.fishlikewater.raiden.json.core.jackson.JacksonUtils;

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
@SuppressWarnings("all")
public final class JSONUtils {

    public static final JacksonUtils JACKSON = new JacksonUtils();

    static {
        // 为mapper注册一个带有SerializerModifier的Factory，此modifier主要做的事情为：值为null时序列化为默认值
        JACKSON.setSerializerFactory(JACKSON.getSerializerFactory().withSerializerModifier(new NeedNotNullSerializerModifier()));
        // 设置时区
        JACKSON.setConfig(JACKSON.getSerializationConfig().with(TimeZone.getTimeZone(ZoneId.of("Asia/Shanghai"))));
        JACKSON.setConfig(JACKSON.getDeserializationConfig().with(TimeZone.getTimeZone(ZoneId.of("Asia/Shanghai"))));
        JACKSON.setConfig(JACKSON.getSerializationConfig().with(Locale.CHINA));
        JACKSON.setConfig(JACKSON.getDeserializationConfig().with(Locale.CHINA));
        JACKSON.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //支持null和空串
        JACKSON.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        //允许出现单引号
        JACKSON.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        //允许出现特殊字符和转义符
        JACKSON.configure(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature(), true);
        //支持结尾逗号
        JACKSON.configure(JsonReadFeature.ALLOW_TRAILING_COMMA.mappedFeature(), true);


        JACKSON.registerModule(new JavaTimeModule());
    }

    private JSONUtils() {
    }

    private static class NeedNotNullSerializerModifier extends BeanSerializerModifier {

        @Override
        public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc, List<BeanPropertyWriter> beanProperties) {
            beanProperties.forEach(b -> b.assignNullSerializer(new JackSonNotNullSerialize(b)));
            return beanProperties;
        }
    }

    private static class JavaTimeModule extends SimpleModule {
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
