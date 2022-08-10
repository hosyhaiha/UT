package com.fpt.capstone.backend.api.BackEnd.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class addTrackingDTO {
    private Integer assigneeId;
    private List<Integer> functionList;
    private Integer milestoneId;
}
