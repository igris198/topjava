package ru.javawebinar.topjava.web;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.web.meal.MealRestController;
import ru.javawebinar.topjava.web.user.AdminRestController;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;


public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);

    ConfigurableApplicationContext appCtx;
    private MealRestController controller;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init();
        appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml");

        AdminRestController adminUserController = appCtx.getBean(AdminRestController.class);
        adminUserController.create(new User(null, "adminName", "admin@mail.ru", "password", Role.ADMIN));
        adminUserController.create(new User(null, "userName", "user@mail.ru", "password", Role.USER));

        controller = appCtx.getBean(MealRestController.class);
    }

    @Override
    public void destroy() {
        super.destroy();
        appCtx.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");

        Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")),
                authUserId());

        log.info(meal.isNew() ? "Create {}" : "Update {}", meal);
        if (meal.isNew()) {
            controller.create(meal);
        } else {
            controller.update(meal, meal.getId());
        }
        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action == null ? "all" : action) {
            case "delete":
                int id = getId(request);
                log.info("Delete id={}", id);
                controller.delete(id);
                response.sendRedirect("meals");
                break;
            case "create":
            case "update":
                final Meal meal = "create".equals(action) ?
                        new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000, authUserId()) :
                        controller.get(getId(request));
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            case "all":
            default:
                String startDateStr = request.getParameter("startDate");
                String endDateStr = request.getParameter("endDate");
                String startTimeStr = request.getParameter("startTime");
                String endTimeStr = request.getParameter("endTime");
                LocalDate startDate = (startDateStr == null || startDateStr.isEmpty()) ? null : LocalDate.parse(startDateStr);
                LocalDate endDate = (endDateStr == null || endDateStr.isEmpty()) ? null : LocalDate.parse(endDateStr);
                LocalTime startTime = (startTimeStr == null || startTimeStr.isEmpty()) ? null : LocalTime.parse(startTimeStr);
                LocalTime endTime = (endTimeStr == null || endTimeStr.isEmpty()) ? null : LocalTime.parse(endTimeStr);
                log.info("doGet. getFiltered. startDateStr:{}, endDateStr:{}, startTimeStr:{}, endTimeStr:{}", startDateStr, endDateStr, startTimeStr, endTimeStr);
                request.setAttribute("meals", controller.getFiltered(startDate, endDate, startTime, endTime));
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}
