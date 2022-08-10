package com.fpt.capstone.backend.api.BackEnd.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "iteration_evaluations")
public class IterationEvaluations extends Auditable implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "milestone_id")
    private Integer milestoneId;

    @Column(name = "class_user_id")
    private Integer classUserId;

    @Column(name = "bonus")
    private BigDecimal bonus;

    @Column(name = "grade")
    private BigDecimal grade;

    @Column(name = "note")
    private String note;

    public IterationEvaluations(Integer milestoneId, Integer classUserId, BigDecimal grade) {
        this.milestoneId = milestoneId;
        this.classUserId = classUserId;
        this.grade = grade;
    }

    public IterationEvaluations(Integer milestoneId, Integer classUserId, BigDecimal bonus, BigDecimal grade) {
        this.milestoneId = milestoneId;
        this.classUserId = classUserId;
        this.bonus = bonus;
        this.grade = grade;
    }
}
