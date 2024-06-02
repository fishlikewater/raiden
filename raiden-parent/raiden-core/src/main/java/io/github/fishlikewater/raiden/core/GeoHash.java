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
package io.github.fishlikewater.raiden.core;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

/**
 * {@code GeoHash}
 * </p>GeoHash算法实现:</p>
 *
 * @author zhangxiang
 * @version 1.0.0
 * @since 2024/05/30
 */
public class GeoHash {

    public static final double MIN_LAT = -90;
    public static final double MAX_LAT = 90;
    public static final double MIN_LNG = -180;
    public static final double MAX_LNG = 180;
    private static final int NUM_BITS = 30;
    private static double minLat;
    private static double minLng;


    private final static char[] DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8',
            '9', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'm', 'n', 'p',
            'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

    final static Map<Character, Integer> LOOKUP = new HashMap<>();

    //初始化编码映射内容
    static {
        int i = 0;
        for (char c : DIGITS) {
            LOOKUP.put(c, i++);
        }
    }

    public GeoHash() {
        setMinLatLng();
    }

    /**
     * 编码
     *
     * @param lat 纬度
     * @param lon 经度
     * @return {@code String}
     */
    public String encode(double lat, double lon) {
        BitSet latBits = getBits(lat, MIN_LAT, MAX_LAT);
        BitSet lonBits = getBits(lon, MIN_LNG, MAX_LNG);
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < NUM_BITS; i++) {
            //添加经度的编码
            buffer.append((latBits.get(i)) ? '1' : '0');
            //添加纬度的编码
            buffer.append((lonBits.get(i)) ? '1' : '0');
        }
        return base32(Long.parseLong(buffer.toString(), 2));
    }


    /**
     * 根据经纬度和范围，获取对应的二进制
     *
     * @param lat     纬度
     * @param floor   范围 down
     * @param ceiling 范围 up
     * @return {@code BitSet}
     */
    private BitSet getBits(double lat, double floor, double ceiling) {
        BitSet buffer = new BitSet(NUM_BITS);
        for (int i = 0; i < NUM_BITS; i++) {
            double mid = (floor + ceiling) / 2;
            //大于中心值记为true
            if (lat >= mid) {
                //将指定索引处的位设置为 true。
                buffer.set(i);
                //区间设置为(mid,ceiling)
                floor = mid;
            } else {
                //区间设置为(floor,mid)
                ceiling = mid;
            }
        }
        return buffer;
    }

    /**
     * 将经纬度合并后的二进制进行指定的32位编码
     *
     * @param bi 二进制数
     * @return {@code String}
     */
    private String base32(long bi) {
        char[] buf = new char[65];
        int charPos = 64;
        boolean negative = (bi < 0);
        if (!negative) {
            bi *= -1;
        }
        while (bi <= -DIGITS.length) {
            //逆序赋值
            buf[charPos--] = DIGITS[(int) (-(bi % 32))];
            bi /= DIGITS.length;
        }
        buf[charPos] = DIGITS[(int) (-bi)];
        if (negative) {
            buf[--charPos] = '-';
        }
        //从charPos开始，到65 - charPos开始解译，也就是从最后赋值的
        return new String(buf, charPos, (65 - charPos));
    }

    private void setMinLatLng() {
        //计算经纬度的最小单元,最小的区间的中心值
        minLat = MAX_LAT - MIN_LAT;
        for (int i = 0; i < NUM_BITS; i++) {
            minLat /= 2.0;
        }
        minLng = MAX_LNG - MIN_LNG;
        for (int i = 0; i < NUM_BITS; i++) {
            minLng /= 2.0;
        }
    }

    /**
     * 对编码后的字符串解码
     *
     * @param geoHash 编码后的字符串
     * @return {@code double}
     */
    @SuppressWarnings("all")
    public double[] decode(String geoHash) {
        StringBuilder buffer = new StringBuilder();
        for (char c : geoHash.toCharArray()) {
            //32的二进制为：100000，要让每位十进制数转为二进制时，都满五位，所以+32
            int i = LOOKUP.get(c) + 32;
            //去掉最高位，减32，还原数字
            buffer.append(Integer.toString(i, 2).substring(1));
        }

        BitSet lonSet = new BitSet();
        BitSet latSet = new BitSet();

        //偶数位，经度，从0开始，每次后移两位
        int j = 0;
        for (int i = 0; i < NUM_BITS * 2; i += 2) {
            boolean isSet = false;
            if (i < buffer.length()) {
                isSet = buffer.charAt(i) == '1';
            }
            latSet.set(j++, isSet);
        }

        //奇数位，纬度，从1开始，每次后移两位
        j = 0;
        for (int i = 1; i < NUM_BITS * 2; i += 2) {
            boolean isSet = false;
            if (i < buffer.length()) {
                isSet = buffer.charAt(i) == '1';
            }
            lonSet.set(j++, isSet);
        }

        double lon = decode(lonSet, MIN_LNG, MAX_LNG);
        double lat = decode(latSet, MIN_LAT, MAX_LAT);

        return new double[]{lat, lon};
    }

    /**
     * 根据二进制和范围解码
     *
     * @param bs      二进制
     * @param floor   范围 down
     * @param ceiling 范围 up
     * @return {@code double}
     */
    private double decode(BitSet bs, double floor, double ceiling) {
        double mid = 0;
        for (int i = 0; i < bs.length(); i++) {
            //区间中心值
            mid = (floor + ceiling) / 2;
            if (bs.get(i)) {
                floor = mid;
            } else {
                ceiling = mid;
            }
        }
        return mid;
    }

    /**
     * 查询该经纬度的geoHash以及该geoHash附近的8个geoHash点
     *
     * @param lat 纬度
     * @param lon 经度
     * @return {@code ArrayList<String>}
     */
    @SuppressWarnings("all")
    public ArrayList<String> getAroundGeoHash(double lat, double lon) {
        ArrayList<String> list = new ArrayList<>();
        double upLat = lat + minLat;
        double downLat = lat - minLat;

        double leftLng = lon - minLng;
        double rightLng = lon + minLng;

        String leftUp = encode(upLat, leftLng);
        list.add(leftUp);

        String leftMid = encode(lat, leftLng);
        list.add(leftMid);

        String leftDown = encode(downLat, leftLng);
        list.add(leftDown);

        String midUp = encode(upLat, lon);
        list.add(midUp);

        String midMid = encode(lat, lon);
        list.add(midMid);

        String midDown = encode(downLat, lon);
        list.add(midDown);

        String rightUp = encode(upLat, rightLng);
        list.add(rightUp);

        String rightMid = encode(lat, rightLng);
        list.add(rightMid);

        String rightDown = encode(downLat, rightLng);
        list.add(rightDown);

        return list;
    }
}


