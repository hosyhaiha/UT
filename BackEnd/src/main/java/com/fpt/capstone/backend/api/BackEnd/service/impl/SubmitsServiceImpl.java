package com.fpt.capstone.backend.api.BackEnd.service.impl;

import com.fpt.capstone.backend.api.BackEnd.dto.*;
import com.fpt.capstone.backend.api.BackEnd.dto.class_users.ClassUsersDTO;
import com.fpt.capstone.backend.api.BackEnd.entity.*;
import com.fpt.capstone.backend.api.BackEnd.repository.*;
import com.fpt.capstone.backend.api.BackEnd.service.SubmitService;
import com.fpt.capstone.backend.api.BackEnd.service.validate.Validate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SubmitsServiceImpl implements SubmitService {
    @Autowired
    private MilestonesRepository milestonesRepository;

    @Autowired
    private FunctionsRepository functionsRepository;

    @Autowired
    private ClassUserRepository classUserRepository;

    @Autowired
    private IterationEvaluationsRepository iterationEvaluationsRepository;

    @Autowired
    private TeamEvaluationsRepository teamEvaluationsRepository;

    @Autowired
    private LocEvaluationsRepository locEvaluationsRepository;

    @Autowired
    private IterationsRepository iterationsRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private Validate validate;

    @Autowired
    private SubmitsRepository submitsRepository;

    @Autowired
    private TrackingsRepository trackingsRepository;

    /**
     * Api lấy danh sách thông tin submit milestones
     *
     * @param projectId
     * @param milestoneId
     * @return
     */
    @Override
    public List<MilestoneSubmitDTO> getMilestoneSubmits(Integer classId, List<Integer> projectId, Integer milestoneId) {
        HashMap<Integer, MilestoneSubmitDTO> result = new HashMap<>();
        List<MilestoneSubmitDTO> milestoneSubmitDTOS = milestonesRepository.getMilestoneSubmits(classId, projectId, milestoneId);
        List<Integer> hasTeamEvalProjectIds = new ArrayList<>();

        for (MilestoneSubmitDTO t : milestoneSubmitDTOS) {
            result.putIfAbsent(t.getProjectId(), t);
            if (Arrays.asList(ModelStatus.SUBMIT_STATUS_HAS_TEAM_EVAL).contains(t.getSubmitStatus()) && !hasTeamEvalProjectIds.contains(t.getProjectId())) {
                hasTeamEvalProjectIds.add(t.getProjectId());
            }
            if (t.getFunctionStatus() != null && t.getFunctionStatus().equals(ModelStatus.STATUS_PENDING)) {
                result.get(t.getProjectId()).setTotalPendingFunctions((result.get(t.getProjectId()).getTotalPendingFunctions() == null) ? 1 : (result.get(t.getProjectId()).getTotalPendingFunctions() + 1));
                result.get(t.getProjectId()).setTotalPlanCommitFunctions((result.get(t.getProjectId()).getTotalPlanCommitFunctions() == null) ? 1 : (result.get(t.getProjectId()).getTotalPlanCommitFunctions() + 1));
            }
            if (t.getFunctionStatus() != null && t.getFunctionStatus().equals(ModelStatus.STATUS_COMMITTED)) {
                result.get(t.getProjectId()).setTotalCommittedFunctions((result.get(t.getProjectId()).getTotalCommittedFunctions() == null) ? 1 : (result.get(t.getProjectId()).getTotalCommittedFunctions() + 1));
                result.get(t.getProjectId()).setTotalPlanCommitFunctions((result.get(t.getProjectId()).getTotalPlanCommitFunctions() == null) ? 1 : (result.get(t.getProjectId()).getTotalPlanCommitFunctions() + 1));
            }
            if (t.getFunctionStatus() != null && t.getFunctionStatus().equals(ModelStatus.STATUS_SUBMITTED)) {
                result.get(t.getProjectId()).setTotalSubmittedFunctions((result.get(t.getProjectId()).getTotalSubmittedFunctions() == null) ? 1 : (result.get(t.getProjectId()).getTotalSubmittedFunctions() + 1));
                result.get(t.getProjectId()).setTotalSubmitEvalFunctions((result.get(t.getProjectId()).getTotalSubmitEvalFunctions() == null) ? 1 : (result.get(t.getProjectId()).getTotalSubmitEvalFunctions() + 1));
            }
            if (t.getFunctionStatus() != null && t.getFunctionStatus().equals(ModelStatus.STATUS_EVALUATED)) {
                result.get(t.getProjectId()).setTotalEvaluatedFunctions((result.get(t.getProjectId()).getTotalEvaluatedFunctions() == null) ? 1 : (result.get(t.getProjectId()).getTotalEvaluatedFunctions() + 1));
                result.get(t.getProjectId()).setTotalSubmitEvalFunctions((result.get(t.getProjectId()).getTotalSubmitEvalFunctions() == null) ? 1 : (result.get(t.getProjectId()).getTotalSubmitEvalFunctions() + 1));
            }
        }
        if (!hasTeamEvalProjectIds.isEmpty()) result = getTeamGrade(hasTeamEvalProjectIds, milestoneId, result);
        return new ArrayList<>(result.values());
    }

    /**
     * Hàm lấy điểm tổng của đánh giá team dựa theo list projectId và milestone truyền vào
     *
     * @param projectId
     * @param milestoneId
     * @return
     */
    @Override
    public HashMap<Integer, MilestoneSubmitDTO> getTeamGrade(List<Integer> projectId, Integer milestoneId, HashMap<Integer, MilestoneSubmitDTO> result) {
        List<TeamEvaluationsDTO> teamEvaluationsDTOS = milestonesRepository.getMilestoneTeamEval(projectId, milestoneId);
        HashMap<Integer, BigDecimal> totalTeamEvaWeight = new HashMap<>();
        teamEvaluationsDTOS.forEach(t -> {
            result.get(t.getProjectId()).getTeamEvaluations().add(t);
            if (t.getWeight() != null) {
                totalTeamEvaWeight.put(t.getProjectId(), (totalTeamEvaWeight.get(t.getProjectId()) == null ? t.getWeight() : totalTeamEvaWeight.get(t.getProjectId()).add(t.getWeight())));
                if (t.getGrade() != null) {
                    result.get(t.getProjectId()).setTeamGrade(result.get(t.getProjectId()).getTeamGrade() == null ? t.getGrade().multiply(t.getWeight()) : (result.get(t.getProjectId()).getTeamGrade()).add(t.getGrade().multiply(t.getWeight())));
                }
            }
        });
        for (Map.Entry<Integer, MilestoneSubmitDTO> entry : result.entrySet()) {
            if (entry.getValue().getTeamGrade() != null)
                entry.getValue().setTeamGrade(entry.getValue().getTeamGrade().divide(totalTeamEvaWeight.get(entry.getValue().getProjectId()), 2, RoundingMode.HALF_UP));
        }

        return result;
    }

    /**
     * Hãm dùng để update hoặc create team evalutation
     *
     * @param teamEvaluationsDTO
     * @return
     */
    @Override
    public TeamEvaluationsDTO editTeamEvaluation(TeamEvaluationsDTO teamEvaluationsDTO) throws Exception {

        TeamEvaluations curTeamEval = teamEvaluationsRepository.findById(teamEvaluationsDTO.getId()).get();
        BigDecimal diffGrade = teamEvaluationsDTO.getGrade().subtract(curTeamEval.getGrade()); // Phần điểm chênh lệch của điểm mới và cũ

        // Check permission
        // Là trainer quản lý team hoặc là admin,
//        validate.validateTeamEval(teamEvaluationsDTO);
        TeamEvaluations teamEvaluations = modelMapper.map(teamEvaluationsDTO, TeamEvaluations.class);
        teamEvaluations.setCreatedBy(curTeamEval.getCreatedBy());
        teamEvaluations.setCreated(curTeamEval.getCreated());
        teamEvaluationsRepository.save(teamEvaluations);
        createOrUpdateIteEval(teamEvaluationsDTO, diffGrade);
//        }

        return teamEvaluationsDTO;
    }


    public void createOrUpdateIteEval(TeamEvaluationsDTO newEvaluation, BigDecimal diffGrade) {
        // Tính toán điểm mới được thêm vào iteration eval
        BigDecimal diffIteGrade = diffGrade == null ? BigDecimal.valueOf(0) : diffGrade.multiply(newEvaluation.getWeight());

        // Danh sách học viên trong lớp cần sửa điểm
        List<Integer> projectIds = Arrays.asList(newEvaluation.getProjectId());
        Page<ClassUsersDTO> classesUserDTOs = classUserRepository.searchClassUsers(null, projectIds, null, null, null, null);
        List<ClassUsersDTO> classesUsers = classesUserDTOs.getContent();
        List<Integer> classUserIds = classesUsers.stream().map(t -> t.getId()).collect(Collectors.toList());

        // Danh sách điểm ite hiện tại của học sinh
        List<IterationEvaluations> iterationEvaluationList = iterationEvaluationsRepository.findAllByMilestoneIdAndClassUserIdIn(newEvaluation.getMilestoneId(), classUserIds);
        HashMap<String, IterationEvaluations> iterationEvaluations = new HashMap<>(); // Key = classUserId-milestoneId
        for (IterationEvaluations t : iterationEvaluationList) {
            iterationEvaluations.put(t.getClassUserId().toString() + "-" + newEvaluation.getMilestoneId().toString(), t);
        }
        // Lấy config điểm của iteration hiện tại
        Iterations curIteration = iterationsRepository.getById(newEvaluation.getIterationId());

        for (ClassUsersDTO t : classesUsers) {
            iterationEvaluations.get(t.getId().toString() + "-" + newEvaluation.getMilestoneId().toString()).setGrade(iterationEvaluations.get(t.getId().toString() + "-" + newEvaluation.getMilestoneId().toString()).getGrade().add(diffIteGrade));
            // update lại student eval trong bảng class_user cho các thành viên trong team
            if (curIteration.getIsOngoing().equals(ModelStatus.IS_ONGOING)) {
                // Điểm tiến trình
                t.setOngoingEval(t.getOngoingEval() == null ? (diffIteGrade.multiply(curIteration.getEvaluationWeight())) : (diffIteGrade.multiply(curIteration.getEvaluationWeight())).add(t.getOngoingEval()));
            } else {
                // Điểm thuyết trình
                t.setFinalPresentEval(t.getFinalPresentEval() == null ? (diffIteGrade.multiply(curIteration.getEvaluationWeight())) : (diffIteGrade.multiply(curIteration.getEvaluationWeight())).add(t.getFinalPresentEval()));
            }
        }

        List<IterationEvaluations> valueList = new ArrayList<>(iterationEvaluations.values());
        iterationEvaluationsRepository.saveAll(valueList);

        List<ClassUsers> classUsersList = classesUsers
                .stream()
                .map(user -> modelMapper.map(user, ClassUsers.class))
                .collect(Collectors.toList());

        classUserRepository.saveAll(classUsersList);
    }

    @Override
    public void updateStatus(Integer projectId, Integer milestoneId) {
        String curSubmitStatus = submitsRepository.getCurStatus(projectId, milestoneId);
        //String curTrackingStatus= trackingsRepository.getCurTrackingStatus(projectId,milestoneId);
        submitsRepository.updateStatus(projectId, milestoneId, ModelStatus.NEXT_STATUS_STEP.get(curSubmitStatus));
        trackingsRepository.updateToStatus(projectId, milestoneId, ModelStatus.NEXT_STATUS_STEP.get(curSubmitStatus), curSubmitStatus);
    }

    /**
     * Hãm dùng để lấy data của loc evaluation
     *
     * @param functionId
     * @return
     */
    @Override
    public FunctionsDTO getFunctionEvaluate(Integer functionId) throws Exception {
        return functionsRepository.getFunctionEvaluate(functionId);
    }

    /**
     * Hãm dùng để lấy thêm mới hoặc cập nhật điểm đánh giác loc của function
     *
     * @param function@return
     */
    @Override
    public FunctionsDTO evaluateFunction(FunctionsDTO function) throws Exception {
        FunctionsDTO oldFunction = getFunctionEvaluate(function.getId());
        LocEvaluations locEvaluation = new LocEvaluations(function.getMilestoneId(), function.getId(), function.getComplexityId(), function.getQualityId(), function.getConvertedLoc(), function.getEvaluationComment());

        if (function.getLocEvaluationId() != null) locEvaluation.setId(oldFunction.getLocEvaluationId());// case update

        if (function.getStatus() == ModelStatus.STATUS_REJECTED) {
            if (oldFunction.getConvertedLoc() != null) {
                throw new Exception("Can not reject because of function has already been evaluated!");
            }
            // update status tracking
            trackingsRepository.evaluateTracking(function.getId(), function.getStatus(), function.getTrackingNote());
            functionsRepository.evaluateFunction(function.getId(), function.getStatus());
        }

        createOrUpdateLocEvaluate(oldFunction, function, locEvaluation);

        return function;
    }

    public void createOrUpdateLocEvaluate(FunctionsDTO oldFunction, FunctionsDTO newFunction, LocEvaluations locEvaluation) {
        locEvaluation = locEvaluationsRepository.save(locEvaluation);
        //update status function, tracking
        functionsRepository.updateFunctionStatus(newFunction.getId(), ModelStatus.STATUS_EVALUATED);
        trackingsRepository.updateTrackingStatus(newFunction.getTrackingId(), ModelStatus.STATUS_EVALUATED);

        EvaluationDTO evaluationDTO = locEvaluationsRepository.getMemberEvalInfo(newFunction.getMilestoneId(), newFunction.getAssigneeId(), newFunction.getProjectId());
        // Cập nhật điểm total loc
        Integer diffLoc = oldFunction.getConvertedLoc() == null ? locEvaluation.getConvertedLoc() : locEvaluation.getConvertedLoc() - oldFunction.getConvertedLoc(); //số lượng loc thay đổi
        evaluationDTO.setMemberTotalLoc(evaluationDTO.getMemberTotalLoc() == null ? diffLoc : evaluationDTO.getMemberTotalLoc() + diffLoc);

        // cập nhật điểm grade = totalLoc / maxLoc * 10 để về điểm hệ số 10
        BigDecimal newMemberGrade = BigDecimal.valueOf(evaluationDTO.getMemberTotalLoc()).divide(BigDecimal.valueOf(evaluationDTO.getMemberMaxLoc())).multiply(BigDecimal.valueOf(10)).subtract(evaluationDTO.getMemberEvalGrade());

        evaluationDTO.setMemberEvalGrade(reRangeGrade(newMemberGrade));

        // Cập nhật điểm ite eval
        BigDecimal newIteGrade = evaluationDTO.getIterationGrade().add(newMemberGrade.multiply(evaluationDTO.getEvaluationWeight()));
        evaluationDTO.setIterationGrade(reRangeGrade(newIteGrade));

        // Cập nhật điểm class user
        BigDecimal newOngoingGrade = evaluationDTO.getOnGoingGrade().add(evaluationDTO.getIterationGrade().multiply(evaluationDTO.getIterationWeight()));
        evaluationDTO.setOnGoingGrade(reRangeGrade(newOngoingGrade));
        evaluationDTO.setFinalTopicGrade(reRangeGrade(newOngoingGrade.add(evaluationDTO.getFinalPresentGrade())));

        // update điểm vào db
        locEvaluationsRepository.updateMemberEval(evaluationDTO.getMemberEvalGrade(), evaluationDTO.getMemberTotalLoc(), evaluationDTO.getMemberEvalId());
        locEvaluationsRepository.updateIteEval(evaluationDTO.getIterationGrade(), evaluationDTO.getIterationEvaluationId());
        locEvaluationsRepository.updateClassUserEval(evaluationDTO.getOnGoingGrade(), evaluationDTO.getFinalTopicGrade(), evaluationDTO.getClassUserId());
    }

    /**
     * Hàm format lại điểm
     * @param grade
     * @return
     */
    public BigDecimal reRangeGrade(BigDecimal grade) {
        // Nếu bé hơn 0 -> set về = 0
        if (grade.compareTo(BigDecimal.valueOf(0)) == -1) return BigDecimal.valueOf(0);
        // Nếu lớn hơn 10 -> set về = 10
        if(grade.compareTo(BigDecimal.valueOf(10)) == 1) return BigDecimal.valueOf(10);

        return grade;
    }
}
