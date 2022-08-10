package com.fpt.capstone.backend.api.BackEnd.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class FunctionsDTO {
    private Integer id;
    private Integer projectId;
    private String projectCode;
    private String projectName;
    private Integer featureId;
    private String featureName;
    private String complexity;
    private Integer complexityId;
    private Integer qualityId;
    private Integer trackingId;
    private Integer milestoneId;
    private Integer assigneeId;
    private String assigneeRollNumber;
    private String assigneeFullName;
    private String milestoneName;
    private String name;
    private String description;
    private String trackingNote;
    private Integer convertedLoc;
    private Integer locEvaluationId;
    private Integer classId;
    private String evaluationComment;
    private String priority;
    private String status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date created;
    private Integer createdBy;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date modified;
    private Integer modifiedBy;
    private String createdByUser;
    @JsonIgnore
    private String createdByEmail;
    @JsonIgnore
    private String createdByRollNumber;
    private String modifiedByUser;
    @JsonIgnore
    private String modifiedByEmail;
    @JsonIgnore
    private String modifiedByRollNumber;


    public FunctionsDTO(
            Integer id, Integer assigneeId, String name, String description, String trackingNote,
            Integer convertedLoc, String evaluationComment, String status, Integer complexityId,
            Integer qualityId, String assigneeRollNumber, String assigneeFullName, String milestoneName,
            String projectName, Integer classId, Integer locEvaluationId, Integer trackingId, Integer milestoneId,
            Integer projectId) {
        this.id = id;
        this.assigneeId = assigneeId;
        this.name = name;
        this.description = description;
        this.trackingNote = trackingNote;
        this.convertedLoc = convertedLoc;
        this.evaluationComment = evaluationComment;
        this.status = status;
        this.complexityId = complexityId;
        this.qualityId = qualityId;
        this.assigneeRollNumber = assigneeRollNumber;
        this.assigneeFullName = assigneeFullName;
        this.milestoneName = milestoneName;
        this.projectName = projectName;
        this.classId = classId;
        this.locEvaluationId = locEvaluationId;
        this.trackingId = trackingId;
        this.milestoneId = milestoneId;
        this.projectId = projectId;
    }

    public FunctionsDTO(Integer id,  Integer projectId, String projectCode, String projectName, Integer featureId, String featureName,
                        String name, String description, String priority, String status, Date created,
                        Integer createdBy, Date modified, Integer modifiedBy, String createdByEmail, String createdByRollNumber,
                        String modifiedByEmail, String modifiedByRollNumber) {
        this.id = id;
        this.projectId = projectId;
        this.projectCode = projectCode;
        this.projectName = projectName;
        this.featureId = featureId;
        this.featureName = featureName;
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.status = status;
        this.created = created;
        this.createdBy = createdBy;
        this.modified = modified;
        this.modifiedBy = modifiedBy;
        this.createdByEmail = createdByEmail;
        this.createdByRollNumber = createdByRollNumber;
        this.modifiedByEmail = modifiedByEmail;
        this.modifiedByRollNumber = modifiedByRollNumber;
        this.createdByUser = formatUserString(createdByEmail, createdByRollNumber);
        this.modifiedByUser = formatUserString(modifiedByEmail, modifiedByRollNumber);
        ;
    }

    public String formatUserString(String fullName, String rollNumber) {
        String name = "";
        if (ObjectUtils.isEmpty(rollNumber) || rollNumber == null) {
            rollNumber = "";
        }
        List<String> splitString = Arrays.asList(fullName.split(" "));

        name += splitString.get(splitString.size() - 1).substring(0, 1).toUpperCase() + splitString.get(splitString.size() - 1).substring(1).toLowerCase();

        for (Integer i = 0; i < splitString.size() - 1; i++) {

            name += splitString.get(i).substring(0, 1).toUpperCase();
        }
        return name + rollNumber;
    }
}
