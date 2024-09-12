package com.cindy.edu_crud.model.repository;

import com.cindy.edu_crud.model.Repo;
import com.cindy.edu_crud.model.Status;
import com.cindy.edu_crud.model.Task;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class TaskFileRepository implements Repo<Task, UUID> {

    public Task save(Task task) {
        List<Task> tasks = findAll();
        Optional<Task> existingTask = tasks.stream().filter(e -> e.getUuid().equals(task.getUuid())).findFirst();

        if (existingTask.isPresent()) {
            Task updatedTask = existingTask.get();
            tasks.remove(updatedTask);
            tasks.add(task);
            reloadFile(tasks);
            return task;
        }

        tasks.add(task);
        reloadFile(tasks);
        return task;
    }

    public List<Task> findAll() {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("tasks.bin"))) {
            List<Task> tasks = (List<Task>) inputStream.readObject();
            return tasks;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public Optional<Task> findById(UUID uuid) {
        List<Task> tasks = findAll();
        return tasks.stream().filter(e -> e.getUuid().equals(uuid)).findFirst();
    }

    public void deleteAll() {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("tasks.bin"))) {
            outputStream.writeObject(new ArrayList<>());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void delete(Task task) {
        List<Task> tasks = findAll();
        tasks.remove(tasks.stream().filter(e -> e.getUuid().equals(task.getUuid())).findFirst().orElseThrow());
        reloadFile(tasks);
    }

    public void setLocalDueDate(LocalDateTime localDateTime, UUID uuid) {
        Task task = findById(uuid).orElseThrow();
        task.setLocalDueDate(localDateTime);
        save(task);
    }

    public List<Task> findAllByCreatedAtBetween(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return findAll().stream().filter(task -> (task.getCreatedAt().isEqual(startDateTime) || task.getCreatedAt().isAfter(startDateTime)) && (task.getCreatedAt().isEqual(endDateTime) || task.getCreatedAt().isBefore(endDateTime))).collect(Collectors.toList());
    }

    public List<Task> findAllByLocalDueDateBetween(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return findAll().stream().filter(task -> (task.getLocalDueDate().isEqual(startDateTime) || task.getLocalDueDate().isAfter(startDateTime)) && (task.getLocalDueDate().isEqual(endDateTime) || task.getLocalDueDate().isBefore(endDateTime))).collect(Collectors.toList());
    }

    public List<Task> findAllByStatus(Status status) {
        return findAll().stream().filter(task -> (task.getStatus().equals(status))).collect(Collectors.toList());
    }

    private void reloadFile(List<Task> tasks) { //save tasks
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("tasks.bin"))) {
            outputStream.writeObject(tasks);
            System.out.println("Tasks saved.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
