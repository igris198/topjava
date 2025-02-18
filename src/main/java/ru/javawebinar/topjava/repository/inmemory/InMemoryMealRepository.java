package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Meal> mealsMap = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(meal -> save(meal, meal.getUserId()));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        meal.setUserId(userId);
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            mealsMap.put(meal.getId(), meal);
            return meal;
        }
        Meal resultMeal = mealsMap.computeIfPresent(meal.getId(), (id, oldMeal) -> oldMeal.getUserId() == userId ? meal : oldMeal);
        return resultMeal != null && resultMeal.getUserId() == userId ? meal : null;
    }

    @Override
    public boolean delete(int id, int userId) {
        Meal meal = mealsMap.get(id);
        if (meal == null) {
            return false;
        }
        if (meal.getUserId() == userId) {
            return mealsMap.remove(id, meal);
        }
        return false;
    }

    @Override
    public Meal get(int id, int userId) {
        Meal meal = mealsMap.get(id);
        return meal != null && meal.getUserId() == userId ? meal : null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        return filterByPredicate(userId, meal -> true);
    }

    @Override
    public List<Meal> getFilteredByDate(int userId, LocalDate startDate, LocalDate endDate) {
        return filterByPredicate(userId, meal -> DateTimeUtil.isDateBetween(meal.getDate(), startDate, endDate));
    }

    private List<Meal> filterByPredicate(int userId, Predicate<Meal> filter) {
        return mealsMap.values().stream()
                .filter(meal -> meal.getUserId() == userId)
                .filter(filter)
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }
}