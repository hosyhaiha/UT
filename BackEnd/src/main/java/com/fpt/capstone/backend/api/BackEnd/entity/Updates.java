package com.fpt.capstone.backend.api.BackEnd.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class Updates {
    private String note;
    private String created_by;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = JsonFormat.DEFAULT_TIMEZONE)
    private Date created;
}
