package ru.javawebinar.topjava.service;

import org.junit.AfterClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(MealServiceTest.class);
    private static final Map<String, Long> testDurationMap = new HashMap<>();
    @Rule
    public TestRule testDurationCounter = new TestWatcher() {
        @Override
        public Statement apply(Statement base, Description description) {
            return new Statement() {
                @Override
                public void evaluate() throws Throwable {
                    long startMs = System.currentTimeMillis();
                    base.evaluate();
                    long duration = System.currentTimeMillis() - startMs;
                    String testName = description.getMethodName();
                    testDurationMap.put(testName, duration);
                    logger.info("Test name:{}, duration:{} ms.", testName, duration);
                }
            };
        }
    };
    @Autowired
    private MealService service;

    @AfterClass
    public static void printRuntimeCounter() {
        logger.info("Final result:");
        testDurationMap.forEach((testName, duration) -> logger.info("{} - {} ms.", testName, duration));
    }

    private Meal resetUser(Meal meal) {
        meal.setUser(null);
        return meal;
    }

    private List<Meal> resetUsers(List<Meal> meals) {
        meals.forEach(this::resetUser);
        return meals;
    }

    @Test
    public void delete() {
        service.delete(MEAL1_ID, USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(MEAL1_ID, USER_ID));
    }

    @Test
    public void deleteNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND, USER_ID));
    }

    @Test
    public void deleteNotOwn() {
        assertThrows(NotFoundException.class, () -> service.delete(MEAL1_ID, ADMIN_ID));
    }

    @Test
    public void create() {
        Meal created = service.create(getNew(), USER_ID);
        int newId = created.id();
        Meal newMeal = getNew();
        newMeal.setId(newId);
        MEAL_MATCHER.assertMatch(resetUser(created), newMeal);
        MEAL_MATCHER.assertMatch(resetUser(service.get(newId, USER_ID)), newMeal);
    }

    @Test
    public void duplicateDateTimeCreate() {
        assertThrows(DataAccessException.class, () ->
                service.create(new Meal(null, meal1.getDateTime(), "duplicate", 100), USER_ID));
    }

    @Test
    public void get() {
        MEAL_MATCHER.assertMatch(resetUser(service.get(ADMIN_MEAL_ID, ADMIN_ID)), adminMeal1);
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND, USER_ID));
    }

    @Test
    public void getNotOwn() {
        assertThrows(NotFoundException.class, () -> service.get(MEAL1_ID, ADMIN_ID));
    }

    @Test
    public void update() {
        Meal updated = getUpdated();
        service.update(updated, USER_ID);
        MEAL_MATCHER.assertMatch(resetUser(service.get(MEAL1_ID, USER_ID)), getUpdated());
    }

    @Test
    public void updateNotOwn() {
        assertThrows(NotFoundException.class, () -> service.update(meal1, ADMIN_ID));
        MEAL_MATCHER.assertMatch(resetUser(service.get(MEAL1_ID, USER_ID)), meal1);
    }

    @Test
    public void getAll() {
        List<Meal> tmpMeals = service.getAll(USER_ID).stream()
                .peek(meal -> meal.setUser(null))
                .collect(Collectors.toList());
        MEAL_MATCHER.assertMatch(tmpMeals, meals);
    }

    @Test
    public void getBetweenInclusive() {
        MEAL_MATCHER.assertMatch(resetUsers(service.getBetweenInclusive(
                        LocalDate.of(2020, Month.JANUARY, 30),
                        LocalDate.of(2020, Month.JANUARY, 30), USER_ID)),
                meal3, meal2, meal1);
    }

    @Test
    public void getBetweenWithNullDates() {
        MEAL_MATCHER.assertMatch(resetUsers(service.getBetweenInclusive(null, null, USER_ID)), meals);
    }
}