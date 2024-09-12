package com.cindy.edu_crud.service;

import com.cindy.edu_crud.model.RepositoryContext;
import com.cindy.edu_crud.model.Task;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import com.cindy.edu_crud.model.Status;

@SpringBootTest
public class TaskServiceTest {
	@Autowired
	private TaskService taskService;

	@MockBean
	private RepositoryContext repositoryContext;

	@Test
	public void saveTest() {
		// Arrange
		Task task = new Task();
		UUID taskId = UUID.randomUUID();
		Task expected = Task.builder()
				.uuid(taskId)
				.name("Example Task")
				.status(Status.IN_PROGRESS)
				.localDueDate(LocalDateTime.parse("2023-08-21T10:00:00"))
				.build();

		when(taskService.save(task)).thenReturn(expected);

		// Act
		Task actual = taskService.save(task);

		// Assert
		assertEquals(expected, actual);
	}

	@Test
	public void updateDueDateTest() {
		LocalDateTime localDueDate = null;
		UUID uuid = null;
		taskService.updateDueDate(localDueDate, uuid);
	}

	@Test
	public void findAllTest() {
		List<Task> expected = new ArrayList<>();
		List<Task> actual = taskService.findAll();

		assertEquals(expected, actual);
	}

	@Test
	public void findByIdTest() {
		UUID uuid = null;
		Optional<Task> expected = null;
		Optional<Task> actual = taskService.findById(uuid);

		assertEquals(expected, actual);
	}

	@Test
	public void deleteTest() {
		UUID uuid = null;
		UUID expected = null;
		UUID actual = taskService.delete(uuid);

		assertEquals(expected, actual);
	}

	@Test
	public void deleteAllTest() {
		taskService.deleteAll();
	}

	@Test
	public void deleteListTest() {
		List<UUID> list = new ArrayList<>();
		taskService.deleteList(list);
	}

	@Test
	public void filterByDateRangeTest() {
		LocalDateTime startDateTime = null;
		LocalDateTime endDateTime = null;
		List<Task> expected = new ArrayList<>();
		List<Task> actual = taskService.filterByDateRange(startDateTime, endDateTime);

		assertEquals(expected, actual);
	}

	@Test
	public void filterByDueDateRangeTest() {
		LocalDateTime startDateTime = null;
		LocalDateTime endDateTime = null;
		List<Task> expected = new ArrayList<>();
		List<Task> actual = taskService.filterByDueDateRange(startDateTime, endDateTime);

		assertEquals(expected, actual);
	}

	@Test
	public void filterByStatusTest() {
		Status status = Status.DONE;
		List<Task> expected = new ArrayList<>();
		List<Task> actual = taskService.filterByStatus(status);

		assertEquals(expected, actual);
	}
}
