package com.zf.product.doufu.test;

import com.alibaba.fastjson.JSONObject;
import com.zf.product.doufu.model.Customer;
import com.zf.product.doufu.model.Goods;
import com.zf.product.doufu.model.Order;
import com.zf.product.doufu.model.Product;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.format.CellFormat;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.*;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


public class ExcelData {
    private XSSFSheet sheet;
    private XSSFWorkbook sheets;

    public XSSFWorkbook getSheets() {
        return sheets;
    }


    /**
     * 构造函数，初始化excel数据
     *
     * @param filePath  excel路径
     * @param sheetName sheet表名
     */
    ExcelData(String filePath, String sheetName) {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(filePath);
            XSSFWorkbook sheets = new XSSFWorkbook(fileInputStream);
            //获取sheet
            sheet = sheets.getSheet(sheetName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据行和列的索引获取单元格的数据
     *
     * @param row
     * @param column
     * @return
     */
    public String getExcelDateByIndex(int row, int column) {
        XSSFRow row1 = sheet.getRow(row);
        String cell = row1.getCell(column).toString();
        return cell;
    }

    /**
     * 根据某一列值为“******”的这一行，来获取该行第x列的值
     *
     * @param caseName
     * @param currentColumn 当前单元格列的索引
     * @param targetColumn  目标单元格列的索引
     * @return
     */
    public String getCellByCaseName(String caseName, int currentColumn, int targetColumn) {
        String operateSteps = "";
        //获取行数
        int rows = sheet.getPhysicalNumberOfRows();
        for (int i = 0; i < rows; i++) {
            XSSFRow row = sheet.getRow(i);
            String cell = row.getCell(currentColumn).toString();
            if (cell.equals(caseName)) {
                operateSteps = row.getCell(targetColumn).toString();
                break;
            }
        }
        return operateSteps;
    }

    //打印excel数据
    public void readExcelData() {
        //获取行数
        int rows = sheet.getPhysicalNumberOfRows();
        for (int i = 0; i < rows; i++) {
            //获取列数
            XSSFRow row = sheet.getRow(i);
            int columns = row.getPhysicalNumberOfCells();
            for (int j = 0; j < columns; j++) {
                String cell = row.getCell(j).toString();
                System.out.println(cell);
            }
        }
    }

    //测试方法
    public static void main(String[] args) {
//        ExcelData sheet1 = new ExcelData("resource/FirstTests.xlsx", "username");
//        //获取第二行第4列
//        String cell2 = sheet1.getExcelDateByIndex(1, 3);
//        //根据第3列值为“customer23”的这一行，来获取该行第2列的值
//        String cell3 = sheet1.getCellByCaseName("customer23", 2, 1);
//        System.out.println(cell2);
//        System.out.println(cell3);
        String path = "/Users/jhyang/IdeaProjects/doufu/doufu-management/src/main/resources/FirstTests.xlsx";
        ExcelData data = new ExcelData(path);
        XSSFWorkbook workbook = data.getSheets();
        Iterator<Sheet> sheetIterator = workbook.sheetIterator();
        List<Customer> customerList = null;
        List<Product> productList = null;
        while (sheetIterator.hasNext()) {
            Sheet sheet = sheetIterator.next();
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
        List<Order> orderList = initOrderList(customerList, productList);
        System.out.println("orderList:" + JSONObject.toJSONString(orderList));

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        String today = dateFormat.format(new Date());
        System.out.println("today:" + today);
        XSSFSheet sheet = workbook.getSheet(today);

        if (sheet != null) {
            removeRow(sheet, 1, sheet.getPhysicalNumberOfRows());
        } else {
            sheet = workbook.createSheet(today);
        }
        writeSheet(path, sheet, orderList);

    }

    public static void removeRow(XSSFSheet sheet, int startRow, int endRow) {
        for (int j = endRow; j >= startRow; j--) {
            sheet.removeRow(sheet.getRow(j));
        }
    }


    public static void writeSheet(String path, XSSFSheet sheet, List<Order> orderList) {
        XSSFWorkbook workbook = null;
        workbook = new XSSFWorkbook();
        //获取参数个数作为excel列数
        int columeCount = 6;
        //获取List size作为excel行数
        int rowCount = 20;
        //创建第一栏
        XSSFRow headRow = sheet.createRow(0);
        String[] titleArray = {"id", "name", "age", "email", "address", "phone"};
        for (int m = 0; m <= columeCount - 1; m++) {
            XSSFCell cell = headRow.createCell(m);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            sheet.setColumnWidth(m, 6000);
            XSSFCellStyle style = workbook.createCellStyle();
            XSSFFont font = workbook.createFont();
            font.setBold(true);
            short color = HSSFColor.RED.index;
            font.setColor(color);
            style.setFont(font);
            //填写数据
//            cell.setCellStyle(style);
            cell.setCellValue(titleArray[m]);

        }
        final int[] index = {0};
        //写入数据
        orderList.stream().forEach(order -> {
            List<Goods> goodsList = order.getGoodsList();
            XSSFRow row = sheet.createRow(index[0] + 1);
            for (int n = 0; n <= columeCount - 1; n++) {
                row.createCell(n);
                row.getCell(0).setCellValue(order.getCustomerName());
                int cell = 1;
                for (int i = 0; i < goodsList.size(); i++) {
                    Goods goods = goodsList.get(i);
                    (row.getCell(cell++) == null ? row.createCell(cell++) : row.getCell(cell++)).setCellValue(goods.getName());
                    (row.getCell(cell++) == null ? row.createCell(cell++) : row.getCell(cell++)).setCellValue(goods.getPrice());
                    (row.getCell(cell++) == null ? row.createCell(cell++) : row.getCell(cell++)).setCellValue(goods.getAmount() == null ? String.valueOf(0) : String.valueOf(goods.getAmount()));
                }
                index[0]++;
            }
        });

        //写到磁盘上
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File(path));
            workbook.write(fileOutputStream);
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<Order> initOrderList(List<Customer> customerList, List<Product> productList) {
        List<Order> orderList = new ArrayList<>();
        customerList.stream().forEach(customer -> {
            Order order = new Order();
            order.setCustomerName(customer.getName());
            List<Goods> goodsList = new ArrayList<>();
            productList.stream().forEach(product -> {
                Goods goods = new Goods();
                goods.setName(product.getName());
                goods.setPrice(product.getPrice());
                goodsList.add(goods);
            });
            order.setGoodsList(goodsList);
            orderList.add(order);
        });
        return orderList;
    }

    public static List<Customer> readCustomer(Sheet Sheet) {
        Customer a = null;
        List<Customer> aList = new ArrayList<Customer>();
        for (int rowNum = 1; rowNum <= Sheet.getPhysicalNumberOfRows(); rowNum++) {
            Row Row = Sheet.getRow(rowNum);
            if (Row != null) {
                //判断这行记录是否存在
                if (Row.getLastCellNum() < 1 || "".equals(getValue(Row.getCell(1)))) {
                    continue;
                }
                //获取每一行
                a = new Customer();
                a.setName(getValue(Row.getCell(0)));
                a.setPhone(getValue(Row.getCell(1)));
                a.setAddress(getValue(Row.getCell(2)));
                a.setRowNumber(rowNum);
                aList.add(a);
            }
        }
        return aList;
    }

    public static List<Product> readProduct(Sheet Sheet) {
        Product a = null;
        List<Product> aList = new ArrayList<Product>();
        for (int rowNum = 1; rowNum <= Sheet.getPhysicalNumberOfRows(); rowNum++) {
            Row Row = Sheet.getRow(rowNum);
            if (Row != null) {
                //判断这行记录是否存在
                if (Row.getLastCellNum() < 1 || "".equals(getValue(Row.getCell(1)))) {
                    continue;
                }
                //获取每一行
                a = new Product();
                a.setName(getValue(Row.getCell(0)));
                a.setPrice(Double.valueOf(getValue(Row.getCell(1))));
                a.setRowNumber(rowNum);
                aList.add(a);
            }
        }
        return aList;
    }

    private static String getValue(Cell cell) {
        int type = CellFormat.ultimateType(cell);
        if (type == Cell.CELL_TYPE_BOOLEAN) {
            return String.valueOf(cell.getBooleanCellValue());
        } else if (type == Cell.CELL_TYPE_NUMERIC) {
            return String.valueOf(cell.getNumericCellValue());
        } else if (type == Cell.CELL_TYPE_BLANK) {
            return "";
        } else {
            return cell.getStringCellValue().trim();
        }
    }

    ExcelData(String filePath) {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(filePath);
            sheets = new XSSFWorkbook(fileInputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}









