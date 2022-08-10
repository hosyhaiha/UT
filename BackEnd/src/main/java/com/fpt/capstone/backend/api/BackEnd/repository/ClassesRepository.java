package com.fpt.capstone.backend.api.BackEnd.repository;

import com.fpt.capstone.backend.api.BackEnd.dto.ClassSettingsDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.classes.ClassesDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.classes.ClassesListDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.milestones.MilestonesDTO;
import com.fpt.capstone.backend.api.BackEnd.entity.ClassSettings;
import com.fpt.capstone.backend.api.BackEnd.entity.Classes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(propagation = Propagation.REQUIRES_NEW)
@Repository
public interface ClassesRepository extends JpaRepository<Classes, Integer> {
    @Query("SELECT new com.fpt.capstone.backend.api.BackEnd.dto.classes.ClassesDTO(" +
            "c.id, c.code, c.trainerId, u3.fullName, s.id,s.code, s.name,c.year,c.term,c.status,c.block5Class," +
            "c.created,c.created_by,u1.fullName,c.modified,c.modified_by,u2.fullName) " +
            " FROM Classes c   " +
            " join Subjects s on s.id=c.subjectId" +
            " join Users u1 on u1.id= c.created_by " +
            " join Users u2 on u2.id=c.modified_by " +
            " join Users u3 on u3.id=c.trainerId " +
            " WHERE c.id =?1")
    ClassesDTO getClassesDetail(int id);

    Classes findByCode(String code);

    @Query("SELECT new com.fpt.capstone.backend.api.BackEnd.dto.classes.ClassesDTO(" +
            "c.id, c.code, c.trainerId, u3.fullName, s.id,s.code, s.name,c.year,c.term,c.status,c.block5Class," +
            "c.created,c.created_by,u1.fullName,c.modified,c.modified_by,u2.fullName) " +
            " FROM Classes c   " +
            " join Subjects s on s.id=c.subjectId" +
            " join Users u1 on u1.id= c.created_by " +
            " join Users u2 on u2.id=c.modified_by " +
            " join Users u3 on u3.id=c.trainerId " +
            " WHERE (:code is null or c.code LIKE %:code%)" +
            " AND (COALESCE(:trainerId) is null or c.trainerId IN :trainerId)" +
            " AND (COALESCE(:subjectCode) is null or s.code IN :subjectCode)" +
            " AND (:year is null or c.year = :year)" +
            " AND (:term is null or c.term = :term)" +
            " AND (:status is null or c.status = :status)" +
            " AND (:block5 is null or c.block5Class = :block5)")
    Page<ClassesDTO> search(
            @Param("code") String code,
            @Param("trainerId") List<Integer> trainerId,
            @Param("subjectCode") List<String> subjectCode,
            @Param("year") Integer year,
            @Param("term") String term,
            @Param("status") String status,
            @Param("block5") Integer block5,
            Pageable pageable);

    @Query(" SELECT new com.fpt.capstone.backend.api.BackEnd.dto.classes.ClassesListDTO(c.id,c.code) " +
            " FROM Classes c " +
            " left JOIN Subjects s on s.id = c.subjectId " +
            " where  (COALESCE(:subjectId) is null or s.id IN :subjectId) " +
            " AND (:status is null or c.status= :status)")
    List<ClassesListDTO> getLabelList(@Param("subjectId") List<Integer> subjectId, @Param("status") String status);

    // Vì lấy left join với milestones nên không thể lấy điều kiện classId theo milestone được mà phải lấy theo classes
    @Query("SELECT new com.fpt.capstone.backend.api.BackEnd.dto.milestones.MilestonesDTO(m.id, i.id, i.name) FROM Iterations i " +
            "JOIN Subjects s on s.id = i.subjectId " +
            "JOIN Classes c on (c.subjectId = s.id and c.id = :classId)" +
            "LEFT JOIN Milestones m on (m.iterationId = i.id AND m.status <> :status)")
    List<MilestonesDTO> getMilestoneConfig(@Param("classId") Integer classId, @Param("status") String status);
}
