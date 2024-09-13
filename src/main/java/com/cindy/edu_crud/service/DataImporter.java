package com.cindy.edu_crud.service;

import com.cindy.edu_crud.model.Status;
import com.cindy.edu_crud.model.Task;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class DataImporter {
    private final TaskService taskService;

    @Autowired
    public DataImporter(TaskService taskService) {
        this.taskService = taskService;
    }
    public void importData() {
        System.out.println(LocalDateTime.now() + " com.cindy.edu_crud.service.DataImporter.importData executed");

        List<String[]> entries = readData("data-to-import.csv");
        int line = readConfig("import.cfg");
        System.out.println(line);
        for (int i = line; i < entries.size(); i++) {
            Task task = convertToTask(entries.get(i));
            taskService.save(task);
        }
        updateConfig(entries.size(), "import.cfg");
    }

    public void updateConfig(int line, String name) {
        try {
            Files.writeString(Path.of(name), String.valueOf(line), StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public int readConfig(String name) {
        try {
            List<String> res = Files.readAllLines(Path.of(name));
            return Integer.parseInt(res.getFirst());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String[]> readData(String file) {
        List<String[]> allData = new ArrayList<>();
        try (CSVReader csvReader = new CSVReader(new FileReader(file))) {
            allData = csvReader.readAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return allData;
    }

    public void writeData(String filename, List<String[]> data) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(filename))) {
            writer.writeAll(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String[]> filterNewEntries(List<String[]> newEntries, List<String[]> savedEntries) {
        List<String[]> entriesToSave = new ArrayList<>();
        for (String[] newEntry : newEntries) {
            boolean isNew = true;
            for (String[] savedEntry : savedEntries) {
                if (Arrays.equals(savedEntry, newEntry)) {
                    isNew = false;
                    break;
                }
            }

            if (isNew) {
                String[] updatedEntry = appendImportedColumn(newEntry);
                entriesToSave.add(updatedEntry);
            }

        }
        return entriesToSave;
    }

    public String[] appendImportedColumn(String[] row) {
        String[] newRow = new String[row.length + 1];
        System.arraycopy(row, 0, newRow, 0, row.length);
        newRow[row.length] = "true";
        return newRow;
    }

    public Task convertToTask(String[] entries) {
        String name = entries[0];
        LocalDateTime createdAt = LocalDateTime.parse(entries[1]);
        Status status = Status.valueOf(entries[2]);
        LocalDateTime localDueDate = LocalDateTime.parse(entries[3]);

        Task task = Task.builder()
                .name(name)
                .createdAt(createdAt)
                .status(status)
                .localDueDate(localDueDate)
                .build();


        return task;
    }

}
