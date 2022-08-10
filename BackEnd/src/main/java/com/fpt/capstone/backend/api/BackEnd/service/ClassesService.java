package com.fpt.capstone.backend.api.BackEnd.service;

import com.fpt.capstone.backend.api.BackEnd.dto.classes.ClassesDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.classes.ClassesInputDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.classes.ClassesListDTO;
import com.fpt.capstone.backend.api.BackEnd.entity.ResponsePaggingObject;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ClassesService {
    ClassesDTO addClasses(ClassesInputDTO classesInputDTO) throws Exception;

    ClassesDTO updateClasses(ClassesInputDTO classesInputDTO) throws Exception;

    ClassesDTO showDetail(int id) throws Exception;

    Page<ClassesDTO> searchBy(String code, List<Integer> trainerId, List<String> subjectCode,
                              Integer year, String term, String status, Integer block5, int page, int limit, ResponsePaggingObject respone) throws Exception;

    List<ClassesListDTO> showListClass(List<Integer> subjectId,String status);

    /**
     * Hàm dùng để check xem lớp đã đủ điều kiện để có thể thêm học sinh vào lớp hay chưa
     * @param classId
     * @return
     */
    Boolean checkClassInitCondition(Integer classId) throws Exception;
}
