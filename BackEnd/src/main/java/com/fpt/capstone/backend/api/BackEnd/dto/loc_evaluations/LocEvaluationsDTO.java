package com.fpt.capstone.backend.api.BackEnd.dto.loc_evaluations;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocEvaluationsDTO {
    private Integer id;
    private Integer milestoneId;
    private Integer functionId;
    private String functionName;
    private Integer featureId;
    private String featureName;
    private Integer complexityId;
    private String complexityTitle;
    private String complexityValue;
    private Integer qualityId;
    private String qualityTitle;
    private String qualityValue;
    private Integer convertedLoc;
    private Byte isLateSubmit;
    private String comment;
    private Integer newMilestoneId;
    private Integer newComplexityId;
    private Integer newQualityId;
    private Integer newConvertedLoc;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date created;
    private Integer createdBy;
    private String createdByName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date modified;
    private Integer modifiedBy;
    private String modifiedByName;
}
