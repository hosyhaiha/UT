package com.fpt.capstone.backend.api.BackEnd.service.impl;

import com.fpt.capstone.backend.api.BackEnd.dto.ModelStatus;
import com.fpt.capstone.backend.api.BackEnd.dto.SubjectConfigDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.classes.ClassesDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.classes.ClassesInputDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.classes.ClassesListDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.milestones.MilestonesDTO;
import com.fpt.capstone.backend.api.BackEnd.entity.*;
import com.fpt.capstone.backend.api.BackEnd.repository.ClassSettingsRepository;
import com.fpt.capstone.backend.api.BackEnd.repository.ClassesRepository;
import com.fpt.capstone.backend.api.BackEnd.repository.SubjectsRepository;
import com.fpt.capstone.backend.api.BackEnd.repository.UserRepository;
import com.fpt.capstone.backend.api.BackEnd.service.ClassesService;
import com.fpt.capstone.backend.api.BackEnd.service.SubjectsService;
import com.fpt.capstone.backend.api.BackEnd.service.UserService;
import com.fpt.capstone.backend.api.BackEnd.service.validate.ConstantsRegex;
import com.fpt.capstone.backend.api.BackEnd.service.validate.Validate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;

import javax.persistence.EntityManager;
import java.util.*;

@Service
public class ClassesServiceImpl implements ClassesService {
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private Validate validate;
    @Autowired
    private SubjectsRepository subjectsRepository;
    @Autowired
    private ClassesRepository classesRepository;
    @Autowired
    private ClassSettingsRepository classSettingsRepository;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private UserService userService;
    @Autowired
    private SubjectsService subjectsService;

    @Override
    public ClassesDTO addClasses(ClassesInputDTO classesInputDTO) throws Exception {
        if (classesInputDTO.getId() != null) {
            classesInputDTO.setId(null);
        }
        validate.validateClasses(classesInputDTO);
        if (classesRepository.findByCode(classesInputDTO.getCode()) != null) throw new Exception("Class code already exist!");
        //validate subject condition
        if (!subjectsService.checkSubjectInitCondition(Integer.valueOf(classesInputDTO.getSubjectId()))) throw new Exception("This subject has not finished setting up");
        Classes classes = modelMapper.map(classesInputDTO, Classes.class);
        // classes.setSubject(subjectsRepository.getById(Integer.valueOf(classesInputDTO.getSubjectId())));
        classesRepository.save(classes);
        classes = entityManager.find(Classes.class, classes.getId());
        return showDetail(classes.getId());
    }

    @Override
    public ClassesDTO updateClasses(ClassesInputDTO classesInput) throws Exception {
        if (ObjectUtils.isEmpty(classesInput.getId())) {
            throw new Exception("ID cannot be empty!");
        }
        if (!classesInput.getId().matches(ConstantsRegex.NUMBER_PATTERN.toString())) {
            throw new Exception("ID must be integer!");
        }
        if (classesRepository.getClassesDetail(Integer.valueOf(classesInput.getId())) == null) {
            throw new Exception("Classes not exist!");
        }
        validate.validateClasses(classesInput);
        Classes classes = classesRepository.getOne(Integer.valueOf(classesInput.getId()));
        if (!classes.getCode().equals(classesInput.getCode())
                && classesRepository.findByCode(classesInput.getCode()) != null) {
            throw new Exception("Classes code already exist!");
        }
        classes = modelMapper.map(classesInput, Classes.class);
        // classes.setSubject(subjectsRepository.getById(Integer.valueOf(classesInput.getSubjectId())));
        classesRepository.save(classes);
        classes = entityManager.find(Classes.class, classes.getId());
        return showDetail(classes.getId());
    }

    @Override
    public ClassesDTO showDetail(int id) throws Exception {
        if (classesRepository.getClassesDetail(id) == null) {
            throw new Exception("Classes not exist");
        }
        return classesRepository.getClassesDetail(id);
    }

    @Override
    public Page<ClassesDTO> searchBy(String code, List<Integer> trainerId, List<String> subjectCode,
                                     Integer year, String term, String status, Integer block5, int page, int limit, ResponsePaggingObject respone) throws Exception {
        Pageable pageable = PageRequest.of(page - 1, limit);

        if (ObjectUtils.isEmpty(code)) code = null;
        if (ObjectUtils.isEmpty(term)) term = null;
        if (ObjectUtils.isEmpty(status)) status = null;

        Users user = userService.getUserLogin();
        if (Objects.equals(user.getSettings().getId(), RoleID.ADMIN) || Objects.equals(user.getSettings().getId(), RoleID.AUTHOR)) {
            respone.getPermission().setAdd(Boolean.TRUE);
            respone.getPermission().setUpdate(Boolean.TRUE);
        }
       // List<Integer> authorityClassIds = permissionService.getSubjectAuthority(null);

        return classesRepository.search(code, trainerId, subjectCode, year, term, status, block5, pageable);

    }

    @Override
    public List<ClassesListDTO> showListClass(List<Integer> subjectId, String status) {
        if (ObjectUtils.isEmpty(status)) {
            status = null;
        }
        List<ClassesListDTO> classesDTOS = classesRepository.getLabelList(subjectId, status);
        return classesDTOS;
    }

    /**
     * Hàm dùng để check xem lớp đã đủ điều kiện để có thể thêm học sinh vào lớp hay chưa
     *
     * @param classId
     * @return
     */
    @Override
    public Boolean checkClassInitCondition(Integer classId) throws Exception {
        // Check đã có bản ghi class_settings cho complexity và quality
        List<ClassSettings> classSettings = classSettingsRepository.findAllByClassIdAndStatusAndTypeIdIn(
                classId, ModelStatus.STATUS_ACTIVE,
                Arrays.asList(new Integer[]{ModelStatus.QUALITY_SETTING_TYPE_ID, ModelStatus.COMPLEXITY_SETTING_TYPE_ID}));
        Boolean complexityOk = false;
        Boolean qualityOk = false;
        for (ClassSettings classSetting: classSettings) {
            if (complexityOk && qualityOk) break;
            if (classSetting.getTypeId().equals(ModelStatus.COMPLEXITY_SETTING_TYPE_ID)) complexityOk = true;
            if (classSetting.getTypeId().equals(ModelStatus.QUALITY_SETTING_TYPE_ID)) qualityOk = true;
        }
        if (!complexityOk) throw new Exception("This class has not yet finished complexity setting!");
        if (!qualityOk) throw new Exception("This class has not yet finished quality setting!");

        // Check milestones: mỗi 1 iteration phải có duy nhất 1 bản ghi milestone
        List<MilestonesDTO> milestones = classesRepository.getMilestoneConfig(classId, ModelStatus.MILESTONE_STATUS_CANCELLED);
        if (milestones.isEmpty()) throw new Exception("This class has not yet configure milestone!");
        HashMap<Integer, Integer> milestoneIterationMap = new HashMap<>();
        for (MilestonesDTO milestone: milestones) {
            if (milestone.getId() == null) throw new Exception("No milestone for iteration " + milestone.getIterationName() + " yet!");
            if (milestoneIterationMap.containsKey(milestone.getIterationId())) throw new Exception("Iteration " + milestone.getIterationName() + " has more than one milestone!");
            milestoneIterationMap.put(milestone.getIterationId(), milestone.getId());
        }
        return true;
    }
}
