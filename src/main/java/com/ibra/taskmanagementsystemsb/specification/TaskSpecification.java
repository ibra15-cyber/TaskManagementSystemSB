package com.ibra.taskmanagementsystemsb.specification;

import com.ibra.taskmanagementsystemsb.entity.Task;
import com.ibra.taskmanagementsystemsb.enums.TaskPriority;
import com.ibra.taskmanagementsystemsb.enums.TaskStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class TaskSpecification {

    public static Specification<Task> hasTitle(String title) {
        return ((root, query, criteriaBuilder) ->
                title != null ? criteriaBuilder.like(root.get("title"), "%" + title+ "%" ): null);
    }

    public static Specification<Task> hasDescription(String description) {
        return ((root, query, criteriaBuilder) ->
                description != null ? criteriaBuilder.like(root.get("description"), "%" + description + "%") : null);
    }

    public static Specification<Task> hasAssignee(String assignee) {
        return ((root, query, criteriaBuilder) ->
                assignee != null ? criteriaBuilder.like(root.get("assignee"), "%" + assignee + "%") : null); // Corrected line
    }

    public static Specification<Task> hasStatus(TaskStatus status) {
        return ((root, query, criteriaBuilder) ->
                status != null ? criteriaBuilder.equal(root.get("status"), status) : null);
    }


    public static Specification<Task> hasPriority(TaskPriority priority) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("priority"), priority);
    }

    public static Specification<Task> createdAt(LocalDateTime createdAt) {
        return (root, query, criteriaBuilder) ->
                createdAt != null ? criteriaBuilder.greaterThan(root.get("createdAt"), createdAt) : null;
    }

    public static Specification<Task> dueBefore(LocalDateTime dueBefore) {
        return (root, query, criteriaBuilder) ->
                dueBefore != null ? criteriaBuilder.lessThan(root.get("dueDate"), dueBefore) : null;
    }
}