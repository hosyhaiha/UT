package com.fpt.capstone.backend.api.BackEnd.service.impl;

import com.fpt.capstone.backend.api.BackEnd.dto.subject_setting.SubjectSettingInputDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.subject_setting.SubjectSettingTypeDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.subject_setting.SubjectSettingsDTO;
import com.fpt.capstone.backend.api.BackEnd.entity.SubjectSettings;
import com.fpt.capstone.backend.api.BackEnd.repository.SubjectSettingsRepository;
import com.fpt.capstone.backend.api.BackEnd.service.SubjectSettingsService;
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
import java.util.List;

@Service
public class SubjectSettingsServiceImpl implements SubjectSettingsService {

    @Autowired
    private SubjectSettingsRepository subjectSettingsRepository;

    @Autowired
    private Validate validate = new Validate();

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    EntityManager entityManager;

    @Override
    public SubjectSettingsDTO addSubjectSetting(SubjectSettingInputDTO subjectSettingInputDTO) throws Exception {
        subjectSettingInputDTO.setStatus(ConstantsStatus.active.toString());
        validate.validateSubjectSetting(subjectSettingInputDTO);
        if (subjectSettingsRepository.findSubjectSettingsByTitle(subjectSettingInputDTO.getTitle()) > 0) {
            throw new Exception("Title already exist");
        }
        if (subjectSettingsRepository.searchByTypeIdDisplayOrder(
                Integer.parseInt(subjectSettingInputDTO.getTypeId()),
                Integer.parseInt(subjectSettingInputDTO.getDisplayOrder()),
                Integer.parseInt(subjectSettingInputDTO.getSubjectId())) > 0) {
            throw new Exception("DisplayOrder already exist on this typeID and subjectID");
        }
        SubjectSettings subjectSettings = convertSubjectSettingInputDTOToSubjectSetting(subjectSettingInputDTO);
        subjectSettingsRepository.save(subjectSettings);
        subjectSettings = entityManager.find(SubjectSettings.class, subjectSettings.getId());
        return getSubjectSettingDetail(subjectSettings.getId());
    }

    @Override
    public SubjectSettingsDTO deleteSubjectSetting(int id) throws Exception {
        if (subjectSettingsRepository.getOne(id) == null) {
            throw new Exception("Setting not found");
        }
        SubjectSettings subjectSettings = subjectSettingsRepository.getOne(id);
        if (subjectSettings.getStatus().equals(ConstantsStatus.inactive.toString())) {
            throw new Exception("Setting is inactive");
        }
        subjectSettings.setStatus(ConstantsStatus.inactive.toString());
        subjectSettingsRepository.save(subjectSettings);
        subjectSettings = entityManager.find(SubjectSettings.class, subjectSettings.getId());
        return getSubjectSettingDetail(subjectSettings.getId());

    }

    @Override
    public SubjectSettingsDTO updateSubjectSetting(SubjectSettingInputDTO subjectSettingInputDTO) throws Exception {
        if (ObjectUtils.isEmpty(subjectSettingInputDTO.getId())) {
            throw new Exception("Subject ID can't be empty");
        }
        if (!subjectSettingInputDTO.getId().matches(ConstantsRegex.NUMBER_PATTERN.toString())) {
            throw new Exception("subjectSettingId must be integer and bigger than 0");
        }
        validate.validateSubjectSetting(subjectSettingInputDTO);
        if (subjectSettingsRepository.getSubjectSettingsDetail(Integer.parseInt(subjectSettingInputDTO.getId())) == null) {
            throw new Exception("Subject setting not found");
        }
        SubjectSettings subjectSettings = subjectSettingsRepository.getOne(Integer.parseInt(subjectSettingInputDTO.getId()));
        if (Integer.parseInt(subjectSettingInputDTO.getTypeId()) != subjectSettings.getTypeId()) {
            throw new Exception("Type id cannot change");
        }
        if (!subjectSettingInputDTO.getTitle().equals(subjectSettings.getTitle())) {
            if (subjectSettingsRepository.findSubjectSettingsByTitle(subjectSettingInputDTO.getTitle()) > 0) {
                throw new Exception("Title already exist");
            }
        }
        if (Integer.parseInt(subjectSettingInputDTO.getDisplayOrder()) != subjectSettings.getDisplayOrder()
                || Integer.parseInt(subjectSettingInputDTO.getTypeId()) != subjectSettings.getTypeId()
                || Integer.parseInt(subjectSettingInputDTO.getSubjectId()) != subjectSettings.getSubjectId()) {
            if (subjectSettingsRepository.searchByTypeIdDisplayOrder(
                    Integer.parseInt(subjectSettingInputDTO.getTypeId()),
                    Integer.parseInt(subjectSettingInputDTO.getDisplayOrder()),
                    Integer.parseInt(subjectSettingInputDTO.getSubjectId())) > 0) {
                throw new Exception("DisplayOrder already exist on this typeID and subjectID");
            }
        }

        subjectSettings = convertSubjectSettingInputDTOToSubjectSetting(subjectSettingInputDTO);
        subjectSettingsRepository.save(subjectSettings);
        subjectSettings = entityManager.find(SubjectSettings.class, subjectSettings.getId());
        return getSubjectSettingDetail(subjectSettings.getId());
    }

    @Override
    public SubjectSettingsDTO getSubjectSettingDetail(int id) {
        return subjectSettingsRepository.getSubjectSettingsDetail(id);
    }

    @Override
    public List<SubjectSettingTypeDTO> getSubjectSettingType() throws Exception {
        return subjectSettingsRepository.getSubjectSettingType();
    }

    @Override
    public Page<SubjectSettingsDTO> searchBy(List<Integer> settingTypeId, List<Integer> subjectId, String title, String value, String status, int page, int per_page) throws Exception {
        Pageable pageable = PageRequest.of(page - 1, per_page);
        if (ObjectUtils.isEmpty(status)) {
            status = null;
        }
        if (!ObjectUtils.isEmpty(status) && !status.matches(ConstantsRegex.STATUS_PATTERN.toString())) {
            throw new Exception("Input status Empty to search all or active/inactive");
        }
        return subjectSettingsRepository.getSubjectSettings(settingTypeId, subjectId, title, value, status, pageable);
    }

    private SubjectSettings convertSubjectSettingInputDTOToSubjectSetting(SubjectSettingInputDTO subjectSettingInputDTO) {
        SubjectSettings subjectSettings = modelMapper.map(subjectSettingInputDTO, SubjectSettings.class);
        subjectSettings.setSubjectId(Integer.valueOf(subjectSettingInputDTO.getSubjectId()));
        subjectSettings.setTypeId(Integer.valueOf(subjectSettingInputDTO.getTypeId()));
        subjectSettings.setDisplayOrder(Integer.valueOf(subjectSettingInputDTO.getDisplayOrder()));
        return subjectSettings;
    }
}
