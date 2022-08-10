package com.fpt.capstone.backend.api.BackEnd.repository;

import com.fpt.capstone.backend.api.BackEnd.dto.TrackingsDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.UserList;
import com.fpt.capstone.backend.api.BackEnd.entity.Trackings;
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
public interface TrackingsRepository extends JpaRepository<Trackings, Integer> {

    @Query("SELECT new com.fpt.capstone.backend.api.BackEnd.dto.TrackingsDTO( t.id,t.milestoneId,m.title,t.functionId," +
            " fc.name,i.id,i.name,p.id,p.code,p.name,c.code,ft.name,t.assignerId,uAssigner.email,t.assigneeId,uAssignee.email," +
            "fc.description,t.submitTime,t.status,t.created,t.created_by,t.modified,t.modified_by,uCreated.email,uModified.email)" +
            " FROM Trackings t   " +
            " join Milestones m on m.id= t.milestoneId " +
            " join Functions fc on fc.id=t.functionId" +
            " join Features ft on ft.id=fc.featureId" +
            " join Projects p on p.id=ft.projectId " +
            " join Classes c on c.id=p.classId" +
            " join Iterations i on m.iterationId=i.id " +
            " join Users uAssigner on uAssigner.id=t.assignerId " +
            " join Users uAssignee on uAssignee.id=t.assigneeId " +
            " join Users uCreated on uCreated.id=t.created_by " +
            " join Users uModified on uModified.id=t.modified_by " +
            " WHERE t.id =:id ")
    TrackingsDTO getTrackingDetail(int id);

    @Query("SELECT new com.fpt.capstone.backend.api.BackEnd.dto.TrackingsDTO(t.id,t.milestoneId,m.title,t.functionId," +
            "fc.name,i.id,i.name,p.id,p.code,p.name,c.code,ft.name,t.assignerId,uAssigner.fullName,t.assigneeId,uAssignee.fullName," +
            "fc.description,t.submitTime,t.status,t.created,t.created_by,t.modified,t.modified_by,uCreated.email,uModified.email)" +
            " FROM Trackings t " +
            " join Milestones m on m.id= t.milestoneId " +
            " join Functions fc on fc.id =t.functionId" +
            " join Features ft on ft.id = fc.featureId" +
            " join Projects p on p.id = ft.projectId" +
            " join Iterations i on m.iterationId=i.id " +
            " join Classes c on c.id = p.classId " +
            " join ClassUsers cu on cu.projectId=p.id " +
            " join Users uAssigner on uAssigner.id = t.assignerId " +
            " join Users uAssignee on uAssignee.id = t.assigneeId " +
            " join Users uCreated on uCreated.id = t.created_by " +
            " join Users uModified on uModified.id = t.modified_by " +
            " WHERE (COALESCE(:projectId) is null or p.id IN :projectId)" +
            " AND (COALESCE(:featureId) is null or ft.id IN :featureId) " +
            " AND (COALESCE(:assignerId) is null or uAssigner.id IN :assignerId)" +
            " AND (COALESCE(:assigneeId) is null or uAssignee.id IN :assigneeId)" +
            " AND (COALESCE(:milestoneId) is null or m.id IN :milestoneId)" +
            " AND (COALESCE(:functionId) is null or fc.id IN :functionId)" +
            " AND (COALESCE(:classId) is null or c.id IN :classId)" +
            " AND (:status is null or t.status = :status ) " +
            " group by t.id")
    Page<TrackingsDTO> search(@Param("projectId") List<Integer> projectId,
                              @Param("featureId") List<Integer> featureId,
                              @Param("milestoneId") List<Integer> milestoneId,
                              @Param("assigneeId") List<Integer> assigneeId,
                              @Param("assignerId") List<Integer> assignerId,
                              @Param("functionId") List<Integer> functionId,
                              @Param("classId") List<Integer> classId,
                              @Param("status") String status,
                              Pageable pageable);

    @Query("SELECT new com.fpt.capstone.backend.api.BackEnd.dto.UserList(u.id,u.fullName,u.rollNumber)" +
            " FROM Users u " +
            " join ClassUsers cu on cu.userId=u.id " +
            " join Projects p on p.id = cu.projectId" +
            " join Features ft on ft.projectId = p.id" +
            " join Functions fc on fc.featureId =ft.id" +
            " join Trackings t on fc.id = t.functionId" +
            " WHERE  t.id = :trackingId ")
    List<UserList> searchUserAssign(@Param("trackingId") Integer trackingId);

    @Modifying
    @Query("UPDATE Trackings set assigneeId=:newAssigneeId, status='committed' where id=:trackingId")
    void updateAssignee(@Param("trackingId") Integer trackingId, @Param("newAssigneeId") Integer newAssigneeId);


    @Modifying
    @Query("UPDATE Trackings set  status='submitted' where functionId=:functionId and milestoneId=:milestoneId")
    void updateSubmitted(@Param("functionId") Integer functionId, @Param("milestoneId") Integer milestoneId);

    Trackings findByFunctionId(Integer functionId);

    @Modifying
    @Query(value = " UPDATE trackings t " +
            " JOIN functions f ON f.id = t.function_id " +
            " join features ft on ft.id=f.feature_id " +
            " SET t.`status` = :newStatus " +
            " WHERE ft.project_id = :projectId and t.milestone_id = :milestoneId and t.`status` = :oldStatus ", nativeQuery = true)
    void updateToStatus(@Param("projectId") Integer projectId,
                        @Param("milestoneId") Integer milestoneId,
                        @Param("newStatus") String newStatus,
                        @Param("oldStatus") String oldStatus
    );

    @Query("SELECT t.status from Trackings  t" +
            " join Functions f on f.id=t.functionId " +
            " join Features ft on ft.id=f.featureId " +
            "where  t.milestoneId=:milestoneId " +
            "and ft.projectId=:projectId ")
    String getCurTrackingStatus(@Param("projectId") Integer projectId, @Param("milestoneId") Integer milestoneId);

    @Modifying
    @Query(value = " UPDATE trackings t " +
            " SET t.`status` = :newStatus AND t.`note` = :note" +
            " WHERE t.function_id = :functionId ", nativeQuery = true)
    void evaluateTracking(@Param("functionId") Integer functionId,
                          @Param("newStatus") String newStatus,
                          @Param("note") String note);

    @Query("select m.title from Trackings t left join Milestones m on m.id=t.milestoneId where t.functionId=:functionId")
    String getMilestoneName(Integer functionId);

    @Query("select m.title from Milestones m  where m.id=:milestonrId")
    String getMilestone(Integer milestonrId);
    @Query("select c.id from Classes c" +
            " left join Projects p on p.classId=c.id " +
            " left join Features ft on ft.projectId=p.id " +
            " left join Functions f on f.featureId=ft.id"+
            " where f.id=:functionId")
    Integer getCurClass(Integer functionId);

    @Modifying
    @Query("UPDATE Trackings set status = :status where id=:trackingId")
    void updateTrackingStatus(@Param("trackingId") Integer trackingId, @Param("status") String status);
}
