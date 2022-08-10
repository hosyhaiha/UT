package com.fpt.capstone.backend.api.BackEnd.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
public class ClassSettingsDTO {
    private Integer id;
    private Integer classId;
    private String classCode;
    private Integer typeId;
    private String title;
    private String value;
    private String description;
    private String status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date created;
    private Integer createdBy;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date modified;
    private Integer modifiedBy;
    private String createdByUser;
    private String modifiedByUser;

    public ClassSettingsDTO(Integer id, Integer classId, String classCode, Integer typeId, String title, String value,
                            String description, String status, Date created, Integer createdBy, Date modified,
                            Integer modifiedBy, String createdByUser, String modifiedByUser) {
        this.id = id;
        this.classId = classId;
        this.classCode = classCode;
        this.typeId = typeId;
        this.title = title;
        this.value = value;
        this.description = description;
        this.status = status;
        this.created = created;
        this.createdBy = createdBy;
        this.modified = modified;
        this.modifiedBy = modifiedBy;
        this.createdByUser = createdByUser;
        this.modifiedByUser = modifiedByUser;
    }

    public ClassSettingsDTO(Integer id, Integer classId, String classCode, Integer typeId, String title, String value) {
        this.id = id;
        this.classId = classId;
        this.classCode = classCode;
        this.typeId = typeId;
        this.title = title;
        this.value = value;
    }
}
