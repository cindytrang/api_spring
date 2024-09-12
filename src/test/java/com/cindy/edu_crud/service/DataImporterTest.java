package com.cindy.edu_crud.service;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.ArrayList;
import com.cindy.edu_crud.model.Task;

@SpringBootTest
public class DataImporterTest {
	@Autowired
	private DataImporter dataImporter;

	@MockBean
	private TaskService taskService;

	@Test
	public void importData() {
		dataImporter.importData();
	}

	@Test
	public void readConfig() {
		String name = "abc";
		int expected = 123;
		int actual = dataImporter.readConfig(name);

		assertEquals(expected, actual);
	}

	@Test
	public void updateConfig() {
		int line = 123;
		String name = "abc";
		dataImporter.updateConfig(line, name);
	}

	@Test
	public void readConfigTODO() {
		String name = "abc";
		int expected = 123;
		int actual = dataImporter.readConfig(name);

		assertEquals(expected, actual);
	}

	@Test
	public void readDataTODO() {
		String file = "abc";
		List<String[]> expected = new ArrayList<>();
		List<String[]> actual = dataImporter.readData(file);

		assertEquals(expected, actual);
	}

	@Test
	public void writeData() {
		String filename = "abc";
		List<String[]> data = new ArrayList<>();
		dataImporter.writeData(filename, data);
	}

	@Test
	public void filterNewEntries() {
		List<String[]> newEntries = new ArrayList<>();
		List<String[]> savedEntries = new ArrayList<>();
		List<String[]> expected = new ArrayList<>();
		List<String[]> actual = dataImporter.filterNewEntries(newEntries, savedEntries);

		assertEquals(expected, actual);
	}

	@Test
	public void appendImportedColumn() {
		String[] row = { "abc", "abc", "abc" };
		String[] expected = { "abc", "abc", "abc" };
		String[] actual = dataImporter.appendImportedColumn(row);

		assertArrayEquals(expected, actual);
	}

	@Test
	public void convertToTask() {
		String[] entries = { "abc", "abc", "abc" };
		Task expected = new Task();
		Task actual = dataImporter.convertToTask(entries);

		assertEquals(expected, actual);
	}
}
