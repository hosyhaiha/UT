package com.fpt.capstone.backend.api.BackEnd.dto.subject_setting;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class SubjectSettingsDTO {
    private Integer id;
    private Integer subjectId;
    private String subjectName;
    private Integer typeId;
    private String SettingType;
    private String title;
    private String value;
    private Integer displayOrder;
    private String status;
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date created;
    private Integer createdBy;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date modified;
    private Integer modifiedBy;
    private String createdByUser;
    private String modifiedByUser;
}
