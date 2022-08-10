package com.fpt.capstone.backend.api.BackEnd.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamCriteriaDTO {
    private Integer criteriaId;
    private String criteriaName;
    private Double weight;
    private String comment;
    private Double Grade;
    private Integer ProjectId;
    private Integer teamEvaluationId;
}
