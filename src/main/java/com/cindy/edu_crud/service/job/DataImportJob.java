package com.cindy.edu_crud.service.job;

import com.cindy.edu_crud.service.DataImporter;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DataImportJob {
    DataImporter dataImporter;

    @Scheduled(fixedDelay = 20000) //(cron = "0 * * * * ?")
    public void importData() {
        dataImporter.importData();
    }
}
