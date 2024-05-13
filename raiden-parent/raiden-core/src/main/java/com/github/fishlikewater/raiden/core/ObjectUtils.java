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
package com.github.fishlikewater.raiden.core;

import cn.hutool.core.util.StrUtil;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

/**
 * {@code ObjectUtils}
 *
 * @author zhangxiang
 * @version 1.0.0
 * @since 2024/04/30
 */
@SuppressWarnings("unused")
public final class ObjectUtils extends StrUtil {

    public static <T> boolean isNullOrEmpty(T target) {
        if (null == target) {
            return true;
        } else if (target instanceof CharSequence) {
            return isBlank((CharSequence) target);
        } else {
            return isCollectionsSupportType(target) && CollectionUtils.isEmpty(target);
        }
    }

    public static <T> boolean isNotNullOrEmpty(T target) {
        return !isNullOrEmpty(target);
    }

    static String format(String text, Object... args) {
        assert null != text;
        FormattingTuple formattingTuple = MessageFormatter.arrayFormat(text, args);
        return formattingTuple.getMessage();
    }

    private static boolean isCollectionsSupportType(Object target) {
        boolean isCollectionOrMap = target instanceof Collection || target instanceof Map;
        boolean isEnumerationOrIterator = target instanceof Enumeration || target instanceof Iterator;
        return isCollectionOrMap || isEnumerationOrIterator || target.getClass().isArray();
    }
}
