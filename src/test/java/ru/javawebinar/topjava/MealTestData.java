package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.AbstractBaseEntity;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class MealTestData {
    public static final int ADMIN_MEAL_ID = 100004;
    public static final int USER_MEAL_ID = 100010;
    public static final LocalDate START_DATE30 = LocalDate.of(2020, Month.JANUARY, 30);
    public static final LocalDate END_DATE30 = LocalDate.of(2020, Month.JANUARY, 30);
    public static final LocalDate START_DATE31 = LocalDate.of(2020, Month.JANUARY, 31);
    public static final LocalDate END_DATE31 = LocalDate.of(2020, Month.JANUARY, 31);

    public static final Meal adminMeal = new Meal(ADMIN_MEAL_ID, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед админа", 1000);
    public static final Meal userMeal = new Meal(USER_MEAL_ID, LocalDateTime.of(2022, Month.JANUARY, 31, 13, 0), "Обед юзера", 500);

    public static final List<Meal> adminMeals = Arrays.asList(
            new Meal(ADMIN_MEAL_ID - 1, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак админа", 500),
            new Meal(ADMIN_MEAL_ID, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед админа", 1000),
            new Meal(ADMIN_MEAL_ID + 1, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин админа", 500),
            new Meal(ADMIN_MEAL_ID + 2, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение админа", 100),
            new Meal(ADMIN_MEAL_ID + 3, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак админа", 1000),
            new Meal(ADMIN_MEAL_ID + 4, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед админа", 500),
            new Meal(ADMIN_MEAL_ID + 5, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин админа", 410)
    );
    public static final List<Meal> adminMealsLimit30 = Arrays.asList(
            new Meal(ADMIN_MEAL_ID - 1, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак админа", 500),
            new Meal(ADMIN_MEAL_ID, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед админа", 1000),
            new Meal(ADMIN_MEAL_ID + 1, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин админа", 500)
    );

    public static final List<Meal> adminMealsLimit31 = Arrays.asList(
            new Meal(ADMIN_MEAL_ID + 2, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение админа", 100),
            new Meal(ADMIN_MEAL_ID + 3, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак админа", 1000),
            new Meal(ADMIN_MEAL_ID + 4, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед админа", 500),
            new Meal(ADMIN_MEAL_ID + 5, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин админа", 410)
    );

    public static final List<Meal> userMeals = Arrays.asList(
            new Meal(USER_MEAL_ID, LocalDateTime.of(2022, Month.JANUARY, 31, 13, 0), "Обед юзера", 500),
            new Meal(USER_MEAL_ID + 1, LocalDateTime.of(2022, Month.JANUARY, 31, 20, 0), "Ужин юзера", 410));

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
        assertThat(actual).usingRecursiveFieldByFieldElementComparator().isEqualTo(expected.stream().sorted(Comparator.comparing(AbstractBaseEntity::getId).reversed()).collect(Collectors.toList()));
    }

}
