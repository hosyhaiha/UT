package com.fpt.capstone.backend.api.BackEnd.repository;

import com.fpt.capstone.backend.api.BackEnd.dto.FeaturesDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.FeaturesListDTO;
import com.fpt.capstone.backend.api.BackEnd.entity.Features;
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
public interface FeaturesRepository extends JpaRepository<Features, Integer> {

    @Query("SELECT new com.fpt.capstone.backend.api.BackEnd.dto.FeaturesDTO(f.id,f.projectId,p.name,p.code,f.name," +
            "f.status,f.created,f.created_by,f.modified,f.modified_by,u1.fullName,u2.fullName)" +
            " FROM Features f   " +
            " join Users u1 on u1.id= f.created_by " +
            " join Projects p on p.id=f.projectId" +
            " join Users u2 on u2.id=f.modified_by " +
            "  WHERE f.id =:id")
    FeaturesDTO getFeatureDetail(int id);

    @Query("SELECT new com.fpt.capstone.backend.api.BackEnd.dto.FeaturesDTO(f.id,f.projectId,p.name,p.code,f.name," +
            "f.status,f.created,f.created_by,f.modified,f.modified_by,u1.fullName,u2.fullName)" +
            " FROM Features f   " +
            " join Users u1 on u1.id= f.created_by " +
            " left join Projects p on p.id=f.projectId " +
            " left join ClassUsers cu on cu.projectId=p.id " +
            " join Users u2 on u2.id=f.modified_by " +
            " WHERE (COALESCE(:projectId) is null or p.id IN :projectId) " +
            " AND (COALESCE(:userId) is null or cu.userId IN :userId) " +
            " AND (:name is null or f.name LIKE %:name%) " +
            " AND (:status is null or f.status like :status% ) " +
            " group by f.id")
    Page<FeaturesDTO> search(@Param("projectId") List<Integer> projectId,
                             @Param("userId") List<Integer> userId,
                             @Param("name") String name,
                             @Param("status") String status,
                             Pageable pageable);
    @Query(" SELECT new com.fpt.capstone.backend.api.BackEnd.dto.FeaturesListDTO(ft.id,ft.name)" +
            "FROM Features ft " +
            "where (:status is null or ft.status= :status) " +
            "AND (COALESCE(:projectId) is null or ft.projectId IN :projectId)")
    List<FeaturesListDTO> getLabelList(
            @Param("status") String status,
            @Param("projectId") List<Integer> projectId);

    Features findByNameAndProjectId(String name,int id);

    Features findByName(String name);
}
