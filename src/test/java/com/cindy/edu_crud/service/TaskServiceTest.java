package com.cindy.edu_crud.service;

import com.cindy.edu_crud.infrastructure.InvalidDatesException;
import com.cindy.edu_crud.model.RepositoryContext;
import com.cindy.edu_crud.model.Status;
import com.cindy.edu_crud.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceTest {

	@Mock
	private RepositoryContext repositoryContext;

	@InjectMocks
	private TaskService taskService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testSave_NewTask() {
		Task task = new Task();
		task.setUuid(UUID.randomUUID());
		task.setLocalDueDate(LocalDateTime.now().plusDays(1));

		when(repositoryContext.findById(task.getUuid())).thenReturn(Optional.empty());
		when(repositoryContext.save(task)).thenReturn(task);

		Task savedTask = taskService.save(task);

		assertNotNull(savedTask);
		verify(repositoryContext).save(task);
	}

	@Test
	void testSave_ExistingTask() {
		Task existingTask = new Task();
		existingTask.setUuid(UUID.randomUUID());
		existingTask.setCreatedAt(LocalDateTime.now().minusDays(1));

		Task updatedTask = new Task();
		updatedTask.setUuid(existingTask.getUuid());
		updatedTask.setLocalDueDate(LocalDateTime.now().plusDays(1));

		when(repositoryContext.findById(existingTask.getUuid())).thenReturn(Optional.of(existingTask));
		when(repositoryContext.save(updatedTask)).thenReturn(updatedTask);

		Task savedTask = taskService.save(updatedTask);

		assertNotNull(savedTask);
		assertEquals(existingTask.getCreatedAt(), savedTask.getCreatedAt());
		verify(repositoryContext).save(updatedTask);
	}

	@Test
	void testSave_InvalidDates() {
		Task existingTask = new Task();
		existingTask.setUuid(UUID.randomUUID());
		existingTask.setCreatedAt(LocalDateTime.now());

		Task updatedTask = new Task();
		updatedTask.setUuid(existingTask.getUuid());
		updatedTask.setLocalDueDate(LocalDateTime.now().minusDays(1));

		when(repositoryContext.findById(existingTask.getUuid())).thenReturn(Optional.of(existingTask));

		assertThrows(InvalidDatesException.class, () -> taskService.save(updatedTask));
	}

	@Test
	void testUpdateDueDate() {
		UUID uuid = UUID.randomUUID();
		LocalDateTime newDueDate = LocalDateTime.now().plusDays(1);

		taskService.updateDueDate(newDueDate, uuid);

		verify(repositoryContext).setLocalDueDate(newDueDate, uuid);
	}

	@Test
	void testFindAll() {
		List<Task> tasks = Arrays.asList(new Task(), new Task());
		when(repositoryContext.findAll()).thenReturn(tasks);

		List<Task> result = taskService.findAll();

		assertEquals(tasks, result);
	}

	@Test
	void testFindById() {
		UUID uuid = UUID.randomUUID();
		Task task = new Task();
		when(repositoryContext.findById(uuid)).thenReturn(Optional.of(task));

		Optional<Task> result = taskService.findById(uuid);

		assertTrue(result.isPresent());
		assertEquals(task, result.get());
	}

	@Test
	void testDelete() {
		UUID uuid = UUID.randomUUID();
		Task task = new Task();
		when(repositoryContext.findById(uuid)).thenReturn(Optional.of(task));

		UUID result = taskService.delete(uuid);

		assertEquals(uuid, result);
		verify(repositoryContext).delete(task);
	}

	@Test
	void testDeleteAll() {
		taskService.deleteAll();

		verify(repositoryContext).deleteAll();
	}

	@Test
	void testDeleteList() {
		UUID uuid1 = UUID.randomUUID();
		UUID uuid2 = UUID.randomUUID();
		Task task1 = new Task();
		Task task2 = new Task();

		when(repositoryContext.findById(uuid1)).thenReturn(Optional.of(task1));
		when(repositoryContext.findById(uuid2)).thenReturn(Optional.of(task2));

		taskService.deleteList(Arrays.asList(uuid1, uuid2));

		verify(repositoryContext).delete(task1);
		verify(repositoryContext).delete(task2);
	}

	@Test
	void testFilterByDateRange() {
		LocalDateTime startDateTime = LocalDateTime.now();
		LocalDateTime endDateTime = startDateTime.plusDays(1);
		List<Task> tasks = Arrays.asList(new Task(), new Task());

		when(repositoryContext.findAllByCreatedAtBetween(startDateTime, endDateTime)).thenReturn(tasks);

		List<Task> result = taskService.filterByDateRange(startDateTime, endDateTime);

		assertEquals(tasks, result);
	}

	@Test
	void testFilterByDateRange_InvalidDates() {
		LocalDateTime startDateTime = LocalDateTime.now();
		LocalDateTime endDateTime = startDateTime.minusDays(1);

		assertThrows(InvalidDatesException.class, () -> taskService.filterByDateRange(startDateTime, endDateTime));
	}

	@Test
	void testFilterByDueDateRange() {
		LocalDateTime startDateTime = LocalDateTime.now();
		LocalDateTime endDateTime = startDateTime.plusDays(1);
		List<Task> tasks = Arrays.asList(new Task(), new Task());

		when(repositoryContext.findAllByLocalDueDateBetween(startDateTime, endDateTime)).thenReturn(tasks);

		List<Task> result = taskService.filterByDueDateRange(startDateTime, endDateTime);

		assertEquals(tasks, result);
	}

	@Test
	void testFilterByDueDateRange_InvalidDates() {
		LocalDateTime startDateTime = LocalDateTime.now();
		LocalDateTime endDateTime = startDateTime.minusDays(1);

		assertThrows(InvalidDatesException.class, () -> taskService.filterByDueDateRange(startDateTime, endDateTime));
	}

	@Test
	void testFilterByStatus() {
		Status status = Status.TODO;
		List<Task> tasks = Arrays.asList(new Task(), new Task());

		when(repositoryContext.findAllByStatus(status)).thenReturn(tasks);

		List<Task> result = taskService.filterByStatus(status);

		assertEquals(tasks, result);
	}
}