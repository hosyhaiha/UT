package com.fpt.capstone.backend.api.BackEnd.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListGradeDTO {
    private Integer studentId;
    private String studentName;
    private String rollNumber;
    private String projectName;
    private BigDecimal totalGrade;
    private Integer iterationId;
    private Integer projectId;
    private Integer milestoneId;
    private String milestoneName;
    private String iterationIdName;
    private BigDecimal iterationGrade;
    private BigDecimal iterationBonus;
    private BigDecimal iterationTotalGrade;
    private String key;
    private BigDecimal ongoingEval;

    public ListGradeDTO(Integer studentId, String studentName, String rollNumber, String projectName,
                        BigDecimal totalGrade, Integer iterationId, String iterationIdName,
                        BigDecimal iterationIdGrade, BigDecimal iterationBonus,
                        BigDecimal ongoingEval, Integer milestoneId, Integer projectId, String milestoneName) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.rollNumber = rollNumber;
        this.projectName = projectName;
        this.totalGrade = totalGrade;
        this.iterationIdName = iterationIdName;
        this.iterationId = iterationId;
        this.ongoingEval = ongoingEval;
        this.iterationGrade = iterationIdGrade;
        this.key = "iterationGrade-" + iterationId;
        this.iterationTotalGrade = getTotalIterationGrade(iterationIdGrade, iterationBonus);
        this.milestoneId = milestoneId;
        this.projectId = projectId;
        this.milestoneName = milestoneName;

    }

    BigDecimal getTotalIterationGrade(BigDecimal iterationIdGrade, BigDecimal iterationBonus) {
        if (iterationBonus == null) {
            iterationBonus = new BigDecimal(0);
        }
        BigDecimal total = iterationIdGrade.add(iterationBonus);
        if (total.intValue() > 10) {
            total = new BigDecimal(10);
        }
        return total;
    }
}
