package com.fpt.capstone.backend.api.BackEnd.dto.milestones;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MilestoneInputDTO {
    private String id;//int
    private String iterationId;//int
    private String classId;//int
    private String title;
    private String description;
    private String from;//date
    private String to;//date
    private String status;
}
