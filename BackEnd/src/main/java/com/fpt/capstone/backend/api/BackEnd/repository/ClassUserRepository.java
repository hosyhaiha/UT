package com.fpt.capstone.backend.api.BackEnd.repository;

import com.fpt.capstone.backend.api.BackEnd.dto.class_users.ClassUsersDTO;
import com.fpt.capstone.backend.api.BackEnd.entity.ClassUsers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Transactional(propagation = Propagation.REQUIRES_NEW)
@Repository
public interface ClassUserRepository extends JpaRepository<ClassUsers, Integer> {
    @Query("SELECT new com.fpt.capstone.backend.api.BackEnd.dto.class_users.ClassUsersDTO(" +
            "cu.id, cu.classId, c.code, cu.projectId, p.code, p.name, u3.id, u3.fullName," +
            "cu.projectLeader, cu.dropoutDate,cu.note, cu.ongoingEval,cu.finalPresentEval,cu.finalTopicEval,cu.status,cu.created," +
            "cu.created_by,u1.email,cu.modified,cu.modified_by,u2.email) " +
            " FROM ClassUsers cu  " +
            " join Classes c on c.id=cu.classId " +
            " join Projects p on p.id=cu.projectId " +
            " join Users  u3 on u3.id=cu.userId " +
            " join Users u1 on u1.id= cu.created_by " +
            " join Users u2 on u2.id=cu.modified_by " +
            " WHERE cu.id =?1")
    ClassUsersDTO getClassUsersDetail(int id);

    @Query("SELECT new com.fpt.capstone.backend.api.BackEnd.dto.class_users.ClassUsersDTO(" +
            "cu.id, cu.classId, c.code, cu.projectId, p.code, p.name, u3.id, u3.fullName," +
            "cu.projectLeader, cu.dropoutDate,cu.note, cu.ongoingEval,cu.finalPresentEval,cu.finalTopicEval,cu.status,cu.created," +
            "cu.created_by,u1.email,cu.modified,cu.modified_by,u2.email) " +
            " FROM ClassUsers cu  " +
            " join Classes c on c.id=cu.classId " +
            " join Projects p on p.id=cu.projectId " +
            " join Users  u3 on u3.id=cu.userId " +
            " LEFT join Users u1 on u1.id= cu.created_by " +
            " LEFT join Users u2 on u2.id=cu.modified_by " +
            " WHERE (COALESCE(:classId) is null or cu.classId IN :classId) " +
            " AND (COALESCE(:projectId) is null or cu.projectId IN :projectId)" +
            " AND (COALESCE(:userId) is null or cu.userId IN :userId)" +
            " AND (:projectLead is null or cu.projectLeader = :projectLead)" +
            " AND (:status is null or c.status = :status)")
    Page<ClassUsersDTO> searchClassUsers(
            @Param("classId") List<Integer> classId,
            @Param("projectId") List<Integer> projectId,
            @Param("userId") List<Integer> userId,
            @Param("projectLead") Integer projectLead,
            @Param("status") String Status,
            Pageable pageable);

    @Query("SELECT new com.fpt.capstone.backend.api.BackEnd.dto.class_users.ClassUsersDTO(" +
            "cu.id, cu.classId, c.code, cu.projectId, p.code, p.name, u3.id, u3.fullName," +
            "cu.projectLeader, cu.dropoutDate,cu.note, cu.ongoingEval,cu.finalPresentEval,cu.finalTopicEval,cu.status,cu.created," +
            "cu.created_by,u1.email,cu.modified,cu.modified_by,u2.email) " +
            " FROM ClassUsers cu  " +
            " join Classes c on c.id=cu.classId " +
            " join Projects p on p.id=cu.projectId " +
            " join Users  u3 on u3.id=cu.userId " +
            " join Users u1 on u1.id= cu.created_by " +
            " join Users u2 on u2.id=cu.modified_by " +
            " WHERE cu.id = ?1 and u3.id =?2 ")
    ClassUsersDTO searchByCLassIDAndUserID(int classID, int userID);

    @Query("SELECT new com.fpt.capstone.backend.api.BackEnd.dto.class_users.ClassUsersDTO(" +
            "cu.id, cu.classId, c.code, cu.projectId, p.code, p.name, u3.id, u3.fullName," +
            "cu.projectLeader, cu.dropoutDate,cu.note, cu.ongoingEval,cu.finalPresentEval,cu.finalTopicEval,cu.status,cu.created," +
            "cu.created_by,u1.email,cu.modified,cu.modified_by,u2.email) " +
            " FROM ClassUsers cu  " +
            " join Classes c on c.id=cu.classId " +
            " join Projects p on p.id=cu.projectId " +
            " join Users  u3 on u3.id=cu.userId " +
            " join Users u1 on u1.id= cu.created_by " +
            " join Users u2 on u2.id=cu.modified_by " +
            " WHERE c.id = ?1 and u3.email =?2 ")
    ClassUsersDTO searchByCLassCodeAndEmail(Integer classId, String email);

    @Query("SELECT new com.fpt.capstone.backend.api.BackEnd.dto.class_users.ClassUsersDTO(" +
            "cu.id, cu.classId, c.code, cu.projectId, p.code, p.name, cu.userId, u3.fullName," +
            "cu.projectLeader, cu.dropoutDate,cu.note, cu.ongoingEval,cu.finalPresentEval,cu.finalTopicEval,cu.status,cu.created," +
            "cu.created_by,u1.email,cu.modified,cu.modified_by,u2.email) " +
            " FROM ClassUsers cu " +
            " join Classes c on c.id=cu.classId " +
            " join Projects p on p.id=cu.projectId " +
            " join Users u1 on u1.id= cu.created_by " +
            " join Users u2 on u2.id=cu.modified_by " +
            " join Users  u3 on u3.id=cu.userId " +
            " WHERE cu.classId = :classId AND cu.projectId = :projectId and cu.projectLeader = :leader "
    )
    ClassUsersDTO searchProjectLeader(@Param("classId") int classId,
                                      @Param("projectId") int projectId,
                                      @Param("leader") int leader);

    ClassUsers findByClassIdAndProjectIdAndDropoutDateAndStatusAndProjectLeader(Integer classId, Integer projectId, Date dropoutDate, String status, Integer projectLeader);

    @Query(value = "SELECT cu.projectLeader from ClassUsers cu" +
            " join Users  u3 on u3.id=cu.userId " +
            " join Projects p on p.id=cu.projectId " +
            "where u3.id=:userId and p.id=:projectId")
    Integer checkLeader(@Param("userId") Integer userId, @Param("projectId") Integer projectId);

    @Query(value = "SELECT u.id " +
            " from ClassUsers cu " +
            " join Users  u on u.id = cu.userId " +
            " join Classes c on c.id=cu.classId " +
            "where c.id=:classId ")
    List<Integer> getByClassId(Integer classId);

    ClassUsers findClassUsersByClassIdAndUserId(Integer classId,Integer userId);
    ClassUsers findByClassIdAndUserIdAndStatus(Integer classId, Integer userId, String status);

    ClassUsers findByClassIdAndProjectIdAndProjectLeader(Integer classId, Integer projectId,Integer projectLeader);

}
