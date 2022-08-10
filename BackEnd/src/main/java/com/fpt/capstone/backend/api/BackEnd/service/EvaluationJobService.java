package com.fpt.capstone.backend.api.BackEnd.service;

import com.fpt.capstone.backend.api.BackEnd.dto.EvaluationCriteriaDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.MilestoneCriteriaDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.ModelStatus;
import com.fpt.capstone.backend.api.BackEnd.entity.*;
import com.fpt.capstone.backend.api.BackEnd.repository.IterationEvaluationsRepository;
import com.fpt.capstone.backend.api.BackEnd.repository.MemberEvaluationRepository;
import com.fpt.capstone.backend.api.BackEnd.repository.MilestonesRepository;
import com.fpt.capstone.backend.api.BackEnd.repository.TeamEvaluationsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class EvaluationJobService {
    @Autowired
    private MilestonesRepository milestonesRepository;

    @Autowired
    private IterationEvaluationsRepository iterationEvaluationsRepository;

    @Autowired
    private TeamEvaluationsRepository teamEvaluationsRepository;

    @Autowired
    private MemberEvaluationRepository memberEvaluationRepository;

    public void testJobs() {
        Milestones m = milestonesRepository.getById(1);
        m.setDescription("thành công nhé em1!");
        milestonesRepository.save(m);
    };

//    public void createStudentEvaluation(ClassUsers student) {
//        System.out.println("success");
//        List<MilestoneCriteriaDTO> criteria = milestonesRepository.getMemberCriteria(student.getClassId());
//        // gen data cho mảng eval criteria
//        for (MilestoneCriteriaDTO milestoneCriteria: criteria) {
//            milestoneCriteria.getEvaluationCriteriaList().add(new EvaluationCriteriaDTO(milestoneCriteria.getCriteriaId(), milestoneCriteria.getIterationId(), milestoneCriteria.getEvalWeight(), milestoneCriteria.getIsTeamCriteria()));
//        }
//
//        List<TeamEvaluations> teamEvaluations = new ArrayList<>();
//        List<MemberEvaluations> memberEvaluations = new ArrayList<>();
//        // thực hiện thêm record
//        for (MilestoneCriteriaDTO milestoneCriteria: criteria) {
//            // Thêm iteration_evaluation
//            IterationEvaluations newIterationEvaluation = new IterationEvaluations(milestoneCriteria.getMilestoneId(), student.getId(), BigDecimal.valueOf(0), BigDecimal.valueOf(0));
//            newIterationEvaluation = iterationEvaluationsRepository.save(newIterationEvaluation);
//            // Thêm team eval, member eval
//            for (EvaluationCriteriaDTO evaluationCriteria : milestoneCriteria.getEvaluationCriteriaList()) {
//                if (evaluationCriteria.getTeamEvaluation() == ModelStatus.IS_TEAM_EVALUATION) {
//                    // Thêm điểm team
//                    teamEvaluations.add(new TeamEvaluations(evaluationCriteria.getId(), milestoneCriteria.getMilestoneId(), student.getProjectId(), BigDecimal.valueOf(0)));
//                } else {
//                    // Thêm điểm cá nhân
//                    memberEvaluations.add(new MemberEvaluations(newIterationEvaluation.getId(), evaluationCriteria.getId(), 0, BigDecimal.valueOf(0)));
//                }
//            }
//        }
//        teamEvaluationsRepository.saveAll(teamEvaluations);
//        memberEvaluationRepository.saveAll(memberEvaluations);
//    }
}
