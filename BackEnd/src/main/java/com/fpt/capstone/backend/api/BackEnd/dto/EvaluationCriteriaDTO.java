package com.fpt.capstone.backend.api.BackEnd.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
public class EvaluationCriteriaDTO {
    private Integer id;
    private Integer iterationId;
    private String iterationName;
    private BigDecimal evaluationWeight;
    private Integer teamEvaluation;
    private Integer maxLoc;
    private String status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date created;
    private Integer createdBy;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date modified;
    private Integer modifiedBy;
    private String createdByUser;
    private String modifiedByUser;

    public EvaluationCriteriaDTO(Integer id, Integer iterationId, String iterationName, BigDecimal evaluationWeight,
                                  Integer teamEvaluation, Integer maxLoc, String status, Date created,
                                  Integer createdBy, Date modified, Integer modifiedBy, String createdByUser,
                                  String modifiedByUser) {
        this.id = id;
        this.iterationId = iterationId;
        this.iterationName = iterationName;
        this.evaluationWeight = evaluationWeight;
        this.teamEvaluation = teamEvaluation;
        this.maxLoc = maxLoc;
        this.status = status;
        this.created = created;
        this.createdBy = createdBy;
        this.modified = modified;
        this.modifiedBy = modifiedBy;
        this.createdByUser = createdByUser;
        this.modifiedByUser = modifiedByUser;
    }

    public EvaluationCriteriaDTO(Integer id, BigDecimal evaluationWeight, Integer teamEvaluation, String status) {
        this.id = id;
        this.evaluationWeight = evaluationWeight;
        this.teamEvaluation = teamEvaluation;
        this.status = status;
    }

    public EvaluationCriteriaDTO(Integer id, Integer iterationId, BigDecimal evaluationWeight, Integer teamEvaluation) {
        this.id = id;
        this.iterationId = iterationId;
        this.evaluationWeight = evaluationWeight;
        this.teamEvaluation = teamEvaluation;
    }
}
