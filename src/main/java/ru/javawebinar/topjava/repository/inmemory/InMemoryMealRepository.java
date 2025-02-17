package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.Collection;
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
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meal.setUserId(userId);
            mealsMap.put(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage
        return mealsMap.computeIfPresent(meal.getId(), (id, oldMeal) -> {
            if (oldMeal.getUserId() == userId) {
                return meal;
            }
            return oldMeal;
        }).getId().equals(meal.getId()) ? meal : null;
    }

    @Override
    public boolean delete(int id, int userId) {
        return mealsMap.computeIfPresent(id, (key, meal) -> meal.getUserId() == userId ? null : meal) == null;
    }

    @Override
    public Meal get(int id, int userId) {
        Meal meal = mealsMap.get(id);
        return meal.getUserId() == userId ? meal : null;
    }

    @Override
    public Collection<Meal> getAll(int userId) {
        return filterByPredicate(userId, meal -> true);
    }

    @Override
    public Collection<Meal> getFilteredByDate(int userId, LocalDate startDate, LocalDate endDate) {
        return filterByPredicate(userId, meal -> DateTimeUtil.isDateBetween(meal.getDate(), startDate, endDate));
    }

    private Collection<Meal> filterByPredicate(int userId, Predicate<Meal> filter) {
        return mealsMap.values().stream()
                .filter(meal -> meal.getUserId() == userId)
                .filter(filter)
                .sorted((meal1, meal2) -> {
                    if (meal1.getDateTime().isEqual(meal2.getDateTime())) {
                        return 0;
                    } else if (meal1.getDateTime().isAfter(meal2.getDateTime())) {
                        return -1;
                    } else {
                        return 1;
                    }
                })
                .collect(Collectors.toList());
    }
}