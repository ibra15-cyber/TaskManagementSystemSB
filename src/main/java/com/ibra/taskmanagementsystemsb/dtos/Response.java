package com.ibra.taskmanagementsystemsb.dtos;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class Response {
    private List<TaskDTO> tasks;
    private TaskDTO task;
    private String status;
    private String message;

    private int totalPage;
    private Long totalElement;
}
