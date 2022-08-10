package com.fpt.capstone.backend.api.BackEnd.service;


import com.fpt.capstone.backend.api.BackEnd.dto.ClassSettingOptionDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.ClassSettingsDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ClassSettingsService {
    ClassSettingsDTO add(ClassSettingsDTO classSettingsDTO) throws Exception;

    ClassSettingsDTO update(ClassSettingsDTO classSettingsDTO) throws Exception;

    ClassSettingsDTO showDetail(int id) throws Exception;

    Page<ClassSettingsDTO> searchBy(List<Integer> classId, String status, int page, int limit);

    List<ClassSettingsDTO> showList(String status);

    /**
     * Hàm lấy data cho select box dựa theo type
     * @param type
     * @return
     */
    List<ClassSettingOptionDTO> getOptions(String type, Integer classId);
}
