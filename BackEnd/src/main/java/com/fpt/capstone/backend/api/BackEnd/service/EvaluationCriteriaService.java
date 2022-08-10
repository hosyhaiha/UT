package com.fpt.capstone.backend.api.BackEnd.service;

import com.fpt.capstone.backend.api.BackEnd.dto.EvaluationCriteriaDTO;
import com.fpt.capstone.backend.api.BackEnd.entity.ResponsePaggingObject;
import org.springframework.data.domain.Page;

import java.util.List;

public interface EvaluationCriteriaService {
    EvaluationCriteriaDTO addCriteria(EvaluationCriteriaDTO evaluationCriteriaDTO) throws Exception;

    EvaluationCriteriaDTO deleteCriteria(int id);

    List<EvaluationCriteriaDTO> showCriteria();

    EvaluationCriteriaDTO updateCriteria(EvaluationCriteriaDTO evaluationCriteriaDTO) throws Exception;

    EvaluationCriteriaDTO findById(int id) throws Exception;

    Page<EvaluationCriteriaDTO> listBy(List<Integer> id, List<Integer> authorId, String status, int page, int per_page, ResponsePaggingObject response) throws Exception;
    public EvaluationCriteriaDTO getCriteriaDetail(int id);
    public List<EvaluationCriteriaDTO> showCriteriaList();
}
