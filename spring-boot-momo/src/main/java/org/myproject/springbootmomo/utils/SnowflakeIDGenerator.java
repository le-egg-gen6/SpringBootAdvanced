package org.myproject.springbootmomo.utils;

import lombok.experimental.UtilityClass;

/**
 * @author nguyenle
 * @since 12:13 AM Tue 4/22/2025
 */
@UtilityClass
public class SnowflakeIDGenerator {

    // Epoch timestamp (January 1, 2022)
    private static final long EPOCH = 1640995200000L;

    //Bit allocation
    private static final long SEQUENCE_BIT = 22L;

    private static final long MAX_SEQUENCE = ~(-1L << SEQUENCE_BIT);

    private static final long TIMESTAMP_SHIFT = SEQUENCE_BIT;

    private static long sequence;
    private static long lastTimestamp;

    public static synchronized long nextId() {
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis < lastTimestamp) {
            throw new RuntimeException("Clock moved backwards.  Refusing to generate id");
        }
        if (currentTimeMillis == lastTimestamp) {
            sequence = (sequence + 1) & MAX_SEQUENCE;
            if (sequence == 0) {
                currentTimeMillis = waitNextMillis(currentTimeMillis);
            }
        } else {
            sequence = 0;
        }
        lastTimestamp = currentTimeMillis;
        return (currentTimeMillis - EPOCH) << TIMESTAMP_SHIFT | sequence;
    }

    private static long waitNextMillis(long lastTimestamp) {
        long currentTimeMillis = System.currentTimeMillis();
        while (currentTimeMillis <= lastTimestamp) {
            currentTimeMillis = System.currentTimeMillis();
        }
        return currentTimeMillis;
    }

    public static long getTimestamp(long id) {
        return (id >> TIMESTAMP_SHIFT) + EPOCH;
    }

    public static long getSequence(long id) {
        return id & MAX_SEQUENCE;
    }

}

