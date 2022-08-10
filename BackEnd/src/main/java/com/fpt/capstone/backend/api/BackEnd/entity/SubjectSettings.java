package com.fpt.capstone.backend.api.BackEnd.entity;

import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "subject_settings")
@Data
public class SubjectSettings extends Auditable implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "subject_id")
    private Integer subjectId;

    @Column(name = "type_id")
    private Integer typeId;

    @Column(name = "title")
    private String title;

    @Column(name = "value")
    private String value;

    @Column(name = "display_order")
    private Integer displayOrder;

    @Column(name = "status")
    private String status;

    @Column(name = "description")
    private String description;
}
