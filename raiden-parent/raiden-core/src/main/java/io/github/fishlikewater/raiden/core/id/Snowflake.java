/*
 * Copyright (c) 2025 zhangxiang (fishlikewater@126.com)
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
package io.github.fishlikewater.raiden.core.id;

import io.github.fishlikewater.raiden.core.StringUtils;

/**
 * {@code Snowflake}
 * <p>ID生成器</p>
 * <p>Twitter Snowflake</p>
 * <p>SnowFlake的结构如下(每部分用-分开):</p>
 * <ol>
 * <li>0 - 0000000000 0000000000 0000000000 0000000000 0 - 00000 - 00000 - 000000000000 </li>
 * <li>1位标识,由于long基本类型在Java中是带符号的,最高位是符号位,正数是0,负数是1,所以id一般是正数,最高位是0 </li>
 * <li>41位时间截(毫秒级),注意,41位时间截不是存储当前时间的时间截,而是存储时间截的差值（当前时间截 - 开始时间截)得到的值,</li>
 * <li>这里的的开始时间截,一般是我们的id生成器开始使用的时间,由我们程序来指定的（如下下面程序IdWorker类的startTime属性）。41位的时间截,可以使用69年,年T = (1L << 41) / (1000L * 60 * 60 * 24 * 365) = 69</li>
 * <li>10位的数据机器位,可以部署在1024个节点,包括5位datacenterId和5位workerId</li>
 * <li>12位序列,毫秒内的计数,12位的计数顺序号支持每个节点每毫秒(同一机器,同一时间截)产生4096个ID序号</li>
 * <li>加起来刚好64位,为一个Long型。</li>
 *
 * <li>SnowFlake的优点是,整体上按照时间自增排序,并且整个分布式系统内不会产生ID碰撞(由数据中心ID和机器ID作区分),并且效率较高,经测试,SnowFlake每秒能够产生26万ID左右。</li>
 * </ol>
 *
 * @author zhangxiang
 * @version 1.0.0
 * @since 2024/05/07
 */
public class Snowflake {
    /**
     * 开始时间截 (2022-01-07)
     */
    private static final long EPOCH = 1641538021379L;

    /**
     * 机器id所占的位数
     */
    private static final long WORKER_ID_BITS = 5L;

    /**
     * 数据标识id所占的位数
     */
    private static final long DATA_CENTER_ID_BITS = 5L;

    /**
     * 支持的最大机器id,结果是31 (这个移位算法可以很快的计算出几位二进制数所能表示的最大十进制数)
     */
    private static final long MAX_WORKER_ID = ~(-1L << WORKER_ID_BITS);

    /**
     * 支持的最大数据标识id,结果是31
     */
    private static final long MAX_DATA_CENTER_ID = ~(-1L << DATA_CENTER_ID_BITS);

    /**
     * 序列在id中占的位数
     */
    private static final long SEQUENCE_BITS = 12L;

    /**
     * 机器ID向左移12位
     */
    private static final long WORKER_ID_SHIFT = 12L;

    /**
     * 数据标识id向左移17位(12+5)
     */
    private static final long DATA_CENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;

    /**
     * 时间截向左移22位(5+5+12)
     */
    private static final long TIMESTAMP_LEFT_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + DATA_CENTER_ID_BITS;

    /**
     * 生成序列的掩码,这里为4095 (0b111111111111=0xfff=4095)
     */
    private static final long SEQUENCE_MASK = ~(-1L << SEQUENCE_BITS);

    /**
     * 工作机器ID(0~31)
     */
    private final long workerId;

    /**
     * 数据中心ID(0~31)
     */
    private final long dataCenterId;

    /**
     * 毫秒内序列(0~4095)
     */
    private long sequence = 0L;

    /**
     * 上次生成ID的时间截
     */
    private long lastTimestamp = -1L;

    /**
     * 构造函数
     *
     * @param workerId     工作ID (0~31)
     * @param dataCenterId 数据中心ID (0~31)
     */
    public Snowflake(Long workerId, Long dataCenterId) {
        if (workerId > MAX_WORKER_ID || workerId < 0) {
            throw new IllegalArgumentException(StringUtils.format("worker Id can't be greater than {} or less than 0", MAX_WORKER_ID));
        }
        if (dataCenterId > MAX_DATA_CENTER_ID || dataCenterId < 0) {
            throw new IllegalArgumentException(StringUtils.format("dataCenter Id can't be greater than {} or less than 0", MAX_DATA_CENTER_ID));
        }

        this.workerId = workerId;
        this.dataCenterId = dataCenterId;
    }

    /**
     * 获得下一个ID (该方法是线程安全的)
     *
     * @return SnowflakeId
     */
    public synchronized Long nextId() {
        // 获取当前时间戳
        long timestamp = this.timeGen();

        //如果当前时间小于上一次ID生成的时间戳,说明系统时钟回退过这个时候应当抛出异常
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(
                    StringUtils.format("Clock moved backwards.  Refusing to generate id for {} milliseconds", (lastTimestamp - timestamp)));
        }

        //如果是同一时间生成的,则进行毫秒内序列
        if (lastTimestamp == timestamp) {
            sequence += 1;
            //毫秒内序列溢出
            if (sequence > SEQUENCE_MASK) {
                //阻塞到下一个毫秒,获得新的时间戳
                timestamp = this.tilNextMillis(lastTimestamp);
            }
        } else {
            //时间戳改变,毫秒内序列重置
            sequence = 0L;
        }

        //上次生成ID的时间截
        lastTimestamp = timestamp;

        //移位并通过或运算拼到一起组成64位的ID
        return ((timestamp - EPOCH) << TIMESTAMP_LEFT_SHIFT)
                | (dataCenterId << DATA_CENTER_ID_SHIFT)
                | (workerId << WORKER_ID_SHIFT)
                | sequence;
    }

    /**
     * 阻塞到下一个毫秒,直到获得新的时间戳
     *
     * @param lastTimestamp 上次生成ID的时间截
     * @return 当前时间戳
     */
    protected Long tilNextMillis(Long lastTimestamp) {
        // 获取当前时间戳
        long timestamp = this.timeGen();
        // 直到-获取下一个时间戳
        while (timestamp <= lastTimestamp) {
            timestamp = this.timeGen();
        }

        return timestamp;
    }

    /**
     * 返回以毫秒为单位的当前时间
     *
     * @return 当前时间(毫秒)
     */
    protected long timeGen() {
        //获取当前时间
        return System.currentTimeMillis();
    }
}
