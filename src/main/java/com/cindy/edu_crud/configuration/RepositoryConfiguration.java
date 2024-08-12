package com.cindy.edu_crud.configuration;

import com.cindy.edu_crud.model.RepositoryContext;
import com.cindy.edu_crud.model.repository.TaskFileRepository;
import com.cindy.edu_crud.model.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.stereotype.Service;

//@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@Configuration
@RequiredArgsConstructor
public class RepositoryConfiguration {
    private final TaskRepository taskRepository;
    private RepositoryContext repositoryContext = new RepositoryContext();

    @Bean("repositoryContext")
    @ConditionalOnProperty(prefix = "app.persistence", name = "type", havingValue = "file")
    public RepositoryContext TaskFileRepository() {
        System.out.println("Using file strategy");
        repositoryContext.setRepository(new TaskFileRepository());
        return repositoryContext;
    }

    @Bean("repositoryContext")
    @Primary
    @ConditionalOnProperty(prefix = "app.persistence", name = "type", havingValue = "db")
    public RepositoryContext taskDatabase(TaskRepository taskRepository) {
        System.out.println("Using database strategy");
        repositoryContext.setRepository(taskRepository);
        return repositoryContext;
    }
}
