package com.fpt.capstone.backend.api.BackEnd.service;

import com.fpt.capstone.backend.api.BackEnd.dto.FunctionsDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.MilestoneSubmitDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.TeamEvaluationsDTO;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public interface SubmitService {
    /**
     * Api lấy danh sách thông tin submit milestones
     *
     * @param projectId
     * @param milestoneId
     * @return
     */
    List<MilestoneSubmitDTO> getMilestoneSubmits(Integer classId, List<Integer> projectId, Integer milestoneId);

    /**
     * Hàm lấy điểm tổng của đánh giá team dựa theo list projectId và milestone truyền vào
     *
     * @param projectId
     * @param milestoneId
     * @return
     */
    HashMap<Integer, MilestoneSubmitDTO> getTeamGrade(List<Integer> projectId, Integer milestoneId, HashMap<Integer, MilestoneSubmitDTO> result);

    /**
     * Hãm dùng để update hoặc create team evalutation
     *
     * @param teamEvaluationsDTO
     * @return
     */
    TeamEvaluationsDTO editTeamEvaluation(TeamEvaluationsDTO teamEvaluationsDTO) throws Exception;

    /**
     * Hãm dùng để lấy data của loc evaluation
     *
     * @param
     * @return
     */
    FunctionsDTO getFunctionEvaluate(Integer functionId) throws Exception;

    /**
     * Hãm dùng để lấy thêm mới hoặc cập nhật điểm đánh giác loc của function
     *
     * @param
     * @return
     */
    FunctionsDTO evaluateFunction(FunctionsDTO function) throws Exception;

    void updateStatus(Integer projectId, Integer milestoneId);
}
