package com.cindy.edu_crud.service;

import com.cindy.edu_crud.infrastructure.InvalidDatesException;
import com.cindy.edu_crud.model.RepositoryContext;
import com.cindy.edu_crud.model.Status;
import com.cindy.edu_crud.model.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final RepositoryContext repositoryContext;

    public Task save(Task task) {
        Task existingTask = repositoryContext.findById(task.getUuid()).orElse(null);
        if(!Objects.isNull(existingTask)) {
            task.setCreatedAt(existingTask.getCreatedAt());
            if (task.getLocalDueDate().isBefore(task.getCreatedAt()))
                throw new InvalidDatesException("Local date: " + task.getLocalDueDate() + " is before create date: " + task.getCreatedAt());
        }
        return repositoryContext.save(task);
    }

    public void updateDueDate(LocalDateTime localDueDate, UUID uuid) {
        repositoryContext.setLocalDueDate(localDueDate, uuid);
    }

    public List<Task> findAll() {
        return repositoryContext.findAll();
    }

    public Optional<Task> findById(UUID uuid) {
        return repositoryContext.findById(uuid);
    }

    public UUID delete(UUID uuid) {
        Optional<Task> task = repositoryContext.findById(uuid);
        repositoryContext.delete(task.orElse(null));
        return uuid;
    }

    public void deleteAll() {
        repositoryContext.deleteAll();
    }

    public void deleteList(List<UUID> list) {
        for (UUID id : list) {
            Optional<Task> task = repositoryContext.findById(id);
            repositoryContext.delete(task.orElse(null));
        }
    }

    public List<Task> filterByDateRange(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if(startDateTime.isAfter(endDateTime))
            throw new InvalidDatesException("Invalid dates: " + startDateTime + " " + endDateTime);
        return repositoryContext.findAllByCreatedAtBetween(startDateTime, endDateTime);
    }

    public List<Task> filterByDueDateRange(LocalDateTime startDateTime, LocalDateTime endDateTime){
        if(startDateTime.isAfter(endDateTime))
            throw new InvalidDatesException("Invalid dates range, start: " + startDateTime + ", end: " + endDateTime);
        return repositoryContext.findAllByLocalDueDateBetween(startDateTime, endDateTime);
    }

    public List<Task> filterByStatus(Status status) {
        return repositoryContext.findAllByStatus(status);
    }
}
