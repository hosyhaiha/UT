package com.fpt.capstone.backend.api.BackEnd.repository;

import com.fpt.capstone.backend.api.BackEnd.dto.setting.SettingRoleDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.setting.SettingTypeDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.setting.SettingsDTO;
import com.fpt.capstone.backend.api.BackEnd.entity.Settings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public interface SettingsRepository extends JpaRepository<Settings, Integer> {
    Optional<Settings> findRoleByValue(String value);

    @Query("SELECT count(p.id) FROM Settings p WHERE p.typeId = ?1"
            + " and p.displayOrder = ?2")
    Integer searchByTypeIdDisplayOrder(int typeId, int displayOrder);

    @Query("SELECT new com.fpt.capstone.backend.api.BackEnd.dto.setting.SettingsDTO( s1.id, s1.typeId,s2.value, s1.title , s1.value" +
            ", s1.displayOrder, s1.status, s1.description, s1.created, s1.created_by, s1.modified, s1.modified_by,u1.fullName,u2.fullName) " +
            "FROM Settings s1 " +
            "JOIN Settings s2 ON s1.typeId = s2.id " +
            "join Users u1 on u1.id=s1.created_by " +
            "join Users u2 on u2.id=s1.modified_by " +
            "WHERE s1.typeId = 1")
    List<SettingsDTO> getTypeSeting();

    @Query("SELECT new com.fpt.capstone.backend.api.BackEnd.dto.setting.SettingsDTO( " +
            "s1.id, s1.typeId,s2.value, s1.title  , s1.value" +
            ", s1.displayOrder, s1.status, s1.description, s1.created, s1.created_by," +
            " s1.modified, s1.modified_by,u1.fullName,u2.fullName) " +
            "FROM Settings s1 " +
            "JOIN Settings s2 ON s1.typeId = s2.id " +
            "join Users u1 on u1.id=s1.created_by " +
            "join Users u2 on u2.id=s1.modified_by " +
            "WHERE (COALESCE(:id) IS NULL OR s2.id in :id) " +
            "AND (:title is null or s1.title LIKE %:title%) " +
            "AND (:value is null or s1.value LIKE %:value%) " +
            "and (:status is null or s1.status = :status) AND s2.id <>0 AND s2.id <>1")
    Page<SettingsDTO> getSetingByType(@Param("id") List<Integer> id,
                                      @Param("title") String title,
                                      @Param("value") String value,
                                      @Param("status") String status,
                                      Pageable pageable);

    @Query("SELECT new com.fpt.capstone.backend.api.BackEnd.dto.setting.SettingsDTO( s1.id, s1.typeId,s2.value, s1.title , s1.value" +
            ", s1.displayOrder, s1.status, s1.description, s1.created, s1.created_by, s1.modified, s1.modified_by,u1.fullName,u2.fullName) " +
            "FROM Settings s1 " +
            "JOIN Settings s2 ON s1.typeId = s2.id " +
            "join Users u1 on u1.id=s1.created_by " +
            "join Users u2 on u2.id=s1.modified_by " +
            "WHERE s1.id = ?1")
    public SettingsDTO getSetingDetail(int id);

    @Query("SELECT new com.fpt.capstone.backend.api.BackEnd.dto.setting.SettingTypeDTO( s1.id,s1.title , s1.value)" +
            "FROM Settings s1 " +
            "WHERE s1.typeId=1 and s1.status='active'")
    List<SettingTypeDTO> getSettingType();

    @Query("SELECT new com.fpt.capstone.backend.api.BackEnd.dto.setting.SettingRoleDTO(s1.id,s1.value)" +
            "FROM Settings s1 " +
            "WHERE s1.id in (7,8,9,10)")
    List<SettingRoleDTO> getSettingRole();

    @Query("SELECT new com.fpt.capstone.backend.api.BackEnd.dto.setting.SettingTypeDTO( s1.id,s1.title , s1.value)" +
            "FROM Settings s1 " +
            "WHERE s1.typeId=1 and s1.status='active' AND s1.id=?1")
    SettingTypeDTO searchSettingType(int id);

    @Query("SELECT new com.fpt.capstone.backend.api.BackEnd.dto.setting.SettingTypeDTO( s1.id,s1.title , s1.value)" +
            "FROM Settings s1 " +
            "WHERE s1.title = ?1")
    List<SettingTypeDTO> searchSettingsByTitle(String title);

}
