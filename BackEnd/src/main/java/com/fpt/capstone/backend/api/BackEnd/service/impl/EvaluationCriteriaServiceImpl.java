package com.fpt.capstone.backend.api.BackEnd.service.impl;

import com.fpt.capstone.backend.api.BackEnd.dto.EvaluationCriteriaDTO;
import com.fpt.capstone.backend.api.BackEnd.entity.EvaluationCriteria;
import com.fpt.capstone.backend.api.BackEnd.entity.ResponsePaggingObject;
import com.fpt.capstone.backend.api.BackEnd.entity.RoleID;
import com.fpt.capstone.backend.api.BackEnd.entity.Users;
import com.fpt.capstone.backend.api.BackEnd.repository.EvaluationCriteriaRepository;
import com.fpt.capstone.backend.api.BackEnd.repository.IterationsRepository;
import com.fpt.capstone.backend.api.BackEnd.service.EvaluationCriteriaService;
import com.fpt.capstone.backend.api.BackEnd.service.UserService;
import com.fpt.capstone.backend.api.BackEnd.service.validate.ConstantsRegex;
import com.fpt.capstone.backend.api.BackEnd.service.validate.ConstantsStatus;
import com.fpt.capstone.backend.api.BackEnd.service.validate.Validate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class EvaluationCriteriaServiceImpl implements EvaluationCriteriaService {
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private IterationsRepository iterationsRepository;
    @Autowired
    private EvaluationCriteriaRepository evaluationCriteriaRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private Validate validate = new Validate();
    @Autowired
    private UserService userService;

    @Override
    public EvaluationCriteriaDTO addCriteria(EvaluationCriteriaDTO evaluationCriteriaDTO) throws Exception {
        evaluationCriteriaDTO.setEvaluationWeight(BigDecimal.valueOf(evaluationCriteriaDTO.getEvaluationWeight().floatValue()/100));
        validate.validateCriteria(evaluationCriteriaDTO);
        EvaluationCriteria evaluationCriteria = modelMapper.map(evaluationCriteriaDTO, EvaluationCriteria.class);
//        evaluationCriteria.setIteration(iterationsRepository.getById(Integer.valueOf(evaluationCriteriaDTO.getIterationId())));
        evaluationCriteria.setStatus(ConstantsStatus.active.toString());
        evaluationCriteria =evaluationCriteriaRepository.save(evaluationCriteria);
        return getCriteriaDetail(evaluationCriteria.getId());
    }


    @Override
    public EvaluationCriteriaDTO deleteCriteria(int id) {
        return null;
    }

    @Override
    public List<EvaluationCriteriaDTO> showCriteria() {
        return null;
    }

    @Override
    public EvaluationCriteriaDTO updateCriteria(EvaluationCriteriaDTO evaluationCriteriaDTO) throws Exception {
        validate.validateCriteria(evaluationCriteriaDTO);

        EvaluationCriteria evaluationCriteria = evaluationCriteriaRepository.getOne(evaluationCriteriaDTO.getId());

//        evaluationCriteria.setIteration(iterationsRepository.getById(evaluationCriteriaDTO.getIterationId()));
        evaluationCriteria.setEvaluationWeight(evaluationCriteriaDTO.getEvaluationWeight());
        evaluationCriteria.setTeamEvaluation(evaluationCriteriaDTO.getTeamEvaluation());
        evaluationCriteria.setStatus(evaluationCriteriaDTO.getStatus());
        evaluationCriteriaRepository.save(evaluationCriteria);
        evaluationCriteria = entityManager.find(EvaluationCriteria.class, evaluationCriteria.getId());
        return getCriteriaDetail(evaluationCriteria.getId());
    }

    @Override
    public EvaluationCriteriaDTO findById(int id) throws Exception {
        return null;
    }

    @Override
    public Page<EvaluationCriteriaDTO> listBy(List<Integer> id, List<Integer> authorId, String status, int page, int per_page, ResponsePaggingObject response) throws Exception {
        Pageable pageable = PageRequest.of(page - 1, per_page);
        Users user = userService.getUserLogin();
        if (ObjectUtils.isEmpty(status)) {
            status = null;
        }
        if (!ObjectUtils.isEmpty(status) && !status.matches(ConstantsRegex.STATUS_PATTERN.toString())) {
            throw new Exception("Input status empty to search all or active/inactive");
        }
        if (Objects.equals(user.getSettings().getId(), RoleID.ADMIN)||Objects.equals(user.getSettings().getId(), RoleID.AUTHOR) ) {
            response.getPermission().setAdd(Boolean.TRUE);
            response.getPermission().setUpdate(Boolean.TRUE);
        }
        if (Objects.equals(user.getSettings().getId(), RoleID.AUTHOR)){
            authorId=new ArrayList<>();
            authorId.add(user.getId());
            return evaluationCriteriaRepository.search(id,authorId, status, pageable);
        }
        return evaluationCriteriaRepository.search(id,authorId, status, pageable);
    }

    @Override
    public EvaluationCriteriaDTO getCriteriaDetail(int id) {
        return evaluationCriteriaRepository.getEvaluationCriteriaDetail(id);
    }

    @Override
    public List<EvaluationCriteriaDTO> showCriteriaList() {
        return null;
    }
}
