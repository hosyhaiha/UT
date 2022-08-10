package com.fpt.capstone.backend.api.BackEnd.entity;

import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Data
@Table(name = "trackings")
public class Trackings extends Auditable implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "milestone_id")
    private Integer milestoneId;

    @Column(name = "function_id")
    private Integer functionId;

    @Column(name = "assigner_id")
    private Integer assignerId;

    @Column(name = "assignee_id")
    private Integer assigneeId;

    @Column(name = "submit_time")
    private Date submitTime;

    @Column(name = "status")
    private String status;

    @Column(name = "note")
    private String note;
}
