package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.repository.inmemory.InMemoryUserRepository;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

import static org.slf4j.LoggerFactory.getLogger;
// HW2
public class UserServlet extends HttpServlet {
    private static final Logger log = getLogger(UserServlet.class);

    private UserRepository repository;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        repository = new InMemoryUserRepository();
    }

/*
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("forward to users");
        request.getRequestDispatcher("/users.jsp").forward(request, response);
    }
}
*/


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");

        User user = new User(id.isEmpty() ? null : Integer.valueOf(id),
                request.getParameter("name"),
                request.getParameter("email"),
                request.getParameter("password"),
                //               Integer.parseInt(request.getParameter("caloriesPerDay")),
  //              request.getParameter("enabled"),
                Role.USER, Role.ADMIN);


        log.info(user.isNew() ? "Create {}" : "Update {}", user);
        repository.save(user);
        response.sendRedirect("users");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action == null ? "all" : action) {
            case "delete":
                int id = getId(request);
                log.info("Delete {}", id);
                repository.delete(id);
                response.sendRedirect("users");
                break;
            case "create":
            case "update":
                final User user = "create".equals(action) ?
                        new User(null,
                                "",
                                "","",
                                Role.USER, Role.ADMIN):
                        repository.get(getId(request));
                request.setAttribute("user", user);
                request.getRequestDispatcher("/userForm.jsp").forward(request, response);
                break;
            case "all":
            default:
               // log.info("getAll");
               // request.setAttribute("meals",
               //         MealsUtil.getTos(repository.getAll(), MealsUtil.DEFAULT_CALORIES_PER_DAY));
                request.getRequestDispatcher("/users.jsp").forward(request, response);
                break;
        }


    }
    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}
