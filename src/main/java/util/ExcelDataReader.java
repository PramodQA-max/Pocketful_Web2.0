package util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.util.*;

public class ExcelDataReader {

    // Return all values in first column
    public static List<String> getColumnData(String fileName, String sheetName) {
        List<String> data = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(fileName);
             Workbook wb = new XSSFWorkbook(fis)) {

            Sheet sheet = wb.getSheet(sheetName);

            // Loop through all rows starting from row 1 (assuming row 0 is header)
            for (int i = 1; i <=sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null && row.getCell(0) != null) {
                    data.add(row.getCell(0).toString());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }

//    public static Map<String,String> getcoloum(String filename,String SheetName) {
//        Map<String, String> data = new HashMap<>();
//
//        try (FileInputStream fis = new FileInputStream(filename);
//             Workbook wb = new XSSFWorkbook(fis)) {
//            Sheet sheet = wb.getSheet(SheetName);
//            Row row = sheet.getRow(0);
//            for (int i = 0; i <= row.getLastCellNum(); i++) {
//                for (int j = 1; j <= row.getLastCellNum(); j++) {
//                    data.put(row.getCell(i).toString(), row.getCell(j).toString());
//                }
//            }
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return  data;
//    }


    public static Object[][] getData(String file,String sheetname)
    {
        List<String> emails= ExcelDataReader.getColumnData(file,sheetname);
        System.out.println(emails);
        Object[][] data= new Object[emails.size()][1];
        for(int i=0;i<=emails.size()-1;i++)
        {
            data[i][0]=emails.get(i);
        }
        return data;
    }

}
