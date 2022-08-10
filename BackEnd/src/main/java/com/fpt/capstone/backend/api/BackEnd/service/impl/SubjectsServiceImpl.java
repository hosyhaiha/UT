package com.fpt.capstone.backend.api.BackEnd.service.impl;

import com.fpt.capstone.backend.api.BackEnd.dto.EvaluationCriteriaDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.IterationsDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.SubjectConfigDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.subject.SubjectInputDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.subject.SubjectsDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.subject.SubjectsListDTO;
import com.fpt.capstone.backend.api.BackEnd.entity.*;
import com.fpt.capstone.backend.api.BackEnd.repository.SubjectsRepository;
import com.fpt.capstone.backend.api.BackEnd.repository.UserRepository;
import com.fpt.capstone.backend.api.BackEnd.service.SubjectsService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class SubjectsServiceImpl implements SubjectsService {

    @Autowired
    private SubjectsRepository subjectsRepository;

    @Autowired
    private Validate validate = new Validate();

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    EntityManager entityManager;

    @Autowired
    private UserService userService;

    @Autowired
    private PermissionService permissionService;

    @Override
    public SubjectsDTO addSubjects(SubjectInputDTO subjectInputDTO) throws Exception {
        validate.validateSubject(subjectInputDTO);
        if (subjectsRepository.findBySubjectCode(subjectInputDTO.getCode()) > 0) {
            throw new Exception("Subjects Code already exist!");
        }
        if (subjectsRepository.findBySubjectName(subjectInputDTO.getName()) > 0) {
            throw new Exception("Subjects Name already exist!");
        }
        Subjects subjects = modelMapper.map(subjectInputDTO, Subjects.class);
        subjectsRepository.save(subjects);
        subjects = entityManager.find(Subjects.class, subjects.getId());
        return findById(subjects.getId());
    }

    @Override
    public void deleteSubjects(int id) {
        Subjects subjects = subjectsRepository.getOne(id);
        subjects.setStatus(ConstantsStatus.inactive.toString());
        subjectsRepository.save(subjects);
    }

    public SubjectsDTO findById(int id) {
        return subjectsRepository.getSubjectDetail(id);
    }

    @Override
    public List<SubjectsDTO> showSubjectsList() {
        List<Subjects> subjects = subjectsRepository.findAll();
        List<SubjectsDTO> subjectsDTOS = subjects.stream()
                .map(subject -> modelMapper.map(subject, SubjectsDTO.class))
                .collect(Collectors.toList());
        return subjectsDTOS;
    }

    @Override
    public SubjectsDTO updateSubject(SubjectInputDTO subjectInputDTO) throws Exception {
        if (ObjectUtils.isEmpty(subjectInputDTO.getId())) {
            throw new Exception("ID cannot be empty!");
        }
        if (!subjectInputDTO.getId().matches(ConstantsRegex.NUMBER_PATTERN.toString())) {
            throw new Exception("ID must be integer and bigger than 0!");
        }
        if (subjectsRepository.getSubjectDetail(Integer.valueOf(subjectInputDTO.getId())) == null) {
            throw new Exception("Subject not exist!");
        }
        validate.validateSubject(subjectInputDTO);
        Subjects subjects = subjectsRepository.getOne(Integer.valueOf(subjectInputDTO.getId()));
        if (!subjects.getName().equals(subjectInputDTO.getName())) {
            if (subjectsRepository.findBySubjectName(subjectInputDTO.getName()) > 0)
                throw new Exception("Subjects Name already exist ");
        }
        if (!subjects.getCode().equals(subjectInputDTO.getCode())) {
            if (subjectsRepository.findBySubjectCode(subjectInputDTO.getCode()) > 0)
                throw new Exception("Subjects Code already exist ");
        }
        subjects = modelMapper.map(subjectInputDTO, Subjects.class);
        subjectsRepository.save(subjects);
        subjects = entityManager.find(Subjects.class, subjects.getId());
        return findById(subjects.getId());
    }

    @Override
    public List<SubjectsListDTO> listBy() throws Exception {
        //   Pageable pageable = PageRequest.of(page - 1, per_page);
        List<Integer> authoritySubjectIds = permissionService.getSubjectAuthority(null);
        List<SubjectsListDTO> subjects = subjectsRepository.search(authoritySubjectIds);
        //List<SubjectsListDTO> subjects = subjectsRepository.search();
        // Page<SubjectsDTO> subjectsDTOS = subjects.map(subjects1 -> modelMapper.map(subjects1, SubjectsDTO.class));
        return subjects;
    }


    @Override
    public Page<SubjectsDTO> searchBy(String code, String name, List<Integer> authorId, String status, int page, int per_page, ResponsePaggingObject respone) throws Exception {
        Users user = userService.getUserLogin();
        List<Integer> authoritySubjectIds = permissionService.getSubjectAuthority(null);
        Pageable pageable = PageRequest.of(page - 1, per_page);

        if (Objects.equals(user.getSettings().getId(), RoleID.ADMIN) || Objects.equals(user.getSettings().getId(), RoleID.AUTHOR)) {
            respone.getPermission().setAdd(Boolean.TRUE);
            respone.getPermission().setUpdate(Boolean.TRUE);
        }
        return subjectsRepository.searchBy(code, name, authorId, status, pageable, authoritySubjectIds);

    }

    /**
     * Hàm dùng để check xem môn học đã được config đầy đủ các data bắt buộc hay chưa, đã được phép tạo lớp học hay chưa
     *
     * @param subjectId
     * @return
     */
    public boolean checkSubjectInitCondition(Integer subjectId) throws Exception {
        List<SubjectConfigDTO> subjectConfigs = subjectsRepository.getSubjectConfigs(subjectId);
        HashMap<Integer, IterationsDTO> iterations = new HashMap<>();
//        HashMap<Integer, EvaluationCriteriaDTO> iterationCriteria = new HashMap<>();
        SubjectsDTO subject = new SubjectsDTO();
        for (SubjectConfigDTO subjectConfig : subjectConfigs) {
            System.out.println(subjectConfig);
            if (subject == null) subject = new SubjectsDTO(
                    subjectConfig.getSubjectId(), subjectConfig.getSubjectCode(), subjectConfig.getSubjectName(),
                    subjectConfig.getAuthorId(), subjectConfig.getSubjectStatus());

            if (subjectConfig.getIterationId() != null) {
                iterations.putIfAbsent(subjectConfig.getIterationId(), new IterationsDTO(
                        subjectConfig.getIterationId(), subjectConfig.getIterationWeight(),
                        subjectConfig.getIterationIsOngoing(), subjectConfig.getIterationStatus()));
            }

            if (subjectConfig.getCriteriaId() != null) {
                iterations.get(subjectConfig.getIterationId()).getEvaluationCriteria().putIfAbsent(subjectConfig.getCriteriaId(), new EvaluationCriteriaDTO(
                        subjectConfig.getCriteriaId(), subjectConfig.getCriteriaWeight(),
                        subjectConfig.getCriteriaIsTeamEvaluation(), subjectConfig.getCriteriaStatus()));
            }
        }

        // Check tổng iteration weight đã bằng 1 hay chưa
        BigDecimal totalIterationWeight = new BigDecimal(0);
        for (IterationsDTO iterationDTO : iterations.values()) {
            // Check xem tất cả iteration đã có đủ config hay chưa
            if (iterationDTO.getEvaluationCriteria().isEmpty())
                throw new Exception("this subject has not yet configured evaluation criteria for iteration" + iterationDTO.getName() + "!");

            totalIterationWeight = totalIterationWeight.add(iterationDTO.getEvaluationWeight());

            // Check tổng trọng số của từng iteration 1 đã đủ 100 hay chưa
            BigDecimal totalIteEvalWeight = new BigDecimal(0);
            for (EvaluationCriteriaDTO evalCriteria : iterationDTO.getEvaluationCriteria().values()) {
                totalIteEvalWeight = totalIteEvalWeight.add(evalCriteria.getEvaluationWeight());
                System.out.println(evalCriteria.getEvaluationWeight());
            }
            if (totalIteEvalWeight.compareTo(BigDecimal.valueOf(1)) != 0)
                throw new Exception("total evaluation criteria weighted of iterations " + iterationDTO.getName() + " not equal to 100%!");
        }
        if (totalIterationWeight.compareTo(BigDecimal.valueOf(1)) != 0)
            throw new Exception("total weighted of iterations not equal to 100%");

        return true;
    }
}
