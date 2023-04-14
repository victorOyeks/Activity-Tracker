package com.example.activityTracker.controller;

import com.example.activityTracker.entity.Task;
import com.example.activityTracker.entity.User;
import com.example.activityTracker.enums.Status;
import com.example.activityTracker.exceptions.CustomUserException;
import com.example.activityTracker.repository.UserRepository;
import com.example.activityTracker.service.TaskService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import java.util.List;

@Controller
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/task")
    public ModelAndView task(HttpSession httpSession, User user) {
        ModelAndView mav = new ModelAndView("home");
        Long userId = (Long) httpSession.getAttribute("userId");
        List<Task> tasks = taskService.findTaskByUserId(userId);
        mav.addObject("task", tasks);
        return mav;
    }

    @GetMapping("/all_task")
    public String allTask(HttpSession httpSession) {
        Long userId = (Long) httpSession.getAttribute("userId");
        List<Task> tasks = taskService.findTaskByUserId(userId);
        httpSession.setAttribute("task", tasks);
        return "redirect:/task";
    }


    @GetMapping("/pending_task")
    public ModelAndView pending(HttpSession httpSession) {
        ModelAndView mav = new ModelAndView("home");
        Long userId = (Long) httpSession.getAttribute("userId");
        List<Task> task = taskService.findAllPending(userId, Status.PENDING);
        mav.addObject("task", task);
        return mav;
    }

    @GetMapping("/in_progress_task")
    public ModelAndView inProgress(HttpSession httpSession) {
        ModelAndView mav = new ModelAndView("home");
        Long userId = (Long) httpSession.getAttribute("userId");
        List<Task> task = taskService.findInProgress(userId, Status.IN_PROGRESS);
        mav.addObject("task", task);
        return mav;
    }

    @GetMapping("/completed_task")
    public ModelAndView completed(HttpSession httpSession) {
        ModelAndView mav = new ModelAndView("home");
        Long userId = (Long) httpSession.getAttribute("userId");
        List<Task> task = taskService.findAllCompleted(userId, Status.COMPLETED);
        mav.addObject("task", task);
        return mav;
    }

    @GetMapping("/new_task")
    public ModelAndView showTaskForm() {
        ModelAndView mav = new ModelAndView("new-task");
        mav.addObject("task", new Task());
        return mav;
    }

    @PostMapping("/task/add")
    public String saveTask(@ModelAttribute("task")Task task, BindingResult bindingResult,
                        HttpServletRequest httpServletRequest){
        if(bindingResult.hasErrors()) {
            return "new-task";
        }
        User user = new User();
        taskService.saveTask(task, user, httpServletRequest);
        return "redirect:/all_task";
    }

    @GetMapping("/task/edit/{id}")
    public ModelAndView showEditForm(@PathVariable(name = "id") Long id) {
        ModelAndView mav = new ModelAndView("edit-activity");
        Task task = taskService.findById(id);
        mav.addObject("task", task);
        return mav;
    }


    @PostMapping("/task/update/{id}")
    public String updateTask(@PathVariable(name = "id") Long id, @ModelAttribute("task") Task task, User user, HttpServletRequest httpServletRequest) throws CustomUserException {
        Task existingTask = taskService.findById(id);
        existingTask.setTitle(task.getTitle());
        existingTask.setDescription(task.getDescription());
        existingTask.setStatus(Status.IN_PROGRESS);
        taskService.saveTask(existingTask, user, httpServletRequest);
        return "redirect:/task";
    }

    @GetMapping("/task/delete/{id}")
    public String deleteTask(@PathVariable(name = "id") Long taskId, HttpSession httpSession) {
        Long userId = (Long) httpSession.getAttribute("userId");
        taskService.delete(taskId);
        List<Task> tasks = taskService.findTaskByUserId(userId);
        httpSession.setAttribute("task", tasks);
        return "redirect:/task";
    }


    @PostMapping("/task/complete/{id}")
    public String completeTask(@PathVariable(name = "id") Long id, User user, HttpServletRequest httpServletRequest){
        Task task = taskService.findById(id);
        task.setStatus(Status.COMPLETED);
        taskService.saveTask(task, user, httpServletRequest);
        return "redirect:/task";
    }

    @GetMapping("/task/move_task/{taskId}")
    public String moveTask(HttpSession httpSession, @PathVariable Long taskId, Model model) {
        Long userId = (Long) httpSession.getAttribute("userId");
        try {
            taskService.moveTask(taskId);
        } catch (CustomUserException ex) {
            model.addAttribute("errorMessage", "Cannot be moved further");
        }
        List<Task> tasks = taskService.findTaskByUserId(userId);
        httpSession.setAttribute("task", tasks);
        return "redirect:/task";
    }



    @GetMapping("/task/move_back/{taskId}")
    public String moveTaskBack(HttpSession httpSession, @PathVariable Long taskId, Model model) throws CustomUserException{
        Long userId = (Long) httpSession.getAttribute("userId");
        try {
            taskService.moveTaskBack(taskId);
        } catch (CustomUserException ex) {
            model.addAttribute("errorMessage", "Cannot be moved further");
        }
        List<Task> tasks = taskService.findTaskByUserId(userId);
        httpSession.setAttribute("task", tasks);
        return "redirect:/task";
    }
}
