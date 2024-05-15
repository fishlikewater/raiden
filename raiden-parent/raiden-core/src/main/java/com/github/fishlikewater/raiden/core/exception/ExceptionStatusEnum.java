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
package com.github.fishlikewater.raiden.core.exception;

/**
 * {@code ExceptionStatusEnum}
 *
 * @author zhangxiang
 * @version 1.0.0
 * @since 2024/04/30
 */
@SuppressWarnings("unused")
public enum ExceptionStatusEnum {

    // 请求成功
    OK("handle.ok", 200, "000000", "请求成功"),
    /**
     * 请求错误
     */
    BAD_REQUEST("handle.api.request.error", 400, "400001", "请求错误"),
    /**
     * 未认证
     */
    UNAUTHORIZED("handle.api.request.unauthorized", 401, "401000", "未认证"),
    /**
     * 令牌过期
     */
    UNAUTHORIZED_EXPIRED("handle.api.request.unauthorized.expired", 401, "401001", "令牌:过期"),
    /**
     * 令牌验签失败
     */
    UNAUTHORIZED_INVALID_SIGNATURE("handle.api.request.unauthorized.invalid.signature", 401, "401002", "令牌:验签失败"),
    /**
     * 缺少参数
     */
    UNAUTHORIZED_INVALID_MISSING_REQUIRED_PARAMETER("handle.api.request.unauthorized.missing.required.parameter", 401, "401003", "请传入: 正确的参数"),
    /**
     * 令牌类型错误
     */
    UNAUTHORIZED_INVALID_TOKEN_TYPE("handle.api.request.unauthorized.invalid.token.type", 401, "401004", "令牌:类型错误"),
    /**
     * 不授信请求
     */
    UNAUTHORIZED_DISTRUST_REQUEST("handle.api.request.unauthorized.distrust", 401, "401005", "不授信请求"),
    /**
     * 账号被禁用
     */
    UNAUTHORIZED_ACCOUNT_FORBIDDEN("handle.api.request.unauthorized.account.forbidden", 401, "401006", "账号禁用，无法登录"),
    /**
     * 无操作权限
     */
    FORBIDDEN("handle.api.request.forbidden", 403, "403000", "无权限"),
    NOT_ACTIVATED_FORBIDDEN("handle.api.request.not.activated.forbidden", 403, "403001", "账号未激活,请修改密码激活账号"),
    /**
     * 500xxx 系统错误
     */
    INNER_ERROR("handle.api.request.inner.error", 500, "500000", "系统错误"),
    /**
     * 请求超时
     */
    TIME_OUT("handle.api.request.timeout.error", 504, "504000", "请求超时"),
    /**
     * 600xxx UAA 错误
     */
    QRCODE_CONFIG_TENANT_ERROR(".qrcode.config.tenant.error", 600, "600001", "扫码租户配置错误"),
    /**
     * 二维码过期
     */
    QRCODE_EXPIRED("qrcode.web.expired.error", 600, "600002", "二维码过期"),

    ;

    private final String alias;
    private final int status;
    private final String code;
    private final String message;

    ExceptionStatusEnum(String alias, int status, String code, String message) {
        this.alias = alias;
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public static ExceptionStatusEnum resolve(int status) {
        for (ExceptionStatusEnum anEnum : values()) {
            if (status == anEnum.status()) {
                return anEnum;
            }
        }

        return INNER_ERROR;
    }

    public static ExceptionStatusEnum resolve(String code) {
        for (ExceptionStatusEnum anEnum : values()) {
            if (anEnum.code().equalsIgnoreCase(code)) {
                return anEnum;
            }
        }

        return INNER_ERROR;
    }

    public String alias() {
        return alias;
    }

    public int status() {
        return status;
    }

    public String code() {
        return code;
    }

    public String message() {
        return message;
    }
}
