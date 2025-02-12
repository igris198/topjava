package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryMealDao implements Dao<Meal> {
    private final Map<Integer, Meal> meals = new ConcurrentHashMap<>();
    private final AtomicInteger idCounter = new AtomicInteger(0);

    public InMemoryMealDao() {
        List<Meal> mealList = Arrays.asList(
            new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
            new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
            new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
            new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
            new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
            new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
            new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );
        mealList.forEach(this::add);
    }

    private int getNewIdCounterValue() {
        return idCounter.incrementAndGet();
    }

    @Override
    public Meal add(Meal obj) {
        int id = getNewIdCounterValue();
        Meal meal = new Meal(id, obj.getDateTime(), obj.getDescription(), obj.getCalories());
        meals.put(id, meal);
        return meal;
    }

    @Override
    public Meal getById(int id) {
        return meals.get(id);
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(meals.values());
    }

    @Override
    public Meal update(Meal obj) {
        return meals.replace(obj.getId(), obj);
    }

    @Override
    public void delete(int id) {
        meals.remove(id);
    }
}
