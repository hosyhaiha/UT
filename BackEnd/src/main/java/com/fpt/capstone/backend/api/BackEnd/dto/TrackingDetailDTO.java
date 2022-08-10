package com.fpt.capstone.backend.api.BackEnd.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class TrackingDetailDTO {
    private Integer id;
    private Integer milestoneId;
    private String milestoneTitle;
    private Integer functionId;
    private String functionName;
    private String functionDescription;
    private Integer projectId;
    private String projectCode;
    private String projectName;
    private String classCode;
    private String featureName;
    private Integer assignerId;
    private String assignerName;
    private Integer assigneeId;
    private String assigneeName;
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss", timezone = JsonFormat.DEFAULT_TIMEZONE)
    private Date submitTime;
    private String status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date created;
    private Integer createdBy;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date modified;
    private Integer modifiedBy;
    private String createdByUser;
    private String modifiedByUser;
}
