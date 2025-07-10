package Util;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Exceldatareader {

    public static Object[][] getExcelData(String filePath, String sheetname) throws IOException {
        FileInputStream fis = new FileInputStream(filePath);
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheet(sheetname);
        if (sheet == null) {
            System.out.println("Available sheets in the Excel file:");
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                System.out.println(" - " + workbook.getSheetName(i));
            }
            workbook.close();
            fis.close();
            throw new RuntimeException("Sheet '" + sheetname + "' does not exist in file: " + filePath);
        }
        int rowcount = sheet.getPhysicalNumberOfRows();
        int colcount = sheet.getRow(0).getPhysicalNumberOfCells();

        Object[][] data = new Object[rowcount - 1][colcount];

        for (int i = 1; i < rowcount; i++) {
            Row row = sheet.getRow(i);
            for (int j = 0; j < colcount; j++) {
                data[i - 1][j] = row.getCell(j).toString();
            }
        }
        workbook.close();
        fis.close();
        return data;
    }
}
