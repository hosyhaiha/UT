package com.fpt.capstone.backend.api.BackEnd.service;

import com.fpt.capstone.backend.api.BackEnd.dto.EvaluationCriteriaDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.ProjectGitIdDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.ProjectsDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.ProjectsListDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProjectService {
    ProjectsDTO addProject(ProjectsDTO projectsDTO) throws Exception;

    ProjectsDTO deleteaddProject(int id);

    List<ProjectsDTO> showProject();

    ProjectsDTO updateProject(ProjectsDTO projectsDTO) throws Exception;

    ProjectsDTO findById(int id) throws Exception;

    Page<ProjectsDTO> listBy(List<Integer> id, String projectName, String status, int page, int per_page) throws Exception;

    public ProjectsDTO getProjectDetail(int id);

    public List<ProjectsListDTO> showProjectList(List<Integer> classId, String status) throws Exception ;

    List<ProjectGitIdDTO> showProjectGitId(List<Integer> classId, List<Integer> projectId);

    List<ProjectGitIdDTO> showProjectGitIdById(List<Integer> projectId);

    /**
     * @param userId id user
     * @return first available project Id
     */
    List<ProjectsDTO> getFirstProject(Integer userId);


}
