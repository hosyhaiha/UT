package com.fpt.capstone.backend.api.BackEnd.service;

import com.fpt.capstone.backend.api.BackEnd.dto.UpdateLogDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.UserLogsDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserLogService {
    List<UserLogsDTO> getListUserLog(Integer id);

    Page<UpdateLogDTO> listBy(Integer functionId, int page, int per_page) throws Exception;
}
