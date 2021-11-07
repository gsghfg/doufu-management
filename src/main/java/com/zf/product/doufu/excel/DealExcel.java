package com.zf.product.doufu.excel;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;

public class DealExcel {

    private static final Logger logger = LoggerFactory.getLogger(DealExcel.class);
    static private String path;
    static private Workbook workbook;
    static private Sheet sheet1;

    private XSSFSheet sheet;
    private XSSFWorkbook sheets;

    public XSSFWorkbook getSheets() {
        return sheets;
    }

    DealExcel(String filePath) {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(filePath);
            sheets = new XSSFWorkbook(fileInputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
