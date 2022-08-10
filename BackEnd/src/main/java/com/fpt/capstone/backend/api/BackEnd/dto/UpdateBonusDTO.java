package com.fpt.capstone.backend.api.BackEnd.dto;

import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@Data

public class UpdateBonusDTO {
    private Integer studentId;
    private Integer milestoneId;

    private  BigDecimal bonus;
}
