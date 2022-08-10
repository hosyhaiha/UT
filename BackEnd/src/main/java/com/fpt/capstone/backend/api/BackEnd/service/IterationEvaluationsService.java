package com.fpt.capstone.backend.api.BackEnd.service;

import com.fpt.capstone.backend.api.BackEnd.dto.ClassEvalDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.ClassEvaluationDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.IterationEvaluationsDTO;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;

public interface IterationEvaluationsService {
    List<IterationEvaluationsDTO> showList(Integer iterationId, List<Integer> projectId, List<Integer> milestoneId, Integer classId) throws Exception;

    Page<ClassEvaluationDTO> classEvaluation(Integer classId, int page, int per_page);

    ClassEvalDTO listIterationClass(Integer classId);

    void updateBonus(Integer studentId, Integer milestoneId, BigDecimal bonus);
}
