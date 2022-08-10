package com.fpt.capstone.backend.api.BackEnd.dto.subject_setting;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SubjectSettingTypeDTO {
    private Integer id;
    private String title;
    private String value;
}
