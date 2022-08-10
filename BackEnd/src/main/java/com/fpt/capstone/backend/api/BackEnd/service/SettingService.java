package com.fpt.capstone.backend.api.BackEnd.service;

import com.fpt.capstone.backend.api.BackEnd.dto.setting.SettingInputDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.setting.SettingRoleDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.setting.SettingTypeDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.setting.SettingsDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SettingService {
    //  public List<SettingsDTO> getTypeSetting();
    public List<SettingTypeDTO> getTypeSetting();

    public Page<SettingsDTO> getSetingByType(List<Integer> id, String title, String value, String status, int page, int per_page) throws Exception;

    public SettingsDTO deleteSetting(int id) throws Exception;

    public SettingsDTO addSettings(SettingInputDTO settingsInput) throws Exception;

    public SettingsDTO updateSetting(SettingInputDTO settingsInput) throws Exception;

    public SettingsDTO getSettingDetail(int id);

    public List<SettingRoleDTO> getSettingRole();

}
