package com.fpt.capstone.backend.api.BackEnd.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Data
@NoArgsConstructor
public class MilestoneCriteriaDTO {
    private Integer milestoneId;
    private Integer iterationId;
    private Integer criteriaId;
    private Integer isTeamCriteria;
    private BigDecimal evalWeight;

    private Integer submitId;

    private Integer teamEvaluationId;

    private List<EvaluationCriteriaDTO> evaluationCriteriaList = new ArrayList<>();

    // constructor for member evaluation criteria
    public MilestoneCriteriaDTO(Integer milestoneId, Integer iterationId, Integer criteriaId,
                                Integer isTeamCriteria, BigDecimal evalWeight) {
        this.milestoneId = milestoneId;
        this.iterationId = iterationId;
        this.criteriaId = criteriaId;
        this.isTeamCriteria = isTeamCriteria;
        this.evalWeight = evalWeight;
    }

    // constructor for team evaluation
    public MilestoneCriteriaDTO(Integer milestoneId, Integer criteriaId) {
        this.milestoneId = milestoneId;
        this.criteriaId = criteriaId;
    }
}
