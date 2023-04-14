package com.example.activityTracker.service;

import com.example.activityTracker.DTO.UserDTO;
import com.example.activityTracker.entity.Task;
import com.example.activityTracker.entity.User;
import com.example.activityTracker.enums.Status;
import com.example.activityTracker.exceptions.CustomUserException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TaskService {

    List<Task> findTaskByUserId(Long userId);

    Task findById(Long id);

    void saveTask(Task task, User user, HttpServletRequest httpServletRequest);

    void update(Task task);

    void delete(Long id);

    void moveTask(Long taskId) throws CustomUserException;

    void moveTaskBack(Long taskId) throws CustomUserException;


    List<Task> findAllPending(Long userId, Status status);

    List<Task> findInProgress(Long userId, Status status);

    List<Task> findAllCompleted(Long userId, Status status);

    public Page<Task> findPaginated(List<Task> tasks, Pageable pageable);

}
