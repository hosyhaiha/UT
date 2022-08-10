package com.fpt.capstone.backend.api.BackEnd.repository;

import com.fpt.capstone.backend.api.BackEnd.dto.MilestoneCriteriaDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.ProjectGitIdDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.ProjectsDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.ProjectsListDTO;
import com.fpt.capstone.backend.api.BackEnd.entity.Projects;
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
public interface ProjectRepository extends JpaRepository<Projects, Integer> {
    @Query("SELECT new com.fpt.capstone.backend.api.BackEnd.dto.ProjectsDTO(p.id,p.classId,c.code,p.code,p.name," +
            "p.status,p.gitlabToken,p.gitlabUrl,p.gitLabProjectId,p.created,p.created_by,p.modified,p.modified_by,u1.fullName,u2.fullName)" +
            " FROM Projects p   " +
            " join Users u1 on u1.id= p.created_by " +
            " join Classes c on c.id=p.classId" +
            " left join Users u2 on u2.id=p.modified_by " +
            " WHERE (:projectName is null or p.name LIKE %:projectName%) " +
            " AND (COALESCE(:classId) is null or c.id IN :classId)" +
            " AND ( c.status = 'active')" +
            " AND ( :status is null or p.status like :status% ) " +
            " AND (COALESCE(:authorityProjectIds) is null or p.id IN :authorityProjectIds)")
    Page<ProjectsDTO> search(@Param("classId") List<Integer> classId,
                             @Param("projectName") String projectName,
                             @Param("status") String status,
                             Pageable pageable,
                             @Param("authorityProjectIds") List<Integer> authorityProjectIds);

    @Query("SELECT new com.fpt.capstone.backend.api.BackEnd.dto.ProjectsDTO(p.id,p.classId,c.code,p.code,p.name," +
            "p.status,p.gitlabToken,p.gitlabUrl,p.gitLabProjectId,p.created,p.created_by,p.modified,p.modified_by,u1.fullName,u2.fullName)" +
            " FROM Projects p   " +
            " join Users u1 on u1.id= p.created_by " +
            " join Classes c on c.id=p.classId" +
            " join Users u2 on u2.id=p.modified_by " +
            "  WHERE p.id =:id")
    ProjectsDTO getProjectDetail(int id);

    @Query("SELECT new com.fpt.capstone.backend.api.BackEnd.dto.ProjectGitIdDTO(p.id, p.classId,p.gitLabProjectId,p.gitlabToken)" +
            " FROM Projects p   " +
            " join Classes c on c.id=p.classId" +
            " WHERE (COALESCE(:projectId) is null or p.id IN :projectId)  " +
            " AND (COALESCE(:classId) is null or c.id IN :classId) ")
    List<ProjectGitIdDTO> getGitProjectId(@Param("classId") List<Integer> classId,
                                          @Param("projectId") List<Integer> projectId);

    Projects findByName(String name);

    Projects findByCodeAndClassId(String code,Integer classId);

    @Query(" SELECT new com.fpt.capstone.backend.api.BackEnd.dto.ProjectsListDTO(p.id,CONCAT(p.code,'-',p.name))" +
            "  FROM Projects p " +
            " left join Classes c on c.id=p.classId " +
            " left join ClassUsers cu on cu.projectId = p.id" +
            " where (COALESCE(:authorityProjectIds) is null or p.id IN :authorityProjectIds)" +
            " AND (:status is null or p.status= :status)" +
            " AND (COALESCE(:classId) is null or c.id IN :classId)" +
            " AND (COALESCE(:userId) is null or cu.userId IN :userId)" +
            " group by p.id")
    List<ProjectsListDTO> getLabelList(@Param("classId") List<Integer> classId,
                                       @Param("userId") List<Integer> userId,
                                       @Param("status") String status,
                                       @Param("authorityProjectIds") List<Integer> authorityProjectIds);

    @Query("SELECT new com.fpt.capstone.backend.api.BackEnd.dto.ProjectsDTO(p.id) FROM Projects p " +
            " join Classes c on (c.id = p.classId and c.status = 'active')" +
            " join ClassUsers cu on (cu.userId = :userId and p.status = 'active' and cu.dropoutDate is null)")
    List<ProjectsDTO> getFirstProject(Integer userId, Pageable pageable);

    @Query("SELECT p.id FROM Projects p " +
            "join Classes c on (c.id = p.classId and (COALESCE(:trainerId) is null or c.trainerId IN :trainerId)) " +
            "join Subjects s on (s.id = c.subjectId and (COALESCE(:authorId) is null or s.authorId IN :authorId))")
    List<Integer> projectAuthority(Integer trainerId, Integer authorId);

    @Query("SELECT p.id FROM Projects p " +
            " left join Classes c on c.id = p.classId  " +
            " left join ClassUsers  cu on cu.classId=c.id" +
            " where cu.userId=:studentId " +
            " group by p.id")
    List<Integer> projectAuthorityStudent(Integer studentId);
Projects findByCode(String code);

    @Query("SELECT new com.fpt.capstone.backend.api.BackEnd.dto.MilestoneCriteriaDTO(m.id, ec.id) FROM Milestones m " +
            "JOIN EvaluationCriteria ec on (ec.status = 'active' and m.iterationId = ec.iterationId and ec.teamEvaluation = 1) " +
            "WHERE m.status <> 'cancelled' and m.classId = :classId")
    List<MilestoneCriteriaDTO> getTeamCriteria(@Param("classId") Integer classId);

    List<Projects> findAllByCodeOrName(String code, String name);
}
