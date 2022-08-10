package com.fpt.capstone.backend.api.BackEnd.service;

import com.fpt.capstone.backend.api.BackEnd.dto.subject_setting.SubjectSettingInputDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.subject_setting.SubjectSettingTypeDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.subject_setting.SubjectSettingsDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SubjectSettingsService {

    SubjectSettingsDTO addSubjectSetting(SubjectSettingInputDTO subjectSettingInputDTO) throws Exception;

    SubjectSettingsDTO deleteSubjectSetting(int id) throws Exception;

    SubjectSettingsDTO updateSubjectSetting(SubjectSettingInputDTO subjectSettingInputDTO) throws Exception;

    SubjectSettingsDTO getSubjectSettingDetail(int id);

    List<SubjectSettingTypeDTO> getSubjectSettingType() throws Exception;

    Page<SubjectSettingsDTO> searchBy(List<Integer> settingTypeId, List<Integer> subjectId, String title, String value, String status, int page, int per_page) throws Exception;
}
