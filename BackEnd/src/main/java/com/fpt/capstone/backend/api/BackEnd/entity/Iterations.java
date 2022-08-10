package com.fpt.capstone.backend.api.BackEnd.entity;

import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "iterations")
@Data
public class Iterations extends Auditable implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "subject_id")
    private Integer subjectId;

    @Column(name = "name")
    private String name;

    @Column(name = "is_ongoing")
    private Integer isOngoing;

    @Column(name = "evaluation_weight")
    private BigDecimal evaluationWeight;

    @Column(name = "status")
    private String status;

    @Column(name = "description")
    private String description;
}
