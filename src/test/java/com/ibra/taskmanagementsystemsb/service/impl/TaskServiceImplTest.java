package com.ibra.taskmanagementsystemsb.service.impl;

import static org.junit.jupiter.api.Assertions.*;


import com.ibra.taskmanagementsystemsb.dtos.TaskDTO;
import com.ibra.taskmanagementsystemsb.entity.Task;
import com.ibra.taskmanagementsystemsb.enums.TaskPriority;
import com.ibra.taskmanagementsystemsb.enums.TaskStatus;
import com.ibra.taskmanagementsystemsb.exceptions.ResourceNotFoundException;
import com.ibra.taskmanagementsystemsb.mapper.EntityMapper;
import com.ibra.taskmanagementsystemsb.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private EntityMapper entityMapper;

    @InjectMocks
    private TaskServiceImpl taskService;

    private Task sampleTask;
    private TaskDTO sampleTaskDTO;
    private List<Task> taskList;
    private List<TaskDTO> taskDTOList;

    @BeforeEach
    void setUp() {
        sampleTask = new Task();
        sampleTask.setId(1L);
        sampleTask.setTitle("Sample Task");
        sampleTask.setDescription("Sample Description");
        sampleTask.setStatus(TaskStatus.PENDING);
        sampleTask.setPriority(TaskPriority.HIGH);
        sampleTask.setAssignee("John Doe");
        sampleTask.setCreatedAt(LocalDateTime.now());
        sampleTask.setDueDate(LocalDateTime.now().plusDays(7));

        sampleTaskDTO = new TaskDTO();
        sampleTaskDTO.setId(1L);
        sampleTaskDTO.setTitle("Sample Task");
        sampleTaskDTO.setDescription("Sample Description");
        sampleTaskDTO.setStatus(TaskStatus.PENDING);
        sampleTaskDTO.setPriority(TaskPriority.HIGH);
        sampleTaskDTO.setAssignee("John Doe");

        Task task2 = new Task();
        task2.setId(2L);
        task2.setTitle("Another Task");

        TaskDTO taskDTO2 = new TaskDTO();
        taskDTO2.setId(2L);
        taskDTO2.setTitle("Another Task");

        taskList = Arrays.asList(sampleTask, task2);
        taskDTOList = Arrays.asList(sampleTaskDTO, taskDTO2);
    }

    @Test
    void getAllTasks_ShouldReturnAllTasks() {
        // Given
        when(taskRepository.findAll()).thenReturn(taskList);
        when(entityMapper.mapTaskToTaskDTO(any(Task.class)))
                .thenReturn(sampleTaskDTO)
                .thenReturn(taskDTOList.get(1));

        // When
        List<TaskDTO> result = taskService.getAllTasks();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Sample Task", result.get(0).getTitle());
        verify(taskRepository, times(1)).findAll();
        verify(entityMapper, times(2)).mapTaskToTaskDTO(any(Task.class));
    }

    @Test
    void getTaskById_WhenTaskExists_ShouldReturnTask() {
        // Given
        when(taskRepository.findById(1L)).thenReturn(Optional.of(sampleTask));
        when(entityMapper.mapTaskToTaskDTO(sampleTask)).thenReturn(sampleTaskDTO);

        // When
        TaskDTO result = taskService.getTaskById(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Sample Task", result.getTitle());
        verify(taskRepository, times(1)).findById(1L);
        verify(entityMapper, times(1)).mapTaskToTaskDTO(sampleTask);
    }

    @Test
    void getTaskById_WhenTaskNotFound_ShouldThrowException() {
        // Given
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> taskService.getTaskById(99L)
        );
        assertEquals("Task not found", exception.getMessage());
        verify(taskRepository, times(1)).findById(99L);
        verify(entityMapper, never()).mapTaskToTaskDTO(any());
    }

    @Test
    void createTask_ShouldCreateAndReturnTask() {
        // Given
        when(entityMapper.mapTaskDTOToTask(sampleTaskDTO)).thenReturn(sampleTask);
        when(taskRepository.save(sampleTask)).thenReturn(sampleTask);
        when(entityMapper.mapTaskToTaskDTO(sampleTask)).thenReturn(sampleTaskDTO);

        // When
        TaskDTO result = taskService.createTask(sampleTaskDTO);

        // Then
        assertNotNull(result);
        assertEquals("Sample Task", result.getTitle());
        verify(entityMapper, times(1)).mapTaskDTOToTask(sampleTaskDTO);
        verify(taskRepository, times(1)).save(sampleTask);
        verify(entityMapper, times(1)).mapTaskToTaskDTO(sampleTask);
    }

    @Test
    void updateTask_WhenTaskExists_ShouldUpdateTask() {
        // Given
        TaskDTO updateDTO = new TaskDTO();
        updateDTO.setTitle("Updated Title");
        updateDTO.setDescription("Updated Description");
        updateDTO.setStatus(TaskStatus.COMPLETED);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(sampleTask));
        when(taskRepository.save(any(Task.class))).thenReturn(sampleTask);
        when(entityMapper.mapTaskToTaskDTO(any(Task.class))).thenReturn(sampleTaskDTO);

        // When
        TaskDTO result = taskService.updateTask(1L, updateDTO);

        // Then
        assertNotNull(result);
        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).save(any(Task.class));
        verify(entityMapper, times(1)).mapTaskToTaskDTO(any(Task.class));
    }

    @Test
    void updateTask_WhenTaskNotFound_ShouldThrowException() {
        // Given
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class,
                () -> taskService.updateTask(99L, sampleTaskDTO));
        verify(taskRepository, times(1)).findById(99L);
        verify(taskRepository, never()).save(any());
    }

    @Test
    void deleteTask_WhenTaskExists_ShouldDeleteTask() {
        // Given
        when(taskRepository.findById(1L)).thenReturn(Optional.of(sampleTask));
        doNothing().when(taskRepository).deleteById(1L);

        // When
        taskService.deleteTask(1L);

        // Then
        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteTask_WhenTaskNotFound_ShouldThrowException() {
        // Given
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class,
                () -> taskService.deleteTask(99L));
        verify(taskRepository, times(1)).findById(99L);
        verify(taskRepository, never()).deleteById(anyLong());
    }

    @Test
    void searchByTitle_ShouldReturnMatchingTasks() {
        // Given
        when(taskRepository.findTasksByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                "Sample", "Description")).thenReturn(Arrays.asList(sampleTask));
        when(entityMapper.mapTaskToTaskDTO(sampleTask)).thenReturn(sampleTaskDTO);

        // When
        List<TaskDTO> result = taskService.searchByTitle("Sample", "Description");

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Sample Task", result.get(0).getTitle());
        verify(taskRepository, times(1))
                .findTasksByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase("Sample", "Description");
    }

    @Test
    void dynamicFilter_WithAllParameters_ShouldReturnFilteredTasks() {
        // Given
        Page<Task> taskPage = new PageImpl<>(Arrays.asList(sampleTask));
        when(taskRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(taskPage);
        when(entityMapper.mapTaskToTaskDTO(sampleTask)).thenReturn(sampleTaskDTO);

        // When
        List<TaskDTO> result = taskService.dynamicFilter(
                "Sample", "Description", "John",
                LocalDateTime.now(), LocalDateTime.now().plusDays(7),
                TaskPriority.HIGH, TaskStatus.PENDING,
                10L, 0L, "title"
        );

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(taskRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void dynamicFilter_WithNullParameters_ShouldHandleNullValues() {
        // Given
        Page<Task> taskPage = new PageImpl<>(taskList);
        when(taskRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(taskPage);
        when(entityMapper.mapTaskToTaskDTO(any(Task.class)))
                .thenReturn(sampleTaskDTO)
                .thenReturn(taskDTOList.get(1));

        // When
        List<TaskDTO> result = taskService.dynamicFilter(
                null, null, null, null, null, null, null,
                10L, 0L, "id"
        );

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(taskRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void updateTask_WithPartialUpdate_ShouldUpdateOnlyProvidedFields() {
        // Given
        TaskDTO partialUpdate = new TaskDTO();
        partialUpdate.setTitle("New Title Only");
        // Other fields are null

        Task existingTask = new Task();
        existingTask.setId(1L);
        existingTask.setTitle("Old Title");
        existingTask.setDescription("Old Description");
        existingTask.setStatus(TaskStatus.PENDING);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(existingTask);
        when(entityMapper.mapTaskToTaskDTO(any(Task.class))).thenReturn(sampleTaskDTO);

        // When
        TaskDTO result = taskService.updateTask(1L, partialUpdate);

        // Then
        assertNotNull(result);
        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).save(any(Task.class));
        // Verify that only title was updated (other fields remain unchanged)
        assertEquals("New Title Only", existingTask.getTitle());
        assertEquals("Old Description", existingTask.getDescription());
        assertEquals(TaskStatus.PENDING, existingTask.getStatus());
    }
}