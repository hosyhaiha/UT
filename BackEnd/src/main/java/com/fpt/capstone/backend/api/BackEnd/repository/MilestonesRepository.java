package com.fpt.capstone.backend.api.BackEnd.repository;

import com.fpt.capstone.backend.api.BackEnd.dto.*;
import com.fpt.capstone.backend.api.BackEnd.dto.milestones.MilestonesDTO;
import com.fpt.capstone.backend.api.BackEnd.entity.Milestones;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface MilestonesRepository extends JpaRepository<Milestones, Integer> {
    @Query("SELECT new com.fpt.capstone.backend.api.BackEnd.dto.milestones.MilestonesDTO(" +
            "m.id,m.iterationId,i.name,m.classId,c.code,m.title,m.description,m.from,m.to,m.status," +
            "m.created,m.created_by,u1.fullName,m.modified,m.modified_by,u2.fullName) " +
            " FROM Milestones m   " +
            " join Iterations i on i.id= m.iterationId " +
            " join Classes c on c.id= m.classId " +
            " join Users u1 on u1.id= m.created_by " +
            " join Users u2 on u2.id=m.modified_by " +
            " WHERE m.id =?1")
    MilestonesDTO getMilestonesDetail(int id);

    @Query("SELECT new com.fpt.capstone.backend.api.BackEnd.dto.milestones.MilestonesDTO(" +
            "m.id,m.iterationId,i.name,m.classId,c.code,m.title,m.description,m.from,m.to,m.status," +
            "m.created,m.created_by,u1.fullName,m.modified,m.modified_by,u2.fullName) " +
            " FROM Milestones m   " +
            " left join Iterations i on i.id= m.iterationId " +
            " left join Classes c on c.id= m.classId " +
            " left join Users u1 on u1.id= m.created_by " +
            " left join Users u2 on u2.id=m.modified_by " +
            " WHERE (COALESCE(:iterationId) is null or i.id IN :iterationId)" +
            " AND (COALESCE(:classId) is null or c.id IN :classId) " +
            " AND (COALESCE(:trainerId) is null or c.trainerId IN :trainerId)" +
            " AND (:title is null or m.title LIKE %:title%)" +
            " AND (i.status = 'active')" +
            " AND (:status is null or m.status like :status% )" +
            " AND (COALESCE(:authorityMilestoneIds) is null or m.id in :authorityMilestoneIds)")
    Page<MilestonesDTO> search(
            @Param("iterationId") List<Integer> iterationId,
            @Param("classId") List<Integer> classId,
            @Param("trainerId") List<Integer> trainerId,
            @Param("title") String title,
            @Param("status") String status,
            Pageable pageable,
            @Param("authorityMilestoneIds") List<Integer> authorityMilestoneIds);

    Milestones getMilestonesByTitle(String title);

    Integer countByClassIdAndIterationId(int ClassID, int IterationID);

    @Query("SELECT new com.fpt.capstone.backend.api.BackEnd.dto.MilestonesListDTO(" +
            "m.id,m.title) " +
            " FROM Milestones m   " +
            " left join Classes c on c.id= m.classId " +
            " left join Projects p on p.classId=c.id" +
            " left join Users u1 on u1.id= m.created_by " +
            " left join Users u2 on u2.id=m.modified_by " +
            " WHERE (COALESCE(:authorityMilestoneIds) is null or m.id in :authorityMilestoneIds)" +
            " AND(COALESCE(:classId) is null or c.id IN :classId) " +
            " AND (COALESCE(:projectId) is null or p.id IN :projectId)" +
            " group by m.id")
    List<MilestonesListDTO> getMilestonesByClass(@Param("classId") List<Integer> classId,
                                                 @Param("projectId") List<Integer> projectId,
                                                 @Param("authorityMilestoneIds") List<Integer> authorityMilestoneIds);

    @Query("SELECT new com.fpt.capstone.backend.api.BackEnd.dto.MilestonesSyncDTO(" +
            " m.title,m.description,m.classId,m.from,m.to ) " +
            " FROM Milestones m  " +
            " join Classes c on  m.classId = c.id " +
            " JOIN Projects p on p.classId = c.id " +
            " WHERE (COALESCE(:projectId) is null or p.id IN :projectId) and" +
            " m.status='open'" +
            " group by m.id")
    List<MilestonesSyncDTO> getMilestonesSync(List<Integer> projectId);

    @Query("Select new com.fpt.capstone.backend.api.BackEnd.dto.MilestoneSubmitDTO(" +
            "m.id, p.id, s.status, p.name, t.status, f.id) FROM Milestones m " +
            "Join Classes c on m.classId = c.id " +
            "Join Projects p on (p.classId = c.id and p.status = 'active') " +
            "LEFT JOIN Submits s on (s.milestoneId = m.id and s.projectId = p.id) " +
            "LEFT join Features ft on ft.projectId = p.id " +
            "LEFT JOIN Functions f on (f.featureId = ft.id)" +
            "LEFT JOIN Trackings t on (t.functionId = f.id) " +
            "WHERE (COALESCE(:classId) is null or c.id = :classId ) " +
            "AND (COALESCE(:projectId) is null or p.id = :projectId )" +
            "AND (COALESCE(:milestoneId) is null or m.id = :milestoneId )" +
            "AND m.status <> 'cancelled' " +
            "GROUP BY f.id"
    )
    List<MilestoneSubmitDTO> getMilestoneSubmits(Integer classId, List<Integer> projectId, Integer milestoneId);

    @Query("SELECT new com.fpt.capstone.backend.api.BackEnd.dto.TeamEvaluationsDTO(te.id, ec.name, ec.id, te.milestoneId, " +
            "p.id, te.grade, ec.evaluationWeight, te.comment, i.id) FROM EvaluationCriteria ec " +
            "JOIN Iterations i on i.id = ec.iterationId " +
            "JOIN Classes  c on c.subjectId = i.subjectId " +
            "JOIN Projects p on p.classId = c.id " +
            "JOIN Milestones m on (i.id = m.iterationId and m.id = :milestoneId)" +
            "LEFT JOIN TeamEvaluations te on (ec.id = te.criteriaId and te.projectId = p.id and ec.status = 'active') " +
            "Where COALESCE(:projectId) is null or p.id in :projectId AND ec.teamEvaluation = 1 " +
            "group by p.id, m.id, ec.id")
    List<TeamEvaluationsDTO> getMilestoneTeamEval(List<Integer> projectId, Integer milestoneId);

    @Query("Select m.id from Milestones m " +
            "left join Classes c on c.id=m.classId " +
            "left join Subjects s on s.id=c.subjectId " +
            "where s.authorId=:id" +
            " group by m.id")
    List<Integer> getMilestoneAuthor(Integer id);

    @Query(" Select m.id from Milestones m " +
            " left join Classes c on c.id=m.classId " +
            " where c.trainerId=:id" +
            " group by m.id")
    List<Integer> getMilestoneTrainer(Integer id);

    @Query(" Select m.id from Milestones m " +
            " left join Classes c on c.id=m.classId " +
            " left join ClassUsers cu on c.id=cu.classId" +
            " where cu.userId=:id" +
            " group by m.id")
    List<Integer> getMilestoneStudent(Integer id);

    @Query("SELECT new com.fpt.capstone.backend.api.BackEnd.dto.MilestoneCriteriaDTO(m.id, m.iterationId, ec.id, ec.teamEvaluation, ec.evaluationWeight) FROM Milestones m " +
            "JOIN EvaluationCriteria ec on (ec.iterationId = m.iterationId and ec.status = 'active' and ec.teamEvaluation = 0) " +
            "WHERE m.classId = :classId and m.status <> 'cancelled' ")
    List<MilestoneCriteriaDTO> getMemberCriteria(@Param("classId") Integer classId);

    //    @Query("SELECT new com.fpt.capstone.backend.api.BackEnd.dto.milestones.MilestonesDTO() " +
//            " FROM Milestones m " +
//            " join EvaluationCriterias ec on (ec.iterationId = m.iterationId and ec.status = 'active' and ec.teamEvaluation = 0) " +
//            " WHERE m.id = :milestoneId")
//    MilestonesDTO getMilestoneEval(@Param("milestoneId") Integer milestoneId);


}
