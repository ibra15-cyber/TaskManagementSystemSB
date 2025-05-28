package com.ibra.taskmanagementsystemsb.controller;

import com.ibra.taskmanagementsystemsb.dtos.Response;
import com.ibra.taskmanagementsystemsb.dtos.TaskDTO;
import com.ibra.taskmanagementsystemsb.exceptions.NotFoundException;
import com.ibra.taskmanagementsystemsb.service.interf.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/task")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<Response> getTasks() {
         List<TaskDTO> taskDTOS = taskService.getAllTasks();
         Response response = Response.builder()
                 .tasks(taskDTOS)
                 .build();
         return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping
    public ResponseEntity<Response> createTask(@RequestBody TaskDTO taskDTO) {
        TaskDTO createdTaskDTO = taskService.createTask(taskDTO);
        Response response = Response.builder()
                .task(createdTaskDTO)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getTaskById(@PathVariable("id") Long id) throws NotFoundException {
        TaskDTO taskDTO = taskService.getTaskById(id);
        Response response = Response.builder()
                .task(taskDTO)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> updateTask(@PathVariable("id") Long id, @RequestBody TaskDTO taskDTO) throws NotFoundException {
        TaskDTO updatedTask =  taskService.updateTask(id, taskDTO);
        Response response = Response.builder()
                .status(String.valueOf(HttpStatus.NO_CONTENT))
                .task(updatedTask)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteTask(@PathVariable("id") Long id) throws NotFoundException {
        taskService.deleteTask(id);
        Response response = Response.builder()
                .status(String.valueOf(HttpStatus.NO_CONTENT))
                .message("Task deleted")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


}
