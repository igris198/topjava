package ru.javawebinar.topjava.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import ru.javawebinar.topjava.DAO.MealInMemoryDAO;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.DateUtil;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);

    private static final String UPDATE_MEAL = "/mealUpdate.jsp";
    private static final String LIST_MEAL = "/meals.jsp";
    private final MealInMemoryDAO mealInMemoryDAO = new MealInMemoryDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String forward;
        String action = req.getParameter("action");
        action = (action == null || action.isEmpty()) ? "list" : action;
        log.debug("mealServlet doGet. action={}, id={}", action, req.getParameter("id") == null ? "null" : req.getParameter("id"));
        int id;
        switch (action) {
            case "delete":
                id = Integer.parseInt(req.getParameter("id"));
                forward = LIST_MEAL;
                mealInMemoryDAO.delete(id);
                req.setAttribute("mealsList", mealInMemoryDAO.getAllMealTo());
                break;
            case "edit":
                id = Integer.parseInt(req.getParameter("id"));
                forward = UPDATE_MEAL;
                Meal meal = mealInMemoryDAO.getById(id);
                req.setAttribute("meal", meal);
                break;
            case "add":
                forward = UPDATE_MEAL;
                break;
            default:
                forward = LIST_MEAL;
                req.setAttribute("mealsList", mealInMemoryDAO.getAllMealTo());
                break;
        }
        log.debug("mealServlet doGet. Forward to {}", forward);
        req.getRequestDispatcher(forward).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idStr = req.getParameter("id");
        Integer id = (idStr == null || idStr.isEmpty()) ? null : Integer.parseInt(idStr);

        String datetimeStr = req.getParameter("datetime");
        LocalDateTime dateTime = DateUtil.parseDateStr(datetimeStr);

        String descriptionStr = req.getParameter("description");
        String caloriesStr = req.getParameter("calories");
        int calories = (caloriesStr == null || caloriesStr.isEmpty()) ? 0 : Integer.parseInt(caloriesStr);

        log.debug("mealServlet doPost. idStr={}, datetimeStr={}, descriptionStr={}, caloriesStr={}", idStr, datetimeStr, descriptionStr, caloriesStr);

        Meal meal = new Meal(id, dateTime, descriptionStr, calories);
        if (id == null) {
            mealInMemoryDAO.add(meal);
        } else {
            mealInMemoryDAO.update(meal);
        }
        log.debug("mealServlet doPost. Forward to {}", LIST_MEAL);
        req.setAttribute("mealsList", mealInMemoryDAO.getAllMealTo());
        req.getRequestDispatcher(LIST_MEAL).forward(req, resp);
    }
}
