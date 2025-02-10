package ru.javawebinar.topjava.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {
    private static final DateTimeFormatter FORMATTER_FROM_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final DateTimeFormatter FORMATTER_FROM_STR = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(FORMATTER_FROM_DATE) : null;
    }

    public static LocalDateTime parseDateStr(String str) {
        return (str == null || str.isEmpty()) ? null : LocalDateTime.parse(str, FORMATTER_FROM_STR);
    }
}
