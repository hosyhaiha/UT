package com.fpt.capstone.backend.api.BackEnd.entity;

import lombok.Data;

@Data
public class FunctionRespone {
    private boolean success;
    private String message;
    private Object data;
    private Integer curProjectId;
}
