package ru.javawebinar.topjava.web;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.Dao;
import ru.javawebinar.topjava.dao.InMemoryMealDao;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.DateUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);

    private static final String UPDATE_MEAL = "/mealUpdate.jsp";
    private static final String LIST_MEAL = "/meals.jsp";
    private Dao<Meal> mealDao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        mealDao = new InMemoryMealDao();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String forward = LIST_MEAL;
        String action = req.getParameter("action");
        action = (action == null || action.isEmpty()) ? "list" : action;
        log.debug("mealServlet doGet. action={}, id={}", action, req.getParameter("id"));
        int id;
        switch (action) {
            case "delete":
                id = Integer.parseInt(req.getParameter("id"));
                mealDao.delete(id);
                break;
            case "edit":
                id = Integer.parseInt(req.getParameter("id"));
                forward = UPDATE_MEAL;
                Meal meal = mealDao.getById(id);
                req.setAttribute("meal", meal);
                break;
            case "add":
                forward = UPDATE_MEAL;
                break;
            default:
                req.setAttribute("mealsList", MealsUtil.filteredByStreams(mealDao.getAll(), LocalTime.MIN, LocalTime.MAX, 2000));
                break;
        }
        if ("delete".equals(action)) {
            log.debug("mealServlet doGet. Redirect to meals");
            resp.sendRedirect("meals");
        } else {
            log.debug("mealServlet doGet. Forward to {}", forward);
            req.getRequestDispatcher(forward).forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String idStr = req.getParameter("id");
        Integer id = (idStr == null || idStr.isEmpty()) ? null : Integer.parseInt(idStr);

        String datetimeStr = req.getParameter("datetime");
        LocalDateTime dateTime = DateUtil.parseDateStr(datetimeStr);

        String descriptionStr = req.getParameter("description");
        String caloriesStr = req.getParameter("calories");
        int calories = Integer.parseInt(caloriesStr);

        log.debug("mealServlet doPost. idStr={}, datetimeStr={}, descriptionStr={}, caloriesStr={}", idStr, datetimeStr, descriptionStr, caloriesStr);

        Meal meal = new Meal(id, dateTime, descriptionStr, calories);
        if (id == null) {
            mealDao.add(meal);
        } else {
            mealDao.update(meal);
        }
        log.debug("mealServlet doPost. Redirect to /meals");
        resp.sendRedirect("meals");
    }
}
