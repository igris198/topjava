package ru.javawebinar.topjava.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static boolean isBetweenHalfOpen(LocalDateTime ldt, LocalTime startTime, LocalTime endTime) {
        LocalTime lt = ldt.toLocalTime();
        return !lt.isBefore(startTime) && lt.isBefore(endTime);
    }

    public static boolean isDateBetween(LocalDate ld, LocalDate startDate, LocalDate endDate) {
        return !ld.isBefore(startDate) && !ld.isAfter(endDate);
    }

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }
}

