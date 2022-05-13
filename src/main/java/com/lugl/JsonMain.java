package com.lugl;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author luguanlin
 * 2022/5/11 17:35
 */
public class JsonMain implements JavaFastString{
    public static void main(String[] args) {
        GlobalVar.initData();
        buildDatas();
    }

    public static void buildDatas() {
        File file = new File(GlobalVar.excelPath);
        File[] files = file.listFiles((dir, name) -> name.endsWith(".xlsx"));
        for (File file1 : files) {
            buildData(file1);
        }
    }

    public static boolean buildData(File file) {
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            if (workbook.getNumberOfSheets() <= 0) {
                System.err.println(file.getName() + " have not sheet");
                return false;
            }
            XSSFSheet sheet = workbook.getSheetAt(0);
            if (sheet == null || sheet.getLastRowNum() <= 3) {
                System.err.println(file.getName() + " have not sheet");
                return false;
            }
            int cellSize = sheet.getRow(0).getLastCellNum();
            ClassBuilder classBuilder = JavaBeanMain.getClassBuilder(file);
            classBuilder.initFieldInfos();
            for (int i = 5; i <= sheet.getLastRowNum(); i++) {
                String cellData = SheetUtil.getCellData(sheet, i, 0);
                if (cellData.equals("")) {
                    break;
                }
                Map<String, String> cellMap = new HashMap<>();
                for (int j = 0; j < cellSize; j++) {
                    try {
                        String name = classBuilder.fields.get(j)[0];
                        cellData = SheetUtil.getCellData(sheet, i, j);
                        cellMap.put(name, cellData);
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                classBuilder.addData(cellMap);
            }
            classBuilder.createJson();
            return true;
        } catch (IOException | InvalidFormatException e) {
            e.printStackTrace();
            return false;
        }
    }


}
