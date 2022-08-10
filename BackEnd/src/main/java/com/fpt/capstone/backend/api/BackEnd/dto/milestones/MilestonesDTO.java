package com.fpt.capstone.backend.api.BackEnd.dto.milestones;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
public class MilestonesDTO {
    private Integer id;
    private Integer iterationId;
    private String iterationName;
    private Integer classId;
    private String classCode;
    private String title;
    private String description;

    private Integer personalEvalCriteria;

    private Integer evalCriteriaWeight;

    private Integer evalCriteriaMaxLoc;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date from;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date to;

    private String status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date created;

    private Integer createdBy;

    private String createdByUser;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date modified;

    private Integer modifiedBy;
    private String modifiedByUser;

    public MilestonesDTO(
            Integer id, Integer iterationId, String iterationName, Integer classId, String classCode,
            String title, String description, Date from, Date to, String status, Date created,
            Integer createdBy, String createdByUser, Date modified, Integer modifiedBy,
            String modifiedByUser) {
        this.id = id;
        this.iterationId = iterationId;
        this.iterationName = iterationName;
        this.classId = classId;
        this.classCode = classCode;
        this.title = title;
        this.description = description;
        this.from = from;
        this.to = to;
        this.status = status;
        this.created = created;
        this.createdBy = createdBy;
        this.createdByUser = createdByUser;
        this.modified = modified;
        this.modifiedBy = modifiedBy;
        this.modifiedByUser = modifiedByUser;
    }

    public MilestonesDTO(Integer id, Integer evalCriteriaWeight, Integer evalCriteriaMaxLoc) {
        this.id = id;
        this.evalCriteriaWeight = evalCriteriaWeight;
        this.evalCriteriaMaxLoc = evalCriteriaMaxLoc;
    }

    public MilestonesDTO(Integer id, Integer iterationId, String iterationName) {
        this.id = id;
        this.iterationId = iterationId;
        this.iterationName = iterationName;
    }
}
