package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryMealDao implements Dao<Meal> {
    private static final Map<Integer, Meal> meals = new ConcurrentHashMap<>();
    private static final AtomicInteger idCounter = new AtomicInteger(0);

    static {
        addMeal(new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500));
        addMeal(new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000));
        addMeal(new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500));
        addMeal(new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100));
        addMeal(new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000));
        addMeal(new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500));
        addMeal(new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));
    }

    private static Integer getNewIdCounterValue() {
        return idCounter.incrementAndGet();
    }

    private static void addMeal(Meal obj) {
        int id = getNewIdCounterValue();
        Meal meal = new Meal(id, obj.getDateTime(), obj.getDescription(), obj.getCalories());
        meals.put(id, meal);
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
    public synchronized Meal update(Meal obj) {
        Integer id = obj.getId();
        if (id != null && meals.get(id) != null) {
            meals.put(id, obj);
            return obj;
        }
        return null;
    }

    @Override
    public void delete(int id) {
        meals.remove(id);
    }
}
