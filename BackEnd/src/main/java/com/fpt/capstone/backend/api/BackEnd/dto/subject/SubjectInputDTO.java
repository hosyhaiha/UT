package com.fpt.capstone.backend.api.BackEnd.dto.subject;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SubjectInputDTO {
    private String id;//int
    private String code;//check trùng
    private String name;//check trùng
    private String authorId;//int
    private String status;
    private String description;
}
