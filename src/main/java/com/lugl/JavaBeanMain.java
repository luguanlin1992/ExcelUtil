package com.lugl;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author luguanlin
 * 2022/5/11 11:22
 */
public class JavaBeanMain {

    public static void main(String[] args) {
        GlobalVar.initData();
        buildCommons();
        buildDatas();
    }


    public static void buildCommons() {
        File file = new File(GlobalVar.excelPath + "/template");
        if (!file.exists()) {
            return;
        }
        File[] files = file.listFiles((dir, name) -> name.endsWith(".xlsx"));
        for (File file1 : files) {
            buildCommon(file1);
        }
    }

    public static void buildCommon(File file) {
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            if (workbook.getNumberOfSheets() <= 0) {
                System.out.println(file.getName() + " have not sheet");
                return;
            }
            XSSFSheet sheet = workbook.getSheetAt(0);
            int rowSize = sheet.getLastRowNum();
            for (int i = 0; i <= rowSize; i++) {
                XSSFRow row = sheet.getRow(i);
                String className = ClassBuilder.className(SheetUtil.getCellData(row, 0));
                ClassBuilder classBuilder = new ClassBuilder(className);
                int cellSize = row.getLastCellNum();
                for (int j = 1; j < cellSize; j++) {
                    String cellData = SheetUtil.getCellData(row, j);
                    String[] split = cellData.split(":");
                    List<String> list = Arrays.asList(split);
                    Collections.reverse(list);
                    if (list.size() < 3) {
                        System.err.println("common error:" + className);
                        continue;
                    }
                    StringBuilder typeBuilder = new StringBuilder();
                    for (int i1 = 2; i1 < list.size(); i1++) {
                        typeBuilder.append(list.get(i1)).append(":");
                    }
                    typeBuilder.setLength(typeBuilder.length() - 1);
                    String type = typeBuilder.toString();
                    String name = list.get(1);
                    String desc = list.get(0);
                    classBuilder.addField(name, type, desc, "");
                }
                classBuilder.build();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
    }

    public static void buildDatas() {
        File file = new File(GlobalVar.excelPath);
        File[] files = file.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".xlsx");
            }
        });
        for (File file1 : files) {
            buildData(file1);
        }
    }

    public static String classDesc(String filename) {
        String temp = filename.split("\\.")[0];
        String[] names = temp.split("_");
        return names.length == 1 ? "" : names[0];
    }

    public static boolean buildData(File file) {
        ClassBuilder classBuilder = getClassBuilder(file);
        if (classBuilder != null) {
            return classBuilder.build();
        }
        return false;
    }

    public static ClassBuilder getClassBuilder(File file) {
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            if (workbook.getNumberOfSheets() <= 0) {
                System.err.println(file.getName() + " have not sheet");
                return null;
            }
            XSSFSheet sheet = workbook.getSheetAt(0);
            if (sheet == null || sheet.getLastRowNum() <= 3) {
                System.err.println(file.getName() + " have not sheet");
                return null;
            }
            ClassBuilder classBuilder = new ClassBuilder(ClassBuilder.className(file.getName()));
            classBuilder.setDesc(classDesc(file.getName()));
            XSSFRow row0 = sheet.getRow(0);
            int cellNum = row0.getLastCellNum();
            for (int i = 0; i < cellNum; i++) {
                String name = SheetUtil.getCellData(row0, i);
                String type = SheetUtil.getCellData(sheet, 1, i);
                String belong = SheetUtil.getCellData(sheet, 2, i);
                String place = SheetUtil.getCellData(sheet, 3, i);
                String desc = SheetUtil.getCellData(sheet, 4, i);
                if (place.equals("all") || place.equals("server")) {
                    classBuilder.addField(name, type, desc, belong);
                }
            }
            return classBuilder;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
        return null;
    }

}
