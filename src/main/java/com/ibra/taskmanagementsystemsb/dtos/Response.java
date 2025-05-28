package com.ibra.taskmanagementsystemsb.dtos;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class Response {
    List<TaskDTO> tasks;
    TaskDTO task;
    String status;
    String message;
}
