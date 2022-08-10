package com.fpt.capstone.backend.api.BackEnd.repository;

import com.fpt.capstone.backend.api.BackEnd.entity.TeamEvaluations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Transactional(propagation = Propagation.REQUIRES_NEW)
@Repository
public interface TeamEvaluationsRepository extends JpaRepository<TeamEvaluations, Integer> {
    @Modifying
    @Query("UPDATE TeamEvaluations te SET te.grade = :grade, te.comment = :comment, te.modified = :modified, te.modifiedBy = :modifiedBy where te.id = :id ")
    void updateTeamEvaluate(@Param("id") Integer id,
                            @Param("grade") BigDecimal grade,
                            @Param("comment") String comment,
                            @Param("modifiedBy") Integer modifiedBy,
                            @Param("modified") String modified);

}
