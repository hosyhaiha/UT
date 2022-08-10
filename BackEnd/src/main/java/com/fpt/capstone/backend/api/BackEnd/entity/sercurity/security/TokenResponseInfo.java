package com.fpt.capstone.backend.api.BackEnd.entity.sercurity.security;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fpt.capstone.backend.api.BackEnd.entity.Provider;
import com.fpt.capstone.backend.api.BackEnd.entity.Users;
import com.fpt.capstone.backend.api.BackEnd.service.impl.security.UserDetailsImpl;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenResponseInfo {
    private String jwtToken;
    private UserResponse user;
    private String role;


    public UserResponse userResponse(Users userDetails, String roles) {
         String host="https://storage.googleapis.com/slpm/";
        String avaLink=null;
        if (userDetails.getAvatarLink()!=null){
            avaLink=host+userDetails.getAvatarLink();
        }
        return UserResponse.builder()
                .id(userDetails.getId())
                .fullName(userDetails.getFullName())
                .email(userDetails.getEmail())
                .avatarUrl(avaLink)
                .birthday(userDetails.getBirthday())
                .address(userDetails.getAddress())
                .tel(userDetails.getTel())
                .rollNumber(userDetails.getRollNumber())
                .schoolName(userDetails.getSchoolName())
                .facebookLink(userDetails.getFacebookLink())
                .linkedinLink(userDetails.getLinkedinLink())
                .status(userDetails.getStatus())
                .roleId(userDetails.getSettings().getId())
                .created(userDetails.getCreated())
                .createdBy(userDetails.getCreatedBy())
                .modified(userDetails.getModified())
                .modifiedBy(userDetails.getModifiedBy())
                .Provider(userDetails.getProvider())
                .role(roles).build();
    }
}

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class UserResponse {

    private long id;

    private String fullName;

    private String email;

    private String avatarUrl;

    private String role;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date birthday;

    private String tel;

    private String rollNumber;

    private String address;

    private String schoolName;

    private String facebookLink;

    private String linkedinLink;

    private Integer roleId;

    private String status;

    private Provider Provider;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date created;

    private Integer createdBy;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date modified;

    private Integer modifiedBy;
}