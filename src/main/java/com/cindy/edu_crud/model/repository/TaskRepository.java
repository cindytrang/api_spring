package com.cindy.edu_crud.model.repository;

import com.cindy.edu_crud.model.Repo;
import com.cindy.edu_crud.model.Status;
import com.cindy.edu_crud.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> , Repo<Task, UUID> {
    //  Query method
    List<Task> findAllByStatus(Status status);

    //  Query method
    List<Task> findAllByLocalDueDateBetween(LocalDateTime createdAtFrom, LocalDateTime createdAtTo);

    //  Query method
    List<Task> findAllByCreatedAtBetween(LocalDateTime createdAtFrom, LocalDateTime createdAtTo);

    @Transactional
    @Modifying
    @Query("UPDATE Task t SET t.localDueDate = ?1 WHERE t.uuid = ?2") //TODO: check if the ' should be added like: '?1'
    void setLocalDueDate(LocalDateTime localDueDate, UUID uuid);
}

