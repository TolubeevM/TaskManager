package com.example.TaskManager.repositories;
import com.example.TaskManager.models.Comment;
import com.example.TaskManager.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByTask(Task task);
    List<Comment> findAll();
}