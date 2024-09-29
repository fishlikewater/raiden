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
package io.github.fishlikewater.raiden.validation.autoconfig;

import io.github.fishlikewater.raiden.validation.core.annotation.Validation;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;

@Aspect
@Order(1)
public class ValidationAspect {

    @Pointcut(value = "@annotation(io.github.fishlikewater.raiden.validation.core.annotation.Validation)")
    public void anyMethod() {
    }

    @Pointcut(value = "@within(io.github.fishlikewater.raiden.validation.core.annotation.Validation)")
    public void anyClass() {

    }

    @Around(value = "anyMethod() && @annotation(validation)")
    public Object aroundAdvice4Method(ProceedingJoinPoint pjp, Validation validation) throws Throwable {
        Class<?>[] groups = validation.groups();
        return pjp.proceed();
    }

    @Around(value = "anyClass() && !@annotation(io.github.fishlikewater.raiden.validation.core.annotation.Validation)")
    public Object aroundAdvice4Class(ProceedingJoinPoint pjp) throws Throwable {
        return pjp.proceed();
    }
}
