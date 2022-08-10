package com.fpt.capstone.backend.api.BackEnd.entity;

import com.fpt.capstone.backend.api.BackEnd.dto.Permission;
import lombok.Data;

@Data

public class ResponsePaggingObject {
    private boolean success;
    private Integer perPages;
    private Integer currentPage;
    private Long total;
    private String message;
    private Object data;

    public ResponsePaggingObject() {
        this.permission = new Permission();
    }

    private Permission permission;

}
