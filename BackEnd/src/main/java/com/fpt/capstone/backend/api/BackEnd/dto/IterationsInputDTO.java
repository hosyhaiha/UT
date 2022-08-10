package com.fpt.capstone.backend.api.BackEnd.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IterationsInputDTO {
    private String id;// int
    private String subjectId;// int
    private String name;
    private String evaluationWeight;//decimal
    private String isOngoing; // int
    private String status;
    private String description;
}
