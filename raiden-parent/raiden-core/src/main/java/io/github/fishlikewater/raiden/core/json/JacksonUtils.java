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
package io.github.fishlikewater.raiden.core.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.fishlikewater.raiden.core.CollectionUtils;
import io.github.fishlikewater.raiden.core.ObjectUtils;
import io.github.fishlikewater.raiden.core.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * {@code JacksonUtils}
 * <p>
 * Jackson 工具类
 * </p>
 *
 * @author fishlikewater@126.com
 * @version 1.0.0
 * @since 2024年05月12日 11:14
 **/
@SuppressWarnings("all")
public class JacksonUtils extends ObjectMapper {

    /**
     * 将对象转换为JSON格式的字符串
     *
     * @param object 要转换的对象
     * @return JSON格式的字符串，如果对象为null，则返回null
     * @throws RuntimeException 如果转换过程中发生JSON处理异常，则抛出运行时异常
     */
    public String toJsonString(Object object) {
        if (ObjectUtils.isNullOrEmpty(object)) {
            return null;
        }
        try {
            return this.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将JSON格式的字符串转换为指定类型的对象
     *
     * @param text  JSON格式的字符串
     * @param clazz 要转换的目标对象类型
     * @param <T>   目标对象的泛型类型
     * @return 转换后的对象，如果字符串为空则返回null
     * @throws RuntimeException 如果转换过程中发生IO异常，则抛出运行时异常
     */
    public <T> T parseObject(String text, Class<T> clazz) {
        if (StringUtils.isBlank(text)) {
            return null;
        }
        try {
            return this.readValue(text, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将字节数组转换为指定类型的对象
     *
     * @param bytes 字节数组
     * @param clazz 要转换的目标对象类型
     * @param <T>   目标对象的泛型类型
     * @return 转换后的对象，如果字节数组为空则返回null
     * @throws RuntimeException 如果转换过程中发生IO异常，则抛出运行时异常
     */
    public <T> T parseObject(byte[] bytes, Class<T> clazz) {
        if (CollectionUtils.isEmpty(bytes)) {
            return null;
        }
        try {
            return this.readValue(bytes, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将JSON格式的字符串转换为指定类型的对象，支持复杂类型
     *
     * @param text          JSON格式的字符串
     * @param typeReference 指定类型的TypeReference对象
     * @param <T>           目标对象的泛型类型
     * @return 转换后的对象，如果字符串为空则返回null
     * @throws RuntimeException 如果转换过程中发生IO异常，则抛出运行时异常
     */
    public <T> T parseObject(String text, TypeReference<T> typeReference) {
        if (StringUtils.isBlank(text)) {
            return null;
        }
        try {
            return this.readValue(text, typeReference);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将JSON格式的字符串转换为指定类型对象的列表
     *
     * @param text  JSON格式的字符串
     * @param clazz 要转换的目标对象类型
     * @param <T>   目标对象的泛型类型
     * @return 转换后的对象的列表，如果字符串为空则返回空列表
     * @throws RuntimeException 如果转换过程中发生IO异常，则抛出运行时异常
     */
    public <T> List<T> parseArray(String text, Class<T> clazz) {
        if (StringUtils.isBlank(text)) {
            return new ArrayList<>();
        }
        try {
            return this.readValue(text, this.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将JSON格式的树节点转换为指定类型的对象
     *
     * @param node  JSON格式的树节点
     * @param clazz 要转换的目标对象类型
     * @param <T>   目标对象的泛型类型
     * @return 转换后的对象
     * @throws RuntimeException 如果转换过程中发生JSON处理异常，则抛出运行时异常
     */
    public <T> T treeParseToValue(TreeNode node, Class<T> clazz) {
        try {
            return this.treeToValue(node, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
