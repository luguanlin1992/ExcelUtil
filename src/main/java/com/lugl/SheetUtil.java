package com.lugl;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import static org.apache.poi.ss.usermodel.CellType.*;

/**
 * @author luguanlin
 * 2022/5/11 12:19
 */
public class SheetUtil {
    public static final String getCellData(XSSFSheet sheet, int rowIndex, int cellIndex) {
        XSSFRow row = sheet.getRow(rowIndex);
        return row == null ? "" : getCellData(row, cellIndex);
    }

    public static final String getCellData(XSSFRow row, int cellIndex) {
        XSSFCell cell = row.getCell(cellIndex);
        if (cell == null) {
            return "";
        }
        if (cell.getCellType() == NUMERIC) {
            return String.valueOf((long) cell.getNumericCellValue()).trim();
        }
        if (cell.getCellType() == BOOLEAN) {
            return String.valueOf(cell.getBooleanCellValue()).trim();
        }
        if (cell.getCellType() == FORMULA) {
            try {
                return String.valueOf(cell.getStringCellValue()).trim();
            } catch (IllegalStateException e) {
                return String.valueOf((long) cell.getNumericCellValue()).trim();
            }
        }
        return cell.getStringCellValue().trim();
    }
}
