package com.cindy.edu_crud.infrastructure;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomExceptionDto {
    private LocalDateTime dateTime;
    private int status;
    private List<String> messages;
    private String shortMessage;
}
