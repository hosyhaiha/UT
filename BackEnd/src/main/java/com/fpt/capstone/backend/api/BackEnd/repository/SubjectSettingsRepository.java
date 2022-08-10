package com.fpt.capstone.backend.api.BackEnd.repository;

import com.fpt.capstone.backend.api.BackEnd.dto.subject_setting.SubjectSettingTypeDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.subject_setting.SubjectSettingsDTO;
import com.fpt.capstone.backend.api.BackEnd.entity.SubjectSettings;
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
public interface SubjectSettingsRepository extends JpaRepository<SubjectSettings, Integer> {
    @Query("SELECT new com.fpt.capstone.backend.api.BackEnd.dto.subject_setting.SubjectSettingsDTO( " +
            "s1.id,s1.subjectId,subject.name, s1.typeId,s2.value, s1.title , s1.value" +
            ", s1.displayOrder, s1.status,s1.description, s1.created, s1.created_by," +
            " s1.modified, s1.modified_by,u1.fullName,u2.fullName) " +
            "FROM SubjectSettings s1 " +
            "JOIN SubjectSettings s2 ON s1.typeId = s2.id " +
            "JOIN Subjects subject ON s1.subjectId=subject.id "+
            "join Users u1 on u1.id=s1.created_by " +
            "join Users u2 on u2.id=s1.modified_by " +
            "WHERE (COALESCE(:settingTypeId) IS NULL OR s2.id IN :settingTypeId) AND s2.id <> 0 AND s2.id <> 1 " +
            "AND (COALESCE(:subjectId) IS NULL OR s1.subjectId IN :subjectId) " +
            "AND (:title is null or s1.title LIKE %:title%) " +
            "AND (:value is null or s1.value LIKE %:value%) " +
            "AND (:status IS NULL OR s1.status = :status)")
    public Page<SubjectSettingsDTO> getSubjectSettings(
            @Param("settingTypeId") List<Integer> settingTypeId,
            @Param("subjectId") List<Integer> subjectId,
            @Param("title") String title,
            @Param("value") String value,
            @Param("status") String status ,
            Pageable pageable);

    @Query("SELECT count(p.id) FROM SubjectSettings p " +
            "WHERE p.typeId = ?1 " +
            "AND p.displayOrder = ?2 " +
            "AND p.subjectId = ?3 ")
    Integer searchByTypeIdDisplayOrder(int typeId, int displayOrder,int subjectId);


    @Query("SELECT count(p.id) FROM SubjectSettings p WHERE p.title = ?1 ")
    Integer findSubjectSettingsByTitle(String title);

    @Query("SELECT new com.fpt.capstone.backend.api.BackEnd.dto.subject_setting.SubjectSettingsDTO( " +
            "s1.id,s1.subjectId,subject.name, s1.typeId,s2.value, s1.title, s1.value" +
            ", s1.displayOrder, s1.status,s1.description, s1.created, s1.created_by," +
            " s1.modified, s1.modified_by,u1.fullName,u2.fullName) " +
            "FROM SubjectSettings s1 " +
            "JOIN SubjectSettings s2 ON s1.typeId = s2.id " +
            "JOIN Subjects subject ON s1.subjectId=subject.id "+
            "join Users u1 on u1.id=s1.created_by " +
            "join Users u2 on u2.id=s1.modified_by " +
            "WHERE s1.id = ?1 AND s2.id <> 0 AND s2.id <> 1")
    public SubjectSettingsDTO getSubjectSettingsDetail(int id);

    @Query("SELECT new com.fpt.capstone.backend.api.BackEnd.dto.subject_setting.SubjectSettingTypeDTO( s1.id, s1.title, s1.value)" +
            "FROM SubjectSettings s1 " +
            "WHERE s1.typeId=1 and s1.status='active'")
    List<SubjectSettingTypeDTO> getSubjectSettingType();

    @Query("SELECT count(s1.id) " +
            "FROM SubjectSettings s1 " +
            "JOIN SubjectSettings s2 ON s1.typeId = s2.id " +
            "join Users u1 on u1.id=s1.created_by " +
            "join Users u2 on u2.id=s1.modified_by " +
            "WHERE s1.id = ?1 AND s2.id=1")
    Integer findSubjectSettingsByTypeId(int id);
}
