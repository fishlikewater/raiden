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
package io.github.fishlikewater.raiden.core.enums;

import io.github.fishlikewater.raiden.core.ObjectUtils;
import io.github.fishlikewater.raiden.core.exception.RaidenExceptionCheck;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * {@code FileMagicNumberEnum}
 * 文件类型及魔数
 *
 * @author zhangxiang
 * @version 1.0.3
 * @since 2024/07/02
 */
public enum FileMagicNumberEnum {

    /**
     * JPEG  (jpg)
     */
    JPEG("JPG", "FFD8FF"),

    PNG("PNG", "89504E47"),

    GIF("GIF", "47494638"),

    TIFF("TIF", "49492A00"),

    /**
     * Windows bitmap (bmp)
     */
    BMP("BMP", "424D"),

    XLS("xls", "D0CF11E0A1B11AE1"),

    DOC("doc", "D0CF11E0A1B11AE1"),

    XLSX("xlsx", "504B0304"),

    DOCX("docx", "504B0304");

    private final String code;

    @Getter
    private final String suffix;

    FileMagicNumberEnum(String suffix, String code) {
        this.code = code;
        this.suffix = suffix;
    }

    public static FileMagicNumberEnum codeOf(String header, String... suffix) {
        if (ObjectUtils.isNullOrEmpty(header)) {
            return null;
        }

        List<String> suffixes = Arrays.asList(suffix);

        List<FileMagicNumberEnum> magics = new ArrayList<>();
        if (ObjectUtils.isNotNullOrEmpty(suffixes)) {
            for (FileMagicNumberEnum magic : values()) {
                if (suffixes.contains(magic.suffix())) {
                    magics.add(magic);
                }
            }
        }

        magics = ObjectUtils.isNotNullOrEmpty(magics) ? magics : Arrays.asList(values());

        for (FileMagicNumberEnum magic : magics) {
            if (header.toUpperCase().startsWith(magic.code())) {
                return magic;
            }
        }

        return RaidenExceptionCheck.INSTANCE.throwUnchecked("this.file.don't.support");
    }

    public String suffix() {
        return suffix;
    }

    public String code() {
        return code;
    }
}