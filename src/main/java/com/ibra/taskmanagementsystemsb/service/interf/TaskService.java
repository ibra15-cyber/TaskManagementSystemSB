package com.ibra.taskmanagementsystemsb.service.interf;


import com.ibra.taskmanagementsystemsb.dtos.TaskDTO;
import com.ibra.taskmanagementsystemsb.dtos.TaskFilterRequest;
import com.ibra.taskmanagementsystemsb.enums.TaskPriority;
import com.ibra.taskmanagementsystemsb.enums.TaskStatus;
import com.ibra.taskmanagementsystemsb.exceptions.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskService {
    List<TaskDTO> getAllTasks();
    TaskDTO getTaskById(Long taskId) throws NotFoundException;
    TaskDTO createTask(TaskDTO taskDTO);
    TaskDTO updateTask(Long taskId, TaskDTO taskDTO) throws NotFoundException;
    void deleteTask(Long taskId) throws NotFoundException;

    List<TaskDTO> searchByTitle(String title, String description);
    List<TaskDTO> searchByAssigneeAndPriority(String assignee, TaskPriority priority);

//    List<TaskDTO> dynamicFilter(TaskFilterRequest request);
    List<TaskDTO> dynamicFilter(String title, String description, String assignee, LocalDateTime createdAt, LocalDateTime dueDate, TaskPriority priority, TaskStatus status, Long pageSize, Long pageNumber, String sortBy);
}
