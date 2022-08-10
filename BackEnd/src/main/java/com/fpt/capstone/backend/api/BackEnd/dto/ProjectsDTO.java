package com.fpt.capstone.backend.api.BackEnd.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectsDTO {
    private Integer id;
    private Integer classId;
    private String classCode;
    private String code;
    private String name;
    private String status;
    private String gitlabToken;
    private String gitlabUrl;
    private Integer gitlabProjectId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date created;
    private Integer createdBy;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date modified;
    private Integer modifiedBy;
    private String createdByUser;
    private String modifiedByUser;

    public ProjectsDTO(Integer id) {
        this.id = id;
    }
}
