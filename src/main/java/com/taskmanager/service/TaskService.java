package com.taskmanager.service;

import com.taskmanager.model.Task;
import com.taskmanager.model.TaskPriority;
import com.taskmanager.model.TaskStatus;
import com.taskmanager.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    
    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);
    
    @Autowired
    private TaskRepository taskRepository;
    
    // Create a new task
    public Task createTask(Task task) {
        logger.info("Creating new task: {}", task.getTitle());
        
        // Set default values if not provided
        if (task.getStatus() == null) {
            task.setStatus(TaskStatus.PENDING);
        }
        if (task.getPriority() == null) {
            task.setPriority(TaskPriority.MEDIUM);
        }
        
        Task savedTask = taskRepository.save(task);
        logger.info("Task created successfully with ID: {}", savedTask.getId());
        return savedTask;
    }
    
    // Get all tasks
    public List<Task> getAllTasks() {
        logger.info("Retrieving all tasks");
        return taskRepository.findAll();
    }
    
    // Get task by ID
    public Optional<Task> getTaskById(Long id) {
        logger.info("Retrieving task with ID: {}", id);
        return taskRepository.findById(id);
    }
    
    // Update task
    public Task updateTask(Long id, Task taskDetails) {
        logger.info("Updating task with ID: {}", id);
        
        return taskRepository.findById(id)
                .map(task -> {
                    if (taskDetails.getTitle() != null) {
                        task.setTitle(taskDetails.getTitle());
                    }
                    if (taskDetails.getDescription() != null) {
                        task.setDescription(taskDetails.getDescription());
                    }
                    if (taskDetails.getStatus() != null) {
                        task.setStatus(taskDetails.getStatus());
                    }
                    if (taskDetails.getPriority() != null) {
                        task.setPriority(taskDetails.getPriority());
                    }
                    if (taskDetails.getDueDate() != null) {
                        task.setDueDate(taskDetails.getDueDate());
                    }
                    if (taskDetails.getAssignedTo() != null) {
                        task.setAssignedTo(taskDetails.getAssignedTo());
                    }
                    
                    Task updatedTask = taskRepository.save(task);
                    logger.info("Task updated successfully: {}", updatedTask.getId());
                    return updatedTask;
                })
                .orElseThrow(() -> {
                    logger.error("Task not found with ID: {}", id);
                    return new RuntimeException("Task not found with ID: " + id);
                });
    }
    
    // Delete task
    public void deleteTask(Long id) {
        logger.info("Deleting task with ID: {}", id);
        
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
            logger.info("Task deleted successfully: {}", id);
        } else {
            logger.error("Task not found with ID: {}", id);
            throw new RuntimeException("Task not found with ID: " + id);
        }
    }
    
    // Get tasks by status
    public List<Task> getTasksByStatus(TaskStatus status) {
        logger.info("Retrieving tasks with status: {}", status);
        return taskRepository.findByStatus(status);
    }
    
    // Get tasks by priority
    public List<Task> getTasksByPriority(TaskPriority priority) {
        logger.info("Retrieving tasks with priority: {}", priority);
        return taskRepository.findByPriority(priority);
    }
    
    // Get tasks by assigned user
    public List<Task> getTasksByAssignedTo(String assignedTo) {
        logger.info("Retrieving tasks assigned to: {}", assignedTo);
        return taskRepository.findByAssignedTo(assignedTo);
    }
    
    // Get overdue tasks
    public List<Task> getOverdueTasks() {
        logger.info("Retrieving overdue tasks");
        return taskRepository.findOverdueTasks(LocalDateTime.now());
    }
    
    // Get high priority pending tasks
    public List<Task> getHighPriorityPendingTasks() {
        logger.info("Retrieving high priority pending tasks");
        return taskRepository.findHighPriorityPendingTasks();
    }
    
    // Search tasks by title
    public List<Task> searchTasksByTitle(String keyword) {
        logger.info("Searching tasks by title keyword: {}", keyword);
        return taskRepository.findByTitleContainingIgnoreCase(keyword);
    }
    
    // Search tasks by description
    public List<Task> searchTasksByDescription(String keyword) {
        logger.info("Searching tasks by description keyword: {}", keyword);
        return taskRepository.findByDescriptionContainingIgnoreCase(keyword);
    }
    
    // Get task statistics
    public TaskStatistics getTaskStatistics() {
        logger.info("Retrieving task statistics");
        
        long totalTasks = taskRepository.count();
        long pendingTasks = taskRepository.countByStatus(TaskStatus.PENDING);
        long inProgressTasks = taskRepository.countByStatus(TaskStatus.IN_PROGRESS);
        long completedTasks = taskRepository.countByStatus(TaskStatus.COMPLETED);
        long cancelledTasks = taskRepository.countByStatus(TaskStatus.CANCELLED);
        
        return new TaskStatistics(totalTasks, pendingTasks, inProgressTasks, completedTasks, cancelledTasks);
    }
    
    // Mark task as completed
    public Task markTaskAsCompleted(Long id) {
        logger.info("Marking task as completed: {}", id);
        
        return taskRepository.findById(id)
                .map(task -> {
                    task.setStatus(TaskStatus.COMPLETED);
                    Task updatedTask = taskRepository.save(task);
                    logger.info("Task marked as completed: {}", updatedTask.getId());
                    return updatedTask;
                })
                .orElseThrow(() -> {
                    logger.error("Task not found with ID: {}", id);
                    return new RuntimeException("Task not found with ID: " + id);
                });
    }
    
    // Inner class for task statistics
    public static class TaskStatistics {
        private final long totalTasks;
        private final long pendingTasks;
        private final long inProgressTasks;
        private final long completedTasks;
        private final long cancelledTasks;
        
        public TaskStatistics(long totalTasks, long pendingTasks, long inProgressTasks, long completedTasks, long cancelledTasks) {
            this.totalTasks = totalTasks;
            this.pendingTasks = pendingTasks;
            this.inProgressTasks = inProgressTasks;
            this.completedTasks = completedTasks;
            this.cancelledTasks = cancelledTasks;
        }
        
        // Getters
        public long getTotalTasks() { return totalTasks; }
        public long getPendingTasks() { return pendingTasks; }
        public long getInProgressTasks() { return inProgressTasks; }
        public long getCompletedTasks() { return completedTasks; }
        public long getCancelledTasks() { return cancelledTasks; }
    }
}
