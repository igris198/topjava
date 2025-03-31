package ru.javawebinar.topjava.util;

import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Formatter;
import org.springframework.format.Parser;
import org.springframework.format.Printer;
import org.springframework.lang.Nullable;
import ru.javawebinar.topjava.annotation.CustomDateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CustomDateTimeFormatAnnotationFormatterFactory implements AnnotationFormatterFactory<CustomDateTimeFormat> {
    @Override
    public Set<Class<?>> getFieldTypes() {
        return new HashSet<>(List.of(LocalDate.class, LocalTime.class));
    }

    @Override
    public Printer<?> getPrinter(CustomDateTimeFormat annotation, @Nullable Class<?> fieldType) {
        return getConverter(annotation);
    }

    @Override
    public Parser<?> getParser(CustomDateTimeFormat annotation, @Nullable Class<?> fieldType) {
        return getConverter(annotation);
    }

    private Formatter<?> getConverter(CustomDateTimeFormat annotation) {
        switch (annotation.type()) {
            case DATE -> {
                return new DateTimeUtil.CustomDateFormatter();
            }
            case TIME -> {
                return new DateTimeUtil.CustomTimeFormatter();
            }
        }
        return null;
    }
}
