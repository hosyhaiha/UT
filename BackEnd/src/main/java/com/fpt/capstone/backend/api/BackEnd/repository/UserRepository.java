package com.fpt.capstone.backend.api.BackEnd.repository;


import com.fpt.capstone.backend.api.BackEnd.dto.AuthorListDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.UserInfoDto;
import com.fpt.capstone.backend.api.BackEnd.dto.UserList;
import com.fpt.capstone.backend.api.BackEnd.dto.UsersDTO;
import com.fpt.capstone.backend.api.BackEnd.entity.Users;
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
import java.util.Optional;

@Transactional(propagation = Propagation.REQUIRES_NEW)
@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {
    Users findByEmail(String email);

    boolean existsUserByEmail(String email);

    @Query("SELECT u FROM Users u WHERE u.email = :email")
    public Users getUserByEmail(@Param("email") String email);

    @Modifying
    @Query("UPDATE Users u   SET u.createdBy = ?1, u.modifiedBy = ?1 where u.id = ?1 ")
    void updateAudit(Integer id);

    @Query("SELECT new com.fpt.capstone.backend.api.BackEnd.dto.UsersDTO(u.id,u.email,u.password,u.fullName,u.birthday," +
            "u.tel,u.address,u.rollNumber,u.schoolName,u.avatarLink,u.facebookLink,u.linkedinLink,u.settings.id,s.value," +
            "u.status,u.Provider,u.created,u.createdBy,u.modified,u.modifiedBy,u1.email,u2.email) " +
            " FROM Users u" +
            " JOIN Settings  s ON s.id=u.settings.id " +
            " JOIN Users u1 ON u1.id=u.createdBy " +
            " join Users u2 ON u2.id=u.modifiedBy " +
            " WHERE ( :email is null or u.email LIKE %:email%) " +
            " AND ( :fullName is null or u.fullName LIKE %:fullName%)  " +
            " AND (:rollNumber is null or u.rollNumber LIKE %:rollNumber%) " +
            " AND (:status is null or u.status = :status) ")
    public Page<UsersDTO> search(@Param("email") String email, @Param("fullName") String fullName, @Param("rollNumber") String rollNumber, @Param("status") String status, Pageable pageable);

    @Query("SELECT new com.fpt.capstone.backend.api.BackEnd.dto.AuthorListDTO(u.id,u.fullName) FROM Users u WHERE u.settings.id =8")
    public List<AuthorListDTO> listAuthor();

    @Modifying
    @Query("update Users u set u.status = 'inactive' where u.id=:id")
    void setInactiveUser(@Param("id") Integer id);

    Optional<Users> findByVerificationCode(String verificationCode);

    @Modifying
    @Query("UPDATE Users u   SET u.avatarLink=?2 where u.id = ?1 ")
    void updateAva(Integer id, String avaLink);

    @Query("SELECT u FROM Users u  WHERE u.settings.title = 'trainer' AND u.id = ?1")
    Users findTrainerById(int id);

    @Query("SELECT new com.fpt.capstone.backend.api.BackEnd.dto.AuthorListDTO(u.id,u.fullName) " +
            "FROM Users u " +
            "WHERE u.settings.title = 'trainer' " +
            "AND (:status is null or u.status= :status) ")
    List<AuthorListDTO> listTrainer(@Param("status") String status);


    @Query("SELECT new com.fpt.capstone.backend.api.BackEnd.dto.UserInfoDto(u.id,u.fullName,u.settings.title) FROM Users u " +
            " WHERE (:status is null or u.status = :status) " +
            " AND (:role is null or u.settings.title = :role)")
    public List<UserInfoDto> listLabelValueUser(@Param("status") String status,
                                                @Param("role") String role);


    @Query("SELECT new com.fpt.capstone.backend.api.BackEnd.dto.UsersDTO(u.id,u.email,u.password,u.fullName,u.birthday," +
            "u.tel,u.address,u.rollNumber,u.schoolName,u.avatarLink,u.facebookLink,u.linkedinLink,u.settings.id,s.value," +
            "u.status,u.Provider,u.created,u.createdBy,u.modified,u.modifiedBy,u1.email,u2.email) " +
            " FROM Users u" +
            " JOIN Settings  s ON s.id=u.settings.id " +
            " JOIN Users u1 ON u1.id=u.createdBy " +
            " join Users u2 ON u2.id=u.modifiedBy " +
            " WHERE  u.id = :id")
    public UsersDTO getUserById(@Param("id") Integer id);

    @Query("SELECT new com.fpt.capstone.backend.api.BackEnd.dto.UserList(u.id,u.fullName,u.rollNumber)" +
            " FROM Users u " +
            " inner JOIN  ClassUsers cu ON cu.userId=u.id  " +
            " JOIN Projects  p on p.id=cu.projectId  " +
            " JOIN Features ft ON ft.projectId=p.id " +
            " WHERE   " +
            " (COALESCE(:projectId) is null or cu.projectId IN :projectId )" +
            " AND u.status ='active' " +
            "and p.status='active'" +
            "group by  u.id"
    )
    public List<UserList> listUserInFeature(@Param("projectId") List<Integer> projectId);
}