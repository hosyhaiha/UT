package com.fpt.capstone.backend.api.BackEnd.entity;

import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "evaluation_Criterials")
@EntityListeners(AuditingEntityListener.class)
@Data
public class EvaluationCriteria extends Auditable  implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "iteration_id")
    private Integer iterationId;

    @Column(name = "name")
    private String name;

    @Column(name = "evaluation_weight")
    private BigDecimal evaluationWeight;

    @Column(name = "team_evaluation")
    private Integer teamEvaluation;

    @Column(name = "max_loc")
    private Integer maxLoc;

    @Column(name = "status")
    private String status;
}
