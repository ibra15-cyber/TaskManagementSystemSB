package com.ibra.taskmanagementsystemsb.service.impl;


import com.ibra.taskmanagementsystemsb.dtos.TaskDTO;
import com.ibra.taskmanagementsystemsb.entity.Task;
import com.ibra.taskmanagementsystemsb.exceptions.NotFoundException;
import com.ibra.taskmanagementsystemsb.mapper.EntityMapper;
import com.ibra.taskmanagementsystemsb.repository.TaskRepository;
import com.ibra.taskmanagementsystemsb.service.interf.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final EntityMapper entityMapper;

    public TaskServiceImpl(TaskRepository taskRepository, EntityMapper entityMapper) {
        this.taskRepository = taskRepository;
        this.entityMapper = entityMapper;
    }

    @Override
    public List<TaskDTO> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();

        return tasks.stream()
                .map(entityMapper::mapTaskToTaskDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TaskDTO getTaskById(Long taskId) throws NotFoundException {
        Task task = taskRepository.findById(taskId).orElseThrow(()-> new NotFoundException("Task not found"));
        return entityMapper.mapTaskToTaskDTO(task);
    }

    @Override
    public TaskDTO createTask(TaskDTO taskDTO) {
        Task task = entityMapper.mapTaskDTOToTask(taskDTO);
        task = taskRepository.save(task);
        return entityMapper.mapTaskToTaskDTO(task);
    }

    @Override
    public TaskDTO updateTask(Long taskId, TaskDTO taskDTO) throws NotFoundException {
        System.out.println(taskDTO);
        Task taskToUpdate = taskRepository.findById(taskId).orElseThrow(()-> new NotFoundException("Task not found"));
        log.debug(                "found tasks.............", taskToUpdate);
        System.out.println(taskToUpdate);
        System.out.println(taskDTO.getAssignee());
        if (taskDTO.getTitle() != null) taskToUpdate.setTitle(taskDTO.getTitle());
        if (taskDTO.getDescription() != null) taskToUpdate.setDescription(taskDTO.getDescription());
        if (taskDTO.getDueDate() != null) taskToUpdate.setDueDate(taskDTO.getDueDate());
        if (taskDTO.getPriority() != null) taskToUpdate.setPriority(taskDTO.getPriority());
        if (taskDTO.getStatus() != null) taskToUpdate.setStatus(taskDTO.getStatus());
        if (taskDTO.getAssignee() != null) taskToUpdate.setAssignee(taskDTO.getAssignee());
        taskRepository.save(taskToUpdate);
        TaskDTO taskDTO1 =  entityMapper.mapTaskToTaskDTO(taskToUpdate);
        System.out.println(taskDTO1);
        return taskDTO1;
    }

    @Override
    public void deleteTask(Long taskId) throws NotFoundException {
        Task task = taskRepository.findById(taskId).orElseThrow(()-> new NotFoundException("Task not found"));
        taskRepository.deleteById(task.getId());
    }

    @Override
    public List<TaskDTO> searchTaskByName(Long userId) {
        return List.of();
    }
}
