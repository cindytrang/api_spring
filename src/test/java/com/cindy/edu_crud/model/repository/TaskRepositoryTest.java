package com.cindy.edu_crud.model.repository;

import com.cindy.edu_crud.model.Task;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import com.cindy.edu_crud.model.Status;
import java.util.UUID;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TaskRepositoryTest {
	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private TestEntityManager testEntityManager;

	// NOT NEEDED
	@Test
	public void findAllByLocalDueDateBetween() {
		LocalDateTime createdAtFrom = null;
		LocalDateTime createdAtTo = null;
		List<Task> expected = new ArrayList<>();
		List<Task> actual = taskRepository.findAllByLocalDueDateBetween(createdAtFrom, createdAtTo);

		assertEquals(expected, actual);
//		assertThat(savedTask).isEqualToComparingFieldByFieldRecursively(objectRetrievedTask);

	}

	@Test
	public void findAllByStatus() {
		Status status = Status.DONE;
		List<Task> expected = new ArrayList<>();
		List<Task> actual = taskRepository.findAllByStatus(status);

		assertEquals(expected, actual);
	}

	@Test
	public void findAllByCreatedAtBetween() {
		LocalDateTime createdAtFrom = null;
		LocalDateTime createdAtTo = null;
		List<Task> expected = new ArrayList<>();
		List<Task> actual = taskRepository.findAllByCreatedAtBetween(createdAtFrom, createdAtTo);

		assertEquals(expected, actual);
	}

	@Test
	public void setLocalDueDate() {
		LocalDateTime localDueDate = null;
		UUID uuid = null;
		taskRepository.setLocalDueDate(localDueDate, uuid);
	}
}
