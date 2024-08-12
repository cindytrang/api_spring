package com.cindy.edu_crud.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface Repo <T,ID>{
    <S extends T> S save(S entity);
    List<T> findAll();
    Optional<T> findById(ID uuid);
    void deleteAll();
    void delete(T obj);
    void setLocalDueDate(LocalDateTime date, ID uuid);
    List<T> findAllByCreatedAtBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);
    List<T> findAllByLocalDueDateBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);
    List<T> findAllByStatus(Status status);
}
