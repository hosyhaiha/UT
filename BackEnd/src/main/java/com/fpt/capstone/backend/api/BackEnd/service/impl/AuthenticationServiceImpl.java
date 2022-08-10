package com.fpt.capstone.backend.api.BackEnd.service.impl;


import com.fpt.capstone.backend.api.BackEnd.dto.UserChangePWDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.UserLoginDto;
import com.fpt.capstone.backend.api.BackEnd.dto.UserLoginGGTO;
import com.fpt.capstone.backend.api.BackEnd.dto.UserRegisterDTO;
import com.fpt.capstone.backend.api.BackEnd.entity.ApiResponse;
import com.fpt.capstone.backend.api.BackEnd.entity.Provider;
import com.fpt.capstone.backend.api.BackEnd.entity.Users;
import com.fpt.capstone.backend.api.BackEnd.entity.sercurity.security.TokenResponseInfo;
import com.fpt.capstone.backend.api.BackEnd.repository.SettingsRepository;
import com.fpt.capstone.backend.api.BackEnd.repository.UserRepository;
import com.fpt.capstone.backend.api.BackEnd.service.AuthenticationService;
import com.fpt.capstone.backend.api.BackEnd.service.MailService;
import com.fpt.capstone.backend.api.BackEnd.service.impl.security.UserDetailsServiceImpl;
import com.fpt.capstone.backend.api.BackEnd.service.validate.ConstantsStatus;
import com.fpt.capstone.backend.api.BackEnd.service.validate.Validate;
import com.fpt.capstone.backend.api.BackEnd.utils.security.JwtUtils;
import lombok.AllArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Date;

@Service
@AllArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtils jwtUtils;

    // private final CommonProperties commonProperties;

    //private final MailServiceImpl mailService;

    private AuthenticationManager authenticationManager;

    //private final CommonUtils utils;

    @Override
    public ResponseEntity<?> authenticate(UserLoginDto userLoginDto) throws AuthenticationException {
        if ((userLoginDto.getEmail().trim().isEmpty())
                || (userLoginDto.getPassword().trim()).isEmpty()) {
            logger.error("Parameter invalid!");
            return ResponseEntity.status(401).body(
                    ApiResponse.builder()
                            .success(false)
                            .message("Parameter invalid!").build()
            );
        }
        Users user = userRepository.findByEmail(userLoginDto.getEmail());

        if (user.getStatus().equals("inactive") || user.getStatus().equals("")) {
            logger.error("Account INACTIVE!");
            return ResponseEntity.status(401).body(
                    ApiResponse.builder()
                            .success(false)
                            .message("Account INACTIVE!").build()
            );
        }
        if (!user.isEnabled()) {
            logger.error("Please verify your account");
            return ResponseEntity.status(401).body(
                    ApiResponse.builder()
                            .success(false)
                            .message("Please verify your account!").build()
            );
        }


        TokenResponseInfo tokenResponseInfo = jwtUtils.generateTokenResponseInfo(userLoginDto.getEmail(), userLoginDto.getPassword(), authenticationManager);

        logger.info("authenticate Ok!");
        return ResponseEntity.ok().body(
                ApiResponse.builder()
                        .success(true)
                        .message("Login success")
                        .data(tokenResponseInfo).build()
        );

    }

    @Autowired
    private UserDetailsServiceImpl userDetailsService;


    @Override
    public ResponseEntity<?> authenticateByEmail(UserLoginGGTO userLoginDto) throws AuthenticationException {

        String email = userLoginDto.getEmail();
        String[] parts = email.split("@");

        if (!parts[1].equals("fpt.edu.vn")) {
            logger.error("Email must be format abc@fpt.edu.vn!");
            return ResponseEntity.status(401).body(
                    ApiResponse.builder()
                            .success(false)
                            .message("Email must be format abc@fpt.edu.vn!").build()
            );

        }
        if ((userLoginDto.getEmail().trim().isEmpty())
        ) {
            logger.error("Parameter invalid!");
            return ResponseEntity.status(401).body(
                    ApiResponse.builder()
                            .success(false)
                            .message("Parameter invalid!").build()
            );
        }

        Users user = userRepository.getUserByEmail(userLoginDto.getEmail());

        if (user == null) {
            Date now = new Date();
            Users newUsers = new Users();
            newUsers.setEmail(userLoginDto.getEmail());
            newUsers.setFullName(userLoginDto.getEmail().substring(0, userLoginDto.getEmail().indexOf("@")));
            newUsers.setEnabled(true);
            newUsers.setCreated(now);
//            newUsers.setCreatedBy(newUsers.getId());
//            newUsers.setModifiedBy(newUsers.getId());
            newUsers.setModified(now);
            newUsers.setProvider(Provider.GOOGLE);
            newUsers.setSettings(settingsRepository.getById(10));
            newUsers.setStatus(ConstantsStatus.active.toString());
            userRepository.save(newUsers);
            System.out.println("Created new user: " + userLoginDto.getEmail());

            int id = newUsers.getId();
            userRepository.updateAudit(id);

            TokenResponseInfo tokenResponseInfo = jwtUtils.generateTokenResponseInfoEmail(userLoginDto);
            logger.info("Authenticate Ok!");
            return ResponseEntity.ok().body(
                    ApiResponse.builder()
                            .success(true)
                            .message("Login success")
                            .data(tokenResponseInfo).build());
        } else {

            if (user.getStatus().equals("inactive") || user.getStatus().equals("")) {
                logger.error("Account INACTIVE!");
                return ResponseEntity.status(401).body(
                        ApiResponse.builder()
                                .success(false)
                                .message("Account INACTIVE!").build()
                );
            }
            if (!user.isEnabled()) {
                logger.error("Please verify your account");
                return ResponseEntity.status(401).body(
                        ApiResponse.builder()
                                .success(false)
                                .message("Please verify your account!").build()
                );
            }


            TokenResponseInfo tokenResponseInfo = jwtUtils.generateTokenResponseInfoEmail(userLoginDto);

            logger.info("Authenticate Ok!");
            return ResponseEntity.ok().body(
                    ApiResponse.builder()
                            .success(true)
                            .message("Login success!!")
                            .data(tokenResponseInfo).build()
            );
        }
    }

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private MailService mailService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private SettingsRepository settingsRepository;
    @Autowired
    private Validate validate;

    public void register(UserRegisterDTO userRegisterDTO)
            throws Exception {
        validate.validateRegisterUser(userRegisterDTO);

        Users users = modelMapper.map(userRegisterDTO, Users.class);
        Date date = new Date();
        String encodedPassword = passwordEncoder.encode(users.getPassword());
        users.setProvider(Provider.LOCAL);
        users.setPassword(encodedPassword);
        users.setCreated(date);
        users.setModified(date);
        users.setSettings(settingsRepository.getById(10));
        users.setStatus(ConstantsStatus.active.toString());
        String randomCode = RandomString.make(32);
        users.setVerificationCode(randomCode);
        users.setEnabled(false);
        userRepository.save(users);
        mailService.sendMailVerifycode(userRegisterDTO, randomCode);
    }

    private void sendVerificationEmail(UserRegisterDTO user, String siteURL)
            throws MessagingException, UnsupportedEncodingException {
        String toAddress = user.getEmail();
        String fromAddress = "Your email address";
        String senderName = "Your company name";
        String subject = "Please verify your registration";
        String content = "Dear [[name]],<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br>"
                + "Your company name.";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", user.getFullName());
        String verifyURL = siteURL + "/verify?code=" + user.getVerificationCode();

        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);

        mailSender.send(message);
    }


    @Override
    public ResponseEntity<?> getVerifyCode(String email) throws Exception {
//        if (StringUtils.isEmpty(email.trim())) {
//            logger.error("Parameter invalid!");
//            return ResponseEntity.badRequest().body(
//                    ApiResponse.builder()
//                            .code(commonProperties.getCODE_UPDATE_FAILED())
//                            .message(commonProperties.getMESSAGE_PARAM_VALUE_EMPTY()).build()
//            );
//        }
//        User user = userRepository.findByEmail(email).orElse(null);
//
//        if (user == null) {
//            logger.error("Email is not correct");
//            return ResponseEntity.badRequest().body(
//                    ApiResponse.builder()
//                            .code(commonProperties.getCODE_UPDATE_FAILED())
//                            .message("Email này không tồn tại trong hệ thống!").build()
//            );
//        }
//
//
//        String verifyCode = utils.generateRandomCode(commonProperties.getCodeSize());
//
//        try {
//            mailService.createMailVerifyCode(email, verifyCode);
//        } catch (Exception ignored) {
//            return ResponseEntity.ok().body(
//                    ApiResponse.builder()
//                            .code(commonProperties.getCODE_UPDATE_FAILED())
//                            .message("Không thể gửi mail!").build()
//            );
//        }
//
//        String newPassword = passwordEncoder.encode(verifyCode);
//        user.setPassword(newPassword);
//        userRepository.save(user);
//
//        return ResponseEntity.ok().body(
//                ApiResponse.builder()
//                        .code(commonProperties.getCODE_UPDATE_SUCCESS())
//                        .message(commonProperties.getMESSAGE_SUCCESS()).build()
//        );
        return null;
    }

    @Override
    public ResponseEntity<?> verify(String code) throws Exception {
        Users users = userRepository.findByVerificationCode(code).get();
        if (!users.getVerificationCode().equals(code)) {
            throw new Exception("Verify fail!! Your verify link is incorrect");

        }
        users.setEnabled(true);
        users.setCreatedBy(users.getId());
        users.setModifiedBy(users.getId());
        userRepository.save(users);
        return ResponseEntity.ok().body(
                ApiResponse.builder()
                        .success(true)
                        .message("Verify successfully")
                        .build()
        );
    }

    public void changePassword(UserChangePWDTO userChangePWDTO)
            throws Exception {
        validate.validateChangePassword(userChangePWDTO);
        UserDetails u = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                u.getUsername(), userChangePWDTO.getOldPassword()));
        Users users = userRepository.findByEmail(u.getUsername());
        if (passwordEncoder.matches(userChangePWDTO.getNewPassword(), users.getPassword())) {
            throw new Exception("New password must be different from the old password!");
        }
        users.setPassword(passwordEncoder.encode(userChangePWDTO.getNewPassword()));
        userRepository.save(users);
    }

}
