package com.fpt.capstone.backend.api.BackEnd.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamEvaluationsDTO {
    private Integer id;
    private Integer criteriaId;
    private Integer iterationId;
    private String criteriaName;
    private Integer milestoneId;
    private Integer projectId;
    private BigDecimal grade;
    private BigDecimal weight;

    private BigDecimal studentIteGrade;
    private BigDecimal studentIteBonus;
    private Integer classUserId;

    private Integer iterationEvaluationId;

    private String comment;
    private java.sql.Timestamp created;
    private Integer createdBy;
    private java.sql.Timestamp modified;
    private Integer modifiedBy;

    public TeamEvaluationsDTO(Integer id, String criteriaName, Integer criteriaId, Integer milestoneId,
                              Integer projectId, BigDecimal grade, BigDecimal weight, String comment, Integer iterationId) {
        this.id = id;
        this.criteriaName = criteriaName;
        this.criteriaId = criteriaId;
        this.milestoneId = milestoneId;
        this.projectId = projectId;
        this.grade = grade;
        this.weight = weight;
        this.comment = comment;
        this.iterationId = iterationId;
    }

    public TeamEvaluationsDTO(Integer id, Integer criteriaId, String criteriaName,
                              Integer milestoneId, Integer projectId, BigDecimal grade,
                              BigDecimal weight, String comment, BigDecimal studentIteGrade,
                              BigDecimal studentIteBonus, Integer iterationEvaluationId, Integer classUserId) {
        this.id = id;
        this.criteriaId = criteriaId;
        this.criteriaName = criteriaName;
        this.milestoneId = milestoneId;
        this.projectId = projectId;
        this.grade = grade;
        this.weight = weight;
        this.comment = comment;
        this.studentIteGrade = studentIteGrade;
        this.studentIteBonus = studentIteBonus;
        this.iterationEvaluationId = iterationEvaluationId;
        this.classUserId = classUserId;
    }
}
