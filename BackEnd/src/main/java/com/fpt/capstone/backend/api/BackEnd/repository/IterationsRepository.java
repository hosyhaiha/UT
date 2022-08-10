package com.fpt.capstone.backend.api.BackEnd.repository;

import com.fpt.capstone.backend.api.BackEnd.dto.IterationListSearchDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.IterationsDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.ListIterationEvalDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.MemberEvaluationsDTO;
import com.fpt.capstone.backend.api.BackEnd.entity.Iterations;
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
public interface IterationsRepository extends JpaRepository<Iterations, Integer> {
    @Query("SELECT new com.fpt.capstone.backend.api.BackEnd.dto.IterationsDTO(p.id,p.subjectId,p.name,s.name,p.evaluationWeight," +
            "p.status,p.description,p.isOngoing,p.created,p.created_by,p.modified,p.modified_by,u1.fullName,u2.fullName) FROM Iterations p   " +
            " join Users u1 on u1.id= p.created_by " +
            " join Subjects s on s.id=p.subjectId " +
            " join Users u2 on u2.id=p.modified_by " +
            " WHERE (:iterationName is null or p.name LIKE %:iterationName%) " +
            " AND (COALESCE(:subjectId) is null or s.id IN :subjectId) " +
            "AND (COALESCE(:authorId) is null or s.authorId IN :authorId) " +
            " AND ( :status is null or p.status like :status% )")
    Page<IterationsDTO> search(@Param("subjectId") List<Integer> subjectId, @Param("iterationName") String iterationName,
                               @Param("authorId") List<Integer> authorId, @Param("status") String status,
                               Pageable pageable);

    @Query("SELECT count(p.id) FROM Iterations p WHERE p.name like ?1 ")
    Integer findByIterationsName(String keyName);

    @Query("SELECT new com.fpt.capstone.backend.api.BackEnd.dto.IterationsDTO(p.id,p.subjectId,p.name,s.name,p.evaluationWeight," +
            "p.status,p.description,p.isOngoing,p.created,p.created_by,p.modified,p.modified_by,u1.fullName,u2.fullName) FROM Iterations p   " +
            " join Users u1 on u1.id= p.created_by " +
            " join Subjects s on s.id=p.subjectId" +
            " join Users u2 on u2.id=p.modified_by " +
            "  WHERE p.id =:id")
    IterationsDTO getIterationsDetail(int id);

    @Query("select distinct i.subjectId from Iterations i")
    List<Integer> getAllSubjectIdInIteration();

    @Query(" SELECT new com.fpt.capstone.backend.api.BackEnd.dto.IterationListSearchDTO(s.id,s.name)" +
            " FROM Iterations s where (:status is null or s.status= :status)")
    public List<IterationListSearchDTO> showAll(@Param("status") String status);


    @Query(" SELECT new com.fpt.capstone.backend.api.BackEnd.dto.ListIterationEvalDTO(i.name,i.id)" +
            " FROM Iterations i" +
            " JOIN Milestones  m on( m.iterationId=i.id and m.classId= :classId)" )
    List<ListIterationEvalDTO> findIterationInClass(@Param("classId") Integer classId);
}
