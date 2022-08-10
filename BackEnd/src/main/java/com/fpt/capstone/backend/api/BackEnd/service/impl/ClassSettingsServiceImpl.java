package com.fpt.capstone.backend.api.BackEnd.service.impl;

import com.fpt.capstone.backend.api.BackEnd.dto.ClassSettingOptionDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.ClassSettingsDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.ModelStatus;
import com.fpt.capstone.backend.api.BackEnd.entity.ClassSettings;
import com.fpt.capstone.backend.api.BackEnd.repository.ClassSettingsRepository;
import com.fpt.capstone.backend.api.BackEnd.repository.ClassesRepository;
import com.fpt.capstone.backend.api.BackEnd.repository.SubjectsRepository;
import com.fpt.capstone.backend.api.BackEnd.service.ClassSettingsService;
import com.fpt.capstone.backend.api.BackEnd.service.validate.Validate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.persistence.EntityManager;
import java.util.List;
@Service
public class ClassSettingsServiceImpl implements ClassSettingsService {
    private final Integer ADMIN_ID = 7;

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

    @Override
    public ClassSettingsDTO add(ClassSettingsDTO classSettingsDTO) throws Exception {

        validate.validateClassSettings(classSettingsDTO);
        if (classSettingsRepository.findByTitle(classSettingsDTO.getTitle()) != null) {
            throw new Exception("Title is already exist");
        }
        ClassSettings classSettings = modelMapper.map(classSettingsDTO, ClassSettings.class);

        classSettingsRepository.save(classSettings);
        classSettings = entityManager.find(ClassSettings.class, classSettings.getId());
        return showDetail(classSettings.getId());
    }

    @Override
    public ClassSettingsDTO update(ClassSettingsDTO classSettingsDTO) throws Exception {
        validate.validateClassSettings(classSettingsDTO);

        ClassSettings classSettings = classSettingsRepository.getById(classSettingsDTO.getId());
        if (!classSettings.getTitle().equals(classSettingsDTO.getTitle())) {
            if (classSettingsRepository.findByTitle(classSettingsDTO.getTitle()) != null) {
                throw new Exception("Title is already exist");
            }
        }
         classSettings = modelMapper.map(classSettingsDTO, ClassSettings.class);

        classSettingsRepository.save(classSettings);
        classSettings = entityManager.find(ClassSettings.class, classSettings.getId());
        return showDetail(classSettings.getId());
    }

    @Override
    public ClassSettingsDTO showDetail(int id) throws Exception {
        if (classSettingsRepository.getById(id) == null) {
            throw new Exception("Classes not exist");
        }
        return classSettingsRepository.getClassesDetail(id);
    }

    @Override
    public Page<ClassSettingsDTO> searchBy(List<Integer> classId, String status, int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        if (ObjectUtils.isEmpty(status)) {
            status = null;
        }
        Page<ClassSettingsDTO> classSettingsDTOS = classSettingsRepository.search(classId, status, pageable);
        return classSettingsDTOS;
    }

    @Override
    public List<ClassSettingsDTO> showList(String status) {
        return null;
    }

    /**
     * Hàm lấy data cho select box dựa theo type
     *
     * @param type
     * @return
     */
    @Override
    public List<ClassSettingOptionDTO> getOptions(String type, Integer classId) {
        return classSettingsRepository.getOptions(ModelStatus.SETTING_TYPE_MAPPER.get(type), ModelStatus.STATUS_ACTIVE, classId);
    }
}
