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
package com.github.fishlikewater.spring.boot.raiden.core.getter;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;

/**
 * {@code BeanFactoryGetter}
 *
 * @author zhangxiang
 * @version 1.0.0
 * @since 2024/04/30
 */
public interface BeanFactoryGetter {

    /**
     * 获取beanFactory
     * @return beanFactory
     */
    BeanFactory beanFactory();

    /**
     * 获取listableBeanFactory
     * @return listableBeanFactory
     */
    default ListableBeanFactory listableBeanFactory() {
        return (ListableBeanFactory) this.beanFactory();
    }
}
