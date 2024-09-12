package com.cindy.edu_crud.controller;

import com.cindy.edu_crud.controller.dto.TaskDto;
import com.cindy.edu_crud.controller.dto.TaskDtoLocalDueDate;
import com.cindy.edu_crud.model.Status;
import com.cindy.edu_crud.model.Task;
import com.cindy.edu_crud.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/v1/api/task")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @GetMapping("/by-id/{uuid}")
    public ResponseEntity<Optional<Task>> findById(@PathVariable UUID uuid) {
        return ResponseEntity.ok(taskService.findById(uuid));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Task>> findAll() {
        return ResponseEntity.ok(taskService.findAll());
    }

    @GetMapping("/filter-by-create-date-range")
    public ResponseEntity<List<Task>> filterByDate(@RequestParam LocalDateTime startDateTime, @RequestParam LocalDateTime endDateTime) {
        return ResponseEntity.ok(taskService.filterByDateRange(startDateTime, endDateTime));
    }

    @GetMapping("/filter-by-due-date-range")
    public ResponseEntity<List<Task>> filterByDueDate(@RequestParam LocalDateTime startDateTime, @RequestParam LocalDateTime endDateTime) {
        return ResponseEntity.ok(taskService.filterByDueDateRange(startDateTime, endDateTime));
    }

    @GetMapping("/filter-by-status")
    public ResponseEntity<List<Task>> filterByStatus(@RequestParam Status status) {
        return ResponseEntity.ok(taskService.filterByStatus(status));
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<Task> update(@RequestBody TaskDto task, @PathVariable UUID uuid) {
        Task taskToSave = Task.builder().name(task.getName()).status(Status.valueOf(task.getStatus())).localDueDate(LocalDateTime.parse(task.getLocalDueDate())).build();
        taskToSave.setUuid(uuid);
        return ResponseEntity.ok(taskService.save(taskToSave));
    }

    @PutMapping("/{uuid}/due-date")
    public ResponseEntity<Task> updateDueDate(@RequestBody TaskDtoLocalDueDate task, @PathVariable UUID uuid) { //TODO: LocalDateTime as PathParam or TestDtoLocalDueDate
        LocalDateTime localDueDate = Task.builder().localDueDate(LocalDateTime.parse(task.getLocalDueDate())).build().getLocalDueDate();
        taskService.updateDueDate(localDueDate, uuid);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<Task> save(@RequestBody TaskDto task) {
        Task taskToSave = Task.builder().name(task.getName()).build();
        return ResponseEntity.ok(taskService.save(taskToSave));
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<UUID> delete(@PathVariable UUID uuid) {
        return ResponseEntity.ok(taskService.delete(uuid));
    }

    @DeleteMapping("/all")
    public ResponseEntity<Void> deleteAll() {
        taskService.deleteAll();
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/deletion-list")
    public ResponseEntity<Void> deleteAll(@RequestBody List<UUID> uuids) {
        taskService.deleteList(uuids);
        return ResponseEntity.noContent().build();
    }

}
