package com.fpt.capstone.backend.api.BackEnd.dto;

import lombok.Data;

@Data
public class UserChangePWDTO {
    private String oldPassword;
    private String newPassword;
    private String comfirmNewPassword;
}
