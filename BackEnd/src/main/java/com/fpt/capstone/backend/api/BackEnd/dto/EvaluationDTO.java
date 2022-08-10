package com.fpt.capstone.backend.api.BackEnd.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class EvaluationDTO {
    private Integer iterationEvaluationId;
    private BigDecimal iterationGrade;
    private BigDecimal iterationWeight;
    private Integer teamEvalId;
    private Integer teamEvalCriteriaId;
    private BigDecimal teamEvalGrade;
    private BigDecimal evaluationWeight;
    private Integer personalEvalCriteriaId;
    private Integer memberEvalId;
    private Integer memberTotalLoc;
    private Integer memberMaxLoc;
    private BigDecimal memberEvalGrade;

    private Integer classUserId;
    private BigDecimal finalPresentGrade;

    private BigDecimal onGoingGrade;

    private BigDecimal finalTopicGrade;

    // personal evaluation
    public EvaluationDTO(
            Integer iterationEvaluationId, BigDecimal iterationGrade, BigDecimal iterationWeight,
            BigDecimal evaluationWeight, Integer personalEvalCriteriaId, Integer memberEvalId,
            Integer memberTotalLoc, Integer memberMaxLoc, BigDecimal memberEvalGrade, BigDecimal onGoingGrade,
            BigDecimal finalTopicGrade, Integer classUserId, BigDecimal finalPresentGrade) {
        this.iterationEvaluationId = iterationEvaluationId;
        this.iterationGrade = iterationGrade;
        this.iterationWeight = iterationWeight;
        this.evaluationWeight = evaluationWeight;
        this.personalEvalCriteriaId = personalEvalCriteriaId;
        this.memberEvalId = memberEvalId;
        this.memberTotalLoc = memberTotalLoc;
        this.memberMaxLoc = memberMaxLoc;
        this.memberEvalGrade = memberEvalGrade;
        this.onGoingGrade = onGoingGrade;
        this.finalTopicGrade = finalTopicGrade;
        this.classUserId = classUserId;
        this.finalPresentGrade = finalPresentGrade;
    }
}
