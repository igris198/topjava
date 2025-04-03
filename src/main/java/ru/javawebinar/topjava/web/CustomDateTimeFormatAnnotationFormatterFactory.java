package ru.javawebinar.topjava.web;

import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Formatter;
import org.springframework.format.Parser;
import org.springframework.format.Printer;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.web.annotation.CustomDateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class CustomDateTimeFormatAnnotationFormatterFactory implements AnnotationFormatterFactory<CustomDateTimeFormat> {
    @Override
    @NonNull
    public Set<Class<?>> getFieldTypes() {
        return new HashSet<>(List.of(LocalDate.class, LocalTime.class));
    }

    @Override
    @NonNull
    public Printer<?> getPrinter(@NonNull CustomDateTimeFormat annotation, @Nullable Class<?> fieldType) {
        return getFormatter(annotation);
    }

    @Override
    @NonNull
    public Parser<?> getParser(@NonNull CustomDateTimeFormat annotation, @Nullable Class<?> fieldType) {
        return getFormatter(annotation);
    }

    private Formatter<?> getFormatter(CustomDateTimeFormat annotation) {
        switch (annotation.type()) {
            case DATE -> {
                return new CustomDateFormatter();
            }
            case TIME -> {
                return new CustomTimeFormatter();
            }
            default -> throw new IllegalArgumentException();
        }
    }

    private static class CustomDateFormatter implements Formatter<LocalDate> {
        @Override
        @NonNull
        public LocalDate parse(@Nullable String text, @Nullable Locale locale) {
            LocalDate localDate = DateTimeUtil.parseLocalDate(text);
            if (localDate == null) {
                throw new IllegalArgumentException();
            }
            return localDate;
        }

        @Override
        @NonNull
        public String print(LocalDate localDate, @Nullable Locale locale) {
            return localDate.toString();
        }
    }

    private static class CustomTimeFormatter implements Formatter<LocalTime> {
        @Override
        @NonNull
        public LocalTime parse(@Nullable String text, @Nullable Locale locale) {
            LocalTime localTime = DateTimeUtil.parseLocalTime(text);
            if (localTime == null) {
                throw new IllegalArgumentException();
            }
            return localTime;
        }

        @Override
        @NonNull
        public String print(LocalTime localTime, @Nullable Locale locale) {
            return localTime.toString();
        }
    }
}
