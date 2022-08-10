package com.fpt.capstone.backend.api.BackEnd.service.impl;

import com.fpt.capstone.backend.api.BackEnd.dto.*;
import com.fpt.capstone.backend.api.BackEnd.entity.*;
import com.fpt.capstone.backend.api.BackEnd.repository.ClassesRepository;
import com.fpt.capstone.backend.api.BackEnd.repository.ProjectRepository;
import com.fpt.capstone.backend.api.BackEnd.repository.SubmitsRepository;
import com.fpt.capstone.backend.api.BackEnd.repository.TeamEvaluationsRepository;
import com.fpt.capstone.backend.api.BackEnd.service.ClassesService;
import com.fpt.capstone.backend.api.BackEnd.service.ProjectService;
import com.fpt.capstone.backend.api.BackEnd.service.UserService;
import com.fpt.capstone.backend.api.BackEnd.service.validate.ConstantsRegex;
import com.fpt.capstone.backend.api.BackEnd.service.validate.Validate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ClassesRepository classesRepository;

    @Autowired
    private ClassesService classesService;

    @Autowired
    private TeamEvaluationsRepository teamEvaluationsRepository;

    @Autowired
    private SubmitsRepository submitsRepository;

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UserService userService;

    @Autowired
    private PermissionService permissionService;
    @Autowired
    private Validate validate = new Validate();

    @Override
    public ProjectsDTO addProject(ProjectsDTO projectsDTO) throws Exception {
        validate.validateProject(projectsDTO);
        // Check trùng code hoặc name
        List<Projects> projectExist = projectRepository.findAllByCodeOrName(projectsDTO.getCode(), projectsDTO.getName());
        for (Projects t : projectExist) {
            if (t.getCode() != null && t.getCode().equals(projectsDTO.getCode())) throw new Exception("project code " + t.getCode() + " already exist, please choose another code");
            if (t.getName() != null && t.getName().equals(projectsDTO.getName())) throw new Exception("project name " + t.getName() + " already exist, please choose another code");
        }

        //Check lớp đã hoàn thành config, được phép thêm học sinh, team vào
        if (!classesService.checkClassInitCondition(projectsDTO.getClassId())) throw new Exception("This class have not finished configured yet!");

        Projects projects = modelMapper.map(projectsDTO, Projects.class);
        // projects.setClasses(classesRepository.getById(Integer.valueOf(projectsDTO.getClassId())));
        projects = projectRepository.save(projects);
        createTeamEvaluation(projects);
        return getProjectDetail(projects.getId());
    }

    /**
     * Hàm dùng để tạo mới các bản ghi teamEvaluation và Submit khi tạo mới team
     * @param project
     */
    public void createTeamEvaluation(Projects project) {
        Users auth = userService.getUserLogin();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String curTime = formatter.format(date);

        List<MilestoneCriteriaDTO> criteria = projectRepository.getTeamCriteria(project.getClassId());

        List<TeamEvaluations> teamEvaluations = new ArrayList<>();
        List<Submits> submits = new ArrayList<>();
        for (MilestoneCriteriaDTO projectCriteria : criteria) {
            teamEvaluations.add(new TeamEvaluations(projectCriteria.getCriteriaId(), projectCriteria.getMilestoneId(), project.getId(), BigDecimal.valueOf(0), auth.getId(), curTime));
            submits.add(new Submits(projectCriteria.getMilestoneId(), project.getId(), ModelStatus.SUBMIT_STATUS_PENDING, auth.getId(), curTime));
        }

        teamEvaluationsRepository.saveAll(teamEvaluations);
        submitsRepository.saveAll(submits);
    }

    @Override
    public ProjectsDTO deleteaddProject(int id) {
        return null;
    }

    @Override
    public List<ProjectsDTO> showProject() {
        return null;
    }

    @Override
    public ProjectsDTO updateProject(ProjectsDTO projectsDTO) throws Exception {
        validate.validateProject(projectsDTO);
        Projects projects = projectRepository.getById(projectsDTO.getId());

        if (!projects.getName().equals(projectsDTO.getName())) {
            if (!projectRepository.findByName(projectsDTO.getName()).equals(null))
                throw new Exception("Project Name already exist ");
        }
        if (!projects.getCode().equals(projectsDTO.getCode())) {
            if (!projectRepository.findByCode(projectsDTO.getCode()).equals(null))
                throw new Exception("Project Code already exist ");
        }
        projects = modelMapper.map(projectsDTO, Projects.class);
        //projects.setClasses(classesRepository.getById(Integer.valueOf(projectsDTO.getClassId())));
        projectRepository.save(projects);
        projects = entityManager.find(Projects.class, projects.getId());
        return getProjectDetail(projects.getId());
    }

    @Override
    public ProjectsDTO findById(int id) throws Exception {
        return null;
    }

    @Override
    public Page<ProjectsDTO> listBy(List<Integer> id, String projectName, String status, int page, int per_page) throws Exception {
        //check quyen
        List<Integer> authorityProjectIds = permissionService.getProjectAuthority(null);

        if (ObjectUtils.isEmpty(status)) status = null;
        if (!ObjectUtils.isEmpty(status) && !status.matches(ConstantsRegex.STATUS_PATTERN.toString())) {
            throw new Exception("Input status Empty to search all or active/inactive");
        }
        return projectRepository.search(id, projectName, status, PageRequest.of(page - 1, per_page), authorityProjectIds);
    }

    @Override
    public ProjectsDTO getProjectDetail(int id) {
        return projectRepository.getProjectDetail(id);
    }

    @Override
    public List<ProjectsListDTO> showProjectList(List<Integer> classId, String status) throws Exception {
        if (ObjectUtils.isEmpty(status)) {
            status = null;
        }

        List<Integer> userId = new ArrayList<>();

        List<Integer> authorityProjectIds = permissionService.getProjectAuthority(null);
        return projectRepository.getLabelList(classId, userId, status,authorityProjectIds);
    }

    @Override
    public List<ProjectGitIdDTO> showProjectGitId(List<Integer> classId, List<Integer> projectId) {
        return projectRepository.getGitProjectId(classId, projectId);
    }

    @Override
    public List<ProjectGitIdDTO> showProjectGitIdById(List<Integer> projectId) {
        return null;
    }

    /**
     * @param userId id user
     * @return first available project Id
     */
    @Override
    public List<ProjectsDTO> getFirstProject(Integer userId) {
        return projectRepository.getFirstProject(userId, PageRequest.of(0, 1));
    }


}
