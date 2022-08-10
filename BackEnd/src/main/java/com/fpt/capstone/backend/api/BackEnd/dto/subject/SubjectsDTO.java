package com.fpt.capstone.backend.api.BackEnd.dto.subject;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
public class SubjectsDTO {
    private Integer id;
    private String code;
    private String name;
    private Integer authorId;
    private String authorName;
    private String status;
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date created;
    private Integer created_by;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date modified;
    private Integer modified_by;
    private String createdByUser;
    private String modifiedByUser;

    public SubjectsDTO(Integer id, String code, String name, Integer authorId, String authorName, String status,
                       String description, Date created, Integer created_by, Date modified, Integer modified_by,
                       String createdByUser, String modifiedByUser) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.authorId = authorId;
        this.authorName = authorName;
        this.status = status;
        this.description = description;
        this.created = created;
        this.created_by = created_by;
        this.modified = modified;
        this.modified_by = modified_by;
        this.createdByUser = createdByUser;
        this.modifiedByUser = modifiedByUser;
    }

    public SubjectsDTO(Integer id, String code, String name, Integer authorId, String status) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.authorId = authorId;
        this.status = status;
    }
}
