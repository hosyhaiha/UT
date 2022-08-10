package com.fpt.capstone.backend.api.BackEnd.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;

@Data
@NoArgsConstructor
public class IterationsDTO {
    private Integer id;
    private Integer subjectId;
    private String name;

    private String subjectName;
    private BigDecimal evaluationWeight;
    private String status;
    private String description;
    private Integer isOngoing;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date created;
    private Integer created_by;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date modified;
    private Integer modified_by;
    private String createdByFullName;
    private String modifiedByFullName;

    private HashMap<Integer, EvaluationCriteriaDTO> evaluationCriteria = new HashMap<>();

    public IterationsDTO(Integer id, Integer subjectId, String name, String subjectName,
                         BigDecimal evaluationWeight, String status, String description, Date created,
                         Integer created_by, Date modified, Integer modified_by, String createdByFullName,
                         String modifiedByFullName) {
        this.id = id;
        this.subjectId = subjectId;
        this.name = name;
        this.subjectName = subjectName;
        this.evaluationWeight = evaluationWeight;
        this.status = status;
        this.description = description;
        this.created = created;
        this.created_by = created_by;
        this.modified = modified;
        this.modified_by = modified_by;
        this.createdByFullName = createdByFullName;
        this.modifiedByFullName = modifiedByFullName;
    }

    public IterationsDTO(Integer id, BigDecimal evaluationWeight, Integer isOngoing, String status) {
        this.id = id;
        this.evaluationWeight = evaluationWeight;
        this.isOngoing = isOngoing;
        this.status = status;
    }

    public IterationsDTO(Integer id, Integer subjectId, String name, String subjectName, BigDecimal evaluationWeight, String status, String description, Integer isOngoing, Date created, Integer created_by, Date modified, Integer modified_by, String createdByFullName, String modifiedByFullName) {
        this.id = id;
        this.subjectId = subjectId;
        this.name = name;
        this.subjectName = subjectName;
        this.evaluationWeight = evaluationWeight;
        this.status = status;
        this.description = description;
        this.isOngoing = isOngoing;
        this.created = created;
        this.created_by = created_by;
        this.modified = modified;
        this.modified_by = modified_by;
        this.createdByFullName = createdByFullName;
        this.modifiedByFullName = modifiedByFullName;
    }
}
