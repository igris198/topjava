package ru.javawebinar.topjava.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.io.IOException;
import java.time.LocalTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<MealTo> meals = MealsUtil.filteredByStreams(MealsUtil.MEALS, LocalTime.MIN, LocalTime.MAX, MealsUtil.CALORIES_PER_DAY);
        req.setAttribute("mealsList", meals);

        log.debug("forward to meals.jsp");
        req.getRequestDispatcher("/meals.jsp").forward(req, resp);
    }
}
