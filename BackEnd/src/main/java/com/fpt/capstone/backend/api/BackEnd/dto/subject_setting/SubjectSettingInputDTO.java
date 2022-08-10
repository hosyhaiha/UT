package com.fpt.capstone.backend.api.BackEnd.dto.subject_setting;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SubjectSettingInputDTO {
    private String id; //
    private String subjectId;//
    private String typeId;//
    private String SettingType;
    private String title;
    private String value;
    private String displayOrder;//
    private String status;
    private String description;
}
