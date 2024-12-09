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
public class Response<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 6203784062620103712L;

    private CompletableFuture<HttpResponse<T>> asyncResponse;

    private HttpResponse<T> syncResponse;

    private Response(CompletableFuture<HttpResponse<T>> asyncResponse) {
        this.asyncResponse = asyncResponse;
    }

    private Response(HttpResponse<T> syncResponse) {
        this.syncResponse = syncResponse;
    }

    public static <T> Response<T> ofAsync(CompletableFuture<HttpResponse<T>> asyncResponse) {
        return new Response<T>(asyncResponse);
    }

    public static <T> Response<T> ofSync(HttpResponse<T> syncResponse) {
        return new Response<T>(syncResponse);
    }
}
