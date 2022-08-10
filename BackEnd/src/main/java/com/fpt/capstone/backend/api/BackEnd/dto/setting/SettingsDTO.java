package com.fpt.capstone.backend.api.BackEnd.dto.setting;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SettingsDTO {

    private Integer id;
    private Integer typeId;
    private String SettingType;
    private String title;
    private String value;
    private Integer displayOrder;
    private String status;
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date created;
    private Integer createdBy;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date modified;
    private Integer modifiedBy;
    private String createdByUser;
    private String modifiedByUser;

}
