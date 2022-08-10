package com.fpt.capstone.backend.api.BackEnd.repository;

import com.fpt.capstone.backend.api.BackEnd.dto.SubjectConfigDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.SubjectConfigDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.SubjectConfigDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.subject.SubjectsDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.subject.SubjectsListDTO;
import com.fpt.capstone.backend.api.BackEnd.entity.Subjects;
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
public interface SubjectsRepository extends JpaRepository<Subjects, Integer> {
    @Query(" SELECT new com.fpt.capstone.backend.api.BackEnd.dto.subject.SubjectsListDTO(s.id,s.code,s.name)" +
            "  FROM Subjects s where s.status='active'" +
            " AND (COALESCE(:authoritySubjectIds) is null or s.id in :authoritySubjectIds)")
    public List<SubjectsListDTO> search(@Param("authoritySubjectIds") List<Integer> authoritySubjectIds);

    @Query(" SELECT new com.fpt.capstone.backend.api.BackEnd.dto.subject.SubjectsListDTO(s.id,s.code,s.name)" +
            "FROM Subjects s " +
            "JOIN Users u ON u.id=s.authorId " +
            "where s.status='active' AND u.email = ?1")
    List<SubjectsListDTO> searchSubjectsByAuthor(String email);

    @Query("SELECT new com.fpt.capstone.backend.api.BackEnd.dto.subject.SubjectsDTO( " +
            "s.id, s.code, s.name , s.authorId, u3.fullName" +
            ", s.status,s.description, s.created, s.created_by," +
            " s.modified, s.modified_by,u1.fullName,u2.fullName) " +
            "FROM Subjects s " +
            "JOIN Users u3 ON u3.id=s.authorId " +
            "join Users u1 on u1.id=s.created_by " +
            "join Users u2 on u2.id=s.modified_by " +
            "WHERE (:code is null or s.code LIKE %:code%) " +
            "AND( :name is null  or s.name LIKE %:name%) " +
            "AND (COALESCE(:authorId) is null or s.authorId IN :authorId)" +
            "AND(:status is null or s.status like :status%)" +
            "AND (COALESCE(:authoritySubjectIds) is null or s.id in :authoritySubjectIds)")
    Page<SubjectsDTO> searchBy(@Param("code") String code,
                               @Param("name") String name,
                               @Param("authorId") List<Integer> authorId,
                               @Param("status") String status,
                               Pageable pageable,
                               @Param("authoritySubjectIds") List<Integer> authoritySubjectIds
    );


    @Query(" SELECT new com.fpt.capstone.backend.api.BackEnd.dto.subject.SubjectsDTO(" +
            "s.id,s.code,s.name,s.authorId, u3.fullName,s.status,s.description,s.created, s.created_by" +
            ", s.modified, s.modified_by, u1.fullName,u2.fullName)" +
            "FROM Subjects s " +
            "JOIN Users u3 ON u3.id=s.authorId " +
            "JOIN Users u1 ON u1.id=s.created_by " +
            "JOIN Users u2 ON u2.id=s.modified_by " +
            "WHERE s.id = ?1")
    public SubjectsDTO getSubjectDetail(int id);

    @Query("SELECT count(p.id) FROM Subjects p WHERE p.name like ?1 ")
    Integer findBySubjectName(String keyName);

    @Query("SELECT count(p.id) FROM Subjects p WHERE p.id = ?1 ")
    Integer findBySubjectId(int id);

    @Query("SELECT count(p.id) FROM Subjects p WHERE p.code like ?1 ")
    Integer findBySubjectCode(String keyCode);

    @Query(" select s.id from Subjects s " +
            " left join Classes c on (c.subjectId=s.id and (COALESCE(:trainerId) is null or c.trainerId IN :trainerId))" +
            " left join ClassUsers cu on (cu.classId=c.id and (COALESCE(:studentId) is null or cu.userId IN :studentId))" +
            " where (COALESCE(:authorId) is null or s.authorId IN :authorId)" +
            " group by s.id")
    List<Integer> getSubjectAuthority(@Param("studentId") Integer studentId, @Param("authorId") Integer authorId, @Param("trainerId") Integer trainerId);

    @Query("select new com.fpt.capstone.backend.api.BackEnd.dto.SubjectConfigDTO(s.id, s.code, s.name, s.authorId, " +
            "s.status, i.id, i.evaluationWeight, i.isOngoing, i.status, ec.id, ec.evaluationWeight, ec.teamEvaluation, ec.status) from Subjects s " +
            "left join Iterations i on (s.id = i.subjectId and i.status = 'active') " +
            "left join EvaluationCriteria ec on ( ec.iterationId = i.id and ec.status = 'active') " +
            "where s.id = :subjectId and s.status = 'active' ")
    List<SubjectConfigDTO> getSubjectConfigs(@Param("subjectId") Integer subjectId);

    @Query(" select s.id from Subjects s " +
            " join Classes c on (c.subjectId=s.id and (COALESCE(:trainerId) is null or c.trainerId IN :trainerId))")
    List<Integer> getSubjectAuthorityTrainerId(@Param("trainerId") Integer trainerId);

    @Query(" select s.id from Subjects s " +
            " left join Classes c on (c.subjectId=s.id )" +
            "  join ClassUsers cu on (cu.classId=c.id and (COALESCE(:studentId) is null or cu.userId IN :studentId))")
    List<Integer> getSubjectStudent(@Param("studentId") Integer studentId);

    @Query(" SELECT s.id " +
            "FROM Subjects s " +
            "JOIN Users u ON u.id=s.authorId " +
            "where u.id=:id")
    List<Integer> getSubjectAuthorityRoleAuthor(Integer id);


}
