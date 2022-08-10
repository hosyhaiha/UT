package com.fpt.capstone.backend.api.BackEnd.dto.class_users;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
@Data
@NoArgsConstructor
public class ClassUsersDTO {
    private Integer id;
    private Integer classId;
    private String classCode;
    private Integer projectId;
    private String projectCode;
    private String projectName;
    private Integer userId;
    private String userEmail;
    private Integer projectLeader;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date dropoutDate;
    private String note;
    private BigDecimal ongoingEval;
    private BigDecimal finalPresentEval;
    private BigDecimal finalTopicEval;
    private String status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date created;
    private Integer createdBy;
    private String createdByUser;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date modified;
    private Integer modifiedBy;
    private String modifiedByUser;

    public ClassUsersDTO(Integer id, Integer classId, String classCode, Integer projectId, String projectCode,
                         String projectName, Integer userId, String userEmail, Integer projectLeader, Date dropoutDate,
                         String note, BigDecimal ongoingEval, BigDecimal finalPresentEval, BigDecimal finalTopicEval,
                         String status, Date created, Integer createdBy, String createdByUser, Date modified,
                         Integer modifiedBy, String modifiedByUser)
    {
        this.id = id;
        this.classId = classId;
        this.classCode = classCode;
        this.projectId = projectId;
        this.projectCode = projectCode;
        this.projectName = projectName;
        this.userId = userId;
        this.userEmail = userEmail;
        this.projectLeader = projectLeader;
        this.dropoutDate = dropoutDate;
        this.note = note;
        this.ongoingEval = ongoingEval;
        this.finalPresentEval = finalPresentEval;
        this.finalTopicEval = finalTopicEval;
        this.status = status;
        this.created = created;
        this.createdBy = createdBy;
        this.createdByUser = createdByUser;
        this.modified = modified;
        this.modifiedBy = modifiedBy;
        this.modifiedByUser = modifiedByUser;
    }

    public ClassUsersDTO(Integer classId, Integer userId) {
        this.classId = classId;
        this.userId = userId;
    }

    public ClassUsersDTO(Integer id, Integer classId, Integer projectId, Integer userId) {
        this.id = id;
        this.classId = classId;
        this.projectId = projectId;
        this.userId = userId;
    }
}
