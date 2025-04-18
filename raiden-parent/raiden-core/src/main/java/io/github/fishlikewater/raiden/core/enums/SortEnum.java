package io.github.fishlikewater.raiden.core.enums;

import io.github.fishlikewater.raiden.core.StringUtils;
import io.github.fishlikewater.raiden.core.exception.RaidenExceptionCheck;

/**
 * {@code SortEnum}
 * 排序方式枚举
 *
 * @author zhangxiang
 * @since 1.1.5
 */
public enum SortEnum {

    ASC("asc", "升序"),
    DESC("desc", "降序");

    private final String alias;
    private final String message;

    SortEnum(String alias, String message) {
        this.alias = alias;
        this.message = message;
    }

    public String alias() {
        return alias;
    }

    public String message() {
        return message;
    }

    public static SortEnum resolve(String alias) {
        for (SortEnum sortEnum : values()) {
            if (StringUtils.equals(alias, sortEnum.alias)) {
                return sortEnum;
            }
        }
        return RaidenExceptionCheck.INSTANCE.throwUnchecked("this.alias.don't.support");
    }
}
