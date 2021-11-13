package com.zf.product.doufu.excel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zf.product.doufu.constants.ExcelConstants;
import com.zf.product.doufu.excel.data.CustomerSheetCell;
import com.zf.product.doufu.excel.data.ProductSheetCell;
import com.zf.product.doufu.model.Customer;
import com.zf.product.doufu.model.Order;
import com.zf.product.doufu.model.Product;
import com.zf.product.doufu.model.base.SheetRow;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class WriteSheet {
    private static final Logger logger = LoggerFactory.getLogger(WriteSheet.class);
    private static String path = "/Users/jhyang/IdeaProjects/doufu/doufu-management/src/main/resources/2021-11-07.xlsx";
    private static String sheetCustomer = "客户";
    private static String sheetProduct = "客户";


    public static void main(String[] args) {
//        List<Customer> beforeCustomerList = ReadSheet.readCustomer();
//        System.out.println("customerList before:" + JSONObject.toJSONString(beforeCustomerList));
//        List<Customer> customerList = new ArrayList<>();
//        Customer customer = new Customer();
//        customer.setName("张三");
//        customer.setPhone("166xxxxxxxx");
//        customer.setAddress("张三的地址");
//        customer.setRowNumber(1);
//        customerList.add(customer);
//        Customer customer2 = new Customer();
//        customer2.setName("李四");
//        customer2.setPhone("166xxxx7119");
//        customer2.setAddress("李四换地方");
//        customer2.setRowNumber(2);
//        customerList.add(customer2);
//        updateCustomer(customerList);
//        List<Customer> afterCustomerList = ReadSheet.readCustomer();
//        System.out.println("customerList after:" + JSONObject.toJSONString(afterCustomerList));

        String str = "[{\"customerName\":\"张三\",\"goodsList\":[{\"productName\":\"豆腐\"},{\"productName\":\"面筋\"},{\"productName\":\"豆芽\"},{\"productName\":\"凉皮\"}]},{\"customerName\":\"李四\",\"goodsList\":[{\"productName\":\"豆腐\"},{\"productName\":\"面筋\"},{\"productName\":\"豆芽\"},{\"productName\":\"凉皮\"}]},{\"customerName\":\"王五\",\"goodsList\":[{\"productName\":\"豆腐\"},{\"productName\":\"面筋\"},{\"productName\":\"豆芽\"},{\"productName\":\"凉皮\"}]}]";
        List<Order> orderList = JSON.parseArray(str, Order.class);
        List<SheetData> sheetDataList = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String today = dateFormat.format(new Date());
        for (int i = 0; i < orderList.size(); i++) {
            logger.info("orderList:{}", JSONObject.toJSONString(orderList));
            List<String> dataList = new ArrayList<>();
            Order order = orderList.get(i);
            dataList.add(order.getCustomerName());
            order.getGoodsList().forEach(goods -> {
                dataList.add(goods.getProductName());
                dataList.add(goods.getProductAmount() == null ? "0" : String.valueOf(goods.getProductAmount()));
            });
            SheetData sheetData = new SheetData();
            sheetData.setRow(i);
            sheetData.setData(dataList.toArray(new String[0]));
            sheetDataList.add(sheetData);
        }
        logger.info("excelSheetDataList:{}", JSONObject.toJSONString(sheetDataList));

        try {
            String path = "/Users/jhyang/IdeaProjects/doufu/doufu-management/src/main/resources/" + today + ".xlsx";
            writeNewExcel(path, today, sheetDataList);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static void updateCustomer(List<Customer> customerList) {
        Map<Integer, Customer> customerMap = customerList.stream().collect(
                Collectors.toMap(Customer::getRowNumber, t -> t));
        updateRow((UpdateSheetRow<Customer>) row -> {
            int rowNum = row.getRowNum();
            Customer customer1 = null;
            if ((customer1 = customerMap.get(rowNum)) != null) {
                XSSFCell nameCell = row.getCell(CustomerSheetCell.name().getCellNum());
                nameCell.setCellValue(customer1.getName());
                XSSFCell addressCell = row.getCell(CustomerSheetCell.address().getCellNum());
                addressCell.setCellValue(customer1.getAddress());
                XSSFCell phoneCell = row.getCell(CustomerSheetCell.phone().getCellNum());
                phoneCell.setCellValue(customer1.getPhone());
            }
        }, ExcelConstants.CUSTOMER_SHEET_NAME);
    }

    public static void updateProduct(List<Product> customerList) {
        Map<Integer, Product> customerMap = customerList.stream().collect(
                Collectors.toMap(Product::getRowNumber, t -> t));
        updateRow((UpdateSheetRow<Customer>) row -> {
            int rowNum = row.getRowNum();
            Product product = null;
            if ((product = customerMap.get(rowNum)) != null) {
                XSSFCell nameCell = row.getCell(ProductSheetCell.name().getCellNum());
                nameCell.setCellValue(product.getName());
                XSSFCell addressCell = row.getCell(ProductSheetCell.price().getCellNum());
                addressCell.setCellValue(product.getPrice());
            }
        }, ExcelConstants.PRODUCT_SHEET_NAME);
    }


    public static <T extends SheetRow> void updateRow(UpdateSheetRow<T> updateSheetRow, String sheetName) {
        try {
            FileInputStream fs = new FileInputStream(ExcelConstants.BASIC_DATA_PATH);
            XSSFWorkbook wb = new XSSFWorkbook(fs);
            XSSFSheet sheet = wb.getSheet(sheetName);
            int lastRowNum = sheet.getLastRowNum();
            for (int rowNum = 0; rowNum < lastRowNum; rowNum++) {
                XSSFRow row = sheet.getRow(rowNum);
                updateSheetRow.doUpdate(row);
            }
            FileOutputStream out = new FileOutputStream(ExcelConstants.BASIC_DATA_PATH);
            out.flush();
            wb.write(out);
            out.close();
        } catch (Exception e) {
            logger.error("updateRow error.", e);
        }
    }


    public static void writeNewExcel(String filePath, String sheetName, List<SheetData> dataList) {
        try {
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet(sheetName);
            dataList.forEach(sheetData -> {
                String[] writeStrings = sheetData.getData();
                //将内容写入指定的行号中
                Row row = sheet.createRow(sheetData.getRow());
                //遍历整行中的列序号
                for (int j = 0; j < writeStrings.length; j++) {
                    //根据行指定列坐标j,然后在单元格中写入数据
                    Cell cell = row.createCell(j);
                    cell.setCellValue(writeStrings[j]);
                }
            });
            OutputStream stream = new FileOutputStream(filePath);
            workbook.write(stream);
            stream.close();
        } catch (Exception e) {
            logger.error("writeNewExcel error.", e);
        }
    }


}

interface UpdateSheetRow<T extends SheetRow> {
    void doUpdate(XSSFRow row);
}
