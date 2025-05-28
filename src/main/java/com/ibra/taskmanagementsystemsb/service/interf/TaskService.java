package com.ibra.taskmanagementsystemsb.service.interf;


import com.ibra.taskmanagementsystemsb.dtos.TaskDTO;
import com.ibra.taskmanagementsystemsb.exceptions.NotFoundException;

import java.util.List;

public interface TaskService {
    List<TaskDTO> getAllTasks();
    TaskDTO getTaskById(Long taskId) throws NotFoundException;
    TaskDTO createTask(TaskDTO taskDTO);
    TaskDTO updateTask(Long taskId, TaskDTO taskDTO) throws NotFoundException;
    void deleteTask(Long taskId) throws NotFoundException;
    List<TaskDTO> searchTaskByName(Long userId);
}
