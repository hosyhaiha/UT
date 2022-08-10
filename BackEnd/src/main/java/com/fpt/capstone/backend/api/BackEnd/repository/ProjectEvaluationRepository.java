package com.fpt.capstone.backend.api.BackEnd.repository;

import com.fpt.capstone.backend.api.BackEnd.dto.ProjectEvaluationDTO;
import com.fpt.capstone.backend.api.BackEnd.entity.ProjectEvaluations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface ProjectEvaluationRepository extends JpaRepository<ProjectEvaluations, Integer> {

    @Query("SELECT  new com.fpt.capstone.backend.api.BackEnd.dto.ProjectEvaluationDTO(pe.id,pe.grade,ec.evaluationWeight,p.id,i.id) from " +
            " ProjectEvaluations pe " +
            " join Projects p on p.id=pe.projectId  " +
            " join EvaluationCriteria  ec on ( ec.id = pe.criteriaId and ec.teamEvaluation = 1 ) " +
            " join Milestones m on (m.id= pe.milestoneId and m.classId=p.classId)  " +
            " join Classes c on c.id=p.classId " +
            " join Iterations i on i.id = m.iterationId " +
            " where  (COALESCE(:milestoneId) is null or m.id IN :milestoneId)" +
            " And i.id=:iterationId")
    List<ProjectEvaluationDTO> projectEvaluationList(@Param("milestoneId") List<Integer> milestoneId, @Param("iterationId") Integer iterationId);
}
