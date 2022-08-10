package com.fpt.capstone.backend.api.BackEnd.service;

import com.fpt.capstone.backend.api.BackEnd.dto.UserRegisterDTO;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;


public interface MailService {
    void sendMailVerifycode(UserRegisterDTO userRegisterDTO, String code) throws MessagingException, UnsupportedEncodingException;

    void sendMailInvite(String userName, String pass) throws MessagingException, UnsupportedEncodingException;

}
