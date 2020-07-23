package ru.javawebinar.topjava.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.service.UserService;
import ru.javawebinar.topjava.web.SecurityUtil;

import javax.servlet.http.HttpServletRequest;

@Controller
public class JspMealController extends AbstractMealController {
 //  @Autowired
  //  private MealService service;
  //  private UserService userServiceservice;

    @GetMapping("/")
    public String root() {
        return "index";
    }

    @GetMapping("/meals")
    public String getAll(Model model) {
      //  model.addAttribute("meals", service.getAll(userId));
        return "meals";
    }
/*
    @PostMapping("/users")
    public String setUser(HttpServletRequest request) {
        int userId = Integer.parseInt(request.getParameter("userId"));
        SecurityUtil.setAuthUserId(userId);
        return "redirect:meals";
    }

 */
}
