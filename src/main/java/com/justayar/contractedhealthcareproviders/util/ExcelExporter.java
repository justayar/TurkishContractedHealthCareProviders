package com.justayar.contractedhealthcareproviders.util;

import com.justayar.contractedhealthcareproviders.bean.HealthCareProvider;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ExcelExporter {

    private static String[] columns = {"Kurum Adı", "İl", "İlçe", "Adres", "Telefon", "Sorgulama Tipi", "Kurum Tipi", "Anadolu Sigorta", "Allianz", "Axa"};
    private static final String INCLUDED_COLUMN_VALUE="Evet";
    private static final String NOT_INCLUDED_COLUMN_VALUE="Hayır";
    public String createReport(String sheetName, List<HealthCareProvider> healthCareProviderList,boolean isCompareTable) throws IOException {

        // new HSSFWorkbook() for generating `.xls` file
        String today;
        try (Workbook workbook = new XSSFWorkbook()) {

            // Create a Sheet
            Sheet sheet = workbook.createSheet(sheetName);

            // Create a Font for styling header cells
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 14);

            // Create a CellStyle with the font
            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);

            // Create a Row
            Row headerRow = sheet.createRow(0);

            // Create cells
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerCellStyle);
            }


            // Create Other rows and cells with employees data
            int rowNum = 1;
            for (HealthCareProvider healthCareProvider : healthCareProviderList) {
                Row row = sheet.createRow(rowNum++);

                row.createCell(0)
                        .setCellValue(healthCareProvider.getOrganizationName());

                row.createCell(1)
                        .setCellValue(healthCareProvider.getCityName());

                row.createCell(2)
                        .setCellValue(healthCareProvider.getCountryName());

                row.createCell(3)
                        .setCellValue(healthCareProvider.getAddress());

                row.createCell(4)
                        .setCellValue(healthCareProvider.getPhone());

                row.createCell(5)
                        .setCellValue(healthCareProvider.getOrganizationType());

                if (isCompareTable) {
                    String includedInAnadoluInsurance = healthCareProvider.isIncludedInAnadoluInsurance() ? INCLUDED_COLUMN_VALUE : NOT_INCLUDED_COLUMN_VALUE;

                    row.createCell(6)
                            .setCellValue(includedInAnadoluInsurance);

                    String includedInAllianzInsurance = healthCareProvider.isIncludedInAllianzInsurance() ? INCLUDED_COLUMN_VALUE : NOT_INCLUDED_COLUMN_VALUE;

                    row.createCell(7)
                            .setCellValue(includedInAllianzInsurance);

                    String includedInAxaInsurance = healthCareProvider.isIncludedInAxaInsurance() ? INCLUDED_COLUMN_VALUE : NOT_INCLUDED_COLUMN_VALUE;

                    row.createCell(8)
                            .setCellValue(includedInAxaInsurance);
                }
            }

            // Resize all columns to fit the content size
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            SimpleDateFormat sm = new SimpleDateFormat("dd-MM-yyyy");
            today = sm.format(new Date());

            // Write the output to a file
            FileOutputStream fileOut = new FileOutputStream(sheetName + "_" + today + ".xlsx");
            workbook.write(fileOut);
            fileOut.close();
        }

        return sheetName + "_" + today + ".xlsx";
    }
}
