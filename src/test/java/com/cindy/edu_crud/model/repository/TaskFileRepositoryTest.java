package com.cindy.edu_crud.model.repository;

import com.cindy.edu_crud.model.Task;
import com.cindy.edu_crud.service.TaskService;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.*;
import java.time.LocalDateTime;
import com.cindy.edu_crud.model.Status;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class TaskFileRepositoryTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private TaskFileRepository taskFileRepository;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}
	@Test
	void save() {

		UUID testUuid = UUID.randomUUID();
		Task mockTask = Task.builder()
				.uuid(testUuid)
				.name("Test Task")
				.status(Status.TODO)
				.localDueDate(LocalDateTime.now().plusDays(1))
				.build();

		taskFileRepository.save(mockTask);
		Task savedTask = taskFileRepository.save(mockTask);
		Task retrievedTask = taskFileRepository.findById(testUuid).orElse(null);

		assertThat(savedTask).isEqualToComparingFieldByField(retrievedTask);
	}

	@Test
	public void findAll() {
		taskFileRepository.deleteAll();
		UUID testUuid = UUID.randomUUID();
		Task mockTask1 = Task.builder()
				.uuid(testUuid)
				.name("Test Task 1")
				.status(Status.TODO)
				.localDueDate(LocalDateTime.now().plusDays(1))
				.build();

		UUID testUuid2 = UUID.randomUUID();
		Task mockTask2 = Task.builder()
				.uuid(testUuid2)
				.name("Test Task 2")
				.status(Status.TODO)
				.localDueDate(LocalDateTime.now().plusDays(1))
				.build();
		taskFileRepository.save(mockTask1);
		taskFileRepository.save(mockTask2);
		List<Task> expected = Arrays.asList(mockTask1, mockTask2);

		List<Task> actualTasks = taskFileRepository.findAll();

		assertEquals(2, actualTasks.size());
		expected.sort(Comparator.comparing(Task::getName));
		actualTasks.sort(Comparator.comparing(Task::getName));

		assertThat(actualTasks).usingRecursiveComparison().isEqualTo(expected);
	}

	@Test
	public void findById() {
		UUID testUuid = UUID.randomUUID();
		Task mockTask = Task.builder()
				.uuid(testUuid)
				.name("cindy Task")
				.status(Status.TODO)
				.localDueDate(LocalDateTime.now().plusDays(1))
				.build();


		Task savedTask = taskFileRepository.save(mockTask);
		Task objectRetrievedTask = taskFileRepository.findById(testUuid).orElse(null);

		assertThat(objectRetrievedTask).isEqualToComparingFieldByField(savedTask);
	}

	@Test
	public void deleteAll() {
		UUID testUuid = UUID.randomUUID();
		Task mockTask = Task.builder()
				.uuid(testUuid)
				.name("Test Task 1")
				.status(Status.TODO)
				.localDueDate(LocalDateTime.now().plusDays(1))
				.build();

		taskFileRepository.save(mockTask);
		taskFileRepository.deleteAll();
		Task returnedTask = taskFileRepository.findById(testUuid).orElse(null);
		assertNull(returnedTask);
	}

	@Test
	public void delete() {
		UUID testUuid = UUID.randomUUID();
		Task mockTask = Task.builder()
				.uuid(testUuid)
				.name("Test Task 1")
				.status(Status.TODO)
				.localDueDate(LocalDateTime.now().plusDays(1))
				.build();

		taskFileRepository.save(mockTask);
		taskFileRepository.delete(mockTask);
		Task returnedTask = taskFileRepository.findById(testUuid).orElse(null);
		assertNull(returnedTask);
	}

	@Test
	public void setLocalDueDate() {
		UUID testUuid = UUID.randomUUID();
		Task mockTask = Task.builder()
				.uuid(testUuid)
				.name("Test Task 1")
				.status(Status.TODO)
				.localDueDate(LocalDateTime.now().plusDays(1))
				.build();

		LocalDateTime localDateTime = LocalDateTime.now().plusDays(5);
		Task actual = taskFileRepository.save(mockTask);
		taskFileRepository.setLocalDueDate(localDateTime,testUuid);

		assertEquals(actual.getLocalDueDate(), mockTask.getLocalDueDate());
	}

	@Test
	public void testFindAllByCreatedAtBetween() {
		LocalDateTime startDateTime = LocalDateTime.now().plusDays(1);
		LocalDateTime endDateTime = LocalDateTime.now().plusDays(2);

		List<Task> expected = new ArrayList<>();
		List<Task> actual = taskFileRepository.findAllByCreatedAtBetween(startDateTime, endDateTime);

		assertEquals(expected, actual);
	}

	@Test
	public void testFindAllByLocalDueDateBetween() {
		LocalDateTime startDateTime = LocalDateTime.now().plusDays(1);
		LocalDateTime endDateTime = LocalDateTime.now().plusDays(2);

		List<Task> expected = new ArrayList<>();
		List<Task> actual = taskFileRepository.findAllByLocalDueDateBetween(startDateTime, endDateTime);

		assertEquals(expected, actual);
	}

	@Test
	public void testFindAllByStatus() {
 		taskFileRepository.deleteAll();

		Task todoTask = Task.builder()
				.uuid(UUID.randomUUID())
				.name("Test Task 1")
				.status(Status.TODO)
				.localDueDate(LocalDateTime.now().plusDays(1))
				.build();
		taskFileRepository.save(todoTask);

		Task doneTask = Task.builder()
				.uuid(UUID.randomUUID())
				.name("Test Task 2")
				.status(Status.DONE)
				.localDueDate(LocalDateTime.now().plusDays(1))
				.build();
		taskFileRepository.save(doneTask);

		List<Task> doneResult = taskFileRepository.findAllByStatus(Status.DONE);
		assertEquals(1, doneResult.size());
		assertEquals(doneTask.getUuid(), doneResult.get(0).getUuid());
		assertEquals(doneTask.getName(), doneResult.get(0).getName());
		assertEquals(Status.DONE, doneResult.get(0).getStatus());

		List<Task> todoResult = taskFileRepository.findAllByStatus(Status.TODO);
		assertEquals(1, todoResult.size());
		assertEquals(todoTask.getUuid(), todoResult.get(0).getUuid());
		assertEquals(todoTask.getName(), todoResult.get(0).getName());
		assertEquals(Status.TODO, todoResult.get(0).getStatus());

		assertEquals(2, taskFileRepository.findAll().size());
	}
}
