package com.fpt.capstone.backend.api.BackEnd.entity;

import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "classes")
@EntityListeners(AuditingEntityListener.class)
@Data
public class Classes extends Auditable implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "code")
    private String code;

    @Column(name = "trainer_id")
    private Integer trainerId;

    @Column(name = "subject_id")
    private Integer subjectId;

    @Column(name = "year")
    private Integer year;

    @Column(name = "term")
    private String term;

    @Column(name = "status")
    private String status;

    @Column(name = "block5_class")
    private Integer block5Class;
}
