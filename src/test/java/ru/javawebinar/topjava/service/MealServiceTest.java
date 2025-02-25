package ru.javawebinar.topjava.service;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.List;

import static org.junit.Assert.assertThrows;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    @Autowired
    private MealService service;

    @BeforeClass
    public static void setUpBeforeClass() {
        SLF4JBridgeHandler.install();
    }

    @AfterClass
    public static void setUpAfterClass() {
        SLF4JBridgeHandler.uninstall();
    }

    @Test
    public void getAdminMeal() {
        Meal meal = service.get(MealTestData.ADMIN_MEAL_ID, UserTestData.ADMIN_ID);
        MealTestData.assertMatch(meal, MealTestData.adminMeal);
    }

    @Test
    public void getWrongUser() {
        assertThrows(NotFoundException.class, () -> service.get(MealTestData.ADMIN_MEAL_ID, UserTestData.USER_ID));
    }

    @Test
    public void getWrongFood() {
        assertThrows(NotFoundException.class, () -> service.get(MealTestData.WRONG_MEAL_ID, UserTestData.USER_ID));
    }

    @Test
    public void delete() {
        service.delete(MealTestData.USER_MEAL_ID, UserTestData.USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(MealTestData.USER_MEAL_ID, UserTestData.USER_ID));
    }

    @Test
    public void deleteWrongUser() {
        assertThrows(NotFoundException.class, () -> service.delete(MealTestData.ADMIN_MEAL_ID, UserTestData.USER_ID));
    }

    @Test
    public void deleteWrongFood() {
        assertThrows(NotFoundException.class, () -> service.delete(MealTestData.WRONG_MEAL_ID, UserTestData.USER_ID));
    }

    @Test
    public void getBetweenInclusive30() {
        List<Meal> meals;
        meals = service.getBetweenInclusive(MealTestData.START_DATE30, MealTestData.END_DATE30, UserTestData.ADMIN_ID);
        MealTestData.assertMatch(meals, MealTestData.adminMealsLimit30);
    }

    @Test
    public void getBetweenInclusive3031() {
        List<Meal> meals = service.getBetweenInclusive(MealTestData.START_DATE30, MealTestData.END_DATE31, UserTestData.ADMIN_ID);
        MealTestData.assertMatch(meals, MealTestData.adminMeals);
    }

    @Test
    public void getBetweenInclusiveNullStartDate() {
        List<Meal> meals = service.getBetweenInclusive(null, MealTestData.END_DATE31, UserTestData.ADMIN_ID);
        MealTestData.assertMatch(meals, MealTestData.adminMeals);
    }

    @Test
    public void getBetweenInclusiveNullEndDate() {
        List<Meal> meals = service.getBetweenInclusive(MealTestData.START_DATE30, null, UserTestData.ADMIN_ID);
        MealTestData.assertMatch(meals, MealTestData.adminMeals);
    }

    @Test
    public void getAllUserMeal() {
        List<Meal> meals = service.getAll(UserTestData.USER_ID);
        MealTestData.assertMatch(meals, MealTestData.userMeals);
    }

    @Test
    public void update() {
        Meal updatedMeal = MealTestData.getUpdated();
        service.update(updatedMeal, UserTestData.USER_ID);
        MealTestData.assertMatch(service.get(MealTestData.USER_MEAL_ID, UserTestData.USER_ID), MealTestData.getUpdated());
    }

    @Test
    public void updateNotFound() {
        Meal updatedMeal = MealTestData.getUpdated();
        assertThrows(NotFoundException.class, () -> service.update(updatedMeal, UserTestData.ADMIN_ID));
    }

    @Test
    public void create() {
        Meal createdMeal = service.create(MealTestData.getNewMeal(), UserTestData.ADMIN_ID);
        Integer newId = createdMeal.getId();
        Meal newMeal = MealTestData.getNewMeal();
        newMeal.setId(newId);
        MealTestData.assertMatch(createdMeal, newMeal);
        MealTestData.assertMatch(service.get(newId, UserTestData.ADMIN_ID), newMeal);
    }

    @Test
    public void duplicateDateTimeCreate() {
        assertThrows(DataAccessException.class, () ->
                service.create(new Meal(MealTestData.userMeal.getDateTime(), "Тестовая еда", 666),
                        UserTestData.USER_ID));
    }
}
