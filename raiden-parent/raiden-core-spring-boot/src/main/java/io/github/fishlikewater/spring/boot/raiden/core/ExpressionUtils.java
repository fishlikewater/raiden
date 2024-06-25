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
package io.github.fishlikewater.spring.boot.raiden.core;

import io.github.fishlikewater.raiden.core.StringUtils;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * {@code ExpressionUtils}
 * SPEL表达式解析工具
 *
 * @author zhangxiang
 * @version 1.0.3
 * @since 2024/06/25
 */
public class ExpressionUtils {
    private static final Map<String, Expression> EXPRESSION_CACHE = new ConcurrentHashMap<>(64);

    /**
     * 获取Expression对象
     *
     * @param expressionString Spring EL 表达式字符串 例如 #{param.id}
     * @return Expression
     */
    public static Expression getExpression(String expressionString) {

        if (StringUtils.isBlank(expressionString)) {
            return null;
        }

        if (EXPRESSION_CACHE.containsKey(expressionString)) {
            return EXPRESSION_CACHE.get(expressionString);
        }

        Expression expression = new SpelExpressionParser().parseExpression(expressionString);
        EXPRESSION_CACHE.put(expressionString, expression);
        return expression;
    }

    /**
     * 根据Spring EL表达式字符串从根对象中求值
     *
     * @param context          {@code EvaluationContext}
     * @param expressionString Spring EL表达式
     * @param clazz            值得类型
     * @param <T>              泛型
     * @return 值
     */
    public static <T> T getExpressionValue(EvaluationContext context, String expressionString, Class<T> clazz) {
        if (context == null) {
            return null;
        }
        Expression expression = getExpression(expressionString);
        if (expression == null) {
            return null;
        }
        return expression.getValue(context, clazz);
    }
}
