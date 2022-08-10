package com.fpt.capstone.backend.api.BackEnd.service;

import com.fpt.capstone.backend.api.BackEnd.dto.AuthorListDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.UserInfoDto;
import com.fpt.capstone.backend.api.BackEnd.dto.UserList;
import com.fpt.capstone.backend.api.BackEnd.dto.UsersDTO;
import com.fpt.capstone.backend.api.BackEnd.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Service
public interface UserService {
    public List<AuthorListDTO> listByAuthor();

    public List<UserList> listUser(List<Integer> projectId) throws Exception;

    ResponseEntity<?> getUserInformation(String jwtToken) throws Exception;

    ResponseEntity<?> saveAvatarLink(String url, String token) throws Exception;

    ResponseEntity<?> getAllUsers(String jwtToken) throws Exception;

    Page<UsersDTO> listBy(String fullName, String email, String rollNumber, String status, int page, int per_page) throws Exception;

    UsersDTO findUserById(Integer id);

    void updateByID(UsersDTO userDTO) throws Exception;

    String uploadAva(MultipartFile fileStream) throws Exception;

    List<UserInfoDto> getUserList(String status, String role);

    void createUserImportExcel(String user) throws MessagingException, UnsupportedEncodingException;

    Users getUserLogin();


}
