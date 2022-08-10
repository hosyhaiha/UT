package com.fpt.capstone.backend.api.BackEnd.dto.setting;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SettingInputDTO {
    private String id;//
    private String typeId;//
    private String title;
    private String value;
    private String displayOrder;//
    private String status;
    private String description;
}
