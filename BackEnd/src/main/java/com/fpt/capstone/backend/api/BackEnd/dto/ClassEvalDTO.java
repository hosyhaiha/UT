package com.fpt.capstone.backend.api.BackEnd.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassEvalDTO {
   private List<ListIterationEvalDTO> listIterationEvalDTOS;
    private  List<Map<String, Object>> listGradeDTOS;
}
