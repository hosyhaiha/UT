package com.fpt.capstone.backend.api.BackEnd.entity;

import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "class_settings")
@EntityListeners(AuditingEntityListener.class)
@Data
public class ClassSettings extends Auditable  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "class_id")
    private Integer classId;

    @Column(name = "type_id")
    private Integer typeId;

    @Column(name = "title")
    private String title;

    @Column(name = "value")
    private String value;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    private String status;
}
