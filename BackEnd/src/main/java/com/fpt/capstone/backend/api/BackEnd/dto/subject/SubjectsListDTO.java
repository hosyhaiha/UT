package com.fpt.capstone.backend.api.BackEnd.dto.subject;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SubjectsListDTO {
    private Integer id;
    private String subjectCode;
    private String subjectName;
}
