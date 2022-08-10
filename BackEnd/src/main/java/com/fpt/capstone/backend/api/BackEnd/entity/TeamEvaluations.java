package com.fpt.capstone.backend.api.BackEnd.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
@NoArgsConstructor
@Table(name = "team_evaluations")
public class TeamEvaluations {
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

    @Column(name = "grade")
    private BigDecimal grade;

    @Column(name = "comment")
    private String comment;

    @Column(name = "created")
    private String created;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "modified")
    private String modified;

    @Column(name = "modified_by")
    private Integer modifiedBy;


    public TeamEvaluations(Integer id, Integer criteriaId, Integer milestoneId, Integer projectId, BigDecimal grade, String comment) {
        this.id = id;
        this.criteriaId = criteriaId;
        this.milestoneId = milestoneId;
        this.projectId = projectId;
        this.grade = grade;
        this.comment = comment;
    }

    public TeamEvaluations(Integer criteriaId, Integer milestoneId, Integer projectId,
                           BigDecimal grade, Integer createdBy, String created) {
        this.criteriaId = criteriaId;
        this.milestoneId = milestoneId;
        this.projectId = projectId;
        this.grade = grade;
        this.created = created;
        this.createdBy = createdBy;
    }
}
