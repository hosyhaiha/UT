package com.fpt.capstone.backend.api.BackEnd.service.impl;

import com.fpt.capstone.backend.api.BackEnd.dto.IterationEvaluationsDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.MemberEvaluationsDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.TeamEvaluationsDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.loc_evaluations.LocEvaluationsDTO;
import com.fpt.capstone.backend.api.BackEnd.repository.IterationEvaluationsRepository;
import com.fpt.capstone.backend.api.BackEnd.repository.IterationsRepository;
import com.fpt.capstone.backend.api.BackEnd.repository.LocEvaluationsRepository;
import com.fpt.capstone.backend.api.BackEnd.service.LocEvaluationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LocEvaluationsServiceImpl implements LocEvaluationsService {

    @Autowired
    LocEvaluationsRepository locEvaluationsRepository;

    @Autowired
    IterationEvaluationsRepository iterationEvaluationsRepository;

    @Override
    public List<LocEvaluationsDTO> showList(int userId, int projectId, int milestoneId) throws Exception {
        return locEvaluationsRepository.getLocEvaluations(userId, projectId, milestoneId);
    }

    /**
     * Hàm lấy điểm team của 1 học sinh
     *
     * @param userId
     * @param projectId
     * @param milestoneId
     * @return
     * @throws Exception
     */
    @Override
    public Map<String, Object> getStudentEvaluation(int userId, int projectId, int milestoneId) throws Exception {
        Map<String, Object> result = new HashMap<>();
        List<TeamEvaluationsDTO> teamEvaluations = iterationEvaluationsRepository.getStudentTeamGrade(projectId, userId, milestoneId);
        IterationEvaluationsDTO iterationEvaluation = new IterationEvaluationsDTO();
        BigDecimal totalTeamGrade = BigDecimal.valueOf(0); // tính điểm của team
        BigDecimal totalTeamWeight = BigDecimal.valueOf(0);
        BigDecimal totalIndividualGrade = BigDecimal.valueOf(0); // tính điểm của cá nhân
        BigDecimal totalIndividualWeight = BigDecimal.valueOf(0);
        for (TeamEvaluationsDTO teamEvaluation : teamEvaluations) {
            if (teamEvaluation.getGrade() != null && teamEvaluation.getWeight() != null) {
                totalTeamGrade = totalTeamGrade.add(teamEvaluation.getGrade().multiply(teamEvaluation.getWeight()));
                totalTeamWeight = totalTeamWeight.add(teamEvaluation.getWeight());
            }

            if (iterationEvaluation.getId() == null) {
                iterationEvaluation.setId(teamEvaluation.getIterationEvaluationId());
                iterationEvaluation.setBonus(teamEvaluation.getStudentIteBonus());
                iterationEvaluation.setTotalGrade(teamEvaluation.getStudentIteGrade());
            }
        }

        iterationEvaluation.setTeamEvalGrade(BigDecimal.valueOf(0)); // default sẽ = 0

        if (totalTeamWeight.compareTo(BigDecimal.valueOf(0)) == 1) iterationEvaluation.setTeamEvalGrade(totalTeamGrade.divide(totalTeamWeight));

        List<MemberEvaluationsDTO> memberEvaluations = iterationEvaluationsRepository.getMemberEvaluation(projectId, userId, milestoneId);
        for (MemberEvaluationsDTO memberEvaluation : memberEvaluations) {
            if (memberEvaluation.getGrade() != null && memberEvaluation.getCriteriaWeight() != null) {
                totalIndividualGrade = totalIndividualGrade.add(memberEvaluation.getGrade().multiply(memberEvaluation.getCriteriaWeight()));
                totalIndividualWeight = totalIndividualWeight.add(memberEvaluation.getCriteriaWeight());
            }
        }

        iterationEvaluation.setIndividualEval(BigDecimal.valueOf(0)); // default sẽ = 0
        if (totalIndividualWeight.compareTo(BigDecimal.valueOf(0)) == 1) iterationEvaluation.setIndividualEval(totalIndividualGrade.divide(totalIndividualWeight));

        result.put("memberEvaluations", memberEvaluations);

        result.put("teamEvaluations", teamEvaluations);
        result.put("iterationEvaluation", iterationEvaluation);

        return result;
    }
}
