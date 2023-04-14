package com.example.activityTracker.controller;

import com.example.activityTracker.DTO.UserDTO;
import com.example.activityTracker.entity.Task;
import com.example.activityTracker.entity.User;
import com.example.activityTracker.service.TaskService;
import com.example.activityTracker.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import java.util.ArrayList;
import java.util.List;



@Controller
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    private final TaskService taskService;

    @GetMapping("/")
    public String showIndexPage() {
        return "login";
    }

    @GetMapping("/user/new")
    public String showNewForm(Model model) {
        model.addAttribute("user", new UserDTO());
        return "register";
    }

    @PostMapping("/user/new")
    public String saveUser(@ModelAttribute("user") UserDTO userDTO) {
        userService.save(userDTO);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public ModelAndView login() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        modelAndView.addObject("user", new UserDTO());
        return modelAndView;
    }


    @PostMapping("/login")
    public String loginUser(@ModelAttribute("user") UserDTO userDTO, HttpServletRequest httpServletRequest, Model model, Task task) {
        User user = userService.findUser(userDTO);
        if (user == null || !user.getPassword().equals(userDTO.getPassword())) {
            model.addAttribute("errorMessage", "Invalid email or password.");
            return "login";
        }
        HttpSession session = httpServletRequest.getSession();
        Long id = userDTO.getUserId();
        log.info("Confirm userId is not null: {}", id);
        session.setAttribute("userId", user.getUserId());
        System.out.println(user.getUserId());
        List<Task> taskList = taskService.findTaskByUserId(userDTO.getUserId());
        if (taskList == null) {
            taskList = new ArrayList<>();
        }
        model.addAttribute("task", taskList);
        return "redirect:/task";
    }


    @GetMapping("/user/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("userId");
        return "redirect:/login";
    }
}
