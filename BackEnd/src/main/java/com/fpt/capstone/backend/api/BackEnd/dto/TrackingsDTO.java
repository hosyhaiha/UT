package com.fpt.capstone.backend.api.BackEnd.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class TrackingsDTO {
    private Integer id;
    private Integer milestoneId;
    private String milestoneTitle;
    private Integer functionId;
    private String functionName;
    private Integer iterationId;
    private String iterationName;
    private Integer projectId;
    private String projectCode;
    private String projectName;
    private String classCode;
    private String featureName;
    private Integer assignerId;
    private String assignerName;
    private Integer assigneeId;
    private String assigneeName;
    private String functionDescription;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date submitTime;
    private String status;

    private Boolean isPlanned;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date created;
    private Integer createdBy;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date modified;
    private Integer modifiedBy;
    private String createdByUser;
    private String modifiedByUser;

    private List<UserList> assigneeOptions;
    private List<String> history;

    public TrackingsDTO(Integer id, Integer milestoneId, String milestoneTitle, Integer functionId, String functionName, Integer iterationId, String iterationName, Integer projectId, String projectCode, String projectName, String classCode, String featureName, Integer assignerId, String assignerName, Integer assigneeId, String assigneeName, String description, Date submitTime, String status, Date created, Integer createdBy, Date modified, Integer modifiedBy, String createdByUser, String modifiedByUser) {
        this.id = id;
        this.milestoneId = milestoneId;
        this.milestoneTitle = milestoneTitle;
        this.functionId = functionId;
        this.functionName = functionName;
        this.iterationId = iterationId;
        this.iterationName = iterationName;
        this.projectId = projectId;
        this.projectCode = projectCode;
        this.projectName = projectName;
        this.classCode = classCode;
        this.featureName = featureName;
        this.assignerId = assignerId;
        this.assignerName = assignerName;
        this.assigneeId = assigneeId;
        this.assigneeName = assigneeName;
        this.functionDescription = description;
        this.submitTime = submitTime;
        this.status = status;
        this.created = created;
        this.createdBy = createdBy;
        this.modified = modified;
        this.modifiedBy = modifiedBy;
        this.createdByUser = createdByUser;
        this.modifiedByUser = modifiedByUser;
        this.isPlanned = status.equals("committed");
    }
}


