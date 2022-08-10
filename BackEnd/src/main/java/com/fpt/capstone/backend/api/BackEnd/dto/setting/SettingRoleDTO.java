package com.fpt.capstone.backend.api.BackEnd.dto.setting;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SettingRoleDTO {
    private Integer value;
    private String label;
}
