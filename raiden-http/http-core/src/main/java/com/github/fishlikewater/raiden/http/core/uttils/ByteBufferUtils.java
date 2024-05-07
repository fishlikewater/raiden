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
package com.github.fishlikewater.raiden.http.core.uttils;

import lombok.extern.slf4j.Slf4j;

import java.net.http.HttpHeaders;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author fishlikewater@126.com
 * @since 2023年09月25日 16:46
 * @version 1.0.0
 **/
@Slf4j
public class ByteBufferUtils {

    static Pattern pattern = Pattern.compile("charset=([a-zA-Z0-9-]+)");

    public static byte[] join(List<ByteBuffer> bytes) {
        int size = remaining(bytes);
        byte[] res = new byte[size];
        int from = 0;
        for (ByteBuffer b : bytes) {
            int l = b.remaining();
            b.get(res, from, l);
            from += l;
        }
        return res;
    }

    public static int remaining(List<ByteBuffer> buffs) {
        long remain = 0;
        for (ByteBuffer buf : buffs) {
            remain += buf.remaining();
            if (remain > Integer.MAX_VALUE) {
                throw new IllegalArgumentException("too many bytes");
            }
        }
        return (int) remain;
    }

    public static boolean hasRemaining(List<ByteBuffer> buffs) {
        for (ByteBuffer buf : buffs) {
            if (buf.hasRemaining()) {
                return true;
            }
        }
        return false;
    }

    public static Charset charsetFrom(HttpHeaders headers) {
        String type = headers.firstValue("Content-type")
                .orElse("text/html; charset=utf-8");
        int i = type.indexOf(";");
        if (i >= 0) {
            type = type.substring(i + 1);
        }
        try {
            Matcher matcher = pattern.matcher(type);
            if (matcher.find()) {
                return Charset.forName(matcher.group(1));
            }
            return StandardCharsets.UTF_8;
        } catch (Throwable x) {
            log.warn("Can't find charset in {} ", type, x);
            return StandardCharsets.UTF_8;
        }
    }
}
