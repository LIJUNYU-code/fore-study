package org.myproject.utils;

public class SnowflakeIdUtil {

    // TODO: 实现雪花算法ID生成器
    // 雪花算法结构(64位):
    //   1位符号位(0) + 41位时间戳 + 10位工作机器ID(5位数据中心ID + 5位机器ID) + 12位序列号
    //
    // 需要实现的字段:
    //   - startTimestamp: 起始时间戳(如 2026-01-01 00:00:00)
    //   - datacenterId: 数据中心ID (0~31)
    //   - workerId: 机器ID (0~31)
    //   - sequence: 序列号 (0~4095)
    //   - lastTimestamp: 上次生成ID的时间戳
    //
    // 核心方法 nextId():
    //   1. 获取当前时间戳
    //   2. 如果当前时间 < 上次时间, 抛出异常(时钟回拨)
    //   3. 如果当前时间 == 上次时间:
    //      sequence = (sequence + 1) & 0xFFF
    //      如果sequence == 0, 等待下一毫秒
    //   4. 如果当前时间 > 上次时间:
    //      sequence = 0
    //   5. 更新lastTimestamp
    //   6. 组装ID: ((timestamp - startTimestamp) << 22) | (datacenterId << 17) | (workerId << 12) | sequence

    private static final long START_TIMESTAMP = 1735689600000L;
    private static final long DATACENTER_ID = 1L;
    private static final long WORKER_ID = 1L;
    private static long sequence = 0L;
    private static long lastTimestamp = -1L;
    private static long maxSequence = 0xFFF;

    private static long getNextMillisecond(long lastTimestamp) {
        while (System.currentTimeMillis() <= lastTimestamp) {
        }
        return System.currentTimeMillis();
    }

    public static synchronized long nextId() {
        // TODO: 实现雪花算法
        long timestamp = System.currentTimeMillis();
        if (timestamp < lastTimestamp) {
            throw new RuntimeException("Clock moved backwards. Refusing to generate id for " + (lastTimestamp - timestamp) + " milliseconds");
        }
        if (timestamp == lastTimestamp) {
            sequence = (sequence + 1) & 0xFFF;
            if (sequence == 0) {
                timestamp = getNextMillisecond(lastTimestamp);
            }
        } else {
            sequence = 0;
        }
        lastTimestamp = timestamp;
        return ((timestamp - START_TIMESTAMP) << 22) | (DATACENTER_ID << 17) | (WORKER_ID << 12) | sequence;
    }

}