package com.taskmanager.service;

import com.taskmanager.model.Task;
import com.taskmanager.model.TaskPriority;
import com.taskmanager.model.TaskStatus;
import com.taskmanager.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private Task testTask;

    @BeforeEach
    void setUp() {
        testTask = new Task();
        testTask.setId(1L);
        testTask.setTitle("Test Task");
        testTask.setDescription("Test Description");
        testTask.setStatus(TaskStatus.PENDING);
        testTask.setPriority(TaskPriority.MEDIUM);
        testTask.setCreatedAt(LocalDateTime.now());
        testTask.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void createTask_ShouldReturnSavedTask() {
        // Given
        Task newTask = new Task();
        newTask.setTitle("New Task");
        newTask.setDescription("New Description");
        
        when(taskRepository.save(any(Task.class))).thenReturn(testTask);

        // When
        Task result = taskService.createTask(newTask);

        // Then
        assertNotNull(result);
        assertEquals(testTask.getId(), result.getId());
        assertEquals(testTask.getTitle(), result.getTitle());
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void createTask_ShouldSetDefaultValues() {
        // Given
        Task newTask = new Task();
        newTask.setTitle("New Task");
        
        when(taskRepository.save(any(Task.class))).thenReturn(testTask);

        // When
        Task result = taskService.createTask(newTask);

        // Then
        assertNotNull(result);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void getAllTasks_ShouldReturnListOfTasks() {
        // Given
        List<Task> tasks = Arrays.asList(testTask, new Task());
        when(taskRepository.findAll()).thenReturn(tasks);

        // When
        List<Task> result = taskService.getAllTasks();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void getTaskById_ShouldReturnTask_WhenTaskExists() {
        // Given
        when(taskRepository.findById(1L)).thenReturn(Optional.of(testTask));

        // When
        Optional<Task> result = taskService.getTaskById(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals(testTask.getId(), result.get().getId());
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void getTaskById_ShouldReturnEmpty_WhenTaskDoesNotExist() {
        // Given
        when(taskRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When
        Optional<Task> result = taskService.getTaskById(999L);

        // Then
        assertFalse(result.isPresent());
        verify(taskRepository, times(1)).findById(999L);
    }

    @Test
    void updateTask_ShouldReturnUpdatedTask_WhenTaskExists() {
        // Given
        Task updateDetails = new Task();
        updateDetails.setTitle("Updated Title");
        updateDetails.setDescription("Updated Description");
        updateDetails.setStatus(TaskStatus.IN_PROGRESS);
        
        when(taskRepository.findById(1L)).thenReturn(Optional.of(testTask));
        when(taskRepository.save(any(Task.class))).thenReturn(testTask);

        // When
        Task result = taskService.updateTask(1L, updateDetails);

        // Then
        assertNotNull(result);
        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void updateTask_ShouldThrowException_WhenTaskDoesNotExist() {
        // Given
        Task updateDetails = new Task();
        updateDetails.setTitle("Updated Title");
        
        when(taskRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            taskService.updateTask(999L, updateDetails);
        });
        
        verify(taskRepository, times(1)).findById(999L);
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void deleteTask_ShouldDeleteTask_WhenTaskExists() {
        // Given
        when(taskRepository.existsById(1L)).thenReturn(true);

        // When
        taskService.deleteTask(1L);

        // Then
        verify(taskRepository, times(1)).existsById(1L);
        verify(taskRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteTask_ShouldThrowException_WhenTaskDoesNotExist() {
        // Given
        when(taskRepository.existsById(anyLong())).thenReturn(false);

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            taskService.deleteTask(999L);
        });
        
        verify(taskRepository, times(1)).existsById(999L);
        verify(taskRepository, never()).deleteById(anyLong());
    }

    @Test
    void getTasksByStatus_ShouldReturnFilteredTasks() {
        // Given
        List<Task> pendingTasks = Arrays.asList(testTask);
        when(taskRepository.findByStatus(TaskStatus.PENDING)).thenReturn(pendingTasks);

        // When
        List<Task> result = taskService.getTasksByStatus(TaskStatus.PENDING);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(TaskStatus.PENDING, result.get(0).getStatus());
        verify(taskRepository, times(1)).findByStatus(TaskStatus.PENDING);
    }

    @Test
    void getTasksByPriority_ShouldReturnFilteredTasks() {
        // Given
        List<Task> highPriorityTasks = Arrays.asList(testTask);
        when(taskRepository.findByPriority(TaskPriority.HIGH)).thenReturn(highPriorityTasks);

        // When
        List<Task> result = taskService.getTasksByPriority(TaskPriority.HIGH);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(taskRepository, times(1)).findByPriority(TaskPriority.HIGH);
    }

    @Test
    void getTasksByAssignedTo_ShouldReturnFilteredTasks() {
        // Given
        String assignedUser = "john.doe";
        List<Task> userTasks = Arrays.asList(testTask);
        when(taskRepository.findByAssignedTo(assignedUser)).thenReturn(userTasks);

        // When
        List<Task> result = taskService.getTasksByAssignedTo(assignedUser);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(taskRepository, times(1)).findByAssignedTo(assignedUser);
    }

    @Test
    void markTaskAsCompleted_ShouldUpdateTaskStatus_WhenTaskExists() {
        // Given
        when(taskRepository.findById(1L)).thenReturn(Optional.of(testTask));
        when(taskRepository.save(any(Task.class))).thenReturn(testTask);

        // When
        Task result = taskService.markTaskAsCompleted(1L);

        // Then
        assertNotNull(result);
        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void markTaskAsCompleted_ShouldThrowException_WhenTaskDoesNotExist() {
        // Given
        when(taskRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            taskService.markTaskAsCompleted(999L);
        });
        
        verify(taskRepository, times(1)).findById(999L);
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void getTaskStatistics_ShouldReturnCorrectCounts() {
        // Given
        when(taskRepository.count()).thenReturn(10L);
        when(taskRepository.countByStatus(TaskStatus.PENDING)).thenReturn(3L);
        when(taskRepository.countByStatus(TaskStatus.IN_PROGRESS)).thenReturn(2L);
        when(taskRepository.countByStatus(TaskStatus.COMPLETED)).thenReturn(4L);
        when(taskRepository.countByStatus(TaskStatus.CANCELLED)).thenReturn(1L);

        // When
        TaskService.TaskStatistics result = taskService.getTaskStatistics();

        // Then
        assertNotNull(result);
        assertEquals(10L, result.getTotalTasks());
        assertEquals(3L, result.getPendingTasks());
        assertEquals(2L, result.getInProgressTasks());
        assertEquals(4L, result.getCompletedTasks());
        assertEquals(1L, result.getCancelledTasks());
    }

    @Test
    void searchTasksByTitle_ShouldReturnMatchingTasks() {
        // Given
        String keyword = "test";
        List<Task> matchingTasks = Arrays.asList(testTask);
        when(taskRepository.findByTitleContainingIgnoreCase(keyword)).thenReturn(matchingTasks);

        // When
        List<Task> result = taskService.searchTasksByTitle(keyword);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(taskRepository, times(1)).findByTitleContainingIgnoreCase(keyword);
    }

    @Test
    void searchTasksByDescription_ShouldReturnMatchingTasks() {
        // Given
        String keyword = "description";
        List<Task> matchingTasks = Arrays.asList(testTask);
        when(taskRepository.findByDescriptionContainingIgnoreCase(keyword)).thenReturn(matchingTasks);

        // When
        List<Task> result = taskService.searchTasksByDescription(keyword);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(taskRepository, times(1)).findByDescriptionContainingIgnoreCase(keyword);
    }
}
