package com.cindy.edu_crud.model;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class RepositoryContext implements Repo<Task, UUID> {
    private Repo<Task, UUID> repository;

    // setStrategy
    public void setRepository(Repo<Task, UUID> repository) {
        this.repository = repository;
    }

    @Override
    public Task save(Task task) {
        return repository.save(task);
    }

    @Override
    public List<Task> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Task> findById(UUID uuid) {
        return repository.findById(uuid);
    }

    @Override
    public void delete(Task obj) {
        repository.delete(obj);
    }

    @Override
    public void setLocalDueDate(LocalDateTime localDueDate, UUID uuid) {
        repository.setLocalDueDate(localDueDate, uuid);
    }

    @Override
    public List<Task> findAllByCreatedAtBetween(LocalDateTime startDateTime, LocalDateTime endDateTime)  {
        return repository.findAllByCreatedAtBetween(startDateTime, endDateTime);
    }

    @Override
    public List<Task> findAllByLocalDueDateBetween(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return repository.findAllByLocalDueDateBetween(startDateTime, endDateTime);
    }

    @Override
    public List<Task> findAllByStatus(Status status) {
        return repository.findAllByStatus(status);
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }


}
