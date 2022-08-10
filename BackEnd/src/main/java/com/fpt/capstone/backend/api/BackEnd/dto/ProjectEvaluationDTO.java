package com.fpt.capstone.backend.api.BackEnd.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectEvaluationDTO {
    private Integer projectEvalId;
    private BigDecimal grade;
    private BigDecimal evaluationWeight;
    private BigDecimal totalGrade;

    private Integer projectId;

    private Integer iterationId;

    public ProjectEvaluationDTO(Integer projectEvalId, BigDecimal grade, BigDecimal evaluationWeight, Integer projectId, Integer iterationId) {
        this.projectEvalId = projectEvalId;
        this.grade = grade;
        this.evaluationWeight = evaluationWeight;
        this.projectId = projectId;
        this.iterationId = iterationId;
        this.totalGrade=grade.multiply(evaluationWeight).multiply(BigDecimal.valueOf(100));
    }
}
