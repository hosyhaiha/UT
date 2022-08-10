package com.fpt.capstone.backend.api.BackEnd.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubmitsDTO {
    private Integer id;
    private Integer milestoneId;
    private Integer projectId;
    private String status;
    private String packageFileLink;
    private java.sql.Timestamp submitTime;
    private java.sql.Timestamp created;
    private Integer createdBy;
    private java.sql.Timestamp modified;
    private Integer modifiedBy;
}
