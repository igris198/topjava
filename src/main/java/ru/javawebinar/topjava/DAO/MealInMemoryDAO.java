package ru.javawebinar.topjava.DAO;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.repository.MealsRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

public class MealInMemoryDAO implements DAO<Meal> {

    @Override
    public void add(Meal obj) {
        Meal meal = new Meal(MealsRepository.getNewIdCounterValue(), obj.getDateTime(), obj.getDescription(), obj.getCalories());
        MealsRepository.meals.add(meal);
    }

    @Override
    public Meal getById(Integer id) {
        return MealsRepository.meals.stream().filter(meal -> Objects.equals(meal.getId(), id)).findFirst().orElse(null);
    }

    public List<MealTo> getAllMealTo() {
        return MealsUtil.filteredByStreams(MealsRepository.meals, LocalTime.MIN, LocalTime.MAX, MealsRepository.CALORIES_PER_DAY);
    }

    @Override
    public void update(Meal obj) {
        Meal meal = getById(obj.getId());
        meal.setDateTime(obj.getDateTime());
        meal.setDescription(obj.getDescription());
        meal.setCalories(obj.getCalories());
    }

    @Override
    public void delete(Integer id) {
        Meal meal = getById(id);
        MealsRepository.meals.remove(meal);
    }

}
