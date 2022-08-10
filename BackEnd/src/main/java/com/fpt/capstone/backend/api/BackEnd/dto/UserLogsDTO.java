package com.fpt.capstone.backend.api.BackEnd.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLogsDTO {
    public static final String CHANGE_DATA = "changeData";
    public static final String NEW_UPDATE = "newUpdate";
    public static final String ADD_DATA = "addData";
    private Integer id;
    private String groupType;
    private String action;
    private String desc;
    private Integer refId;
    private String refColumnName;
    private String refTableName;
    private String oldValue;
    private String newValue;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-YYY", timezone = JsonFormat.DEFAULT_TIMEZONE)
    private Date created;
    private Integer createdBy;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = JsonFormat.DEFAULT_TIMEZONE)
    private Date modified;
    private Integer modifiedBy;
    private String createdByUser;
    private String modifiedByUser;
}
