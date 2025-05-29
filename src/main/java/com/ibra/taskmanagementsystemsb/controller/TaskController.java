package com.ibra.taskmanagementsystemsb.controller;

import com.ibra.taskmanagementsystemsb.dtos.Response;
import com.ibra.taskmanagementsystemsb.dtos.TaskDTO;
import com.ibra.taskmanagementsystemsb.enums.TaskPriority;
import com.ibra.taskmanagementsystemsb.enums.TaskStatus;
import com.ibra.taskmanagementsystemsb.exceptions.ResourceNotFoundException;
import com.ibra.taskmanagementsystemsb.service.interf.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/task")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<Response> getAllTasks() {
         List<TaskDTO> taskDTOS = taskService.getAllTasks();
         Response response = Response.builder()
                 .tasks(taskDTOS)
                 .build();
         return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping
    public ResponseEntity<Response> createATask(@RequestBody TaskDTO taskDTO) {
        TaskDTO createdTaskDTO = taskService.createTask(taskDTO);
        Response response = Response.builder()
                .task(createdTaskDTO)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getTaskById(@PathVariable("id") Long id) throws ResourceNotFoundException {
        TaskDTO taskDTO = taskService.getTaskById(id);
        Response response = Response.builder()
                .task(taskDTO)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> updateTask(@PathVariable("id") Long id, @RequestBody TaskDTO taskDTO) throws ResourceNotFoundException {
        TaskDTO updatedTask =  taskService.updateTask(id, taskDTO);
        Response response = Response.builder()
                .status(String.valueOf(HttpStatus.NO_CONTENT))
                .task(updatedTask)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteTask(@PathVariable("id") Long id) throws ResourceNotFoundException {
        taskService.deleteTask(id);
        Response response = Response.builder()
                .status(String.valueOf(HttpStatus.NO_CONTENT))
                .message("Task deleted")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/search")
    public ResponseEntity<Response> getByName(@RequestParam(required = false) String title,
                                              @RequestParam(required = false) String description) {
        List<TaskDTO> taskDTOS = taskService.searchByTitle(title, description);
        Response response = Response.builder()
                .message("Tasks retrieved successfully")
                .status(String.valueOf(HttpStatus.OK))
                .tasks(taskDTOS)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/filter")
        public ResponseEntity<Response> filterTask(@RequestParam(required = false) String title,
                                                   @RequestParam(required = false) String description,
                                                   @RequestParam(required = false) String assignee,
                                                   @RequestParam(required = false) LocalDateTime createdAt,
                                                   @RequestParam(required = false) LocalDateTime dueDate,
                                                   @RequestParam(required = false) TaskPriority priority,
                                                   @RequestParam(required = false) TaskStatus status,
                                                   @RequestParam(defaultValue = "0") Long pageNumber,
                                                   @RequestParam(defaultValue = "100") Long pageSize,
                                                   @RequestParam(defaultValue = "id") String sortBy){
        List<TaskDTO> taskDTOS = taskService.dynamicFilter(title, description, assignee, createdAt, dueDate, priority, status, pageSize, pageNumber, sortBy);
        Response response = Response.builder()
                .message("Tasks retrieved successfully")
                .status(String.valueOf(HttpStatus.OK))
                .tasks(taskDTOS)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }



}
