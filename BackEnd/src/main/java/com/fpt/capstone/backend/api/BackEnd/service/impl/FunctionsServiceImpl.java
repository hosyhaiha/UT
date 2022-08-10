package com.fpt.capstone.backend.api.BackEnd.service.impl;

import com.fpt.capstone.backend.api.BackEnd.dto.*;
import com.fpt.capstone.backend.api.BackEnd.entity.*;
import com.fpt.capstone.backend.api.BackEnd.repository.*;
import com.fpt.capstone.backend.api.BackEnd.service.ExcelService;
import com.fpt.capstone.backend.api.BackEnd.service.FunctionsService;
import com.fpt.capstone.backend.api.BackEnd.service.UserService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class FunctionsServiceImpl implements FunctionsService {
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private PermissionService permissionService;
    @Autowired
    private FeaturesRepository featuresRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FunctionsRepository functionsRepository;
    @Autowired
    private SubjectSettingsRepository subjectSettingsRepository;
    @Autowired
    private Validate validate ;

    @Override
    public FunctionsDTO addFunction(FunctionsDTO functionsDTO) throws Exception {
        validate.validateFunction(functionsDTO);

        if (functionsRepository.searchByNameOnFeature(functionsDTO.getFeatureId(), functionsDTO.getName()) > 0) {
            throw new Exception("Name of this function is already exist in this feature");
        }

        Functions functions = modelMapper.map(functionsDTO, Functions.class);
        functions.setStatus(ModelStatus.STATUS_PENDING); // default tạo mới function sẽ có status = pending
        functions = functionsRepository.save(functions);
        return getFunctionDetail(functions.getId());
    }

    @Override
    public FunctionsDTO deleteFunction(int id) {
        return null;
    }

    @Override
    public List<FunctionsDTO> showFunction() {
        return null;
    }

    @Override
    public FunctionsDTO updateFunction(FunctionsDTO functionsDTO) throws Exception {
        validate.validateFunction(functionsDTO);

        Functions functions = functionsRepository.getById(functionsDTO.getId());
        if (!functions.getName().equals(functionsDTO.getName())) {
            if (functionsRepository.searchByNameOnFeature(functionsDTO.getFeatureId(), functionsDTO.getName()) > 0) {
                throw new Exception("Name of this function is already exist in this feature");
            }
        }
        functions = modelMapper.map(functionsDTO, Functions.class);
        functionsRepository.save(functions);
        functions = entityManager.find(Functions.class, functions.getId());
        return getFunctionDetail(functions.getId());
    }

    @Override
    public FunctionsDTO findById(int id) throws Exception {
        return null;
    }

    @Override
    public Page<FunctionsDTO> listBy(List<Integer> projectID, List<Integer> featureId, String name, String status, int page, int limit) throws Exception {
        // check quyen
//        List<Integer> authorityFunctionIds =  permissionService.getFunctionAuthority(null);
//        return functionsRepository.search(projectID, featureId,  name, status, PageRequest.of(page - 1, limit), authorityFunctionIds);
        return functionsRepository.search(projectID, featureId,  name, status, PageRequest.of(page - 1, limit), null);
    }

    @Override
    public FunctionsDTO getFunctionDetail(int id) {
        return functionsRepository.getFunctionDetail(id);
    }

    @Override
    public List<FunctionsDTO> showFunctionList() {
        return null;
    }

    @Override
    public List<ColumnTypeDTO> showColumns(String table, String field) {
        List<String> result = new ArrayList<>();
        List<ColumnTypeDTO> columnTypeDTOS = new ArrayList<>();
        String value = functionsRepository.showColumns(table, field);
        value = value.substring(value.indexOf('(') + 2, value.length() - 2);
        result.addAll(Arrays.asList(value.split("','")));
        result.forEach(r -> {
            columnTypeDTOS.add(new ColumnTypeDTO(r, r));
        });
        return columnTypeDTOS;
    }

    @Override
    public List<Map> toListOfMap(List<FunctionsDTO> functions) {
        List<Map> result = new ArrayList<>();
        functions.forEach(function -> {
            result.add(toMap(function));
        });
        return result;
    }

    @Override
    public Map<String, String> toMap(FunctionsDTO function) {
        Map<String, String> result = new HashMap<>();
        result.put("id", function.getId().toString());
        result.put("projectCode", function.getProjectCode());
        result.put("projectName", function.getProjectName());
        result.put("featureName", function.getFeatureName());
        result.put("name", function.getName());
        result.put("description",function.getDescription());
        result.put("priority", function.getPriority());
        result.put("status", function.getStatus());
        result.put("created", function.getCreated().toString());
        result.put("modified", function.getModified().toString());
        result.put("createdByUser", function.getCreatedByUser());
        result.put("modifiedByUser", function.getModifiedByUser());
        return result;
    }

    public LinkedHashMap<String, String> generateFunctionExcelHeader() {

        LinkedHashMap<String, String> excelFunctionHeader = new LinkedHashMap<>();
        excelFunctionHeader.put("id", "Id");
        excelFunctionHeader.put("projectName", "Project name");
        excelFunctionHeader.put("projectCode", "Project code");
        excelFunctionHeader.put("featureName", "Feature name");
        excelFunctionHeader.put("name", "Function name");
        excelFunctionHeader.put("description", "Description");
        excelFunctionHeader.put("priority", "Priority");
        excelFunctionHeader.put("status", "Status");
        excelFunctionHeader.put("created", "Created");
        excelFunctionHeader.put("modified", "Modified");
        excelFunctionHeader.put("createdByUser", "Created by");
        excelFunctionHeader.put("modifiedByUser", "Modified by");
        return excelFunctionHeader;
    }

    public LinkedHashMap<String, String> configFunctionExcelHeader() {

        LinkedHashMap<String, String> excelFunctionHeader = new LinkedHashMap<>();
        excelFunctionHeader.put("featureName", "Feature name");
        excelFunctionHeader.put("name", "Function name");
        excelFunctionHeader.put("description", "Description");
        excelFunctionHeader.put("priority", "Priority");
        return excelFunctionHeader;
    }

    @Autowired
    private ExcelService excelService;

    @Override
    public ResponseEntity<?> importFunctions(MultipartFile fileStream) throws IOException {
        try {
            File file = convertMultiPartToFile(fileStream);
            List<Functions> functions = new ArrayList<Functions>();
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

            for (Integer i = 0; i < excelFunctionHeader.size(); i++) {
                key = listKeys.get(i);
                if (!(String.valueOf(headerRow.getCell(i))).equals(excelFunctionHeader.get(key))) {
                    throw new IOException("Wrong header at row 1, column " + CellReference.convertNumToColString(i));
                }
            }

            // Tạo cell mới ở header row = 0, cell = totalHeaderCell + 1 có tên là Result log
            excelService.createRow(headerRow, workbook, excelFunctionHeader.size(), "Error log");
            Iterator<Row> rows = sheet.rowIterator();

            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();
                // skip header
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }
                int i = -1;
                Iterator<Cell> cellsInRow = currentRow.cellIterator();
                Functions function = new Functions();
                function.setStatus(ModelStatus.STATUS_PENDING);
                int cellIdx = 0;
                String resultLog = "";
                short lastCellNum = currentRow.getLastCellNum();
                while (cellsInRow.hasNext()) {
                    i++;
                    Cell currentCell = currentRow.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    if (i == lastCellNum) {
                        break;
                    }
                    if (currentCell == null || currentCell.getCellType().equals(CellType.BLANK)) {
                        currentCell.setCellValue("");
                    }
                    switch (cellIdx) {
                        case 0:
                            if (currentCell.getStringCellValue().trim().equals("")) {
                                resultLog += (resultLog.isEmpty() ? "" : ", ") + "Feature name cannot empty";
                                break;
                            }
                            Features feature = featuresRepository.findByName(currentCell.getStringCellValue().trim());
                            if (feature == null) {
                                resultLog += (resultLog.isEmpty() ? "" : ", ") + "Feature does not exist";
                                break;
                            }
                            function.setFeatureId(feature.getId());
                            break;
                        case 1:
                            if (currentCell.getStringCellValue().trim().equals("")) {
                                resultLog += (resultLog.isEmpty() ? "" : ", ") + "Function name cannot empty";
                                break;
                            }
                            if (functionsRepository.searchByNameOnFeature(function.getFeatureId(), currentCell.getStringCellValue().trim()) > 0) {
                                resultLog += (resultLog.isEmpty() ? "" : ", ") + "Name of this function is already exist in this feature";
                                break;
                            }
                            function.setName(currentCell.getStringCellValue());
                            break;
                        case 2:
                            function.setDescription(currentCell.getStringCellValue());
                            break;
                        case 3:
                            if (currentCell.getStringCellValue().trim().equals("")) {
                                resultLog += (resultLog.isEmpty() ? "" : ", ") + "Priority cannot empty";
                                break;
                            }
                            if (!currentCell.getStringCellValue().trim().matches(ConstantsRegex.PRIORITY_STATUS_PATTERN.toString())) {
                                resultLog += (resultLog.isEmpty() ? "" : ", ") + "Priority must be urgent/high/medium/low/lowest only!";
                                break;
                            }
                            function.setPriority(currentCell.getStringCellValue());
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
                functions.add(function);
            }


            if (!hasErr) {
                functionsRepository.saveAll(functions);
                workbook.close();
                return ResponseEntity.status(200).body(
                        ApiResponse.builder()
                                .success(true)
                                .message("Import excel successfully").build()
                );
            } else {
                String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());

                File currDir = new File(String.valueOf(this.getClass().getResourceAsStream("/ExportFile/function/.")));
                String path = currDir.getAbsolutePath();
                String fileLocation = path.substring(0, path.length() - 1) + timeStamp + "-import-function-error-log.xlsx";
                FileOutputStream outputStream = new FileOutputStream(fileLocation);
                workbook.write(outputStream);
                workbook.close();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        ApiResponse.builder()
                                .success(false)
                                .message("Import excel error").data(excelService.uploadExcelToGS(fileLocation, "funciton")).build()
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
    public List<FunctionListDTO> showFeatureList() {
        List<FunctionListDTO> featuresListDTOS = functionsRepository.getLabelList();
        return featuresListDTOS;
    }

    @Autowired
    UserService userService;

    @Override
    public List<FunctionListTrackingDTO> showTrackingFeature(Integer projectId, List<Integer> featureName, Users users, String functionStatus) {
        if (Objects.equals(users.getSettings().getId(), RoleID.STUDENT)) {
            List<FunctionListTrackingDTO> featuresListDTOS = functionsRepository.getFunctionTracking(users.getId(), projectId, featureName, functionStatus);
            return featuresListDTOS;
        }
        return null;

    }

    @Override
    public List<FunctionTrackingSubmitListDTO> showListFunctionInTraking(List<Integer> projectId, List<Integer> milestoneId) {
        return functionsRepository.showListFunctionInTracking(projectId, milestoneId);
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
    public String exportFunctions(List<FunctionsDTO> functions) throws IOException {
        LinkedHashMap<String, String> excelFunctionHeader = generateFunctionExcelHeader();
        List<Map> functionMap = toListOfMap(functions);
        return excelService.exportExcel(excelFunctionHeader, functionMap, "function");
    }

    @Override
    public Integer getProjectByFunction(Integer functionId) {
        return functionsRepository.findProjectId(functionId);
    }
}