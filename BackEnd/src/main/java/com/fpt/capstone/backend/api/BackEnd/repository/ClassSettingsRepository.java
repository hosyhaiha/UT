package com.fpt.capstone.backend.api.BackEnd.repository;

import com.fpt.capstone.backend.api.BackEnd.dto.ClassSettingOptionDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.ClassSettingsDTO;
import com.fpt.capstone.backend.api.BackEnd.entity.ClassSettings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClassSettingsRepository extends JpaRepository<ClassSettings, Integer> {
    ClassSettings findByTitle(String title);

    @Query("SELECT new com.fpt.capstone.backend.api.BackEnd.dto.ClassSettingsDTO(cs.id,cs.classId,c.code,cs.typeId," +
            "cs.title,cs.value,cs.description,cs.status,cs.created,cs.created_by,cs.modified,cs.modified_by,u1.email,u2.email ) " +
            " FROM ClassSettings cs   " +
            " join ClassSettings cs2 on cs2.id= cs.typeId " +
            " join Classes c on c.id=cs.classId "+
            " join Users u1 on u1.id= cs.created_by " +
            " join Users u2 on u2.id=cs.modified_by " +
            " WHERE c.id =?1 ")
    ClassSettingsDTO getClassesDetail(Integer id);

    @Query("SELECT new com.fpt.capstone.backend.api.BackEnd.dto.ClassSettingsDTO(cs.id,cs.classId,c.code,cs.typeId," +
            "cs.title,cs.value,cs.description,cs.status,cs.created,cs.created_by,cs.modified,cs.modified_by,u1.email,u2.email ) " +
            " FROM ClassSettings cs   " +
            " join ClassSettings cs2 on cs2.id= cs.typeId " +
            " join Classes c on c.id=cs.classId "+
            " join Users u1 on u1.id= cs.created_by " +
            " join Users u2 on u2.id=cs.modified_by " +
            " WHERE (COALESCE(:classId) is null or c.id IN :classId)  " +
            " and (:status is null or cs.status = :status ) ")
    Page<ClassSettingsDTO> search(@Param("classId") List<Integer> classId, @Param("status") String status, Pageable pageable);

    @Query("SELECT new com.fpt.capstone.backend.api.BackEnd.dto.ClassSettingOptionDTO(cs.id, cs.title, cs.value) " +
            "FROM ClassSettings cs   " +
            "WHERE (cs.typeId = :typeId)  " +
            "AND (COALESCE(:status) is null or cs.status IN :status) " +
            "AND cs.classId = :classId")
    List<ClassSettingOptionDTO> getOptions(Integer typeId, String status, Integer classId);

    List<ClassSettings> findAllByClassIdAndStatusAndTypeIdIn(Integer classId, String status, List<Integer> typeId);
}
