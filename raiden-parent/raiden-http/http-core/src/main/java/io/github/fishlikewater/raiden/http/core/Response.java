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
package io.github.fishlikewater.raiden.http.core;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

/**
 * {@code Response}
 * 响应
 *
 * @author zhangxiang
 * @version 1.1.0
 * @since 2024/12/09
 */
@Data
public class Response implements Serializable {

    @Serial
    private static final long serialVersionUID = 6203784062620103712L;

    private CompletableFuture<HttpResponse<Object>> asyncResponse;

    private HttpResponse<Object> syncResponse;

    private Object fallbackResponse;

    private Response(CompletableFuture<HttpResponse<Object>> asyncResponse) {
        this.asyncResponse = asyncResponse;
    }

    private Response(HttpResponse<Object> syncResponse) {
        this.syncResponse = syncResponse;
    }

    private Response(Object fallbackResponse) {
        this.fallbackResponse = fallbackResponse;
    }

    public static Response ofAsync(CompletableFuture<HttpResponse<Object>> asyncResponse) {
        return new Response(asyncResponse);
    }

    public static Response ofSync(HttpResponse<Object> syncResponse) {
        return new Response(syncResponse);
    }

    public static Response ofFallback(Object fallbackResponse) {
        return new Response(fallbackResponse);
    }
}
