package com.fpt.capstone.backend.api.BackEnd.entity;

import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "class_users")
@EntityListeners(AuditingEntityListener.class)
@Data
public class ClassUsers extends Auditable implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "class_id")
    private Integer classId;

    @Column(name = "project_id")
    private Integer projectId;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "project_leader")
    private Integer projectLeader;

    @Column(name = "dropout_date")
    private Date dropoutDate;

    @Column(name = "note")
    private String note;

    @Column(name = "ongoing_eval")
    private BigDecimal ongoingEval;

    @Column(name = "final_present_eval")
    private BigDecimal finalPresentEval;

    @Column(name = "final_topic_eval")
    private BigDecimal finalTopicEval;

    @Column(name = "status")
    private String status;
}
