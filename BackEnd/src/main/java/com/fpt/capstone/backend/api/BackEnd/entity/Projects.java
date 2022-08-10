package com.fpt.capstone.backend.api.BackEnd.entity;

import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "projects")
@EntityListeners(AuditingEntityListener.class)
@Data
public class Projects extends Auditable implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "class_id")
    private Integer classId;

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "status")
    private String status;

    @Column(name = "gitlab_token")
    private String gitlabToken;

    @Column(name = "gitlab_url")
    private String gitlabUrl;

    @Column(name = "gitlab_project_id")
    private Integer gitLabProjectId;
}
