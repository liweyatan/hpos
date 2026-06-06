package com.hpos.common;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

public class OrderNoGenerator {

    private static final String PREFIX = "REG";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static String currentDate = "";
    private static final AtomicInteger SEQUENCE = new AtomicInteger(0);

    public static synchronized String generate() {
        String today = LocalDate.now().format(DATE_FORMAT);

        if (!today.equals(currentDate)) {
            currentDate = today;
            SEQUENCE.set(0);
        }

        int seq = SEQUENCE.incrementAndGet();
        return PREFIX + today + String.format("%06d", seq);
    }
}
