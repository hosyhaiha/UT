package com.fpt.capstone.backend.api.BackEnd.repository;

import com.fpt.capstone.backend.api.BackEnd.dto.EvaluationDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.loc_evaluations.LocEvaluationsDTO;
import com.fpt.capstone.backend.api.BackEnd.entity.LocEvaluations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Transactional(propagation = Propagation.REQUIRES_NEW)
@Repository
public interface LocEvaluationsRepository extends JpaRepository<LocEvaluations, Integer> {
    @Query("SELECT new com.fpt.capstone.backend.api.BackEnd.dto.EvaluationDTO(ie.id, ie.grade, i.evaluationWeight, " +
            "ec.evaluationWeight, ec.id, me.id, me.convertedLoc, ec.maxLoc, me.grade, cu.ongoingEval, cu.finalTopicEval, cu.id, cu.finalPresentEval) " +
            "FROM EvaluationCriteria ec " +
            "JOIN Milestones m on m.iterationId = ec.iterationId " +
            "JOIN MemberEvaluations me on (me.criteriaId = ec.id) " +
            "JOIN IterationEvaluations ie on (ie.id = me.evaluationId) " +
            "JOIN Iterations i on ec.iterationId = i.id " +
            "JOIN ClassUsers cu on (ie.classUserId = cu.id and cu.projectId = :projectId and cu.userId = :assigneeId) " +
            "Where ie.milestoneId = :milestoneId " +
            "And ec.teamEvaluation = 0")
    EvaluationDTO getMemberEvalInfo(@Param("milestoneId") Integer milestoneId,
                                    @Param("assigneeId") Integer assigneeId,
                                    @Param("projectId") Integer projectId
    );

    @Query("SELECT le.comment from LocEvaluations  le left join Functions f on le.functionId=f.id where le.functionId=:functionId")
    String getEvalComment(Integer functionId);

    @Modifying
    @Query("UPDATE MemberEvaluations me set me.grade = :grade, me.convertedLoc = :totalLoc where me.id = :id")
    void updateMemberEval(@Param("grade") BigDecimal grade, @Param("totalLoc") Integer totalLoc, @Param("id") Integer id);

    @Modifying
    @Query("UPDATE IterationEvaluations ie set ie.grade = :grade where ie.id = :id")
    void updateIteEval(@Param("grade") BigDecimal grade, @Param("id") Integer id);

    @Modifying
    @Query("UPDATE ClassUsers cu set cu.ongoingEval = :ongoingGrade, cu.finalTopicEval = :finalGrade where cu.id = :id")
    void updateClassUserEval(@Param("ongoingGrade") BigDecimal ongoingGrade, @Param("finalGrade") BigDecimal finalGrade, @Param("id") Integer id);

    @Query("SELECT new com.fpt.capstone.backend.api.BackEnd.dto.loc_evaluations.LocEvaluationsDTO(le.id, " +
            "le.milestoneId, le.functionId, fu.name, fu.featureId, fe.name, le.complexityId, cs1.title, cs1.value, " +
            "le.qualityId, cs2.title, cs2.value, le.convertedLoc, le.isLateSubmit, le.comment, le.newMilestoneId, " +
            "le.newComplexityId, le.newQualityId, le.newConvertedLoc,le.created,le.created_by,u1.fullName, le.modified, " +
            "le.modified_by, u2.fullName) " +
            "FROM LocEvaluations le " +
            "JOIN Functions fu on fu.id = le.functionId " +
            "JOIN Features fe on fe.id = fu.featureId " +
            "JOIN ClassSettings cs1 on cs1.id = le.complexityId " +
            "JOIN ClassSettings cs2 on cs2.id = le.qualityId " +
            "join Trackings t on t.functionId=fu.id " +
            "left join Users u1 on u1.id=le.created_by " +
            "left join Users u2 on u2.id=le.modified_by " +
            "Where cs1.typeId=11 AND cs2.typeId = 12 " +
            "AND t.assigneeId = :userId " +
            "AND t.milestoneId = :milestoneId " +
            "AND fe.projectId = :projectId")
    List<LocEvaluationsDTO> getLocEvaluations(@Param("userId") Integer userId,
                                              @Param("projectId") Integer projectId,
                                              @Param("milestoneId") Integer milestoneId);
}
