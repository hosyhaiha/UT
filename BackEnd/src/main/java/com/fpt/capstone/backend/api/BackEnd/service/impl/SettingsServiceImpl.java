package com.fpt.capstone.backend.api.BackEnd.service.impl;

import com.fpt.capstone.backend.api.BackEnd.dto.setting.SettingInputDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.setting.SettingRoleDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.setting.SettingTypeDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.setting.SettingsDTO;
import com.fpt.capstone.backend.api.BackEnd.entity.Settings;
import com.fpt.capstone.backend.api.BackEnd.repository.SettingsRepository;
import com.fpt.capstone.backend.api.BackEnd.service.SettingService;
import com.fpt.capstone.backend.api.BackEnd.service.validate.ConstantsRegex;
import com.fpt.capstone.backend.api.BackEnd.service.validate.ConstantsStatus;
import com.fpt.capstone.backend.api.BackEnd.service.validate.Validate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.persistence.EntityManager;
import java.util.List;

@Service

public class SettingsServiceImpl implements SettingService {

    @Autowired
    ModelMapper mapper;
    @Autowired
    EntityManager entityManager;
    @Autowired
    private SettingsRepository settingsRepository;

    @Autowired
    private Validate validate;

    @Override
    public List<SettingTypeDTO> getTypeSetting() {
        return settingsRepository.getSettingType();
    }

    @Override
    public Page<SettingsDTO> getSetingByType(List<Integer> id, String title, String value, String status, int page, int per_page) throws Exception {
        Pageable pageable = PageRequest.of(page - 1, per_page, Sort.by("id"));
        if (ObjectUtils.isEmpty(status)) {
            status = null;
        }
        if (!ObjectUtils.isEmpty(status) && !status.matches(ConstantsRegex.STATUS_PATTERN.toString())) {
            throw new Exception("Input status Empty to search all or active/inactive");
        }
        return settingsRepository.getSetingByType(id, title, value, status, pageable);
    }

    @Override
    public SettingsDTO addSettings(SettingInputDTO settingsInput) throws Exception {
        if (settingsInput == null) {
            throw new Exception("Setting cannot be null");
        }
        settingsInput.setStatus(ConstantsStatus.active.toString());
        validate.validateSetting(settingsInput);
        if (settingsRepository.searchSettingsByTitle(settingsInput.getTitle()).size() > 0) {
            throw new Exception("Setting title already exist");
        }
        if (settingsRepository.searchByTypeIdDisplayOrder(Integer.valueOf(settingsInput.getTypeId())
                , Integer.parseInt(settingsInput.getDisplayOrder())) > 0) {
            throw new Exception("DisplayOrder already exist on this typeID");
        }
        Settings settings = mapper.map(settingsInput, Settings.class);
        settingsRepository.save(settings);
        settings = entityManager.find(Settings.class, settings.getId());
        return getSettingDetail(settings.getId());
    }

    @Override
    public SettingsDTO deleteSetting(int id) throws Exception {
        if (settingsRepository.getSetingDetail(id) == null) {
            throw new Exception("Setting not found");
        }
        Settings settings = settingsRepository.getOne(id);
        if (settings.getStatus().equals(ConstantsStatus.inactive.toString())) {
            throw new Exception("Setting is inactive");
        }
        settings.setStatus(ConstantsStatus.inactive.toString());
        settingsRepository.save(settings);
        settings = entityManager.find(Settings.class, settings.getId());
        return getSettingDetail(settings.getId());
    }

    @Override
    public SettingsDTO updateSetting(SettingInputDTO settingsInput) throws Exception {

        if (ObjectUtils.isEmpty(settingsInput.getId())) {
            throw new Exception("ID can't be empty");
        }
        if (settingsRepository.getSetingDetail(Integer.parseInt(settingsInput.getId())) == null) {
            throw new Exception("Setting not found");
        }
        validate.validateSetting(settingsInput);
        Settings settings = settingsRepository.getOne(Integer.valueOf(settingsInput.getId()));
        if (!settings.getTitle().equals(settingsInput.getTitle()) &&
                settingsRepository.searchSettingsByTitle(settingsInput.getTitle()).size() > 0) {
            throw new Exception("Setting title already exist");
        }
        if (Integer.valueOf(settingsInput.getDisplayOrder()) != settings.getDisplayOrder()
                || Integer.valueOf(settingsInput.getTypeId()) != settings.getTypeId()) {
            if (settingsRepository.searchByTypeIdDisplayOrder(Integer.valueOf(settingsInput.getTypeId())
                    , Integer.valueOf(settingsInput.getDisplayOrder())) > 0) {
                throw new Exception("This display order already exist!");
            }
        }
        settings = mapper.map(settingsInput, Settings.class);
        settingsRepository.save(settings);
        settings = entityManager.find(Settings.class, settings.getId());
        return getSettingDetail(settings.getId());

    }

    private Settings convertToEntity(SettingsDTO settingsDTO) throws Exception {
        Settings settings = mapper.map(settingsDTO, Settings.class);
        return settings;
    }

    //
    @Override
    public SettingsDTO getSettingDetail(int id) {
        return settingsRepository.getSetingDetail(id);
    }

    @Override
    public List<SettingRoleDTO> getSettingRole() {
        List<SettingRoleDTO> settingRoleDTOS = settingsRepository.getSettingRole();
        return settingRoleDTOS;
    }


//    public List<SettingsDTO> showSettingsList(int page, int size) {
//        Page<Settings> settings = settingsRepository.findAll(PageRequest.of(page, size));
//        List<SettingsDTO> settingsDTOS = settings.stream()
//                .map(setting -> mapper.map(setting, SettingsDTO.class))
//                .collect(Collectors.toList());
//        return settingsDTOS;
//    }
//
//
//    @Override
//    public Page<SettingsDTO> listBy(String keyTitle, String keyValue, int page, int per_page) {
//        Pageable pageable = PageRequest.of(page - 1, per_page);
//        return settingsRepository.search(keyTitle, keyValue, pageable);
//    }
//
//
//    @Override
//    public Integer getTotalSetting(String keyTitle, String keyValue) {
//
//        return settingsRepository.getTotalSetting(keyTitle, keyValue);
//    }
//
//    @Override
//    public List<SettingsDTO> showSettingsList() {
//        List<Settings> settings = settingsRepository.findAll();
//        List<SettingsDTO> settingsDTOS = settings.stream()
//                .map(setting -> mapper.map(setting, SettingsDTO.class))
//                .collect(Collectors.toList());
//        return settingsDTOS;
//    }

}
