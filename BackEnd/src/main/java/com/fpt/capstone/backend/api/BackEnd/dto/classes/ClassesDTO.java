package com.fpt.capstone.backend.api.BackEnd.dto.classes;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fpt.capstone.backend.api.BackEnd.dto.Permission;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassesDTO {
    private Integer id;
    private String code;
    private Integer trainerId;
    private String trainerName;
    private Integer subjectId;
    private String subjectCode;
    private String subjectName;
    private Integer year;
    private String term;
    private String status;
    private Integer block5Class;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date created;
    private Integer createdBy;
    private String createdByFullName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date modified;
    private Integer modifiedBy;
    private String modifiedByFullName;
}
