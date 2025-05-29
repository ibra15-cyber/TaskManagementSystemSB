package com.ibra.taskmanagementsystemsb.controller;

import com.ibra.taskmanagementsystemsb.dtos.Response;
import com.ibra.taskmanagementsystemsb.dtos.TaskDTO;
import com.ibra.taskmanagementsystemsb.enums.TaskPriority;
import com.ibra.taskmanagementsystemsb.enums.TaskStatus;
import com.ibra.taskmanagementsystemsb.exceptions.ResourceNotFoundException;
import com.ibra.taskmanagementsystemsb.service.interf.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class TaskControllerTest {

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    private TaskDTO taskDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        taskDTO = new TaskDTO();
        taskDTO.setId(1L);
        taskDTO.setTitle("Test Task");
        taskDTO.setDescription("Test Description");
        taskDTO.setPriority(TaskPriority.HIGH);
        taskDTO.setStatus(TaskStatus.PENDING);
        taskDTO.setAssignee("test@example.com");
        taskDTO.setDueDate(LocalDateTime.now().plusDays(1));
    }

    @Test
    void testGetAllTasks() {
        List<TaskDTO> tasks = Arrays.asList(taskDTO);
        when(taskService.getAllTasks()).thenReturn(tasks);

        ResponseEntity<Response> response = taskController.getAllTasks();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getTasks().size());
        verify(taskService).getAllTasks();
    }

    @Test
    void testCreateATask() {
        when(taskService.createTask(any(TaskDTO.class))).thenReturn(taskDTO);

        ResponseEntity<Response> response = taskController.createATask(taskDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Test Task", response.getBody().getTask().getTitle());
        verify(taskService).createTask(any(TaskDTO.class));
    }

    @Test
    void testGetTaskById() throws ResourceNotFoundException {
        when(taskService.getTaskById(1L)).thenReturn(taskDTO);

        ResponseEntity<Response> response = taskController.getTaskById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Test Task", response.getBody().getTask().getTitle());
        verify(taskService).getTaskById(1L);
    }

    @Test
    void testGetTaskByIdNotFound() throws ResourceNotFoundException {
        when(taskService.getTaskById(999L)).thenThrow(new ResourceNotFoundException("Task not found"));

        assertThrows(ResourceNotFoundException.class, () -> {
            taskController.getTaskById(999L);
        });
        verify(taskService).getTaskById(999L);
    }

    @Test
    void testUpdateTask() throws ResourceNotFoundException {
        when(taskService.updateTask(eq(1L), any(TaskDTO.class))).thenReturn(taskDTO);

        ResponseEntity<Response> response = taskController.updateTask(1L, taskDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Test Task", response.getBody().getTask().getTitle());
        verify(taskService).updateTask(eq(1L), any(TaskDTO.class));
    }

    @Test
    void testUpdateTaskNotFound() throws ResourceNotFoundException {
        when(taskService.updateTask(eq(999L), any(TaskDTO.class)))
                .thenThrow(new ResourceNotFoundException("Task not found"));

        assertThrows(ResourceNotFoundException.class, () -> {
            taskController.updateTask(999L, taskDTO);
        });
        verify(taskService).updateTask(eq(999L), any(TaskDTO.class));
    }

    @Test
    void testDeleteTask() throws ResourceNotFoundException {
        doNothing().when(taskService).deleteTask(1L);

        ResponseEntity<Response> response = taskController.deleteTask(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Task deleted", response.getBody().getMessage());
        verify(taskService).deleteTask(1L);
    }

    @Test
    void testDeleteTaskNotFound() throws ResourceNotFoundException {
        doThrow(new ResourceNotFoundException("Task not found")).when(taskService).deleteTask(999L);

        assertThrows(ResourceNotFoundException.class, () -> {
            taskController.deleteTask(999L);
        });
        verify(taskService).deleteTask(999L);
    }

    @Test
    void testGetByName() {
        List<TaskDTO> tasks = Arrays.asList(taskDTO);
        when(taskService.searchByTitle("Test", "Description")).thenReturn(tasks);

        ResponseEntity<Response> response = taskController.getByName("Test", "Description");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getTasks().size());
        assertEquals("Tasks retrieved successfully", response.getBody().getMessage());
        verify(taskService).searchByTitle("Test", "Description");
    }

    @Test
    void testFilterTask() {
        List<TaskDTO> tasks = Arrays.asList(taskDTO);
        when(taskService.dynamicFilter(
                anyString(), anyString(), anyString(), any(), any(),
                any(TaskPriority.class), any(TaskStatus.class),
                anyLong(), anyLong(), anyString())).thenReturn(tasks);

        ResponseEntity<Response> response = taskController.filterTask(
                "Test", "Description", "assignee",
                LocalDateTime.now(), LocalDateTime.now().plusDays(1),
                TaskPriority.HIGH, TaskStatus.PENDING,
                0L, 10L, "id"
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getTasks().size());
        assertEquals("Tasks retrieved successfully", response.getBody().getMessage());
        verify(taskService).dynamicFilter(
                anyString(), anyString(), anyString(), any(), any(),
                any(TaskPriority.class), any(TaskStatus.class),
                anyLong(), anyLong(), anyString());
    }
}