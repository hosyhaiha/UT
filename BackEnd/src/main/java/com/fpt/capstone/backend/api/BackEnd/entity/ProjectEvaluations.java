package com.fpt.capstone.backend.api.BackEnd.entity;

import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "team_evaluations")
@EntityListeners(AuditingEntityListener.class)
@Data
public class ProjectEvaluations extends Auditable implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "criteria_id")
    private Integer criteriaId;

    @Column(name = "milestone_id")
    private Integer milestoneId;

    @Column(name = "project_id")
    private Integer projectId;

    @Column(name = "converted_loc")
    private Integer convertedLoc;

    @Column(name = "grade")
    private BigDecimal grade;

    @Column(name = "comment")
    private String comment;
}
