package com.fpt.capstone.backend.api.BackEnd.entity;

import com.fpt.capstone.backend.api.BackEnd.dto.Permission;
import lombok.Data;

@Data
public class TrackingUpdateResponse {
    private boolean success;
    private Integer perPages;
    private Integer currentPage;
    private Long total;
    private String message;
    private Object data;

    private String functionName;
    private String milestoneName;
    private String evaluateComment;
    private Integer curClass;


}
