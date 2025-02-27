package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int ADMIN_MEAL_ID = START_SEQ + 4;
    public static final int USER_MEAL_ID = START_SEQ + 9;
    public static final int WRONG_MEAL_ID = START_SEQ + 10000;
    public static final LocalDate START_DATE30 = LocalDate.of(2020, Month.JANUARY, 30);
    public static final LocalDate END_DATE30 = LocalDate.of(2020, Month.JANUARY, 30);
    public static final LocalDate END_DATE31 = LocalDate.of(2020, Month.JANUARY, 31);

    public static final Meal adminMeal = new Meal(ADMIN_MEAL_ID, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед админа 30", 1000);
    public static final Meal userMeal = new Meal(USER_MEAL_ID, LocalDateTime.of(2022, Month.JANUARY, 31, 0, 0), "Обед юзера", 500);

    public static final Meal admin30012020minDate = new Meal(ADMIN_MEAL_ID - 1, LocalDateTime.of(2020, Month.JANUARY, 30, 0, 0), "Завтрак админа 30", 500);
    public static final Meal admin30012020averageDate = adminMeal;
    public static final Meal admin30012020maxDate = new Meal(ADMIN_MEAL_ID + 1, LocalDateTime.of(2020, Month.JANUARY, 30, 23, 59, 59), "Ужин админа 30", 500);

    public static final Meal admin31012020minDate = new Meal(ADMIN_MEAL_ID + 2, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Завтрак админа 31", 100);
    public static final Meal admin31012020averageDate = new Meal(ADMIN_MEAL_ID + 3, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Обед админа 31", 1000);
    public static final Meal admin31012020maxDate = new Meal(ADMIN_MEAL_ID + 4, LocalDateTime.of(2020, Month.JANUARY, 31, 23, 59,59), "Ужин админа 31", 901);

    public static final Meal userMealMinDate = userMeal;
    public static final Meal userMealMaxDate = new Meal(USER_MEAL_ID + 1, LocalDateTime.of(2022, Month.JANUARY, 31, 23, 59), "Ужин юзера", 410);

    public static final List<Meal> adminMeals = Arrays.asList(
            admin31012020maxDate,
            admin31012020averageDate,
            admin31012020minDate,
            admin30012020maxDate,
            admin30012020averageDate,
            admin30012020minDate
    );

    public static final List<Meal> adminMealsLimit30 = Arrays.asList(
            admin30012020maxDate,
            admin30012020averageDate,
            admin30012020minDate
    );

    public static final List<Meal> userMeals = Arrays.asList(
            userMealMaxDate,
            userMealMinDate
    );

    public static Meal getNewMeal() {
        return new Meal(LocalDateTime.of(2025, Month.FEBRUARY, 20, 20, 0), "Тестовая еда", 333);
    }

    public static Meal getUpdated() {
        Meal updated = new Meal(userMeal);
        updated.setDateTime(LocalDateTime.of(2025, Month.FEBRUARY, 20, 18, 0));
        updated.setDescription("Updated description");
        updated.setCalories(333);
        return updated;
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static void assertMatch(List<Meal> actual, List<Meal> expected) {
        assertThat(actual).usingRecursiveFieldByFieldElementComparator().isEqualTo(expected);
    }

}
