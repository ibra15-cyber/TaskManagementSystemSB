package com.ibra.taskmanagementsystemsb.service.impl;


import com.ibra.taskmanagementsystemsb.dtos.TaskDTO;
import com.ibra.taskmanagementsystemsb.entity.Task;
import com.ibra.taskmanagementsystemsb.enums.TaskPriority;
import com.ibra.taskmanagementsystemsb.enums.TaskStatus;
import com.ibra.taskmanagementsystemsb.exceptions.ResourceNotFoundException;
import com.ibra.taskmanagementsystemsb.mapper.EntityMapper;
import com.ibra.taskmanagementsystemsb.repository.TaskRepository;
import com.ibra.taskmanagementsystemsb.service.interf.TaskService;
import com.ibra.taskmanagementsystemsb.specification.TaskSpecification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    public TaskDTO getTaskById(Long taskId) throws ResourceNotFoundException {
        Task task = taskRepository.findById(taskId).orElseThrow(()-> new ResourceNotFoundException("Task not found"));
        return entityMapper.mapTaskToTaskDTO(task);
    }

    @Override
    public TaskDTO createTask(TaskDTO taskDTO) {
        Task task = entityMapper.mapTaskDTOToTask(taskDTO);
        task = taskRepository.save(task);
        return entityMapper.mapTaskToTaskDTO(task);
    }

    @Override
    public TaskDTO updateTask(Long taskId, TaskDTO taskDTO) throws ResourceNotFoundException {
        System.out.println(taskDTO);
        Task taskToUpdate = taskRepository.findById(taskId).orElseThrow(()-> new ResourceNotFoundException("Task not found"));
        log.debug("found tasks.............", taskToUpdate);
        System.out.println(taskToUpdate);

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
    public void deleteTask(Long taskId) throws ResourceNotFoundException {
        Task task = taskRepository.findById(taskId).orElseThrow(()-> new ResourceNotFoundException("Task not found"));
        taskRepository.deleteById(task.getId());
    }

    @Override
    public List<TaskDTO> searchByTitle(String title, String description) {
        List<Task> tasks = taskRepository.findTasksByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(title, description);
        return tasks.stream()
                .map(entityMapper::mapTaskToTaskDTO)
                .toList();

    }

    @Override
    public List<TaskDTO> dynamicFilter(String title, String description, String assignee, LocalDateTime createdAt, LocalDateTime dueDate, TaskPriority priority, TaskStatus status, Long pageSize, Long pageNumber, String sortBy){
        log.debug("Received TaskFilterRequest: {}", title);
        Pageable pageable = PageRequest.of(Math.toIntExact(pageNumber), Math.toIntExact(pageSize),
                Sort.by(Sort.Direction.DESC, sortBy));

        Specification<Task> spec = Specification.allOf();

        spec = spec.and(TaskSpecification.hasTitle(title));
        spec = spec.and(TaskSpecification.hasDescription(description));
        spec = spec.and(TaskSpecification.createdAt(createdAt));
        spec = spec.and(TaskSpecification.dueBefore(dueDate));
        spec = spec.and(TaskSpecification.hasStatus(status));
        spec = spec.and(TaskSpecification.hasPriority(priority));
        spec = spec.and(TaskSpecification.hasAssignee(assignee));

        log.debug("Executing dynamic filter with specification: {}", spec);
        Page<Task> tasks = taskRepository.findAll(spec, pageable);
        log.debug("Found {} tasks after dynamic filter.", tasks.getTotalElements());

        tasks.forEach(task -> log.debug("Found Task: {}", task)); // Log each found task

        return tasks.stream()
                .map(entityMapper::mapTaskToTaskDTO)
                .toList();
    }

}

