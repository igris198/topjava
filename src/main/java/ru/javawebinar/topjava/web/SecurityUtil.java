package ru.javawebinar.topjava.web;

import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.util.MealsUtil.DEFAULT_CALORIES_PER_DAY;

public class SecurityUtil {
    private static final Logger log = getLogger(SecurityUtil.class);

    private static int authUserId;

    public static void setAuthUserId(int userId) {
        log.info("set authUserId:{}", userId);
        authUserId = userId;
    }

    public static int authUserId() {
        log.info("get authUserId:{}", authUserId);
        return authUserId;
    }

    public static int authUserCaloriesPerDay() {
        return DEFAULT_CALORIES_PER_DAY;
    }
}