package com.cindy.edu_crud.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TaskDto {
    private String name;
    private String status;
    private String localDueDate;
}
