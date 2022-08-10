package com.fpt.capstone.backend.api.BackEnd.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "submits")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Data
public class Submits {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "milestone_id")
    private Integer milestoneId;

    @Column(name = "project_id")
    private Integer projectId;

    @Column(name = "package_file_link")
    private String packageFileLink;

    @Column(name = "submit_time")
    private Date submitTime;

    @Column(name = "status")
    private String status;

    @Column(name = "created")
    private String created;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "modified")
    private String modified;

    @Column(name = "modified_by")
    private Integer modifiedBy;

    public Submits(Integer milestoneId, Integer projectId, String status, Integer createdBy, String created) {
        this.milestoneId = milestoneId;
        this.projectId = projectId;
        this.status = status;
        this.createdBy = createdBy;
        this.created = created;
    }
}
