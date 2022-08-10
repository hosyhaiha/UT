package com.fpt.capstone.backend.api.BackEnd.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListIterationEvalDTO {
    private String iterationName;
    private String key;


    private Integer id;

    public ListIterationEvalDTO(String iterationName, Integer id) {
        this.iterationName = iterationName;
        this.id = id;
        this.key="iterationGrade-"+id;
    }
}
