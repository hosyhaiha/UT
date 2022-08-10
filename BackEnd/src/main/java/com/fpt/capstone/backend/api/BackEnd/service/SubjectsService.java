package com.fpt.capstone.backend.api.BackEnd.service;

import com.fpt.capstone.backend.api.BackEnd.dto.subject.SubjectInputDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.subject.SubjectsDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.subject.SubjectsListDTO;
import com.fpt.capstone.backend.api.BackEnd.entity.ResponsePaggingObject;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SubjectsService {
    SubjectsDTO addSubjects(SubjectInputDTO subjectInputDTO) throws Exception;

    void deleteSubjects(int id);

    List<SubjectsDTO> showSubjectsList();

    SubjectsDTO updateSubject(SubjectInputDTO subjectInputDTO) throws Exception;

    SubjectsDTO findById(int id);

    List<SubjectsListDTO> listBy() throws Exception;

    Page<SubjectsDTO> searchBy(String key_code,String key_name,List<Integer> authorId,String key_status,int page,int per_page, ResponsePaggingObject response) throws Exception;

    /**
     * Hàm dùng để check xem môn học đã được config đầy đủ các data bắt buộc hay chưa, đã được phép tạo lớp học hay chưa
     *
     * @param subjectId
     * @return
     */
    boolean checkSubjectInitCondition(Integer subjectId) throws Exception;
}
