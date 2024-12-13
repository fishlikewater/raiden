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
package io.github.fishlikewater.raiden.json.core.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.github.fishlikewater.raiden.core.StringUtils;
import io.github.fishlikewater.raiden.json.core.annotation.JackSonSerializeNotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.time.temporal.Temporal;
import java.util.Collections;
import java.util.Date;

/**
 * <p>
 * json 字段为null时的自定义序列化方式
 * 只有字段为null时的序列化才会使用此方式，有值的和正常一样
 * </p>
 *
 * @author fishlikewater@126.com
 * @version V1.0.0
 * @since 2022年04月08日 21:10
 **/
@NoArgsConstructor
@AllArgsConstructor
public class JackSonNotNullSerialize extends JsonSerializer<Object> {

    private BeanProperty property;

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value != null) {
            gen.writeObject(value);
            return;
        }
        JavaType javaType = property.getType();
        JackSonSerializeNotNull needNotNull = property.getAnnotation(JackSonSerializeNotNull.class);
        if (needNotNull == null) {
            needNotNull = property.getContextAnnotation(JackSonSerializeNotNull.class);
        }
        if (needNotNull == null) {
            gen.writeObject(null);
            return;
        }
        // 是否排除
        if (needNotNull.isExclude()) {
            gen.writeObject(null);
            return;
        }
        // 有自定义值
        if (StringUtils.isNotBlank(needNotNull.customV())) {
            gen.writeObject(needNotNull.customV());
            return;
        }
        // bool
        if (needNotNull.boolT() && javaType.isTypeOrSubTypeOf(Boolean.class)) {
            gen.writeObject(needNotNull.boolV());
            return;
        }
        // Number
        if (needNotNull.numberT() && javaType.isTypeOrSubTypeOf(Number.class)) {
            gen.writeObject(needNotNull.numberV());
            return;
        }
        // String
        if (needNotNull.stringT() && javaType.isTypeOrSubTypeOf(String.class)) {
            gen.writeObject(needNotNull.stringV());
            return;
        }
        // 集合、数组
        if (needNotNull.collT() && this.determineIsCollection(javaType)) {
            gen.writeObject(Collections.emptyList());
            return;
        }

        // 时间
        if (needNotNull.dateT() && this.determineIsTime(javaType)) {
            gen.writeObject(needNotNull.dateV());
            return;
        }

        gen.writeObject(null);
    }

    private boolean determineIsTime(JavaType javaType) {
        return javaType.isTypeOrSubTypeOf(Date.class) || javaType.isTypeOrSubTypeOf(Temporal.class);
    }

    private boolean determineIsCollection(JavaType javaType) {
        return javaType.isArrayType() || javaType.isCollectionLikeType();
    }

}
