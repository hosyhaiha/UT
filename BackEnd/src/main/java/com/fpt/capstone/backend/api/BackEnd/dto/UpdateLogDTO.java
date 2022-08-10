package com.fpt.capstone.backend.api.BackEnd.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateLogDTO {
    //newvalue=milestoneId
    private Integer logId;
    private String description;
    @JsonIgnore
    private String refColumnName;

    private String newValue;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date created;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date modified;
    private String createdByUser;
    private String modifiedByUser;

    public UpdateLogDTO(Integer logId, String description, String refColumnName, String newValue, Date created, Date modified, String createdByUser, String modifiedByUser) {
        this.logId = logId;
        this.description = description;
        this.refColumnName = refColumnName;
        this.newValue = newValue;
        this.created = created;
        this.modified = modified;
        this.createdByUser = createdByUser;
        this.modifiedByUser = modifiedByUser;
    }
    private String milestonTitle;
}
