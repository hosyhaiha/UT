package com.fpt.capstone.backend.api.BackEnd.repository;

import com.fpt.capstone.backend.api.BackEnd.dto.*;
import com.fpt.capstone.backend.api.BackEnd.dto.ListGradeDTO;
import com.fpt.capstone.backend.api.BackEnd.entity.IterationEvaluations;
import org.springframework.data.domain.Pageable;
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
public interface IterationEvaluationsRepository extends JpaRepository<IterationEvaluations, Integer> {

    @Query("select new com.fpt.capstone.backend.api.BackEnd.dto.IterationEvaluationsDTO(ie.id,ie.milestoneId,ie.classUserId," +
            " cu.userId,u1.rollNumber,u1.fullName,p.id,p.name,ie.bonus,me.id,me.convertedLoc,ie.grade, me.grade, ie.note, m.title, ie.created,ie.created_by," +
            " ie.modified,ie.modified_by,u2.fullName, u3.fullName)" +
            " from IterationEvaluations ie " +
            " left join ClassUsers cu on ie.classUserId=cu.id " +
            "join Users u1 on u1.id=cu.userId" +
            " left join MemberEvaluations  me on me.evaluationId= ie.id " +
            " left join Projects p on p.id = cu.projectId" +
            " join Milestones  m on m.id=ie.milestoneId " +
            " join Classes  c on (c.id=m.classId and p.classId=c.id )" +
            " join Iterations  i on m.iterationId=i.id " +
            " join EvaluationCriteria ec on ec.iterationId = i.id" +
            " left join MemberEvaluations  me on me.evaluationId= ie.id " +
            " join Users u2 on u2.id= ie.created_by " +
            " join Users u3 on u3.id=ie.modified_by " +
            " where i.id=:iterationId " +
            " AND c.id=:classId" +
            " AND (COALESCE(:projectId) is null or p.id IN :projectId) " +
            " AND (COALESCE(:milestoneId) is null or m.id IN :milestoneId) " +
            " group by ie.id "
    )
    List<IterationEvaluationsDTO> showList(@Param("iterationId") Integer iterationId,
                                           @Param("projectId") List<Integer> projectId,
                                           @Param("milestoneId") List<Integer> milestoneId,
                                           @Param("classId") Integer classId);

    List<IterationEvaluations> findAllByMilestoneIdAndClassUserIdIn(Integer milestoneId, List<Integer> classUserId);

    @Query("select new com.fpt.capstone.backend.api.BackEnd.dto.ListGradeDTO(cu.userId,u.fullName,u.rollNumber," +
            "p.name,cu.finalTopicEval,i.id,i.name,ie.grade,ie.bonus,cu.ongoingEval, m.id, p.id, m.title)" +
            " from IterationEvaluations ie " +
            " join ClassUsers cu on cu.id=ie.classUserId " +
            " join Projects p on p.id=cu.projectId " +
            "join Users u on u.id = cu.userId" +
            " join Milestones m on m.id=ie.milestoneId " +
            " join Iterations i on m.iterationId=i.id" +
            " where cu.userId IN :listStudent"
    )
    List<ListGradeDTO> getStudentGrade(List<Integer> listStudent);

    @Modifying
    @Query("UPDATE IterationEvaluations ie set ie.bonus=:bonus,ie.grade=:newGrade where ie.milestoneId=:milestoneId and ie.classUserId=:classUserId")
    void updateBonus(Integer milestoneId, Integer classUserId, BigDecimal bonus, BigDecimal newGrade);

    @Query("select new com.fpt.capstone.backend.api.BackEnd.dto.TeamEvaluationsDTO(te.id, ec.id, ec.name, " +
            "te.milestoneId, cu.projectId, te.grade, ec.evaluationWeight, te.comment, ie.grade, ie.bonus, ie.id, cu.id) from IterationEvaluations ie " +
            "join ClassUsers cu on cu.id = ie.classUserId " +
            "LEFT join TeamEvaluations te on te.projectId = cu.projectId " +
            "LEFT join EvaluationCriteria ec on ec.id = te.criteriaId " +
            "where cu.projectId = :projectId and cu.userId = :userId and te.milestoneId = :milestoneId " +
            "GROUP BY te.id")
    List<TeamEvaluationsDTO> getStudentTeamGrade(@Param("projectId") Integer projectId,
                                                 @Param("userId") Integer userId,
                                                 @Param("milestoneId") Integer milestoneId);

    @Query("select new com.fpt.capstone.backend.api.BackEnd.dto.MemberEvaluationsDTO(me.id, me.evaluationId, ec.id, " +
            "me.grade, me.note, ec.evaluationWeight, ec.name) from MemberEvaluations me " +
            "join ClassUsers cu on (cu.id = me.evaluationId and cu.projectId = :projectId and cu.userId = :userId) " +
            "join EvaluationCriteria ec on ec.id = me.criteriaId " +
            "join Iterations i on i.id = ec.iterationId " +
            "join Milestones m on m.iterationId = i.id " +
            "where m.id = :milestoneId")
    List<MemberEvaluationsDTO> getMemberEvaluation(@Param("projectId") Integer projectId,
                                                   @Param("userId") Integer userId,
                                                   @Param("milestoneId") Integer milestoneId);

    IterationEvaluations findByMilestoneIdAndAndClassUserId(Integer milestoneId, Integer classUserId);

}
