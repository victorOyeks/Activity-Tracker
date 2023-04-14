package com.example.activityTracker.service.implementation;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.example.activityTracker.entity.User;
import com.example.activityTracker.enums.Status;
import com.example.activityTracker.exceptions.CustomUserException;
import com.example.activityTracker.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.example.activityTracker.entity.Task;
import com.example.activityTracker.repository.TaskRepository;
import com.example.activityTracker.service.TaskService;


@Service
@Slf4j
public class TaskServiceImpl implements TaskService {

    private TaskRepository taskRepository;

    private UserRepository userRepository;


    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Task> findTaskByUserId(Long userId) {
        return taskRepository.findTaskByUserId(userId);
    }

    @Override
    public Task findById(Long id) {
        return taskRepository.findById(id).orElse(null);
    }


    @Override
    public void saveTask(Task task, User user, HttpServletRequest httpServletRequest) {
        user.setUserId(user.getUserId());
        user.setEmail(user.getEmail());
        HttpSession session = httpServletRequest.getSession();
        Optional<User> userOptional = userRepository.findById(Long.valueOf(session.getAttribute("userId").toString()));
        userOptional.ifPresent(task::setUser);
        task.setStatus(Status.PENDING);
        taskRepository.save(task);
    }

    @Override
    public void update(Task task) {
        task.setUpdatedAt(LocalDateTime.now());
        taskRepository.save(task);
    }

    @Override
    public void delete(Long id) {
        Task task = findById(id);
        if (task != null) {
            taskRepository.delete(task);
        }
    }

    @Override
    @Transactional
    public void moveTask(Long taskId) throws CustomUserException {
        Task task = taskRepository.findById(taskId).orElseThrow(() ->
                new CustomUserException("Task with id " + taskId + " not found"));

        switch (task.getStatus()) {
            case PENDING:
                task.setStatus(Status.IN_PROGRESS);
                break;
            case IN_PROGRESS:
                task.setStatus(Status.COMPLETED);
                break;
            case COMPLETED:
                throw new CustomUserException("Task has already been completed and cannot be moved further.");
            default:
                throw new CustomUserException("Invalid task status. Cannot be moved further.");
        }

        task.setUpdatedAt(LocalDateTime.now());
    }



    @Override
    @Transactional
    public void moveTaskBack(Long taskId) throws CustomUserException {
        Task task = taskRepository.findById(taskId).orElseThrow(() ->
                new CustomUserException("Task with id " + taskId + " not found"));

        switch (task.getStatus()) {
            case COMPLETED:
                task.setStatus(Status.IN_PROGRESS);
                break;
            case IN_PROGRESS:
                task.setStatus(Status.PENDING);
                break;
            case PENDING:
                throw new CustomUserException("Task has already been completed and cannot be moved further.");
            default:
                throw new CustomUserException("Invalid task status.");
        }

        task.setUpdatedAt(LocalDateTime.now());
    }

    @Override
    public List<Task> findAllPending(Long userId, Status status) {
        return taskRepository.findAllPending(userId, status);
    }

    @Override
    public List<Task> findInProgress(Long userId, Status status) {
        return taskRepository.findAllInProgress(userId, status);
    }

    @Override
    public List<Task> findAllCompleted(Long userId, Status status) {
        return taskRepository.findAllCompleted(userId, status);
    }

    public Page<Task> findPaginated(List<Task> tasks, Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Task> list;

        if (tasks.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, tasks.size());
            list = tasks.subList(startItem, toIndex);
        }

        return new PageImpl<Task>(list, PageRequest.of(currentPage, pageSize), tasks.size());
    }

}
