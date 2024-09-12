package com.cindy.edu_crud.controller;

import com.cindy.edu_crud.model.Status;
import com.cindy.edu_crud.model.Task;
import com.cindy.edu_crud.model.repository.TaskRepository;
import com.cindy.edu_crud.service.TaskService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.cindy.edu_crud.controller.dto.TaskDto;

import java.time.LocalDateTime;
import java.util.*;

import com.cindy.edu_crud.controller.dto.TaskDtoLocalDueDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;


@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private TaskService taskService;


	@Test
	public void testFindById() throws Exception {
		// Arrange
		UUID testUuid = UUID.randomUUID();
		Task mockTask = Task.builder()
				.uuid(testUuid)
				.name("Test Task")
				.status(Status.TODO)
				.localDueDate(LocalDateTime.now().plusDays(1))
				.build();

		when(taskService.findById(testUuid)).thenReturn(Optional.of(mockTask));

		// Act // Assert
		this.mockMvc.perform(get("/v1/api/task/by-id/{uuid}", testUuid))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(jsonPath("$.uuid").value(testUuid.toString()))
			.andExpect(jsonPath("$.name").value("Test Task"))
			.andExpect(jsonPath("$.status").value("TODO"));
	}

	@Test
	public void testFindAll() throws Exception {
		// Arrange
		List<Task> mockTasks = Arrays.asList(
		  Task.builder().uuid(UUID.randomUUID()).name("Task 1").status(Status.TODO).createdAt(LocalDateTime.now()).localDueDate(LocalDateTime.now().plusDays(1)).build(),
		  Task.builder().uuid(UUID.randomUUID()).name("Task 2").status(Status.TODO).createdAt(LocalDateTime.now()).localDueDate(LocalDateTime.now().plusDays(1)).build(),
		  Task.builder().uuid(UUID.randomUUID()).name("Task 3").status(Status.TODO).createdAt(LocalDateTime.now()).localDueDate(LocalDateTime.now().plusDays(1)).build());


		when(taskService.findAll()).thenReturn(mockTasks);

		// Act & Assert
		this.mockMvc.perform(get("/v1/api/task/all"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(jsonPath("$", hasSize(3)))
			.andExpect(jsonPath("$[0].name").value("Task 1"))
			.andExpect(jsonPath("$[0].status").value("TODO"))
			.andExpect(jsonPath("$[1].name").value("Task 2"))
			.andExpect(jsonPath("$[1].status").value("TODO"))
			.andExpect(jsonPath("$[2].name").value("Task 3"))
			.andExpect(jsonPath("$[2].status").value("TODO"));
	}

	@Test
	public void testFilterByDate() throws Exception {
		// Arrange
		LocalDateTime startDateTime = LocalDateTime.of(2023, 1, 1, 12, 0);
		LocalDateTime endDateTime = LocalDateTime.of(2023, 1, 2, 12, 0);
		UUID taskId = UUID.randomUUID();
		Task mockTask = Task.builder()
				.uuid(taskId)
				.name("Task within date range")
				.status(Status.TODO)
				.createdAt(startDateTime.plusHours(1))
				.localDueDate(startDateTime.plusDays(1))
				.build();

		when(taskService.filterByDateRange(startDateTime, endDateTime))
				.thenReturn(Collections.singletonList(mockTask));

		// Act & Assert
		mockMvc.perform(get("/v1/api/task/filter-by-create-date-range")
		    .param("startDateTime", "2023-01-01T12:00:00")
		    .param("endDateTime", "2023-01-02T12:00:00"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].uuid").value(taskId.toString()))
				.andExpect(jsonPath("$[0].name").value("Task within date range"))
				.andExpect(jsonPath("$[0].status").value("TODO"))
				.andExpect(jsonPath("$[0].createdAt").isNotEmpty())
				.andExpect(jsonPath("$[0].localDueDate").isNotEmpty());

	}

	@Test
	public void testFilterByDueDate() throws Exception {
		// Arrange
		LocalDateTime startDateTime = LocalDateTime.of(2023, 1, 1, 12, 0);
		LocalDateTime endDateTime = LocalDateTime.of(2023, 1, 2, 12, 0);
		UUID taskId = UUID.randomUUID();
		Task mockTask = Task.builder()
				.uuid(taskId)
				.name("Task due within range")
				.status(Status.IN_PROGRESS)
				.createdAt(startDateTime.minusDays(1))
				.localDueDate(startDateTime.plusHours(6))
				.build();
		when(taskService.filterByDueDateRange(startDateTime, endDateTime)).thenReturn(Collections.singletonList(mockTask));

		// Act & Assert
		mockMvc.perform(get("/v1/api/task/filter-by-due-date-range")
		    .param("startDateTime", "2023-01-01T12:00:00")
		    .param("endDateTime", "2023-01-02T12:00:00"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].uuid").value(taskId.toString()))
				.andExpect(jsonPath("$[0].name").value("Task due within range"))
				.andExpect(jsonPath("$[0].status").value("IN_PROGRESS"))
				.andExpect(jsonPath("$[0].createdAt").isNotEmpty())
				.andExpect(jsonPath("$[0].localDueDate").isNotEmpty());

		// Verify
		verify(taskService).filterByDueDateRange(startDateTime, endDateTime);
	}

	@Test
	public void testUpdate() throws Exception {
		// Arrange
		UUID taskId = UUID.randomUUID();
		TaskDto taskDto = new TaskDto("Updated Task", "IN_PROGRESS", "2023-08-21T10:00:00");

		Task task = Task.builder()
				.uuid(taskId)
				.name("Updated Task")
				.status(Status.IN_PROGRESS)
				.localDueDate(LocalDateTime.parse("2023-08-21T10:00:00"))
				.build();

		when(taskService.save(any(Task.class))).thenReturn(task);

		// Act & Assert
		mockMvc.perform(put("/v1/api/task/{uuid}", taskId)
		    .content(new ObjectMapper().writeValueAsString(taskDto))
		    .contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.uuid").value(taskId.toString()))
				.andExpect(jsonPath("$.name").value("Updated Task"))
				.andExpect(jsonPath("$.status").value("IN_PROGRESS"))
				.andExpect(jsonPath("$.localDueDate").value("2023-08-21T10:00:00"));

	}

	@Test
	public void testUpdateDueDate() throws Exception {
		// Arrange
		UUID taskId = UUID.randomUUID();
		TaskDtoLocalDueDate taskDtoLocalDueDate = new TaskDtoLocalDueDate("2023-08-21T10:00:00");

		// Act & Assert
		mockMvc.perform(put("/v1/api/task/{uuid}/due-date", taskId)
						.content(new ObjectMapper().writeValueAsString(taskDtoLocalDueDate))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

		verify(taskService).updateDueDate(LocalDateTime.parse("2023-08-21T10:00:00"), taskId);
	}

	@Test
	public void testFilterByStatus() throws Exception {
		// Arrange
		Status status = Status.TODO;
		List<Task> mockTasks = Arrays.asList(
				Task.builder().uuid(UUID.randomUUID()).name("Task 1").status(status).build(),
				Task.builder().uuid(UUID.randomUUID()).name("Task 2").status(status).build()
		);

		when(taskService.filterByStatus(status)).thenReturn(mockTasks);

		// Act & Assert
		mockMvc.perform(get("/v1/api/task/filter-by-status")
						.param("status", "TODO"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].status").value("TODO"))
				.andExpect(jsonPath("$[1].status").value("TODO"));
	}

	@Test
	public void testSave() throws Exception {
		// Arrange
		TaskDto taskDto = new TaskDto("Test Task", "IN_PROGRESS", "2023-08-21T10:00:00");
		Task task = Task.builder()
				.uuid(UUID.randomUUID())
				.name("Test Task")
				.status(Status.IN_PROGRESS)
				.localDueDate(LocalDateTime.parse("2023-08-21T10:00:00"))
				.build();

		when(taskService.save(any(Task.class))).thenReturn(task);

		// Act & Assert
		this.mockMvc.perform(post("/v1/api/task")
		    .content(new ObjectMapper().writeValueAsString(taskDto))
		    .contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.uuid").exists())
				.andExpect(jsonPath("$.name").value("Test Task"))
				.andExpect(jsonPath("$.createdAt").exists())
				.andExpect(jsonPath("$.status").value("IN_PROGRESS"));
	}


	@Test
	public void testDelete() throws Exception {
		// Arrange
		UUID taskId = UUID.randomUUID();

		when(taskService.delete(taskId)).thenReturn(taskId);

		// Act & Assert
		this.mockMvc.perform(delete("/v1/api/task/{uuid}", taskId))
				.andExpect(status().isOk())
				.andExpect(content().string("\"" + taskId.toString() + "\""));

		verify(taskService).delete(taskId);
	}

	@Test
	public void testDeleteAllList() throws Exception {
		// Arrange
		List<UUID> uuids = Arrays.asList(UUID.randomUUID(), UUID.randomUUID());

		// Act & Assert
		this.mockMvc.perform(delete("/v1/api/task/deletion-list")
		    .content(new ObjectMapper().writeValueAsString(uuids))
		    .contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());
	}

	@Test
	public void testDeleteAll() throws Exception {
		// Act & Assert
		this.mockMvc.perform(delete("/v1/api/task/all")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());
	}


}
