package com.ibra.taskmanagementsystemsb.repository;

import com.ibra.taskmanagementsystemsb.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findBy
}
