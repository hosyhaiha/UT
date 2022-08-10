package com.fpt.capstone.backend.api.BackEnd.service.impl.security;


import com.fpt.capstone.backend.api.BackEnd.entity.Provider;
import com.fpt.capstone.backend.api.BackEnd.entity.Users;
import com.fpt.capstone.backend.api.BackEnd.repository.SettingsRepository;
import com.fpt.capstone.backend.api.BackEnd.repository.UserRepository;
import com.fpt.capstone.backend.api.BackEnd.service.validate.ConstantsStatus;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users user = userRepository.findByEmail(email);
        if (user != null) {
//            GrantedAuthority authorities = user.getSettings().stream()
//                    .map(role -> new SimpleGrantedAuthority(role.getName()))
//                    .collect(Collectors.toSet());
            final List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
            authorities.add((GrantedAuthority) new SimpleGrantedAuthority(user.getSettings().getValue()));


            return UserDetailsImpl.builder()
                    .id(user.getId())
                    .fullName(user.getFullName())
                    .avatarUrl(user.getAvatarLink())
                    .username(user.getEmail())
                    .password(user.getPassword())
                    .authorities(authorities).build();
        }
        else {
            throw new UsernameNotFoundException(email);
        }
    }
    @Autowired
    private SettingsRepository settingsRepository;
    public void processOAuthPostLogin(String email) {
        Users existUser = userRepository.getUserByEmail(email);
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timeStamp= formatter.format(new Date(System.currentTimeMillis()));
        int createdBy=1;


        if (existUser == null) {
            Users newUsers = new Users();
            newUsers.setEmail(email);
            newUsers.setFullName(email.substring(0,email.indexOf("@")));
            newUsers.setEnabled(true);
            newUsers.setCreated(Timestamp.valueOf(timeStamp));
            newUsers.setProvider(Provider.GOOGLE);
            newUsers.setSettings(settingsRepository.getById(10));
            newUsers.setStatus(ConstantsStatus.active.toString());
            userRepository.save(newUsers);
            System.out.println("Created new user: " + email);
        }

    }
}
