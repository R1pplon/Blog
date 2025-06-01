package com.example.demo.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    private static final DateTimeFormatter SHORT_DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final DateTimeFormatter FULL_DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String formatShortDate(LocalDateTime dateTime) {
        return dateTime.format(SHORT_DATE_FORMATTER);
    }

    public static String formatFullDateTime(LocalDateTime dateTime) {
        return dateTime.format(FULL_DATE_FORMATTER);
    }
}