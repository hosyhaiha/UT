package com.fpt.capstone.backend.api.BackEnd.dto.setting;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
@Data
@AllArgsConstructor
public class SettingTypeDTO {
    private Integer id;
    private String title;
    private String value;

}
