package ru.javawebinar.topjava.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

public class ValidationUtil {
    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    private static final Logger log = LoggerFactory.getLogger(ValidationUtil.class);

    public static <T> void validate(T t) {
        Set<ConstraintViolation<T>> violations = validator.validate(t);
        if (!violations.isEmpty()) {
            for (ConstraintViolation<T> violation : violations) {
                log.error(violation.getMessage());
            }
            throw new ConstraintViolationException(violations);
        }
    }
}
