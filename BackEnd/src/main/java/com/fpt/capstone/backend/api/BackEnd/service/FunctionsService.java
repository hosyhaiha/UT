package com.fpt.capstone.backend.api.BackEnd.service;

import com.fpt.capstone.backend.api.BackEnd.dto.*;
import com.fpt.capstone.backend.api.BackEnd.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface FunctionsService {
    FunctionsDTO addFunction(FunctionsDTO functionsDTO) throws Exception;

    FunctionsDTO deleteFunction(int id);

    List<FunctionsDTO> showFunction();

    FunctionsDTO updateFunction(FunctionsDTO functionsDTO) throws Exception;

    FunctionsDTO findById(int id) throws Exception;

    Page<FunctionsDTO> listBy(List<Integer> projectID, List<Integer> featureId,  String name, String status, int page, int limit) throws Exception;

    public FunctionsDTO getFunctionDetail(int id);

    public List<FunctionsDTO> showFunctionList();

    List<ColumnTypeDTO> showColumns(String table, String field);

    List<Map> toListOfMap(List<FunctionsDTO> functions);

    Map<String, String> toMap(FunctionsDTO function);

    String exportFunctions(List<FunctionsDTO> functions) throws IOException;

    Map<String, String> generateFunctionExcelHeader();

    // Nhận về file từ Fe rồi đưa qua service đọc file để xử lý
    ResponseEntity<?> importFunctions(MultipartFile fileStream) throws IOException;

    List<FunctionListDTO> showFeatureList();

    List<FunctionListTrackingDTO> showTrackingFeature(Integer projectId, List<Integer> featureId, Users user, String functionStatus);

    List<FunctionTrackingSubmitListDTO> showListFunctionInTraking(List<Integer> projectId, List<Integer> milestoneId);

    Integer getProjectByFunction(Integer functionId);


}
