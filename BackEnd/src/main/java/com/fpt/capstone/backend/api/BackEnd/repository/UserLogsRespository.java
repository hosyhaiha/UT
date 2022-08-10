package com.fpt.capstone.backend.api.BackEnd.repository;


import com.fpt.capstone.backend.api.BackEnd.dto.AddUpdateLogDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.UpdateLogDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.UserLogsDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.UsersDTO;
import com.fpt.capstone.backend.api.BackEnd.entity.UserLogs;
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
public interface UserLogsRespository extends JpaRepository<UserLogs, Integer> {

    @Query(" SELECT new com.fpt.capstone.backend.api.BackEnd.dto.UserLogsDTO(ul.id,ul.groupType,ul.action,ul.desc," +
            " ul.refId,ul.refColumnName,ul.refTableName,ul.oldValue,ul.newValue,ul.created,ul.created_by,ul.modified," +
            " ul.modified_by,ul.desc,ul.desc) " +
            " FROM UserLogs ul " +
            " WHERE  ul.refTableName = :tablename " +
            " AND ul.action in :action" +
            " AND ul.groupType in :groupType " +
            " AND ul.refId = :id ")
    List<UserLogsDTO> getUserLogsByTrackingId(@Param("id") Integer id, @Param("tablename") String tablename,
                                              @Param("groupType") List<String> groupType,
                                              @Param("action") List<String> action);

    @Query("select new com.fpt.capstone.backend.api.BackEnd.dto.UsersDTO(u.id,u.fullName,u.rollNumber) " +
            " from Users u where u.id in :userId ")
    List<UsersDTO> getUserDetails(@Param("userId") List<Integer> userId);
    @Query(value = "select new com.fpt.capstone.backend.api.BackEnd.dto.UpdateLogDTO(ul.id,ul.desc,ul.refColumnName,ul.newValue,ul.created,ul.modified,u1.email,u2.email) " +
            " from UserLogs ul " +
            " left join Trackings t on t.id=ul.refId " +
            " left join Functions f on f.id=t.functionId " +
            " join Users u1 on u1.id= ul.created_by " +
            " join Users u2 on u2.id=ul.modified_by " +
            " WHERE  ul.refTableName = 'trackings'" +
            " AND ul.action = 'updateTracking'" +
            " AND ul.groupType = 'update' " +
            " AND f.id=:functionId" +
            " ORDER BY ul.created desc ")
    Page<UpdateLogDTO> logList(Integer functionId, Pageable pageable);
//    @Query("select new com.fpt.capstone.backend.api.BackEnd.dto.AddUpdateLogDTO() " +
//            " from UserLogs ul" +
//            " where ul.id in :id ")
//    AddUpdateLogDTO getUserLogById(Integer id);
}
