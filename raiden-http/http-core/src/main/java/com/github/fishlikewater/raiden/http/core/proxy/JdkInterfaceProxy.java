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
package com.github.fishlikewater.raiden.http.core.proxy;

import com.github.fishlikewater.raiden.http.core.processor.HttpClientBeanFactory;
import com.github.fishlikewater.raiden.http.core.processor.HttpClientProcessor;
import lombok.Setter;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 *
 * @author fishlikewater@126.com
 * @since 2021年12月26日 13:10
 * @version 1.0.0
 **/
@Setter
public class JdkInterfaceProxy  implements InvocationHandler,InterfaceProxy {

    private HttpClientProcessor httpClientProcessor;

    private HttpClientBeanFactory httpClientBeanFactory;

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getInstance(Class<T> interfaceClass){
        return (T) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{interfaceClass}, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        return handler(method, args, httpClientProcessor, httpClientBeanFactory);
    }
}
