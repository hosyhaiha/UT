package com.fpt.capstone.backend.api.BackEnd.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data

@NoArgsConstructor
public class FunctionListTrackingDTO {
    private Integer key;
    private String label;
    @JsonIgnore
    private Integer curProject;
    private Boolean chosen=false;

    public FunctionListTrackingDTO(Integer key, String label, Integer curProject) {
        this.key = key;
        this.label = label;
        this.curProject = curProject;
    }

}
