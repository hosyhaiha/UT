package com.fpt.capstone.backend.api.BackEnd.service.impl;

import com.fpt.capstone.backend.api.BackEnd.dto.*;
import com.fpt.capstone.backend.api.BackEnd.entity.ClassUsers;
import com.fpt.capstone.backend.api.BackEnd.entity.IterationEvaluations;
import com.fpt.capstone.backend.api.BackEnd.entity.Milestones;
import com.fpt.capstone.backend.api.BackEnd.repository.*;
import com.fpt.capstone.backend.api.BackEnd.service.IterationEvaluationsService;
import com.fpt.capstone.backend.api.BackEnd.service.MilestonesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class IterationEvaluationsServiceImpl implements IterationEvaluationsService {
    @Autowired
    IterationEvaluationsRepository iterationEvaluationsRepository;
    @Autowired
    ProjectEvaluationRepository projectEvaluationRepository;

    @Autowired
    IterationsRepository iterationsRepository;
    @Autowired
    ClassUserRepository classUserRepository;
    @Autowired
    MilestonesRepository milestonesRepository;

    @Override
    public List<IterationEvaluationsDTO> showList(Integer iterationId, List<Integer> projectId, List<Integer> milestoneId, Integer classId) {


        List<IterationEvaluationsDTO> iterationEvaluationsDTOSList = iterationEvaluationsRepository.showList(iterationId, projectId, milestoneId, classId);

        List<ProjectEvaluationDTO> list = projectEvaluationRepository.projectEvaluationList(milestoneId, iterationId);

        HashMap<Integer, ProjectGradeDTO> hashMap = new HashMap<>();
        for (ProjectEvaluationDTO key : list) {
            if (hashMap.containsKey(key.getProjectId())) {
                hashMap.get(key.getProjectId()).setTotalGrade((hashMap.get(key.getProjectId()).getTotalGrade()).add((key.getGrade()).multiply(key.getEvaluationWeight())));
                hashMap.get(key.getProjectId()).setTotalWeight((hashMap.get(key.getProjectId()).getTotalWeight()).add(key.getEvaluationWeight()));
            } else {
                ProjectGradeDTO projectGradeDTO = new ProjectGradeDTO(key.getGrade().multiply(key.getEvaluationWeight()), key.getEvaluationWeight());
                hashMap.put(key.getProjectId(), projectGradeDTO);
            }
            System.out.println("project Id:" + key.getProjectId() + "/n" + "total grade:"
                    + hashMap.get(key.getProjectId()).getTotalGrade());
        }
        for (IterationEvaluationsDTO i : iterationEvaluationsDTOSList) {
            ProjectGradeDTO projectGradeDTO = hashMap.get(i.getProjectId());
            i.setTeamEvalGrade(projectGradeDTO.getTotalGrade().divide(projectGradeDTO.getTotalWeight(), 2, RoundingMode.HALF_EVEN));
        }
        return iterationEvaluationsDTOSList;
    }

    @Override
    public Page<ClassEvaluationDTO> classEvaluation(Integer classId, int page, int per_page) {
        return null;
    }

    @Override
    public ClassEvalDTO listIterationClass(Integer classId) {
        //iteration list of class
        List<ListIterationEvalDTO> listIterationEvalDTOS = iterationsRepository.findIterationInClass(classId);
        //Student list of class
        List<Integer> listStudent = classUserRepository.getByClassId(classId);
        //Grade list of student
        List<ListGradeDTO> listGradeDTOS = iterationEvaluationsRepository.getStudentGrade(listStudent);
        Map<Integer, Map<String, Object>> listGrades = new HashMap<>();
        for (ListGradeDTO listGradeDTO : listGradeDTOS) {
            // nếu chưa có trong map thì tạo mới DTO, nếu có rồi thì chỉ put điểm ite vào
            if (listGrades.get(listGradeDTO.getStudentId()) == null) {
                Map<String, Object> listGradeItem = new HashMap<>();

                listGradeItem.put("iterationId", listGradeDTO.getIterationId());
                listGradeItem.put("iterationIdName", listGradeDTO.getIterationIdName());
                listGradeItem.put("milestoneId", listGradeDTO.getMilestoneId());
                listGradeItem.put("milestoneName", listGradeDTO.getMilestoneName());
                listGradeItem.put("projectId", listGradeDTO.getProjectId());
                listGradeItem.put("projectName", listGradeDTO.getProjectName());
                listGradeItem.put("rollNumber", listGradeDTO.getRollNumber());
                listGradeItem.put("studentId", listGradeDTO.getStudentId());
                listGradeItem.put("studentName", listGradeDTO.getStudentName());
                listGradeItem.put("totalGrade", listGradeDTO.getTotalGrade());
                listGradeItem.put(listGradeDTO.getKey(), listGradeDTO.getIterationGrade());
                listGrades.put(listGradeDTO.getStudentId(), listGradeItem);
            } else {
                listGrades.get(listGradeDTO.getStudentId()).put(listGradeDTO.getKey(), listGradeDTO.getIterationGrade());
            }
        }
        ClassEvalDTO classEvalDTO = new ClassEvalDTO();
        classEvalDTO.setListIterationEvalDTOS(listIterationEvalDTOS);
        classEvalDTO.setListGradeDTOS(new ArrayList<>(listGrades.values()));
        return classEvalDTO;
    }

    @Override
    public void updateBonus(Integer studentId, Integer milestoneId, BigDecimal bonus) {

        if (bonus == null) {
            bonus = new BigDecimal(0);
        }

        Milestones milestones = milestonesRepository.getById(milestoneId);
        Integer classId = milestones.getClassId();
        ClassUsers classUsers = classUserRepository.findClassUsersByClassIdAndUserId(classId, studentId);
        Integer classUserId = classUsers.getId();
        IterationEvaluations iterationEvaluations = iterationEvaluationsRepository.findByMilestoneIdAndAndClassUserId(milestoneId, classUserId);
        BigDecimal oldBounus = iterationEvaluations.getBonus();
        BigDecimal oldGrade = iterationEvaluations.getGrade();
        BigDecimal newGrade = oldGrade.subtract(oldBounus).add(bonus);

        if (newGrade.intValue() > 10.00) {
            newGrade = new BigDecimal(10);
        }
        iterationEvaluationsRepository.updateBonus(milestoneId, classUserId, bonus, newGrade);
    }
}
