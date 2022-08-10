package com.fpt.capstone.backend.api.BackEnd.repository;


import com.fpt.capstone.backend.api.BackEnd.dto.EvaluationCriteriaDTO;
import com.fpt.capstone.backend.api.BackEnd.entity.EvaluationCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(propagation = Propagation.REQUIRES_NEW)
@Repository
public interface EvaluationCriteriaRepository extends JpaRepository<EvaluationCriteria, Integer> {

    @Query("SELECT new com.fpt.capstone.backend.api.BackEnd.dto.EvaluationCriteriaDTO(e.id,e.iterationId,i.name," +
            "e.evaluationWeight,e.teamEvaluation,e.maxLoc,e.status,e.created,e.created_by,e.modified,e.modified_by,u1.fullName,u2.fullName)" +
            " FROM EvaluationCriteria e   " +
            " join Users u1 on u1.id= e.created_by " +
            " join Iterations i on i.id=e.iterationId " +
            " join Subjects s on s.id=i.subjectId "+
            " join Users u2 on u2.id=e.modified_by " +
            " WHERE (COALESCE(:iterationId) is null or i.id IN :iterationId)" +
            " AND (COALESCE(:authorId) is null or s.authorId IN :authorId)" +
            " AND ( :status is null or e.status = :status )")
    Page<EvaluationCriteriaDTO> search(@Param("iterationId") List<Integer> iterationId,
                                        @Param("authorId") List<Integer> authorId
            , @Param("status") String status, Pageable pageable) ;
    @Query("SELECT new com.fpt.capstone.backend.api.BackEnd.dto.EvaluationCriteriaDTO(e.id,e.iterationId,i.name," +
            "e.evaluationWeight,e.teamEvaluation,e.maxLoc,e.status,e.created,e.created_by,e.modified,e.modified_by,u1.fullName,u2.fullName)" +
            " FROM EvaluationCriteria e   " +
            " join Users u1 on u1.id= e.created_by " +
            " join Iterations i on i.id=e.iterationId"+
            " join Users u2 on u2.id=e.modified_by " +
            "  WHERE e.id =:id")
    EvaluationCriteriaDTO getEvaluationCriteriaDetail(int id);
}
