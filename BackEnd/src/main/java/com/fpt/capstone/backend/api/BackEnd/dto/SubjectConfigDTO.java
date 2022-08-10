package com.fpt.capstone.backend.api.BackEnd.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubjectConfigDTO {
    private Integer subjectId;
    private String subjectCode;
    private String subjectName;
    private Integer authorId;
    private String subjectStatus;

    private Integer iterationId;
    private BigDecimal iterationWeight;
    private Integer iterationIsOngoing;
    private String iterationStatus;

    private Integer criteriaId;
    private BigDecimal criteriaWeight;
    private Integer criteriaIsTeamEvaluation;
    private String criteriaStatus;
}
