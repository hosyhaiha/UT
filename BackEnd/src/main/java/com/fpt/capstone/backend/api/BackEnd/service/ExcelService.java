package com.fpt.capstone.backend.api.BackEnd.service;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface ExcelService {
    String exportExcel(LinkedHashMap<String, String> header, List<Map> tableData, String object) throws IOException;
    String uploadExcelToGS(String fileLocation,String object) throws IOException;
   void createRow(Row row, Workbook workbook,Integer cellIndex, String rowName );

//    Boolean importExcel(MultipartFile file);
}
