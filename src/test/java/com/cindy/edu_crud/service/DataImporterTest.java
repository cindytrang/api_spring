package com.cindy.edu_crud.service;

import com.cindy.edu_crud.model.Status;
import com.cindy.edu_crud.model.Task;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;


@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class DataImporterTest {

	@Autowired
	private DataImporter dataImporter;

	@MockBean
	private TaskService taskService;

	private final String TEST_CONFIG_FILE = "test_import.cfg";
	private final String TEST_CSV_FILE = "test_data.csv";

	@BeforeEach
	public void setup() throws IOException {
		dataImporter = new DataImporter(taskService);

		Files.writeString(Path.of(TEST_CONFIG_FILE), "0");

		List<String[]> testData = Arrays.asList(
				new String[]{"Task1", "2023-09-13T10:00:00", "PENDING", "2023-09-14T10:00:00"},
				new String[]{"Task2", "2023-09-13T11:00:00", "IN_PROGRESS", "2023-09-15T11:00:00"}
		);
		dataImporter.writeData(TEST_CSV_FILE, testData);
	}

	@AfterEach
	public void cleanup() {
		new File(TEST_CONFIG_FILE).delete();
		new File(TEST_CSV_FILE).delete();
	}

	@Test
	public void testImportData() throws IOException {
//		TODO
	}

	@Test
	public void testUpdateConfig() throws IOException {
		dataImporter.updateConfig(5, TEST_CONFIG_FILE);
		assertEquals("5", Files.readString(Path.of(TEST_CONFIG_FILE)));
	}

	@Test
	public void testReadConfig() throws IOException {
		Files.writeString(Path.of(TEST_CONFIG_FILE), "10");
		assertEquals(10, dataImporter.readConfig(TEST_CONFIG_FILE));
	}

	@Test
	public void testReadData() {
		List<String[]> data = dataImporter.readData(TEST_CSV_FILE);
		assertEquals(2, data.size());
		assertArrayEquals(new String[]{"Task1", "2023-09-13T10:00:00", "PENDING", "2023-09-14T10:00:00"}, data.get(0));
	}

	@Test
	public void testWriteData() {
		List<String[]> newEntries = new ArrayList<>();
		newEntries.add(new String[]{"Task1", "2023-09-13T10:00:00", "PENDING", "2023-09-14T10:00:00"});

		dataImporter.writeData("test_write.csv", newEntries);

		List<String[]> readData = dataImporter.readData("test_write.csv");
		assertEquals(1, readData.size());
		for (int i = 0; i < newEntries.size(); i++) {
			assertArrayEquals(newEntries.get(i), readData.get(i),
					"Array at index " + i + " should match");
		}
		new File("test_write.csv").delete();
	}

	@Test
	public void testFilterNewEntries() {
		List<String[]> newEntries = new ArrayList<>();
		newEntries.add(new String[]{"Task1", "2023-09-13T10:00:00", "PENDING", "2023-09-14T10:00:00"});
		newEntries.add(new String[]{"Task3", "2023-09-13T12:00:00", "NEW", "2023-09-16T12:00:00"});

		List<String[]> savedEntries = new ArrayList<>();
		savedEntries.add(new String[]{"Task1", "2023-09-13T10:00:00", "PENDING", "2023-09-14T10:00:00"});

		List<String[]> filteredEntries = dataImporter.filterNewEntries(newEntries, savedEntries);
		assertEquals(1, filteredEntries.size());
		assertArrayEquals(new String[]{"Task3", "2023-09-13T12:00:00", "NEW", "2023-09-16T12:00:00", "true"}, filteredEntries.get(0));
	}

	@Test
	public void testAppendImportedColumn() {
		String[] input = {"Task1", "2023-09-13T10:00:00", "PENDING", "2023-09-14T10:00:00"};
		String[] output = dataImporter.appendImportedColumn(input);
		assertArrayEquals(new String[]{"Task1", "2023-09-13T10:00:00", "PENDING", "2023-09-14T10:00:00", "true"}, output);
	}

	@Test
	public void testConvertToTask() {
		String[] input = {"Task1", "2023-09-13T10:00:00", "TODO", "2023-09-14T10:00:00"};
		Task task = dataImporter.convertToTask(input);

		assertEquals("Task1", task.getName());
		assertEquals(LocalDateTime.parse("2023-09-13T10:00:00"), task.getCreatedAt());
		assertEquals(Status.TODO, task.getStatus());
		assertEquals(LocalDateTime.parse("2023-09-14T10:00:00"), task.getLocalDueDate());
	}
}