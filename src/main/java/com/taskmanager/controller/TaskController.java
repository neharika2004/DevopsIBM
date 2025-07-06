package com.taskmanager.controller;

import com.taskmanager.model.Task;
import com.taskmanager.model.TaskPriority;
import com.taskmanager.model.TaskStatus;
import com.taskmanager.service.TaskService;
import io.micrometer.core.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "*")
public class TaskController {
    
    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);
    
    @Autowired
    private TaskService taskService;
    
    // Health check endpoint
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Task Management API is running!");
    }
    
    // Create a new task
    @PostMapping
    @Timed(value = "task.create", description = "Time taken to create a task")
    public ResponseEntity<Task> createTask(@Valid @RequestBody Task task) {
        logger.info("Received request to create task: {}", task.getTitle());
        try {
            Task createdTask = taskService.createTask(task);
            return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error creating task: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Get all tasks
    @GetMapping
    @Timed(value = "task.getAll", description = "Time taken to get all tasks")
    public ResponseEntity<List<Task>> getAllTasks() {
        logger.info("Received request to get all tasks");
        try {
            List<Task> tasks = taskService.getAllTasks();
            return new ResponseEntity<>(tasks, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error retrieving tasks: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Get task by ID
    @GetMapping("/{id}")
    @Timed(value = "task.getById", description = "Time taken to get a task by ID")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        logger.info("Received request to get task with ID: {}", id);
        try {
            Optional<Task> task = taskService.getTaskById(id);
            return task.map(t -> new ResponseEntity<>(t, HttpStatus.OK))
                      .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            logger.error("Error retrieving task with ID {}: {}", id, e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Update task
    @PutMapping("/{id}")
    @Timed(value = "task.update", description = "Time taken to update a task")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @Valid @RequestBody Task taskDetails) {
        logger.info("Received request to update task with ID: {}", id);
        try {
            Task updatedTask = taskService.updateTask(id, taskDetails);
            return new ResponseEntity<>(updatedTask, HttpStatus.OK);
        } catch (RuntimeException e) {
            logger.error("Task not found with ID: {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Error updating task with ID {}: {}", id, e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Delete task
    @DeleteMapping("/{id}")
    @Timed(value = "task.delete", description = "Time taken to delete a task")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        logger.info("Received request to delete task with ID: {}", id);
        try {
            taskService.deleteTask(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            logger.error("Task not found with ID: {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Error deleting task with ID {}: {}", id, e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Get tasks by status
    @GetMapping("/status/{status}")
    @Timed(value = "task.getByStatus", description = "Time taken to get tasks by status")
    public ResponseEntity<List<Task>> getTasksByStatus(@PathVariable TaskStatus status) {
        logger.info("Received request to get tasks with status: {}", status);
        try {
            List<Task> tasks = taskService.getTasksByStatus(status);
            return new ResponseEntity<>(tasks, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error retrieving tasks by status {}: {}", status, e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Get tasks by priority
    @GetMapping("/priority/{priority}")
    @Timed(value = "task.getByPriority", description = "Time taken to get tasks by priority")
    public ResponseEntity<List<Task>> getTasksByPriority(@PathVariable TaskPriority priority) {
        logger.info("Received request to get tasks with priority: {}", priority);
        try {
            List<Task> tasks = taskService.getTasksByPriority(priority);
            return new ResponseEntity<>(tasks, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error retrieving tasks by priority {}: {}", priority, e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Get tasks by assigned user
    @GetMapping("/assigned/{assignedTo}")
    @Timed(value = "task.getByAssigned", description = "Time taken to get tasks by assigned user")
    public ResponseEntity<List<Task>> getTasksByAssignedTo(@PathVariable String assignedTo) {
        logger.info("Received request to get tasks assigned to: {}", assignedTo);
        try {
            List<Task> tasks = taskService.getTasksByAssignedTo(assignedTo);
            return new ResponseEntity<>(tasks, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error retrieving tasks assigned to {}: {}", assignedTo, e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Get overdue tasks
    @GetMapping("/overdue")
    @Timed(value = "task.getOverdue", description = "Time taken to get overdue tasks")
    public ResponseEntity<List<Task>> getOverdueTasks() {
        logger.info("Received request to get overdue tasks");
        try {
            List<Task> tasks = taskService.getOverdueTasks();
            return new ResponseEntity<>(tasks, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error retrieving overdue tasks: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Get high priority pending tasks
    @GetMapping("/high-priority-pending")
    @Timed(value = "task.getHighPriorityPending", description = "Time taken to get high priority pending tasks")
    public ResponseEntity<List<Task>> getHighPriorityPendingTasks() {
        logger.info("Received request to get high priority pending tasks");
        try {
            List<Task> tasks = taskService.getHighPriorityPendingTasks();
            return new ResponseEntity<>(tasks, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error retrieving high priority pending tasks: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Search tasks by title
    @GetMapping("/search/title")
    @Timed(value = "task.searchByTitle", description = "Time taken to search tasks by title")
    public ResponseEntity<List<Task>> searchTasksByTitle(@RequestParam String keyword) {
        logger.info("Received request to search tasks by title keyword: {}", keyword);
        try {
            List<Task> tasks = taskService.searchTasksByTitle(keyword);
            return new ResponseEntity<>(tasks, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error searching tasks by title {}: {}", keyword, e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Search tasks by description
    @GetMapping("/search/description")
    @Timed(value = "task.searchByDescription", description = "Time taken to search tasks by description")
    public ResponseEntity<List<Task>> searchTasksByDescription(@RequestParam String keyword) {
        logger.info("Received request to search tasks by description keyword: {}", keyword);
        try {
            List<Task> tasks = taskService.searchTasksByDescription(keyword);
            return new ResponseEntity<>(tasks, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error searching tasks by description {}: {}", keyword, e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Get task statistics
    @GetMapping("/statistics")
    @Timed(value = "task.getStatistics", description = "Time taken to get task statistics")
    public ResponseEntity<TaskService.TaskStatistics> getTaskStatistics() {
        logger.info("Received request to get task statistics");
        try {
            TaskService.TaskStatistics statistics = taskService.getTaskStatistics();
            return new ResponseEntity<>(statistics, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error retrieving task statistics: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Mark task as completed
    @PatchMapping("/{id}/complete")
    @Timed(value = "task.markCompleted", description = "Time taken to mark a task as completed")
    public ResponseEntity<Task> markTaskAsCompleted(@PathVariable Long id) {
        logger.info("Received request to mark task as completed: {}", id);
        try {
            Task completedTask = taskService.markTaskAsCompleted(id);
            return new ResponseEntity<>(completedTask, HttpStatus.OK);
        } catch (RuntimeException e) {
            logger.error("Task not found with ID: {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Error marking task as completed with ID {}: {}", id, e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
