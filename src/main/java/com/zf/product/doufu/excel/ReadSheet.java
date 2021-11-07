package com.zf.product.doufu.excel;

import com.alibaba.fastjson.JSONObject;
import com.zf.product.doufu.excel.data.CellData;
import com.zf.product.doufu.excel.data.CustomerSheetCell;
import com.zf.product.doufu.excel.data.ProductSheetCell;
import com.zf.product.doufu.excel.data.ReadCell;
import com.zf.product.doufu.model.Customer;
import com.zf.product.doufu.model.Product;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ReadSheet {
    private XSSFSheet sheet;
    private XSSFWorkbook sheets;

    public XSSFWorkbook getSheets() {
        return sheets;
    }

    ReadSheet(String filePath) {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(filePath);
            sheets = new XSSFWorkbook(fileInputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String path = "/Users/jhyang/IdeaProjects/doufu/doufu-management/src/main/resources/FirstTests.xlsx";
        ReadSheet data = new ReadSheet(path);
        XSSFWorkbook workbook = data.getSheets();
        Iterator<XSSFSheet> sheetIterator = workbook.xssfSheetIterator();
        List<Customer> customerList = null;
        List<Product> productList = null;
        while (sheetIterator.hasNext()) {
            XSSFSheet sheet = sheetIterator.next();
            String name = sheet.getSheetName();
            System.out.println("name:" + name);
            if ("客户".equals(name)) {
                customerList = readCustomer(sheet);
                System.out.println("customerList:" + JSONObject.toJSONString(customerList));
            } else if ("商品".equals(name)) {
                productList = readProduct(sheet);
                System.out.println("productList:" + JSONObject.toJSONString(productList));
            }
        }
    }

    public static List<Customer> readCustomer(XSSFSheet sheet) {
        Customer a = null;
        List<Customer> aList = new ArrayList<Customer>();
        initHeader(sheet.getRow(0), new CustomerSheetCell());
        for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
            XSSFRow Row = sheet.getRow(rowNum);
            if (Row != null) {
                //判断这行记录是否存在
                if (Row.getLastCellNum() < 1 || "".equals(getValue(Row.getCell(1)))) {
                    continue;
                }
                //获取每一行
                a = new Customer();
                a.setName(getValue(Row.getCell(CustomerSheetCell.name().getCellNum())));
                a.setPhone(getValue(Row.getCell(CustomerSheetCell.phone().getCellNum())));
                a.setAddress(getValue(Row.getCell(CustomerSheetCell.address().getCellNum())));
                a.setRowNumber(rowNum);
                aList.add(a);
            }
        }
        return aList;
    }

    private static <T extends ReadCell> void initHeader(XSSFRow header, T t) {
        short headerLastCellNum = header.getLastCellNum();
        for (short i = 0; i < headerLastCellNum; i++) {
            String cellName = header.getCell(i).getStringCellValue();
            if (StringUtils.isNotBlank(cellName)) {
                CellData cellData = null;
                cellData = t.get(cellName);
                if (cellData != null) {
                    cellData.setCellNum(new Integer(i));
                }
            }
        }
    }


    public static List<Product> readProduct(XSSFSheet sheet) {
        Product a = null;
        List<Product> aList = new ArrayList<Product>();
        initHeader(sheet.getRow(0), new ProductSheetCell());
        for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
            XSSFRow row = sheet.getRow(rowNum);
            if (row != null) {
                //判断这行记录是否存在
                if (row.getLastCellNum() < 1 || "".equals(getValue(row.getCell(1)))) {
                    continue;
                }
                //获取每一行
                a = new Product();
                a.setName(getValue(row.getCell(ProductSheetCell.name().getCellNum())));
                a.setPrice(Double.valueOf(getValue(row.getCell(ProductSheetCell.price().getCellNum()))));
                a.setRowNumber(rowNum);
                aList.add(a);
            }
        }
        return aList;
    }

//    private static void initReadProductHeader(XSSFRow header) {
//        short headerLastCellNum = header.getLastCellNum();
//        for (short i = 0; i < headerLastCellNum; i++) {
//            String cellName = header.getCell(i).getStringCellValue();
//            if (StringUtils.isNotBlank(cellName)) {
//                CellData cellData = ProductSheetCell.get(cellName);
//                if (cellData != null) {
//                    cellData.setCellNum(new Integer(i));
//                }
//            }
//        }
//    }


    private static String getValue(Cell cell) {
        CellType cellType = cell.getCellTypeEnum();
        String value = "";
        switch (cellType) {
            case NUMERIC:
                value = String.valueOf(cell.getNumericCellValue());
                break;
            case _NONE:
            case ERROR:
            case BLANK:
            case FORMULA:
                break;
            case STRING:
                value = cell.getStringCellValue().trim();
                break;
            case BOOLEAN:
                value = String.valueOf(cell.getBooleanCellValue());
                break;
        }
        return value.trim();
    }
}
