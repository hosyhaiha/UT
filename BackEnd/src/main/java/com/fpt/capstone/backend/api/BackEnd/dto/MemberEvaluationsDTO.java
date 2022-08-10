package com.fpt.capstone.backend.api.BackEnd.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@NoArgsConstructor
public class MemberEvaluationsDTO {
    private Integer id;
    private Integer evaluationId;
    private Integer criteriaId;

    private String criteriaName;
    private Integer convertedLoc;
    private BigDecimal grade;
    private String note;

    private BigDecimal criteriaWeight;

    public MemberEvaluationsDTO(Integer id, Integer evaluationId, Integer criteriaId, Integer convertedLoc, BigDecimal grade, String note) {
        this.id = id;
        this.evaluationId = evaluationId;
        this.criteriaId = criteriaId;
        this.convertedLoc = convertedLoc;
        this.grade = grade;
        this.note = note;
    }

    public MemberEvaluationsDTO(Integer id, Integer evaluationId, Integer criteriaId, BigDecimal grade,
                                String note, BigDecimal criteriaWeight, String criteriaName) {
        this.id = id;
        this.evaluationId = evaluationId;
        this.criteriaId = criteriaId;
        this.grade = grade;
        this.note = note;
        this.criteriaWeight = criteriaWeight;
        this.criteriaName = criteriaName;
    }
}
