package com.fpt.capstone.backend.api.BackEnd.dto.class_users;

import lombok.Data;

@Data
public class ClassUserInputDTO {
    private String id;//int
    private String classId;//int
    private String projectId;//int
    private String userId;//int
    private String projectLeader;//0 or 1
    private String dropoutDate;//date
    private String note;
    private String ongoingEval;//Float
    private String finalPresentEval;//Float
    private String finalTopicEval;//Float
    private String status;
}
