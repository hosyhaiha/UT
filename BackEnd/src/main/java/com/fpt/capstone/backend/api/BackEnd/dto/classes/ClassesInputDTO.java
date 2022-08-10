package com.fpt.capstone.backend.api.BackEnd.dto.classes;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClassesInputDTO {
    private String id; //int
    private String code;
    private String trainerId;// int
    private String subjectId; //int
    private String year; //int
    private String term;
    private String status;
    private String block5Class;//int 0-1
}
