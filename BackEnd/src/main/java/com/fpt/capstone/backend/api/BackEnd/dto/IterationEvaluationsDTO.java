package com.fpt.capstone.backend.api.BackEnd.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IterationEvaluationsDTO {
    private Integer id;
    private Integer iterationId;
    private Integer classUserId;
    private Integer studentId;
    private String rollNumber;
    private String fullName;
    private Integer projectId;
    private String projectName;
    private Integer milestoneId;
    private BigDecimal evalWeight;
    private BigDecimal bonus;

    private BigDecimal teamEvalGrade;
    private Integer memberEvalId;
    private Integer memberEvalLoc;
    private BigDecimal totalGrade;

    private BigDecimal individualEval;
    private String note;
    private String milestoneName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date created;
    private Integer createdBy;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date modified;
    private Integer modifiedBy;
    private String createdByUser;
    private String modifiedByUser;

    public IterationEvaluationsDTO(
            Integer id, Integer iterationId, Integer classUserId, Integer studentId,
            String rollNumber, String fullName, Integer projectId, String projectName,
            BigDecimal bonus, Integer memberEvalId, Integer memberEvalLoc,
            BigDecimal totalGrade,BigDecimal individualEval, String note,String milestoneName,
            Date created, Integer createdBy, Date modified, Integer modifiedBy, String createdByUser,
            String modifiedByUser) {
        this.id = id;
        this.iterationId = iterationId;
        this.classUserId = classUserId;
        this.studentId = studentId;
        this.rollNumber = rollNumber;
        this.fullName = fullName;
        this.projectId = projectId;
        this.projectName = projectName;
        this.bonus = bonus;
        this.memberEvalId = memberEvalId;
        this.memberEvalLoc = memberEvalLoc;
        this.totalGrade = totalGrade;
        this.individualEval=individualEval;
        this.note = note;
        this.milestoneName=milestoneName;
        this.created = created;
        this.createdBy = createdBy;
        this.modified = modified;
        this.modifiedBy = modifiedBy;
        this.createdByUser = createdByUser;
        this.modifiedByUser = modifiedByUser;
    }
}
