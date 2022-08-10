package com.fpt.capstone.backend.api.BackEnd.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UserChangePasswordDto {
    @NotNull
    String oldPassword;
    @NotNull
    String newPassword;
}
