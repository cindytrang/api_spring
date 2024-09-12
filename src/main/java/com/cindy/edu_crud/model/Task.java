package com.cindy.edu_crud.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Task implements Serializable {
    @Id
    @Builder.Default
    private UUID uuid = UUID.randomUUID();
    private String name;
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
    @Builder.Default
    private Status status = Status.TODO;
    private LocalDateTime localDueDate;
//  private LocalDateTime lastModifiedAt;
}
