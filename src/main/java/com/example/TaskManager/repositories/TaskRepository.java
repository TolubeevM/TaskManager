package com.example.TaskManager.repositories;

import com.example.TaskManager.models.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAll();
    @Query("SELECT t FROM Task t WHERE t.author = :author")
    Page<Task> findByAuthor(@Param("author") String author, Pageable pageable);

    @Query("SELECT t FROM Task t JOIN t.executors e WHERE e.id = :executorId")
    Page<Task> findByExecutor(@Param("executorId") Long executorId, Pageable pageable);
    @Query("SELECT t FROM Task t LEFT JOIN t.executors e WHERE t.author = :author OR e.username = :executorUsername")
    Page<Task> findByAuthorOrExecutor(@Param("author") String author, @Param("executorUsername") String executorUsername, Pageable pageable);

}