package com.cindy.edu_crud.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
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
