package com.cindy.edu_crud.model.repository;

import com.cindy.edu_crud.model.Task;
import java.time.LocalDateTime;
import java.util.List;

import com.cindy.edu_crud.service.TaskService;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import com.cindy.edu_crud.model.Status;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;
import java.util.UUID;

/**
 * Test class for TaskRepository using Spring Data JPA.
 *
 * Key points:
 * 1. @DataJpaTest and @AutoConfigureTestDatabase:
 *    - @DataJpaTest configures the test environment for JPA tests, setting up database, Hibernate, Spring Data, and DataSource.
 *    - @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) uses the actual database configuration for testing.
 *
 * 2. TestEntityManager usage:
 *    - TestEntityManager is a wrapper around JPA EntityManager designed for testing.
 *    - Used directly in each test method to ensure fresh test data and avoid interdependencies.
 *
 * 3. Advantages over @BeforeEach approach
 *    - Avoids issues with accumulated data between tests that @BeforeEach might cause.
 *    - Provides more control over the database state for each specific test.
 *    - Makes tests self-contained and easier to understand with visible setup within each method.
 *
 * 4. Importance of entityManager.flush():
 *    - Ensures all pending entity changes are immediately persisted to the database.
 *    - Guarantees data is in the database before running repository methods, crucial for accurate testing.
 *
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TaskRepositoryTest {
	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	public void testFindAllByStatus() {
		Task todoTask = Task.builder()
				.uuid(UUID.randomUUID())
				.name("Todo Task")
				.status(Status.TODO)
				.localDueDate(LocalDateTime.now().plusDays(1))
				.build();
		entityManager.persist(todoTask);

		Task doneTask = Task.builder()
				.uuid(UUID.randomUUID())
				.name("Done Task")
				.status(Status.DONE)
				.localDueDate(LocalDateTime.now().minusDays(1))
				.build();

		entityManager.persist(doneTask);
		entityManager.flush();

		List<Task> todoTasks = taskRepository.findAllByStatus(Status.TODO);
		assertThat(todoTasks).hasSize(1);
		assertThat(todoTasks.get(0).getName()).isEqualTo("Todo Task");

		List<Task> doneTasks = taskRepository.findAllByStatus(Status.DONE);
		assertThat(doneTasks).hasSize(1);
		assertThat(doneTasks.get(0).getName()).isEqualTo("Done Task");
	}

	@Test
	public void testFindAllByLocalDueDateBetween() {
		LocalDateTime now = LocalDateTime.now();
		Task task1 = Task.builder()
				.uuid(UUID.randomUUID())
				.name("Task 1")
				.status(Status.TODO)
				.localDueDate(now.minusDays(1))
				.build();
		Task task2 = Task.builder()
				.uuid(UUID.randomUUID())
				.name("Task 2")
				.status(Status.TODO)
				.localDueDate(now.plusDays(1))
				.build();
		entityManager.persist(task1);
		entityManager.persist(task2);
		entityManager.flush();

		LocalDateTime from = now.minusDays(2);
		LocalDateTime to = now.plusDays(2);

		List<Task> tasks = taskRepository.findAllByLocalDueDateBetween(from, to);

		assertThat(tasks).hasSize(2);
	}

	@Test
	public void testFindAllByCreatedAtBetween() {
		LocalDateTime now = LocalDateTime.now();
		Task task1 = Task.builder()
				.uuid(UUID.randomUUID())
				.name("Task 1")
				.status(Status.TODO)
				.localDueDate(now.plusDays(1))
				.createdAt(now.minusMinutes(1))
				.build();
		Task task2 = Task.builder()
				.uuid(UUID.randomUUID())
				.name("Task 2")
				.status(Status.TODO)
				.localDueDate(now.plusDays(2))
				.createdAt(now.plusMinutes(1))
				.build();
		entityManager.persist(task1);
		entityManager.persist(task2);
		entityManager.flush();

		LocalDateTime from = now.minusMinutes(5);
		LocalDateTime to = now.plusMinutes(5);

		List<Task> tasks = taskRepository.findAllByCreatedAtBetween(from, to);

		assertThat(tasks).hasSize(2);
	}

	@Test
	public void testSetLocalDueDate() {
		Task todoTask = Task.builder()
				.uuid(UUID.randomUUID())
				.name("Todo Task")
				.status(Status.TODO)
				.localDueDate(LocalDateTime.now().plusDays(1))
				.build();
		entityManager.persist(todoTask);
		entityManager.flush();

		LocalDateTime newDueDate = LocalDateTime.now().plusDays(7);
		taskRepository.setLocalDueDate(newDueDate, todoTask.getUuid());
		entityManager.clear(); // Clear the persistence context

		Task updatedTask = entityManager.find(Task.class, todoTask.getUuid());
		assertThat(updatedTask.getLocalDueDate()).isEqualToIgnoringNanos(newDueDate);
	}
}
