package com.fpt.capstone.backend.api.BackEnd.repository;

import com.fpt.capstone.backend.api.BackEnd.entity.Submits;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Transactional(propagation = Propagation.REQUIRES_NEW)
@Repository
public interface SubmitsRepository extends JpaRepository<Submits, Integer> {
    Submits findByMilestoneIdAndProjectId(Integer milestoneId, Integer projectId);

    @Modifying
    @Query("update Submits set packageFileLink=:linkZip,submitTime=:submitTime  where  milestoneId=:milestoneId " +
            "and projectId=:projectId")
    void updateLinkZip(@Param("milestoneId") Integer milestoneId,
                       @Param("projectId") Integer projectId,
                       @Param("submitTime") Date submitTime,
                       @Param("linkZip") String linkZip);

    @Query("SELECT status from Submits  where  milestoneId=:milestoneId " +
            "and projectId=:projectId")
    String getCurStatus(@Param("projectId") Integer projectId,
                        @Param("milestoneId") Integer milestoneId);

    @Modifying
    @Query("update Submits set status= :status where  milestoneId=:milestoneId " +
            "and projectId=:projectId")
    void updateStatus(@Param("projectId") Integer projectId,
                      @Param("milestoneId") Integer milestoneId,
                      @Param("status") String status);

}