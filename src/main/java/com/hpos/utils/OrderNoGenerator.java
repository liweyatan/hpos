package com.hpos.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 订单号生成器
 * <p>
 * 格式：REG + 日期(yyyyMMdd) + 6位序号
 * 示例：REG20260601000001
 * 说明：每天从 000001 开始计数，支持高并发
 * </p>
 */
public class OrderNoGenerator {

    /** 订单号前缀 */
    private static final String PREFIX = "REG";

    /** 日期格式化器 */
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");

    /** 当天日期的字符串缓存（每天切换时重置序号） */
    private static String currentDate = "";

    /** 线程安全的计数器 */
    private static final AtomicInteger SEQUENCE = new AtomicInteger(0);

    /**
     * 生成下一个订单号
     *
     * @return 唯一的订单号
     */
    public static synchronized String generate() {
        String today = LocalDate.now().format(DATE_FORMAT);

        // 如果日期变了，重置序号
        if (!today.equals(currentDate)) {
            currentDate = today;
            SEQUENCE.set(0);
        }

        // 递增序号并格式化为6位
        int seq = SEQUENCE.incrementAndGet();
        return PREFIX + today + String.format("%06d", seq);
    }
}
