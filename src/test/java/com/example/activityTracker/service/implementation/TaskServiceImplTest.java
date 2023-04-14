package com.example.activityTracker.service.implementation;

import com.example.activityTracker.entity.Task;
import com.example.activityTracker.enums.Status;
import com.example.activityTracker.repository.TaskRepository;
import com.example.activityTracker.service.TaskService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDateTime;

@SpringBootTest
public class TaskServiceImplTest {
    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @Autowired
    public TaskServiceImplTest(TaskRepository taskRepository, TaskService taskService) {
        this.taskRepository = taskRepository;
        this.taskService = taskService;
    }

    @Test
    public void testSaveTask(){
        Task task = new Task();
        task.setTitle("Daily StandUps");
        task.setDescription("I will  wake up to daily standUps");
        task.setStatus(Status.PENDING);
        task.setStatus(Status.IN_PROGRESS);
        task.setStatus(Status.COMPLETED);
    }
}