package com.fpt.capstone.backend.api.BackEnd.entity;

import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Data
@Table(name = "user_logs")
public class UserLogs extends Auditable implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "group_type")
    private String groupType;

    @Column(name = "`action`")
    private String action;

    @Column(name = "`desc`")
    private String desc;

    @Column(name = "ref_id")
    private Integer refId;

    @Column(name = "ref_column_name")
    private String refColumnName;

    @Column(name = "ref_table_name")
    private String refTableName;

    @Column(name = "old_value")
    private String oldValue;

    @Column(name = "new_value")
    private String newValue;
}
