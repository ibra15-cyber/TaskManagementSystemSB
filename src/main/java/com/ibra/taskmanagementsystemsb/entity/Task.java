package com.ibra.taskmanagementsystemsb.entity;


import com.ibra.taskmanagementsystemsb.enums.TaskPriority;
import com.ibra.taskmanagementsystemsb.enums.TaskStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Setter
@Getter
@Entity(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime dueDate;
    private TaskStatus status;
    private TaskPriority priority;
    private String assignee;

    public Task() {
        this.status = TaskStatus.PENDING;
        this.priority = TaskPriority.MEDIUM;
    }

    public Task(String title, String description, LocalDateTime dueDate) {
        this();
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
    }

    public Task(long l, String s, String s1, TaskStatus status, TaskPriority priority, String assignee, LocalDate now) {}

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", createdAt=" + createdAt +
                ", dueDate=" + dueDate +
                ", status=" + status +
                ", priority=" + priority +
                ", assignee='" + assignee + '\'' +
                '}';
    }
}
