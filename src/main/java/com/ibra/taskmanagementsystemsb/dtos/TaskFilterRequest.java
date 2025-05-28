package com.ibra.taskmanagementsystemsb.dtos;

import com.ibra.taskmanagementsystemsb.enums.TaskPriority;
import com.ibra.taskmanagementsystemsb.enums.TaskStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskFilterRequest {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private LocalDateTime createdAt;
    private LocalDateTime dueDate;
    private String assignee;

    private int pageSize;
    private int pageNumber;
    private String sortBy;
}
