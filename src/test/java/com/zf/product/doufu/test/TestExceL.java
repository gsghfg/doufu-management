package com.zf.product.doufu.test;

import com.alibaba.fastjson.JSONObject;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;


public class TestExceL {

    public static void main(String[] args) throws Exception {
        updateTest();
    }

    public static void updateTest() throws Exception {
        String path = "/Users/jhyang/IdeaProjects/doufu/doufu-management/src/main/resources/2021-11-07.xlsx";
        FileInputStream fs = new FileInputStream(path);  //获取d://test.xls
        XSSFWorkbook wb = new XSSFWorkbook(fs);
        XSSFSheet sheet = wb.getSheet("2021-11-07");  //获取到工作表，因为一个excel可能有多个工作表
        int lastRowNum = sheet.getPhysicalNumberOfRows();
        for (int rowNum = 0; rowNum < lastRowNum; rowNum++) {
            List<String> dataList = new ArrayList<>();
            XSSFRow row = sheet.getRow(rowNum);  //获取第一行（excel中的行默认从0开始，所以这就是为什么，一个excel必须有字段列头），即，字段列头，便于赋值
            short lastCellNum = row.getLastCellNum();
            for (short i = 0; i < lastCellNum; i++) {
                XSSFCell cell = row.getCell((int) i);
//                System.out.println("cellNum:" + i + ", value:" + cell.getStringCellValue());
                String count = cell.getStringCellValue();
                if ("0".equals(count)) {
                    cell.setCellValue(String.valueOf(rowNum + 0.5));
                }
                dataList.add(cell.getStringCellValue());

            }
            System.out.println("rowNum:" + rowNum + ", data:" + JSONObject.toJSONString(dataList.toArray(new String[0])));
        }


//        System.out.println(sheet.getLastRowNum() + " " + row.getLastCellNum());  //分别得到最后一行的行号，和一条记录的最后一个单元格

        FileOutputStream out = new FileOutputStream(path);  //向d://test.xls中写数据
        out.flush();
        wb.write(out);
        out.close();
//        System.out.println(row.getPhysicalNumberOfCells() + " " + row.getLastCellNum());
    }


    public static void appendTest() throws Exception {
        String path = "/Users/jhyang/IdeaProjects/doufu/doufu-management/src/main/resources/2021-11-07.xlsx";
        FileInputStream fs = new FileInputStream(path);  //获取d://test.xls
        XSSFWorkbook wb = new XSSFWorkbook(fs);
        XSSFSheet sheet = wb.getSheetAt(0);  //获取到工作表，因为一个excel可能有多个工作表
        XSSFRow row = sheet.getRow(0);  //获取第一行（excel中的行默认从0开始，所以这就是为什么，一个excel必须有字段列头），即，字段列头，便于赋值
        row.getLastCellNum();


        System.out.println(sheet.getPhysicalNumberOfRows() + " " + row.getLastCellNum());  //分别得到最后一行的行号，和一条记录的最后一个单元格

        FileOutputStream out = new FileOutputStream(path);  //向d://test.xls中写数据
        row = sheet.createRow((short) (sheet.getPhysicalNumberOfRows() + 1)); //在现有行号后追加数据
        row.createCell(0).setCellValue("leilei"); //设置第一个（从0开始）单元格的数据
        row.createCell(1).setCellValue(24); //设置第二个（从0开始）单元格的数据


        out.flush();
        wb.write(out);
        out.close();
        System.out.println(row.getPhysicalNumberOfCells() + " " + row.getLastCellNum());
    }

}   