package com.fpt.capstone.backend.api.BackEnd.service.impl;

import com.fpt.capstone.backend.api.BackEnd.dto.UserRegisterDTO;
import com.fpt.capstone.backend.api.BackEnd.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.logging.Logger;

@Service
public class MailServiceImpl implements MailService {
    @Autowired
    private JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String sendFrom;

    @Override
    public void sendMailVerifycode(UserRegisterDTO userRegisterDTO, String code) throws MessagingException, UnsupportedEncodingException {
        String subject = "Please click on the link below to verify your account";
        String senderName = "SLPM AENA";
        String verifyURL = "http://localhost:3000/verify-email?code=" + code;
        String content = "Dear [[fullName]],<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br>"
                + "SLPM.";
        content = content.replace("[[fullName]]", userRegisterDTO.getFullName());
        content = content.replace("[[URL]]", verifyURL);
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(sendFrom);
            helper.setTo(userRegisterDTO.getEmail());
            helper.setSubject(subject);
            helper.setText(content, true);
            mailSender.send(message);
            Logger.getLogger("Email sent to email" + userRegisterDTO.getEmail());
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void sendMailInvite(String userName, String pass) throws MessagingException, UnsupportedEncodingException {
        String subject = "You are invited to the SLPM system";


        String content = "Dear [[fullName]],<br>"
                + "You are invited to the SLPM system with:<br>"
                + "User name: [[userName]],<br>"
                + "Password: [[pass]].<br>"
                + "Thank you,<br>"
                + "SLPM.";
        content = content.replace("[[fullName]]", userName);
        content = content.replace("[[userName]]", userName);
        content = content.replace("[[pass]]", pass);
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(sendFrom);
            helper.setTo(userName);
            helper.setSubject(subject);
            helper.setText(content, true);
            mailSender.send(message);
            Logger.getLogger("Email sent to email" + userName);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
