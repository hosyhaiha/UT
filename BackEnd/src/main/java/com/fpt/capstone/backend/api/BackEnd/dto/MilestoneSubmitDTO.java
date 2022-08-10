package com.fpt.capstone.backend.api.BackEnd.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MilestoneSubmitDTO {
    private Integer milestoneId;
    private Integer projectId;
    private Integer functionId;
    private String submitStatus;
    private Integer totalPlanCommitFunctions;
    private Integer totalSubmitEvalFunctions;
    private Integer totalPendingFunctions;
    private Integer totalCommittedFunctions;
    private Integer totalSubmittedFunctions;
    private Integer totalEvaluatedFunctions;
    private String functionStatus;
    private String projectName;
    private BigDecimal teamGrade;
    private List<TeamEvaluationsDTO> teamEvaluations = new ArrayList<>();

    public MilestoneSubmitDTO(Integer milestoneId, Integer projectId, String submitStatus, String projectName, String functionStatus, Integer functionId) {
        this.milestoneId = milestoneId;
        this.projectId = projectId;
        this.submitStatus = submitStatus;
        this.projectName = projectName;
        this.functionStatus = functionStatus;
        this.functionId = functionId;
    }
}
