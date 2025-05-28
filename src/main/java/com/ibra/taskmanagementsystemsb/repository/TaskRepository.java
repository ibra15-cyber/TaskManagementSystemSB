package com.ibra.taskmanagementsystemsb.repository;

import com.ibra.taskmanagementsystemsb.entity.Task;
import com.ibra.taskmanagementsystemsb.enums.TaskPriority;
import com.ibra.taskmanagementsystemsb.enums.TaskStatus;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {
    List<Task> findTasksByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String title, String description);
    List<Task> findByAssigneeAndPriority(String assignee, TaskPriority priority);
}
