package com.fpt.capstone.backend.api.BackEnd.service.impl;

import com.fpt.capstone.backend.api.BackEnd.dto.EvaluationCriteriaDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.MilestoneCriteriaDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.ModelStatus;
import com.fpt.capstone.backend.api.BackEnd.dto.class_users.ClassUserInputDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.class_users.ClassUsersDTO;
import com.fpt.capstone.backend.api.BackEnd.entity.*;
import com.fpt.capstone.backend.api.BackEnd.repository.*;
import com.fpt.capstone.backend.api.BackEnd.service.*;
import com.fpt.capstone.backend.api.BackEnd.service.validate.ConstantsRegex;
import com.fpt.capstone.backend.api.BackEnd.service.validate.Validate;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ClassUserServiceImpl implements ClassUserService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ClassUserRepository classUserRepository;

    @Autowired
    private ClassesRepository classesRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClassesService classesService;

    @Autowired
    private MilestonesRepository milestonesRepository;

    @Autowired
    private IterationEvaluationsRepository iterationEvaluationsRepository;

    @Autowired
    private TeamEvaluationsRepository teamEvaluationsRepository;

    @Autowired
    private MemberEvaluationRepository memberEvaluationRepository;

    @Autowired
    private EvaluationJobService evaluationJobService;

    @Autowired
    private Validate validate;

    @Autowired
    UserService userService;

    @Override
    public ClassUsersDTO add(ClassUserInputDTO classUserInputDTO) throws Exception {
        // Default = 0 điểm, chỉ thay đổi điểm khi thao tác trên hệ thống
        classUserInputDTO.setOngoingEval("0");
        classUserInputDTO.setFinalPresentEval("0");
        classUserInputDTO.setFinalTopicEval("0");
        validate.validateClassUser(classUserInputDTO);
        if (classUserRepository.findByClassIdAndUserIdAndStatus(Integer.valueOf(classUserInputDTO.getClassId()), Integer.valueOf(classUserInputDTO.getUserId()), ModelStatus.STATUS_ACTIVE) != null) {
            throw new Exception("User ID already exist on this Class ID");
        }

        if (
                Integer.valueOf(classUserInputDTO.getProjectLeader()) == 1
                        && classUserRepository.findByClassIdAndProjectIdAndDropoutDateAndStatusAndProjectLeader(
                        Integer.valueOf(classUserInputDTO.getClassId()),
                        Integer.valueOf(classUserInputDTO.getProjectId()),
                        null,
                        ModelStatus.STATUS_ACTIVE,
                        ModelStatus.IS_PROJECT_LEADER) != null
        ) {
            throw new Exception("This project already has a leader!");
        }

        if (classUserInputDTO.getDropoutDate() != null)
            classUserInputDTO.setDropoutDate(validate.convertDDMMYYtoYYMMDD(classUserInputDTO.getDropoutDate()));
        //Check lớp đã hoàn thành config, được phép thêm học sinh, team vào
        if (!classesService.checkClassInitCondition(Integer.valueOf(classUserInputDTO.getClassId())))
            throw new Exception("This class have not finished configured yet!");

        ClassUsers classUsers = modelMapper.map(classUserInputDTO, ClassUsers.class);
        classUsers = classUserRepository.save(classUsers);
        // Tạo các bản ghi điểm cho user
//        ClassUsers finalClassUsers = classUsers;
//        jobScheduler.enqueue(() -> evaluationJobService.createStudentEvaluation(finalClassUsers));
        createStudentEvaluation(classUsers);
        return showDetail(classUsers.getId());
    }

    public void createStudentEvaluation(ClassUsers student) {
        Users auth = userService.getUserLogin();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String curTime = formatter.format(date);

        List<MilestoneCriteriaDTO> criteria = milestonesRepository.getMemberCriteria(student.getClassId());
        HashMap<Integer, MilestoneCriteriaDTO> criteriaMap = new HashMap<>();
        // gen data cho mảng eval criteria
        for (MilestoneCriteriaDTO milestoneCriteria : criteria) {
            criteriaMap.putIfAbsent(milestoneCriteria.getIterationId(), milestoneCriteria);
            criteriaMap.get(milestoneCriteria.getIterationId()).getEvaluationCriteriaList().add(new EvaluationCriteriaDTO(milestoneCriteria.getCriteriaId(), milestoneCriteria.getIterationId(), milestoneCriteria.getEvalWeight(), milestoneCriteria.getIsTeamCriteria()));
        }

        List<MemberEvaluations> memberEvaluations = new ArrayList<>();
        // thực hiện thêm record
        for (MilestoneCriteriaDTO milestoneCriteria : criteriaMap.values()) {
            // Thêm iteration_evaluation
            IterationEvaluations newIterationEvaluation = new IterationEvaluations(milestoneCriteria.getMilestoneId(), student.getId(), BigDecimal.valueOf(0), BigDecimal.valueOf(0));
            newIterationEvaluation = iterationEvaluationsRepository.save(newIterationEvaluation);
            // Thêm điểm cá nhân
            for (EvaluationCriteriaDTO evaluationCriteria : milestoneCriteria.getEvaluationCriteriaList()) {
                memberEvaluations.add(new MemberEvaluations(newIterationEvaluation.getId(), evaluationCriteria.getId(), 0, BigDecimal.valueOf(0), auth.getId(), curTime));
            }
        }
        memberEvaluationRepository.saveAll(memberEvaluations);
    }

    @Override
    public ClassUsersDTO update(ClassUserInputDTO classUserInputDTO) throws Exception {
        if (ObjectUtils.isEmpty(classUserInputDTO.getId())) {
            throw new Exception("ID cannot be empty!");
        }
        if (!classUserInputDTO.getId().matches(ConstantsRegex.NUMBER_PATTERN.toString())) {
            throw new Exception("ID must be number!");
        }
        if (classUserRepository.getById(Integer.valueOf(classUserInputDTO.getId())) == null) {
            throw new Exception("Class User does not exist!");
        }
        validate.validateClassUser(classUserInputDTO);
        ClassUsers classUser = classUserRepository.getOne(Integer.valueOf(classUserInputDTO.getId()));
        if (Integer.valueOf(classUserInputDTO.getClassId()) != classUser.getClassId()
                || Integer.valueOf(classUserInputDTO.getUserId()) != classUser.getUserId()) {
            if (classUserRepository.searchByCLassIDAndUserID(Integer.valueOf(classUserInputDTO.getClassId())
                    , Integer.valueOf(classUserInputDTO.getUserId())) != null) {
                throw new Exception("User ID already exist on this Class ID");
            }
        }

        if (Integer.valueOf(classUserInputDTO.getClassId()) != classUser.getClassId()
                || Integer.valueOf(classUserInputDTO.getProjectId()) != classUser.getProjectId()
                || Integer.valueOf(classUserInputDTO.getProjectLeader()) != classUser.getProjectLeader()) {
            if (Integer.valueOf(classUserInputDTO.getProjectLeader()) == 1
                    && classUserRepository.searchProjectLeader(
                    Integer.valueOf(classUserInputDTO.getClassId()),
                    Integer.valueOf(classUserInputDTO.getProjectId()),
                    Integer.valueOf(classUserInputDTO.getProjectLeader())) != null) {
                throw new Exception("This project has a leader!");
            }
        }

        classUser = modelMapper.map(classUserInputDTO, ClassUsers.class);
//        classUser.setClasses(classesRepository.getById(Integer.valueOf(classUserInputDTO.getClassId())));
//        classUser.setProject(projectRepository.getById(Integer.valueOf(classUserInputDTO.getProjectId())));
//        classUser.setUser(userRepository.getById(Integer.valueOf(classUserInputDTO.getUserId())));
        classUserRepository.save(classUser);
        classUser = entityManager.find(ClassUsers.class, classUser.getId());
        return showDetail(classUser.getId());
    }

    @Override
    public ClassUsersDTO showDetail(int id) throws Exception {
        if (classUserRepository.getClassUsersDetail(id) == null) {
            throw new Exception("Class user not exist");
        }
        return classUserRepository.getClassUsersDetail(id);
    }

    @Override
    public Page<ClassUsersDTO> searchBy(List<Integer> classId, List<Integer> projectId, List<Integer> userId, Integer projectLead, String Status, int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        if (ObjectUtils.isEmpty(Status)) Status = null;
        return classUserRepository.searchClassUsers(classId, projectId, userId, projectLead, Status, pageable);
    }

    @Override
    public List<ClassUsersDTO> showList(String status) {
        return null;
    }

    @Override
    public Map<String, String> toMap(ClassUsersDTO classUsersDTO) {
        String dropOutDate = "";
        if (classUsersDTO.getDropoutDate() != null) {
            DateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy");
            dropOutDate = dateFormat.format(classUsersDTO.getDropoutDate());
        }
        String isTeamLeader = "";
        if (classUsersDTO.getProjectLeader() == 1) {
            isTeamLeader = "Leader";
        }

        Map<String, String> result = new HashMap<>();
        result.put("id", classUsersDTO.getId().toString());
        result.put("classCode", classUsersDTO.getClassCode());
        result.put("projectCode", classUsersDTO.getProjectCode());
        result.put("projectName", classUsersDTO.getProjectName());
        result.put("userName", classUsersDTO.getUserEmail());
        result.put("projectLeader", isTeamLeader);
        result.put("dropoutDate", dropOutDate);
        result.put("note", classUsersDTO.getNote());
        result.put("ongoingEval", classUsersDTO.getOngoingEval().toString());
        result.put("finalPresentEval", classUsersDTO.getFinalPresentEval().toString());
        result.put("finalTopicEval", classUsersDTO.getFinalTopicEval().toString());
        result.put("status", classUsersDTO.getStatus());
        result.put("created", classUsersDTO.getCreated().toString());
        result.put("modified", classUsersDTO.getModified().toString());
        result.put("createdByUser", classUsersDTO.getCreatedByUser());
        result.put("modifiedByUser", classUsersDTO.getModifiedByUser());
        return result;
    }

    @Override
    public String exportClassUser(List<ClassUsersDTO> classesUserDTOs) throws IOException {
        LinkedHashMap<String, String> excelFunctionHeader = generateFunctionExcelHeader();
        List<Map> functionMap = toListOfMap(classesUserDTOs);
        return excelService.exportExcel(excelFunctionHeader, functionMap, "classUser");
    }

    @Override
    public ResponseEntity<?> importClassUser(MultipartFile fileStream) throws IOException {
        try {
            File file = convertMultiPartToFile(fileStream);
            List<ClassUsers> classUsersList = new ArrayList<ClassUsers>();
            Integer headerIndex = 0;
            Boolean hasErr = false;
            Workbook workbook = new XSSFWorkbook(file);

            Sheet sheet = workbook.getSheetAt(0);
            Row headerRow = sheet.getRow(0);
            LinkedHashMap<String, String> excelFunctionHeader = configFunctionExcelHeader();
            Set<String> keySet = excelFunctionHeader.keySet();

            List<String> listKeys = new ArrayList<String>(keySet);
            String key = "";
            Integer numRow = headerRow.getPhysicalNumberOfCells();

            //check header
            for (Integer i = 0; i < excelFunctionHeader.size(); i++) {
                key = listKeys.get(i);
                if (!(String.valueOf(headerRow.getCell(i))).equals(excelFunctionHeader.get(key))) {
                    throw new IOException("Wrong header at row 1, column " + CellReference.convertNumToColString(i));
                }
            }

            // Tạo cell mới ở header row = 0, cell = totalHeaderCell + 1 có tên là Result log
            excelService.createRow(headerRow, workbook, excelFunctionHeader.size(), "Error log");

            Iterator<Row> rows = sheet.rowIterator();
            DataFormatter formatter = new DataFormatter();
            int rowNumber = 0;

            while (rows.hasNext()) {
                String resultLog = "";
                String gmail = "";
                Users users = new Users();

                Row currentRow = rows.next();
                // skip header
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }
                int i = -1;

                Iterator<Cell> cellsInRow = currentRow.cellIterator();
                ClassUsers classUsers = new ClassUsers();
                int cellIdx = 0;

                short lastCellNum = currentRow.getLastCellNum();
                while (cellsInRow.hasNext()) {
                    i++;
                    Cell currentCell = currentRow.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    formatter.formatCellValue(currentCell);
                    if (i == lastCellNum) {
                        break;
                    }
                    if (currentCell == null || currentCell.getCellType().equals(CellType.BLANK)) {
                        currentCell.setCellValue("");
                    }


                    switch (cellIdx) {
                        //class Code
                        case 0:
                            if (currentCell.getStringCellValue().trim().equals("")) {
                                resultLog += (resultLog.isEmpty() ? "" : ", ") + "Class code cannot empty";
                                break;
                            }
                            Classes classes = classesRepository.findByCode(currentCell.getStringCellValue().trim());
                            if (classes == null) {
                                resultLog += (resultLog.isEmpty() ? "" : ", ") + "Class code not exist";
                                break;
                            }
                            classUsers.setClassId(classes.getId());
                            break;
                        //Project code
                        case 1:
                            if (currentCell.getStringCellValue().trim().equals("")) {
                                resultLog += (resultLog.isEmpty() ? "" : ", ") + "Project Code cannot empty";
                                break;
                            }
                            Projects projects = projectRepository.findByCodeAndClassId(currentCell.getStringCellValue().trim(), classUsers.getClassId());

                            if (projects == null) {
                                resultLog += (resultLog.isEmpty() ? "" : ", ") + "Project code not exist in this class";
                                break;
                            }
                            classUsers.setProjectId(projects.getId());
                            break;
                        //Email
                        case 2:
                            if (currentCell.getStringCellValue().trim().equals("")) {
                                resultLog += (resultLog.isEmpty() ? "" : ", ") + "User email cannot empty";
                                break;
                            }
                            gmail = currentCell.getStringCellValue().trim();
                            Users userInput = userRepository.findByEmail(gmail);

                            //send mail
                            if (userInput != null) {
                                //check user exist on class

                                if (classUsers.getClassId() != null) {
                                    if (classUserRepository.searchByCLassCodeAndEmail(
                                            classUsers.getClassId(),
                                            currentCell.getStringCellValue().trim()) != null) {
                                        resultLog += (resultLog.isEmpty() ? "" : ", ") + "User already exist on this Class";
                                        break;
                                    } else {
                                        classUsers.setUserId(userInput.getId());
                                    }
                                }
                            }

                            break;
                        case 3:
                            if (currentCell == null || currentCell.getCellType().equals(CellType.BLANK)) {
                                resultLog += (resultLog.isEmpty() ? "" : ", ") + "Project Leader cannot empty";
                                break;
                            }

                            if (!currentCell.getStringCellValue().trim().equals("Leader") && !currentCell.getStringCellValue().trim().isEmpty()) {
                                resultLog += (resultLog.isEmpty() ? "" : ", ") + "Project Leader is 'Leader' or empty";
                                break;
                            }
                            if (currentCell.getStringCellValue().trim().equals("Leader")) {
                                if (classUserRepository.findByClassIdAndProjectIdAndProjectLeader(classUsers.getClassId(), classUsers.getProjectId(), ModelStatus.IS_PROJECT_LEADER) == null) {
                                    classUsers.setProjectLeader(1);
                                    break;
                                } else {
                                    resultLog += (resultLog.isEmpty() ? "" : ", ") + "This project already have project leader";
                                    break;
                                }
                            }
                            classUsers.setProjectLeader(0);
                            break;
                        //Drop out date
                        case 4:
                            if (currentCell.getCellType().equals(CellType.NUMERIC)) {
                                Date date = currentCell.getDateCellValue();
                                classUsers.setDropoutDate(date);
                                break;
                            } else {
                                String strPattern = "^(0[1-9]|[12][0-9]|[3][01])/(0[1-9]|1[012])/\\d{4}$";
                                if (!currentCell.getStringCellValue().matches(strPattern)) {
                                    resultLog += (resultLog.isEmpty() ? "" : ", ") + "Drop date must be format like:dd/mm/yyyy";
                                    break;
                                }
                            }
                            //Note
                        case 5:
                            classUsers.setNote(currentCell.getStringCellValue());
                            break;
                        case 6:
                            if (currentCell.getStringCellValue().trim().equals("")) {
                                resultLog += (resultLog.isEmpty() ? "" : ", ") + "Status cannot empty";
                                break;
                            }
                            if (!currentCell.getStringCellValue().trim().matches(String.valueOf(ConstantsRegex.STATUS_PATTERN))) {
                                resultLog += (resultLog.isEmpty() ? "" : ", ") + "Status must be active or inactive only!";
                                break;
                            }
                            classUsers.setStatus(currentCell.getStringCellValue());
                            break;
                        default:
                            break;
                    }
                    cellIdx++;
                }
                if (!resultLog.isEmpty()) {
                    hasErr = true;
                    //ghi them zo shell
                    Cell logShell = currentRow.createCell(cellIdx);
                    logShell.setCellValue(resultLog);
                    sheet.setColumnWidth(headerIndex, 5000);
                    continue;
                }
                //Create and send mail
                if (users == null) {
                    userService.createUserImportExcel(gmail);
                    Users user2 = userRepository.findByEmail(gmail);
                    classUsers.setUserId(user2.getId());
                }
                classUsersList.add(classUsers);
            }
            if (!hasErr) {
                classUserRepository.saveAll(classUsersList);
                workbook.close();
                return ResponseEntity.status(200).body(
                        ApiResponse.builder()
                                .success(true)
                                .message("Import excel successfully").build()
                );
            } else {
                String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
                File currDir = new File(String.valueOf(this.getClass().getResourceAsStream("ExportFile/classUser/.")));
                String path = currDir.getAbsolutePath();
                String fileLocation = path.substring(0, path.length() - 1) + timeStamp + "-import-function-error-log.xlsx";
                FileOutputStream outputStream = new FileOutputStream(fileLocation);
                workbook.write(outputStream);
                workbook.close();

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        ApiResponse.builder()
                                .success(false)
                                .message("Import excel error").data(excelService.uploadExcelToGS(fileLocation, "classUser")).build()
                );
            }
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        } catch (InvalidFormatException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean checkLeader(Integer userId, Integer projectId) {
        if (classUserRepository.checkLeader(userId, projectId) == 1)
            return true;
        return false;
    }

    private File convertMultiPartToFile(MultipartFile fileStream) throws IOException {
        if (fileStream.getSize() > 20000000) {
            throw new IOException("Update has not been successfully uploaded. Requires less than 10 MB size");
        }
        File convFile = new File(fileStream.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(fileStream.getBytes());
        fos.close();
        System.out.println("convert " + fileStream.getOriginalFilename() + " to " + convFile.getName());
        return convFile;
    }

    @Override
    public List<Map> toListOfMap(List<ClassUsersDTO> classesUserDTOs) {
        List<Map> result = new ArrayList<>();
        classesUserDTOs.forEach(classUser -> {
            result.add(toMap(classUser));
        });
        return result;
    }

    public LinkedHashMap<String, String> generateFunctionExcelHeader() {
        LinkedHashMap<String, String> excelFunctionHeader = new LinkedHashMap<>();
        excelFunctionHeader.put("id", "Id");
        excelFunctionHeader.put("classCode", "Class Code");
        excelFunctionHeader.put("projectCode", "Project code");
        excelFunctionHeader.put("userName", "Name");
        excelFunctionHeader.put("projectLeader", "Project Leader");
        excelFunctionHeader.put("dropoutDate", "Dropout Date");
        excelFunctionHeader.put("note", "Note");
        excelFunctionHeader.put("ongoingEval", "Ongoing Eval");
        excelFunctionHeader.put("finalPresentEval", "Final Present Eval");
        excelFunctionHeader.put("finalTopicEval", "Final Topic Eval");
        excelFunctionHeader.put("status", "Status");
        excelFunctionHeader.put("created", "Created");
        excelFunctionHeader.put("modified", "Modified");
        excelFunctionHeader.put("createdByUser", "Created by");
        excelFunctionHeader.put("modifiedByUser", "Modified by");
        return excelFunctionHeader;
    }

    public LinkedHashMap<String, String> configFunctionExcelHeader() {
        LinkedHashMap<String, String> excelFunctionHeader = new LinkedHashMap<>();
        excelFunctionHeader.put("classCode", "Class Code");
        excelFunctionHeader.put("projectCode", "Project code");
        excelFunctionHeader.put("userEmail", "Email");
        excelFunctionHeader.put("projectLeader", "Project Leader");
        excelFunctionHeader.put("dropoutDate", "Dropout Date");
        excelFunctionHeader.put("note", "Note");
        excelFunctionHeader.put("status", "Status");
        return excelFunctionHeader;
    }

    @Autowired
    private ExcelService excelService;
}
