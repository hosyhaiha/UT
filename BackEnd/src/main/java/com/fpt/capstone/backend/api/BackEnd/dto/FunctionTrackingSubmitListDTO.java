package com.fpt.capstone.backend.api.BackEnd.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.List;

@Data
@NoArgsConstructor
public class FunctionTrackingSubmitListDTO {
    private Integer functionId;
    private String functionName;
    private Integer featureId;
    private String featureName;
    private String status;
    private Integer assigneeId;
    private String assignee;
    private String assigneeName;
    private String rollNumber;
    @JsonIgnore
    private String iterationName;

    private Integer projectId;

    private String projectName;

    private Integer key;


    public FunctionTrackingSubmitListDTO(Integer functionId, String functionName, Integer featureId, String featureName, String status, Integer assigneeId, String assigneeName, String rollNumber, String iterationName, Integer projectId, String projectName) {
        this.functionId = functionId;
        this.functionName = functionName;
        this.featureId = featureId;
        this.featureName = featureName;
        this.status = status;
        this.assigneeId = assigneeId;
        this.assigneeName = assigneeName;
        this.rollNumber = rollNumber;
        this.iterationName = iterationName;
        this.projectId = projectId;
        this.projectName = projectName;
        this.assignee = formatUserString(assigneeName, rollNumber);
        this.key=functionId;
    }

    private Boolean chosen = false;

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
