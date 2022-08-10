package com.fpt.capstone.backend.api.BackEnd.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "member_evaluations")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Data
public class MemberEvaluations {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "evaluation_id")
    private Integer evaluationId;

    @Column(name = "criteria_id")
    private Integer criteriaId;

    @Column(name = "converted_loc")
    private Integer convertedLoc;

    @Column(name = "grade")
    private BigDecimal grade;

    @Column(name = "note")
    private String note;

    @Column(name = "created")
    private String created;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "modified")
    private String modified;

    @Column(name = "modified_by")
    private Integer modifiedBy;

    public MemberEvaluations(Integer evaluationId, Integer criteriaId, Integer convertedLoc,
                             BigDecimal grade, Integer createdBy, String created) {
        this.evaluationId = evaluationId;
        this.criteriaId = criteriaId;
        this.convertedLoc = convertedLoc;
        this.grade = grade;
        this.created = created;
        this.createdBy = createdBy;
    }
}
