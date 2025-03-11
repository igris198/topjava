package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;

import java.util.List;

import static ru.javawebinar.topjava.MealTestData.MEAL_MATCHER;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(Profiles.DATAJPA)
public class DataJpaUserServiceTest extends UserServiceTest {
    @Override
    public void setup() {
        super.setup();
    }

    @Override
    public void create() {
        super.create();
    }

    @Override
    public void duplicateMailCreate() {
        super.duplicateMailCreate();
    }

    @Override
    public void delete() {
        super.delete();
    }

    @Override
    public void deletedNotFound() {
        super.deletedNotFound();
    }

    @Override
    public void get() {
        super.get();
    }

    @Override
    public void getNotFound() {
        super.getNotFound();
    }

    @Override
    public void getByEmail() {
        super.getByEmail();
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public void getAll() {
        super.getAll();
    }

    @Test
    public void getWithMeal() {
        User userWithMeal = super.service.getWithMeal(USER_ID);
        List<Meal> mealList = userWithMeal.getMeals();
        USER_MATCHER.assertMatch(user, UserTestData.user);
        MEAL_MATCHER.assertMatch(mealList, MealTestData.meals);
    }
}
