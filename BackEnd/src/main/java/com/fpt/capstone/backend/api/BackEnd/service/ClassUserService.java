package com.fpt.capstone.backend.api.BackEnd.service;

import com.fpt.capstone.backend.api.BackEnd.dto.class_users.ClassUserInputDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.class_users.ClassUsersDTO;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ClassUserService {
    ClassUsersDTO add(ClassUserInputDTO classUserInputDTO) throws Exception;

    ClassUsersDTO update(ClassUserInputDTO classUserInputDTO) throws Exception;

    ClassUsersDTO showDetail(int id) throws Exception;

    Page<ClassUsersDTO> searchBy(List<Integer> classId, List<Integer> projectId, List<Integer> userId,
                                 Integer projectLead, String status, int page, int limit);

    List<ClassUsersDTO> showList(String status);
    Map<String, String> toMap(ClassUsersDTO classUsersDTO);
    List<Map> toListOfMap(List<ClassUsersDTO> classesUserDTOs);
    String exportClassUser(List<ClassUsersDTO> classesUserDTOs) throws IOException;
    ResponseEntity<?> importClassUser(MultipartFile fileStream) throws IOException;
    boolean checkLeader(Integer userId,Integer projectId);
}
