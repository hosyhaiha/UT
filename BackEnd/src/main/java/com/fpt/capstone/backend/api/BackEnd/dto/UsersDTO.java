package com.fpt.capstone.backend.api.BackEnd.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fpt.capstone.backend.api.BackEnd.entity.Provider;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsersDTO {
    private Integer id;
    private String email;
    @JsonIgnore
    private String password;
    private String fullName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date birthday;
    private String tel;
    private String address;
    private String rollNumber;
    private String schoolName;
    private String avatarLink;
    private String facebookLink;
    private String linkedinLink;
    private Integer roleId;
    private String role;
    private String status;
    private Provider provider;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date created;
    private Integer createdBy;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date modified;
    private Integer modifiedBy;
    private String createdByEmail;
    private String modifiedByEmail;

    public UsersDTO(Integer id, String fullName, String rollNumber) {
        this.id = id;
        this.fullName = fullName;
        this.rollNumber = rollNumber;
    }

    public String getAvatarLink() {
        return "https://storage.googleapis.com/slpm/"+avatarLink;
    }
}
