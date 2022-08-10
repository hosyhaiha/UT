package com.fpt.capstone.backend.api.BackEnd.service.impl;

import com.fpt.capstone.backend.api.BackEnd.dto.MilestonesListDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.MilestonesSyncDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.ProjectGitIdDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.milestones.MilestoneInputDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.milestones.MilestonesDTO;
import com.fpt.capstone.backend.api.BackEnd.entity.Milestones;
import com.fpt.capstone.backend.api.BackEnd.entity.ResponsePaggingObject;
import com.fpt.capstone.backend.api.BackEnd.entity.RoleID;
import com.fpt.capstone.backend.api.BackEnd.entity.Users;
import com.fpt.capstone.backend.api.BackEnd.repository.ClassesRepository;
import com.fpt.capstone.backend.api.BackEnd.repository.IterationsRepository;
import com.fpt.capstone.backend.api.BackEnd.repository.MilestonesRepository;
import com.fpt.capstone.backend.api.BackEnd.service.MilestonesService;
import com.fpt.capstone.backend.api.BackEnd.service.ProjectService;
import com.fpt.capstone.backend.api.BackEnd.service.UserService;
import com.fpt.capstone.backend.api.BackEnd.service.validate.Validate;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class MilestonesServiceImpl implements MilestonesService {
    @Autowired
    private IterationsRepository iterationsRepository;

    @Autowired
    private ClassesRepository classesRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private MilestonesRepository milestonesRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private Validate validate;
    @Autowired
    private UserService userService;
    @Autowired
    private PermissionService permissionService;

    @Override
    public MilestonesDTO add(MilestoneInputDTO milestoneInput) throws Exception {
        validate.validateMilestone(milestoneInput);
        if (milestonesRepository.getMilestonesByTitle(milestoneInput.getTitle()) != null) {
            throw new Exception("Title already exist!");
        }
        if (milestonesRepository.countByClassIdAndIterationId(
                Integer.valueOf(milestoneInput.getClassId()),
                Integer.valueOf(milestoneInput.getIterationId())) > 0) {
            throw new Exception("Iteration already exist on this Class!");
        }
        milestoneInput.setFrom(validate.convertDDMMYYtoYYMMDD(milestoneInput.getFrom()));
        milestoneInput.setTo(validate.convertDDMMYYtoYYMMDD(milestoneInput.getTo()));
        Milestones milestones = modelMapper.map(milestoneInput, Milestones.class);
        milestonesRepository.save(milestones);
        milestones = entityManager.find(Milestones.class, milestones.getId());
        return showDetail(milestones.getId());
    }

    @Override
    public MilestonesDTO showDetail(int id) {
        return milestonesRepository.getMilestonesDetail(id);
    }

    @Override
    public MilestonesDTO edit(MilestoneInputDTO milestoneInput) throws Exception {
        if (ObjectUtils.isEmpty(milestoneInput.getId())) {
            throw new Exception("ID cannot be empty!");
        }
        if (milestonesRepository.getById(Integer.valueOf(milestoneInput.getId())) == null) {
            throw new Exception("Milestone not found!");
        }
        validate.validateMilestone(milestoneInput);
        milestoneInput.setFrom(validate.convertDDMMYYtoYYMMDD(milestoneInput.getFrom()));
        milestoneInput.setTo(validate.convertDDMMYYtoYYMMDD(milestoneInput.getTo()));
        Milestones milestones = milestonesRepository.getOne(Integer.valueOf(milestoneInput.getId()));
        if (!milestones.getTitle().equals(milestoneInput.getTitle())
                && milestonesRepository.getMilestonesByTitle(milestoneInput.getTitle()) != null) {
            throw new Exception("Title already exist!");
        }
        if (milestones.getClassId() != Integer.valueOf(milestoneInput.getClassId())
                || milestones.getIterationId() != Integer.valueOf(milestoneInput.getIterationId())) {
            if (milestonesRepository.countByClassIdAndIterationId(
                    Integer.valueOf(milestoneInput.getClassId()),
                    Integer.valueOf(milestoneInput.getIterationId())) > 0) {
                throw new Exception("Iteration already exist on this Class!");
            }
        }
        milestones = modelMapper.map(milestoneInput, Milestones.class);
        milestonesRepository.save(milestones);
        milestones = entityManager.find(Milestones.class, milestones.getId());
        return showDetail(milestones.getId());
    }

    @Override
    public Page<MilestonesDTO> listBy(List<Integer> iterationId, List<Integer> classId, List<Integer> trainerId,
                                      String title, String status, int page, int limit, ResponsePaggingObject response) throws Exception {
        Pageable pageable = PageRequest.of(page - 1, limit);
        Users user = userService.getUserLogin();
        if (Objects.equals(user.getSettings().getId(), RoleID.ADMIN) || Objects.equals(user.getSettings().getId(), RoleID.TRAINER)) {
            response.getPermission().setAdd(Boolean.TRUE);
            response.getPermission().setUpdate(Boolean.TRUE);
        }

        List<Integer> authoritySubjectIds = permissionService.getMilestoneAuthority(null);
        return milestonesRepository.search(iterationId, classId, trainerId, title, status, pageable,authoritySubjectIds);

    }

    @Override
    public List<MilestonesListDTO> listMilestoneByClass(List<Integer> classId, List<Integer> projectId) throws Exception {
        List<Integer> authoritySubjectIds = permissionService.getMilestoneAuthority(null);
        List<MilestonesListDTO> milestonesDTOS = milestonesRepository.getMilestonesByClass(classId, projectId,authoritySubjectIds);
        return milestonesDTOS;
    }

    @Override
    public List<MilestonesSyncDTO> listMilestoneSync(List<Integer> projectId) {
        List<MilestonesSyncDTO> milestonesDTOS = milestonesRepository.getMilestonesSync(projectId);
        return milestonesDTOS;
    }

    @Autowired
    ProjectService projectService;


    @Override
    public String syncGitLab(List<Integer> classId, List<Integer> projectId) throws Exception {
        try {

            List<ProjectGitIdDTO> projectGitIdDTOS = projectService.showProjectGitId(classId, projectId);
            List<Integer> projectIds = projectGitIdDTOS.stream().map(ProjectGitIdDTO::getProjectId).collect(Collectors.toList());
            List<MilestonesSyncDTO> milestonesSyncDTOList = listMilestoneSync(projectIds);
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            projectGitIdDTOS.forEach(project -> {
                List<MilestonesSyncDTO> milestones = milestonesSyncDTOList.stream()
                        .filter(milestonesSyncDTO -> project.getClassId().equals(milestonesSyncDTO.getClassId())).collect(Collectors.toList());
                project.setMilestones(milestones);
                //lay gitlab milestone
                try {
                    project.setGitLabMilestones(getGitLabMilestones(project.getGitId(), project.getToken()));
                    headers.setBearerAuth(project.getToken());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });

            projectGitIdDTOS.forEach(project -> {
                if (project.getGitId() != null) {
                    //Lay ra nhung thang co trong project.getMilestones() maf khong co trong setGitLabMilestones

                    //Thi ban nhung thang do len git

                    String url = "https://gitlab.com/api/v4/projects/" + project.getGitId() + "/milestones";
                    project.getMilestones().forEach(milestone -> {
                        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
                        map.add("title", milestone.getTitle());
                        if (milestone.getDescription() != null) map.add("description", milestone.getDescription());
                        if (milestone.getTo().toString() != null) map.add("due_date", milestone.getTo().toString());
                        if (milestone.getFrom().toString() != null)
                            map.add("start_date", milestone.getFrom().toString());
                        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
                        restTemplate.exchange(url, HttpMethod.POST, request, String.class);

                    });
                }
            });
            return "Add milestone successfully";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @Override
    public List<String> getGitLabMilestones(Integer gitlabId, String token) throws Exception {
        if (gitlabId == null) return null;

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBearerAuth(token);
        List<String> list = new ArrayList<>();
        String url = "https://gitlab.com/api/v4/projects/" + gitlabId + "/milestones";
        HttpEntity<String> request = new HttpEntity<String>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
        String body = response.getBody();
        JsonArray jsObject = new Gson().fromJson(body, JsonArray.class);
        jsObject.forEach(milestone -> {
            JsonObject jobj = new Gson().fromJson(milestone, JsonObject.class);
            list.add(jobj.get("title").toString());
        });
        return list;
    }
}
