package com.fpt.capstone.backend.api.BackEnd.service.impl.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fpt.capstone.backend.api.BackEnd.entity.Users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Service
@Builder
public class UserDetailsImpl implements UserDetails {
    private Users users;

    private Integer id;


    private String username;

    private String fullName;

    private String avatarUrl;

    private Date birthday;

    private String tel;

    private String address;

    private String schoolName;

    private String facebookLink;

    private String linkedinLink;

    private String status;

    private Date created;

    private Integer createdBy;

    private Date modified;

    private Integer modifiedBy;

    @JsonIgnore
    private String password;
    private int roleID;
    private boolean enable;

    private Collection<? extends GrantedAuthority> authorities;

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
      return true;
     // return users.isEnabled();
    }
}
