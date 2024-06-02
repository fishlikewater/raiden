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
package io.github.fishlikewater;

import io.github.fishlikewater.raiden.core.GeoHash;
import org.junit.Test;

/**
 * {@code GeoHashTest}
 *
 * @author zhangxiang
 * @version 1.0.0
 * @since 2024/05/30
 */
public class GeoHashTest {

    @Test
    public void testGeoHash() {
        GeoHash geohash = new GeoHash();
        String geoHashCode = geohash.encode(40.222012152454, 116.24828312144);
        System.out.println(geoHashCode);
        //对geoHash点解码
        double[] geo = geohash.decode(geoHashCode);
        //geo[0]为纬度，geo[1]为经度
        System.out.println(STR."\{geo[0]} \{geo[1]}");
    }
}
