package com.fpt.capstone.backend.api.BackEnd.service;

import com.fpt.capstone.backend.api.BackEnd.dto.TeamEvaluationsDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.loc_evaluations.LocEvaluationsDTO;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public interface LocEvaluationsService {

    List<LocEvaluationsDTO> showList(int userId, int projectId, int milestoneId) throws Exception;

    /**
     * Hàm lấy điểm team của 1 học sinh
     * @param userId
     * @param projectId
     * @param milestoneId
     * @return
     * @throws Exception
     */
    Map<String, Object> getStudentEvaluation(int userId, int projectId, int milestoneId) throws Exception;

}
