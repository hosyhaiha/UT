package com.fpt.capstone.backend.api.BackEnd.service.impl;

import com.fpt.capstone.backend.api.BackEnd.dto.*;
import com.fpt.capstone.backend.api.BackEnd.entity.*;
import com.fpt.capstone.backend.api.BackEnd.repository.*;
import com.fpt.capstone.backend.api.BackEnd.service.*;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TrackingsServiceImpl implements TrackingsService {
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ClassesRepository classesRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private TrackingsRepository trackingsRepository;
    @Autowired
    FunctionsRepository functionsRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    UserService userService;
    @Autowired
    FunctionsService functionsService;
    @Autowired
    MilestonesRepository milestonesRepository;
    @Autowired
    SubmitsRepository submitsRepository;
    @Autowired
    LocEvaluationsRepository locEvaluationsRepository;

    @Override
    public void addTracking(List<Integer> functionList, Integer milestoneId, Integer assigneeId) throws Exception {
        Users users = userService.getUserLogin();
        if (milestoneId == null) {
            throw new Exception("Please chose one milestone to submit!");
        }
        if (assigneeId == null) {
            throw new Exception("Please chose one assigneeId to submit!");
        }
        List<Trackings> trackingsList = new ArrayList<>();
        for (Integer id : functionList) {
            Trackings trackings = new Trackings();
            trackings.setFunctionId(id);
            trackings.setMilestoneId(milestoneId);
            trackings.setAssigneeId(assigneeId);
            trackings.setAssignerId(users.getId());
            trackings.setStatus("pending");
            trackingsList.add(trackings);
            functionsRepository.updateFunctionStatus(id);
            id++;
        }
        trackingsRepository.saveAll(trackingsList);
    }

    @Override
    public TrackingsDTO deleteTracking(int id) {
        return null;
    }

    @Override
    public List<TrackingsDTO> showTracking() {
        return null;
    }

    @Override
    public void updateTracking(Integer trackingId, Integer newAssigneeId) throws Exception {
        if (newAssigneeId == null) {
            throw new Exception("Please chose one assigneeId to submit!");
        }
        Trackings trackings = trackingsRepository.getById(trackingId);
        if (!trackings.getAssigneeId().equals(newAssigneeId)) {
            addLog(trackings, newAssigneeId);
        }
        trackingsRepository.updateAssignee(trackingId, newAssigneeId);
    }

    @Override
    public void addLog(Trackings trackings, Integer newAssigneeId) throws Exception {
        try {
            UserLogs userLogs = new UserLogs();
            userLogs.setGroupType("history");
            userLogs.setAction("changeData");
            userLogs.setRefId(trackings.getId());
            userLogs.setRefColumnName("assignee_id");
            userLogs.setRefTableName("trackings");
            userLogs.setOldValue(String.valueOf(trackings.getAssigneeId()));
            userLogs.setNewValue(String.valueOf(newAssigneeId));
            userLogsRespository.save(userLogs);



        } catch (Exception e) {
            throw new Exception("Add log false!");
        }

    }

    public void addSubmit(Integer milestoneId, Integer projectId, String linkZip) throws Exception {
        try {
            Submits submits = new Submits();
            Date now = new Date();
            submits.setMilestoneId(milestoneId);
            submits.setProjectId(projectId);
            submits.setSubmitTime(now);
            submits.setPackageFileLink(linkZip);
            submits.setStatus("submitted");
            submitsRepository.save(submits);
        } catch (Exception e) {
            throw new Exception("Add submit false!");
        }

    }

    @Override
    public void submitTracking(MultipartFile fileStream, List<FunctionTrackingSubmitListDTO> functionTrackingSubmitListDTO, Integer milestoneId, Integer projectId) throws Exception {
        try {
            Date now = new Date();

            if (functionTrackingSubmitListDTO == null) {
                if (fileStream == null) {
                    throw new Exception("Choose file to submit");
                }
                File file = convertMultiPartToFile(fileStream);
                checkFileExtension(file.getName());
                String linkZip = uploadZipToGS(fileStream, "milestones", "functions");
                submitsRepository.updateLinkZip(milestoneId, projectId, now, linkZip);
            } else {
                Trackings trackings = new Trackings();
                Submits submits = submitsRepository.findByMilestoneIdAndProjectId(trackings.getMilestoneId(), projectId);
                try {
                    if (fileStream != null) {
                        File file = convertMultiPartToFile(fileStream);
                        checkFileExtension(file.getName());
                        String linkZip = uploadZipToGS(fileStream, "milestones", "functions");
                        if (submits != null) {
                            submitsRepository.updateLinkZip(trackings.getMilestoneId(), projectId, now, linkZip);
                        } else {
                            addSubmit(trackings.getMilestoneId(), projectId, linkZip);
                        }
                    } else {
                        submitsRepository.updateLinkZip(trackings.getMilestoneId(), projectId, now, null);
                    }
                } catch (Exception e) {
                    throw new Exception("Upload gs fail");
                }

                for (FunctionTrackingSubmitListDTO functions : functionTrackingSubmitListDTO) {
                    trackings = trackingsRepository.findByFunctionId(functions.getFunctionId());
                    Milestones milestones = milestonesRepository.getById(trackings.getMilestoneId());
                    if (functions.getChosen() == true) {
                        functionsRepository.updateFunctionStatusToSubmit(functions.getFunctionId());
                        trackingsRepository.updateSubmitted(trackings.getFunctionId(), trackings.getMilestoneId());
                        // add submit

                    }
                    if (trackings.getAssigneeId() != functions.getAssigneeId()) {
                        addLog(trackings, functions.getAssigneeId());
                        trackingsRepository.updateAssignee(trackings.getId(), functions.getAssigneeId());
                    }
                }
            }
        } catch (Exception e) {
            throw new Exception("Submit false!" + e);
        }
    }

    @Override
    public String getMilestoneName(Integer functionId) {
        return trackingsRepository.getMilestoneName(functionId);
    }

    @Override
    public String getEvalComment(Integer functionId) {
        return locEvaluationsRepository.getEvalComment(functionId);
    }

    @Override
    public String getFunctionName(Integer functionId) {
        Functions functions = functionsRepository.getById(functionId);
        return functions.getName();
    }

    @Override
    public AddUpdateLogDTO addLogUpdate(AddUpdateLogDTO updateLogDTO) {
        UserLogs userLogs = new UserLogs();
        userLogs.setNewValue(updateLogDTO.getMilestoneId().toString());
        userLogs.setGroupType("update");
        userLogs.setAction("updateTracking");
        userLogs.setDesc(updateLogDTO.getDescription());
        Trackings trackings = trackingsRepository.findByFunctionId(updateLogDTO.getFunctionId());
        userLogs.setRefId(trackings.getId());
        userLogs.setRefColumnName("milestone_id");
        userLogs.setRefTableName("trackings");
        userLogsRespository.save(userLogs);
        updateLogDTO.setId(userLogs.getId());
        updateLogDTO.setMilestoneName(getMilestoneName(updateLogDTO.getFunctionId()));
        updateLogDTO.setCreated(userLogs.getCreated());
        return updateLogDTO;
    }

    @Override
    public AddUpdateLogDTO updateLogUpdate(AddUpdateLogDTO updateLogDTO) {
        UserLogs userLogs = userLogsRespository.getById(updateLogDTO.getId());
        userLogs.setNewValue(updateLogDTO.getMilestoneId().toString());
        userLogs.setGroupType("update");
        userLogs.setAction("updateTracking");
        userLogs.setDesc(updateLogDTO.getDescription());
        Trackings trackings = trackingsRepository.findByFunctionId(updateLogDTO.getFunctionId());
        userLogs.setRefId(trackings.getId());
        userLogs.setRefColumnName("milestone_id");
        userLogs.setRefTableName("trackings");
        userLogsRespository.save(userLogs);
        updateLogDTO.setMilestoneName(trackingsRepository.getMilestone(updateLogDTO.getMilestoneId()));
        updateLogDTO.setCreated(userLogs.getCreated());
        return updateLogDTO;
    }

    @Override
    public Integer getCurrClass(Integer functionId) {
        return trackingsRepository.getCurClass(functionId);
    }


    private void checkFileExtension(String fileName) throws ServletException {
        if (fileName != null && !fileName.isEmpty() && fileName.contains(".")) {
            String[] allowedExt = {"zip"};
            for (String ext : allowedExt) {
                if (fileName.endsWith(ext)) {
                    return;
                }
            }
            throw new ServletException("File must be zip!");
        }
    }

    private File convertMultiPartToFile(MultipartFile fileStream) throws IOException {
        if (fileStream.getSize() > 20000000) {
            throw new IOException("Update has not been successfully uploaded. Requires less than 20 MB size");
        }
        File convFile = new File(fileStream.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(fileStream.getBytes());
        fos.close();
        System.out.println("convert " + fileStream.getOriginalFilename() + " to " + convFile.getName());
        return convFile;
    }

    public String uploadZipToGS(MultipartFile fileStream, String milstoneName, String projectName) throws Exception {
        try {
            String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
            String bucketName = "slpm";
            ClassLoader classLoader = getClass().getClassLoader();
            InputStream is = classLoader.getResourceAsStream("ancient-ceiling-352503-4ad907700d34.json");
            Credentials credentials = GoogleCredentials.fromStream(is);
            Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
            String fileName = timeStamp + "-milestone-" + milstoneName + projectName + "-data";
            String filePath = fileStream.getOriginalFilename();
            BlobId blobId = BlobId.of(bucketName, fileName);
            BlobInfo blobInfo = storage.create(
                    BlobInfo
                            .newBuilder(blobId)
                            .setContentType("application/zip")
                            .build(), Files.readAllBytes(Paths.get(filePath))
            );
            System.out.println(blobInfo.getMediaLink());
            System.out.println(blobInfo.getSelfLink());
            System.out.println(blobInfo.getBlobId());
            return blobInfo.getMediaLink();
        } catch (Exception e) {

            throw new Exception("Upload to gs false");
        }
    }

    @Override
    public TrackingsDTO findById(int id) throws Exception {
        return null;
    }

    @Autowired
    private ProjectService projectService;

    @Override
    public Page<TrackingsDTO> listBy(List<Integer> projectId, List<Integer> featureId, List<Integer> milestoneId,
                                     List<Integer> assigneeId, List<Integer> assignerId, List<Integer> functionId,
                                     List<Integer> classId, String status, int page, int per_page) throws Exception {
        String projectStatus = null;
        Pageable pageable = PageRequest.of(page - 1, per_page);
        if (ObjectUtils.isEmpty(status)) {
            status = null;
        }
        if (ObjectUtils.isEmpty(projectId)) {
            projectId = new ArrayList<>();

            Integer firstProjectId = projectService.showProjectList(classId, projectStatus).get(0).getValue();

            projectId.add(firstProjectId);
            return trackingsRepository.search(projectId, featureId, milestoneId, assigneeId, assignerId, functionId, classId, status, pageable);
        }
        return trackingsRepository.search(projectId, featureId, milestoneId, assigneeId, assignerId, functionId, classId, status, pageable);
    }

    @Autowired
    UserLogService userLogService;
    @Autowired
    UserLogsRespository userLogsRespository;

    @Override
    public TrackingsDTO getTrackingDetail(Integer id) {
        TrackingsDTO trackingsDTO = trackingsRepository.getTrackingDetail(id);
        List<UserLogsDTO> userLogsDTOS = userLogService.getListUserLog(id);
        List<Integer> userIds = new ArrayList<>();
        userLogsDTOS.forEach(t -> {
            if (t.getCreated() != null) userIds.add(t.getCreatedBy());
            if (t.getModifiedBy() != null) userIds.add(t.getModifiedBy());
            if (t.getRefColumnName().equals("assignee_id")) {
                if (t.getNewValue() != null) userIds.add(Integer.valueOf(t.getNewValue()));
                if (t.getOldValue() != null) userIds.add(Integer.valueOf(t.getOldValue()));
            }
        });
        List<Integer> userInfoIds = userIds.stream().distinct().collect(Collectors.toList());

        List<UsersDTO> userInfo = userLogsRespository.getUserDetails(userInfoIds);
        Map<Integer, UsersDTO> userInfoMap = new HashMap<>();
        for (UsersDTO usersDTO : userInfo) {
            userInfoMap.put(usersDTO.getId(), usersDTO);
        }
        List<String> log = new ArrayList<>();
        userLogsDTOS.forEach(t -> log.add(formatLog(userInfoMap, t)));
        trackingsDTO.setHistory(log);
        trackingsDTO.setAssigneeOptions(trackingsRepository.searchUserAssign(id));
        return trackingsDTO;
    }

    @Override
    public List<TrackingsDTO> showTrackingList(String status) {
        return null;
    }

    public String formatLog(Map<Integer, UsersDTO> userInfoMap, UserLogsDTO userLog) {
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        String date = DateFor.format(userLog.getCreated());
        String log = "";
        if (userLog.getAction().equals(UserLogsDTO.CHANGE_DATA)) {
            String actor = formatUserString(userInfoMap.get(userLog.getCreatedBy()).getFullName(), userInfoMap.get(userLog.getCreatedBy()).getRollNumber());
            if (userLog.getRefColumnName().equals("status")) {
                log = "[" + date + "] " + actor + " changed submitting status to " + userLog.getNewValue() + ((userLog.getDesc().isEmpty() && !userLog.getNewValue().equals("rejected")) ? "" : " (" + userLog.getDesc() + ")");
            }
            if (userLog.getRefColumnName().equals("assignee_id")) {
                log = "[" + date + "] " + actor + " assigned to " + formatUserString(userInfoMap.get(Integer.valueOf(userLog.getNewValue())).getFullName(), userInfoMap.get(Integer.valueOf(userLog.getNewValue())).getRollNumber());
            }
        }
        if (userLog.getAction().equals(UserLogsDTO.ADD_DATA)) {
            if (userLog.getRefColumnName().equals("assignee_id")) {
                log = "[" + date + "] " + "Added by " + formatUserString(userInfoMap.get(userLog.getCreatedBy()).
                        getFullName(), userInfoMap.get(userLog.getCreatedBy()).getRollNumber()) + ", assigned to " + formatUserString(userInfoMap.get(Integer.valueOf(userLog.getNewValue())).getFullName(), userInfoMap.get(Integer.valueOf(userLog.getNewValue())).getRollNumber());
            }
        }
        return log;
    }

    public String formatUserString(String fullName, String rollNumber) {
        String name = "";

        List<String> splitString = Arrays.asList(fullName.split(" "));

        name += splitString.get(splitString.size() - 1).substring(0, 1).toUpperCase() + splitString.get(splitString.size() - 1).substring(1).toLowerCase();

        for (Integer i = 0; i < splitString.size() - 1; i++) {

            name += splitString.get(i).substring(0, 1).toUpperCase();
        }
        return name + rollNumber;
    }
}
