package com.fpt.capstone.backend.api.BackEnd.utils.security;


import com.fpt.capstone.backend.api.BackEnd.dto.UserLoginGGTO;
import com.fpt.capstone.backend.api.BackEnd.entity.Users;
import com.fpt.capstone.backend.api.BackEnd.entity.sercurity.security.TokenResponseInfo;
import com.fpt.capstone.backend.api.BackEnd.repository.SettingsRepository;
import com.fpt.capstone.backend.api.BackEnd.repository.UserRepository;
import com.fpt.capstone.backend.api.BackEnd.service.impl.security.UserDetailsImpl;
import com.fpt.capstone.backend.api.BackEnd.service.impl.security.UserDetailsServiceImpl;
import io.jsonwebtoken.*;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    //    private final CommonProperties commonProperties;
    public static final long JWT_TOKEN_VALIDITY = 10 * 60 * 60;

    public String generateJwtToken(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Date now = new Date();
        Date expireDate = new Date(now.getTime() + 86400000);

        return Jwts.builder()
                .setSubject((userDetails.getUsername()))
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, "Bearer")
                .compact();
    }

    public String generateTokenEmail(UserDetailsImpl userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername());
    }

    private String doGenerateToken(Map<String, Object> claims, String email) {
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + 86400000);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, "Bearer")
                .compact();


    }


    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser()
                .setSigningKey("Bearer")
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateJwtToken(String authToken) throws Exception {
        try {
            Jwts.parser()
                    .setSigningKey("Bearer")
                    .parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature!", e.getMessage());

        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token!", e.getMessage());

        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired!", e.getMessage());

        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported!", e.getMessage());

        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty!", e.getMessage());

        }
        return false;
    }

    @Autowired
    private UserRepository userRepository;

    public TokenResponseInfo generateTokenResponseInfo(String email, String password, AuthenticationManager authenticationManager) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        email, password
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Set<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        Users users = userRepository.findByEmail(userDetails.getUsername());

        return TokenResponseInfo.builder()
                .jwtToken(jwt)
                .role(roles.iterator().next())
                .user(new TokenResponseInfo().userResponse(users, roles.iterator().next())).build();
    }


    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private SettingsRepository settingsRepository;
    public TokenResponseInfo generateTokenResponseInfoEmail(UserLoginGGTO userLoginDto) {
        Users users = userRepository.findByEmail(userLoginDto.getEmail());



        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(users.getEmail());
        String jwt = generateTokenEmail(userDetails);
        Set<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
        
        return TokenResponseInfo.builder()
                .jwtToken(jwt)
                .role(roles.iterator().next())
                .user(new TokenResponseInfo().userResponse(users, roles.iterator().next())).build();

    }


}
