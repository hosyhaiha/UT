package com.fpt.capstone.backend.api.BackEnd.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "users")
@Data
public class Users implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "full_name")
    private String fullName;

    @JsonFormat(pattern = "dd-MM-yyyy")
    @Column(name = "birthday")
    private Date birthday;

    @Column(name = "tel")
    private String tel;

    @Column(name = "address")
    private String address;

    @Column(name = "roll_number")
    private String rollNumber;

    @Column(name = "school_name")
    private String schoolName;

    @Column(name = "gender")
    private String gender;

    @Column(name = "avatar_link")
    private String avatarLink;

    @Column(name = "facebook_link")
    private String facebookLink;

    @Column(name = "linkedin_link")
    private String linkedinLink;

    @ManyToOne
    @JoinColumn(name = "role_id")
    @JsonBackReference
    private Settings settings;

    @Column(name = "status")
    private String status;

    @JsonFormat(pattern = "dd-MM-yyyy")
    @Column(name = "created")
    private Date created;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "modified")
    private Date modified;

    @JsonFormat(pattern = "dd-MM-yyyy")
    @Column(name = "modified_by")
    private Integer modifiedBy;

    @Column(name = "verification_code")
    private String verificationCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "auth_provider")
    private Provider Provider;
    private boolean enabled = true;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
