package com.taskmanager.repository;

import com.taskmanager.model.Task;
import com.taskmanager.model.TaskPriority;
import com.taskmanager.model.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    
    // Find tasks by status
    List<Task> findByStatus(TaskStatus status);
    
    // Find tasks by priority
    List<Task> findByPriority(TaskPriority priority);
    
    // Find tasks by assigned user
    List<Task> findByAssignedTo(String assignedTo);
    
    // Find tasks by status and priority
    List<Task> findByStatusAndPriority(TaskStatus status, TaskPriority priority);
    
    // Find tasks created after a specific date
    List<Task> findByCreatedAtAfter(LocalDateTime date);
    
    // Find tasks due before a specific date
    List<Task> findByDueDateBefore(LocalDateTime date);
    
    // Find tasks by title containing keyword (case-insensitive)
    List<Task> findByTitleContainingIgnoreCase(String keyword);
    
    // Find tasks by description containing keyword (case-insensitive)
    List<Task> findByDescriptionContainingIgnoreCase(String keyword);
    
    // Custom query to find overdue tasks
    @Query("SELECT t FROM Task t WHERE t.dueDate < :currentDate AND t.status != 'COMPLETED'")
    List<Task> findOverdueTasks(@Param("currentDate") LocalDateTime currentDate);
    
    // Custom query to count tasks by status
    @Query("SELECT COUNT(t) FROM Task t WHERE t.status = :status")
    long countByStatus(@Param("status") TaskStatus status);
    
    // Custom query to find tasks with high priority that are not completed
    @Query("SELECT t FROM Task t WHERE t.priority = 'HIGH' AND t.status != 'COMPLETED'")
    List<Task> findHighPriorityPendingTasks();
    
    // Find tasks by multiple criteria
    @Query("SELECT t FROM Task t WHERE " +
           "(:status IS NULL OR t.status = :status) AND " +
           "(:priority IS NULL OR t.priority = :priority) AND " +
           "(:assignedTo IS NULL OR t.assignedTo = :assignedTo)")
    List<Task> findTasksByCriteria(@Param("status") TaskStatus status,
                                   @Param("priority") TaskPriority priority,
                                   @Param("assignedTo") String assignedTo);
}
