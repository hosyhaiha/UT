package com.fpt.capstone.backend.api.BackEnd.service.impl;

import com.fpt.capstone.backend.api.BackEnd.service.ExcelService;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExcelServiceImpl implements ExcelService {

    @Override
    public String exportExcel(LinkedHashMap<String, String> excelHeader, List<Map> data, String object) throws IOException {
        Workbook functionData = new XSSFWorkbook();
        Sheet sheet = functionData.createSheet("Sheet1");
        Integer headerIndex = 0;
        CellStyle headerStyle = functionData.createCellStyle();
        XSSFFont font = ((XSSFWorkbook) functionData).createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 11);
        font.setBold(true);
        headerStyle.setFont(font);

        Row header = sheet.createRow(0);

        for (String value : excelHeader.values()) {
            Cell headerCell = header.createCell(headerIndex);
            headerCell.setCellValue(value);
            headerCell.setCellStyle(headerStyle);
            sheet.setColumnWidth(headerIndex, 5000);
            headerIndex++;
        }

        for (Integer i = 0; i < data.size(); i++) {
            Map datum = data.get(i);
            Row dataRow = sheet.createRow(i + 1);
            headerIndex = 0;
            for (String key : excelHeader.keySet()) {
                String value = datum.get(key) == null ? "" : datum.get(key).toString();
                Cell dataRowCell = dataRow.createCell(headerIndex);
                dataRowCell.setCellValue(value);
                headerIndex++;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());

        File currDir = new File(String.valueOf(this.getClass().getResourceAsStream("ExportFile/" + object + "/.")));
        String path = currDir.getAbsolutePath();
        String fileLocation = path.substring(0, path.length() - 1) + timeStamp + "-export-" + object + "-data.xlsx";
        FileOutputStream outputStream = new FileOutputStream(fileLocation);
        functionData.write(outputStream);
        // functionData.close();
        //Upload gs
        // uploadExcelToGS(fileLocation,object);
        //Xoa file vua tao
//        currDir.deleteOnExit();

        //tra ve url
        return uploadExcelToGS(fileLocation, object);
    }

    @Override
    public String uploadExcelToGS(String fileLocation, String object) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
        String bucketName = "slpm";
        String filePath = fileLocation;
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream is = classLoader.getResourceAsStream("ancient-ceiling-352503-4ad907700d34.json");
        Credentials credentials = GoogleCredentials.fromStream(is);
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        String fileName = timeStamp + "-export-" + object + "-data";
        BlobId blobId = BlobId.of(bucketName, fileName);
        BlobInfo blobInfo = storage.create(
                BlobInfo
                        .newBuilder(blobId)
                        .setContentType("application/vnd.ms-excel")
                        .build(), Files.readAllBytes(Paths.get(filePath))
        );

        return blobInfo.getMediaLink();
    }

    @Override
    public void createRow(Row row, Workbook workbook, Integer cellIndex, String rowName) {
        CellStyle headerStyle = workbook.createCellStyle();
        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 11);
        font.setBold(true);
        headerStyle.setFont(font);
        Cell headerCell = row.createCell(cellIndex);
        headerCell.setCellStyle(headerStyle);
        headerCell.setCellValue(rowName);
    }
}
