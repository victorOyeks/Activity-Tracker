package com.example.activityTracker.repository;

import com.example.activityTracker.entity.Task;
import com.example.activityTracker.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository <Task, Long> {
    @Query(value = "select * from task t where t.user_id = ?1", nativeQuery = true)
    List<Task> findTaskByUserId(Long userId);

    @Query(value = "select * from task t where t.status = 0", nativeQuery = true)
    List<Task> findAllPending(Long userId, Status status);

    @Query(value = "select * from task t where t.status = 1", nativeQuery = true)
    List<Task> findAllInProgress(Long userId, Status status);

    @Query(value = "select * from task t where t.status = 2", nativeQuery = true)
    List<Task> findAllCompleted(Long userId, Status status);
}
