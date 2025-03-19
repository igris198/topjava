package ru.javawebinar.topjava.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

public class JdbcUtil {
    private static final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private static final Validator validator = validatorFactory.getValidator();
    private static final Logger log = LoggerFactory.getLogger(JdbcUtil.class);

    public static <T> void validate(T t) {
        Set<ConstraintViolation<T>> violations = validator.validate(t);
        if (!violations.isEmpty()) {
            for (ConstraintViolation<T> violation : violations) {
                log.error(violation.getMessage());
            }
            throw new UnsupportedOperationException();
        }
    }
}
