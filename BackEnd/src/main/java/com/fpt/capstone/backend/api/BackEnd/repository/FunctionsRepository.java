package com.fpt.capstone.backend.api.BackEnd.repository;

import com.fpt.capstone.backend.api.BackEnd.dto.FunctionListDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.FunctionListTrackingDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.FunctionTrackingSubmitListDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.FunctionsDTO;
import com.fpt.capstone.backend.api.BackEnd.entity.Functions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(propagation = Propagation.REQUIRES_NEW)
@Repository
public interface FunctionsRepository extends JpaRepository<Functions, Integer> {

    @Query("SELECT new com.fpt.capstone.backend.api.BackEnd.dto.FunctionsDTO(fc.id,p.id,p.code,p.name,ft.id,ft.name,fc.name," +
            "fc.description,fc.priority,fc.status,fc.created,fc.created_by,fc.modified,fc.modified_by,u2.email,u2.rollNumber,u3.email,u3.rollNumber)" +
            " FROM Functions fc   " +
            " join Features ft on ft.id=fc.featureId " +
            " join Projects p on p.id=ft.projectId " +
            " join Users u2 on u2.id= fc.created_by " +
            " join Users u3 on u3.id=fc.modified_by " +

            "  WHERE fc.id =:id")
    FunctionsDTO getFunctionDetail(int id);

    @Query("SELECT new com.fpt.capstone.backend.api.BackEnd.dto.FunctionsDTO(f.id, t.assigneeId, f.name, " +
            "f.description, t.note, le.convertedLoc, le.comment, t.status, le.complexityId, le.qualityId, " +
            "u.rollNumber, u.fullName, m.title, p.name, p.classId, le.id, t.id, m.id, p.id) FROM Functions f " +
            "JOIN Trackings t on (f.id = t.functionId) " +
            "LEFT JOIN LocEvaluations le on (le.milestoneId = t.milestoneId AND le.functionId = t.functionId) " +
            " join Features ft on ft.id=f.featureId " +
            "JOIN Projects p on p.id = ft.projectId " +
            "JOIN Users u on u.id = t.assigneeId " +
            "JOIN Milestones m on m.id = t.milestoneId " +
            "Where f.id = :functionId " +
            "AND p.status = 'active' " +
            "AND t.status = 'submitted'")
    FunctionsDTO getFunctionEvaluate(Integer functionId);

    Functions findByName(String name);

    @Query("SELECT new com.fpt.capstone.backend.api.BackEnd.dto.FunctionsDTO(fc.id, p.id,p.code, p.name," +
            " fc.featureId, ft.name,fc.name,fc.description," +
            " fc.priority,fc.status,fc.created,fc.created_by,fc.modified,fc.modified_by,u2.fullName,u2.rollNumber,u3.fullName,u3.rollNumber)" +
            " FROM Functions fc   " +
            " join Features ft on ft.id=fc.featureId " +
            " join Projects p on p.id=ft.projectId " +
            " left join Users u2 on u2.id= fc.created_by " +
            " left join Users u3 on u3.id=fc.modified_by " +
            " WHERE (COALESCE(:projectId) is null or p.id IN :projectId)" +
            " AND (COALESCE(:featureId) is null or ft.id IN :featureId)  " +
            " AND (:name is null or fc.name LIKE %:name%) " +
            " AND (:status is null or fc.status = :status ) " +
            " AND (COALESCE(:authorityFunctionIds) is null or fc.id in :authorityFunctionIds)")
    Page<FunctionsDTO> search(@Param("projectId") List<Integer> projectId,
                              @Param("featureId") List<Integer> featureId,
                              @Param("name") String name,
                              @Param("status") String status,
                              Pageable pageable,
                              @Param("authorityFunctionIds") List<Integer> authorityFunctionIds
    );

    @Query("SELECT count(fc.id) FROM Functions fc " +
            " WHERE fc.featureId = :featureId " +
            " AND fc.name = :name ")
    Integer searchByNameOnFeature(@Param("featureId") Integer featureId, @Param("name") String name);

    @Query(" SELECT new com.fpt.capstone.backend.api.BackEnd.dto.FunctionListDTO(fc.id,fc.name)" +
            "FROM Functions fc ")
    List<FunctionListDTO> getLabelList();

    @Query(value = "SELECT COLUMN_TYPE FROM information_schema.columns WHERE table_name = :table and COLUMN_NAME = :field", nativeQuery = true)
    String showColumns(@Param("table") String table, @Param("field") String field);

    @Query(" SELECT new com.fpt.capstone.backend.api.BackEnd.dto.FunctionListTrackingDTO(fc.id,CONCAT(fc.name,'-',ft.name,'(',fc.priority,')'),p.id) " +
            "FROM Functions fc " +
            " join Features ft on ft.id=fc.featureId " +
            " join Projects p on p.id=ft.projectId " +
            " join ClassUsers cu on cu.projectId=p.id" +
            " where cu.userId =:userId " +
            " AND fc.status = :functionStatus " +
            " AND (COALESCE(:projectId) is null or p.id = :projectId) " +
            "AND (COALESCE(:featureId) is null or ft.id IN :featureId) ")
    List<FunctionListTrackingDTO> getFunctionTracking(@Param("userId") Integer userId, @Param("projectId") Integer projectId, @Param("featureId") List<Integer> featureId, @Param("functionStatus") String functionStatus);

    @Query(" SELECT new com.fpt.capstone.backend.api.BackEnd.dto.FunctionTrackingSubmitListDTO(fc.id,fc.name,ft.id,ft.name,fc.status, " +
            " u.id,u.fullName,u.rollNumber,i.name,p.id,p.name ) " +
            " FROM Functions fc " +
            " left join Features ft on ft.id=fc.featureId " +
            " left join Projects p on p.id = ft.projectId  " +
            " left join Trackings t on t.functionId=fc.id " +
            " join Users  u on u.id = t.assigneeId " +
            " join Milestones m on m.id = t.milestoneId" +
            " join Iterations i on i.id=m.iterationId" +
            " where (fc.status='pending' or fc.status='planned') " +
            " AND  p.status='active'" +
            " AND (COALESCE(:projectId) is null or p.id IN :projectId) " +
            " AND (COALESCE(:milestoneId) is null or t.milestoneId IN :milestoneId) " +
            " group by fc.id")
    List<FunctionTrackingSubmitListDTO> showListFunctionInTracking(@Param("projectId") List<Integer> projectId, @Param("milestoneId") List<Integer> milestoneId);

    @Modifying
    @Query("UPDATE Functions set status='pending' where id=:functionId")
    void updateFunctionStatus(@Param("functionId") Integer functionId);

    @Modifying
    @Query("UPDATE Functions set status='submitted' where id=:functionId")
    void updateFunctionStatusToSubmit(@Param("functionId") Integer functionId);

    @Query("select distinct ft.projectId from Functions fc " +
            " join Features ft on ft.id=fc.featureId " +
            "where fc.id=:functionId")
    Integer findProjectId(Integer functionId);

    @Query(" select f.id from Functions f " +
            "join Features ft on f.id = f.featureId " +
            "join Projects p on p.id = ft.projectId " +
            "join Classes c on (c.id = p.classId AND (COALESCE(:trainerId) is null or c.trainerId IN :trainerId)) " +
            "join Subjects s on (s.id = c.subjectId AND (COALESCE(:authorId) is null or s.authorId IN :authorId)) " +
            "join ClassUsers cu on (cu.projectId = p.id AND c.id = cu.classId AND (COALESCE(:studentId) is null or cu.userId IN :studentId))" )
    List<Integer> getFunctionAuthority(Integer studentId, Integer authorId, Integer trainerId);

    @Modifying
    @Query(value = " UPDATE functions f " +
            " SET t.`status` = :newStatus AND t.`note` = :note" +
            " WHERE t.id = :functionId ", nativeQuery = true)
    void evaluateFunction(@Param("functionId") Integer functionId,
                          @Param("newStatus") String newStatus);

    @Modifying
    @Query("UPDATE Functions set status = :status where id=:functionId")
    void updateFunctionStatus(@Param("functionId") Integer functionId, @Param("status") String status);
}
