package com.fpt.capstone.backend.api.BackEnd.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

@EntityListeners(AuditingEntityListener.class)
@Data
@Entity
@NoArgsConstructor
@Table(name = "loc_evaluations")
public class LocEvaluations extends Auditable implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "milestone_id")
    private Integer milestoneId;

    @Column(name = "function_id")
    private Integer functionId;

    @Column(name = "complexity_id")
    private Integer complexityId;

    @Column(name = "quality_id")
    private Integer qualityId;

    @Column(name = "converted_loc")
    private Integer convertedLoc;

    @Column(name = "is_late_submit")
    private Byte isLateSubmit;

    @Column(name = "comment")
    private String comment;

    @Column(name = "new_milestone_id")
    private Integer newMilestoneId;

    @Column(name = "new_complexity_id")
    private Integer newComplexityId;

    @Column(name = "new_quality_id")
    private Integer newQualityId;

    @Column(name = "new_converted_loc")
    private Integer newConvertedLoc;

    public LocEvaluations(Integer milestoneId, Integer functionId, Integer complexityId, Integer qualityId, Integer convertedLoc, String comment) {
        this.milestoneId = milestoneId;
        this.functionId = functionId;
        this.complexityId = complexityId;
        this.qualityId = qualityId;
        this.convertedLoc = convertedLoc;
        this.comment = comment;
    }
}
