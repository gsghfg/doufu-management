package com.zf.product.doufu.excel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zf.product.doufu.constants.ExcelConstants;
import com.zf.product.doufu.excel.data.CustomerSheetCell;
import com.zf.product.doufu.excel.data.ProductSheetCell;
import com.zf.product.doufu.model.Customer;
import com.zf.product.doufu.model.Goods;
import com.zf.product.doufu.model.Order;
import com.zf.product.doufu.model.Product;
import com.zf.product.doufu.model.base.SheetRow;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class SheetWriter {
    private static final Logger logger = LoggerFactory.getLogger(SheetWriter.class);
    private static String path = "/Users/jhyang/IdeaProjects/doufu/doufu-management/src/main/resources/2021-11-07.xlsx";
    private static String sheetCustomer = "客户";
    private static String sheetProduct = "客户";



    public SheetWriter() {
    }

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

//        testAppendCustomer();

//        testAppendProduct();
//        testWriteOrder();
        testWriteOrder2();
    }

    public static void testAppendCustomer() {
        SheetReader.readCustomer();
        for (int i = 1; i < 10; i++) {
            Customer customer = new Customer();
            customer.setName("张三" + i);
            customer.setPhone("166xxxxxxxx1" + i);
            customer.setAddress("张三的地址" + i);
            appendCustomer(customer);
        }
    }

    public static void testAppendProduct() {
        SheetReader.readProduct();
        for (int i = 1; i < 10; i++) {
            Product product = new Product();
            product.setName("doufu" + i);
            product.setPrice(new Double(i));
            appendProduct(product);
        }
    }

    public static void testWriteOrder2() {
        List<Order> orderList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Order order = new Order();
            order.setCustomerName("张三" + i);
            List<Goods> goodsList = new ArrayList<>();
            for (int j = 0; j < 10; j++) {
                Goods goods = new Goods();
                goods.setName("商品" + j);
                goods.setPrice(4.3d);
                goods.setAmount(Double.valueOf(i * j));
                goodsList.add(goods);
            }
            order.setGoodsList(goodsList);
            orderList.add(order);
        }
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(2021, 11, 13); // 实际的calendar对象所表示的日期为2016年6月1日
//        calendar.getTime();
        String strDate = "2021-11-16";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = sdf.parse(strDate);
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(date);
            writeOrder(orderList, date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public static void testWriteOrder() {
        String str = "[{\"customerName\":\"张三\",\"goodsList\":[{\"productName\":\"豆腐\"},{\"productName\":\"面筋\"},{\"productName\":\"豆芽\"},{\"productName\":\"凉皮\"}]},{\"customerName\":\"李四\",\"goodsList\":[{\"productName\":\"豆腐\"},{\"productName\":\"面筋\"},{\"productName\":\"豆芽\"},{\"productName\":\"凉皮\"}]},{\"customerName\":\"王五\",\"goodsList\":[{\"productName\":\"豆腐\"},{\"productName\":\"面筋\"},{\"productName\":\"豆芽\"},{\"productName\":\"凉皮\"}]}]";
        List<Order> orderList = JSON.parseArray(str, Order.class);
        List<SheetData> sheetDataList = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat monthFormat = new SimpleDateFormat("yyyyMM");
        String today = dateFormat.format(new Date());
        String month = monthFormat.format(new Date());
        for (int i = 0; i < orderList.size(); i++) {
            logger.info("orderList:{}", JSONObject.toJSONString(orderList));
            List<String> dataList = new ArrayList<>();
            Order order = orderList.get(i);
            dataList.add(order.getCustomerName());
            order.getGoodsList().forEach(goods -> {
                dataList.add(goods.getName());
                dataList.add(goods.getAmount() == null ? "0" : String.valueOf(goods.getAmount()));
            });
            SheetData sheetData = new SheetData();
            sheetData.setRow(i);
            sheetData.setData(dataList.toArray(new String[0]));
            sheetDataList.add(sheetData);
        }
        logger.info("excelSheetDataList:{}", JSONObject.toJSONString(sheetDataList));

        try {
            String path = "/Users/jhyang/IdeaProjects/doufu/doufu-management/src/main/resources/order-" + month + ".xlsx";
            saveOrUpdateSheet(path, "2021-11-16", sheetDataList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeOrder(List<Order> orderList, Date orderTime) {
        List<SheetData> sheetDataList = new ArrayList<>();
        String orderDay = ExcelConstants.DATE_FORMAT.format(orderTime);
        String orderMonth = ExcelConstants.MONTH_FORMAT.format(orderTime);
        int rowNum = 0;
        for (int i = 0; i < orderList.size(); i++) {
            logger.info("orderList:{}", JSONObject.toJSONString(orderList));
            List<String> dataList = new ArrayList<>();
            Order order = orderList.get(i);
            for (Goods goods : order.getGoodsList()) {
                if (StringUtils.isBlank(goods.getName()) || goods.getAmount() == null || goods.getAmount() <= 0d) {
                    continue;
                }
                dataList.add(goods.getName());
                dataList.add(String.valueOf(goods.getPrice()));
                dataList.add(String.valueOf(goods.getAmount()));
            }
            if (!dataList.isEmpty()) {
                dataList.add(0, order.getCustomerName());
                SheetData sheetData = new SheetData();
                sheetData.setRow(order.getRowNumber() == null ? rowNum++ : order.getRowNumber());
                sheetData.setData(dataList.toArray(new String[0]));
                sheetDataList.add(sheetData);
            }
        }
        logger.info("excelSheetDataList:{}", JSONObject.toJSONString(sheetDataList));

        try {
            saveOrUpdateSheet(String.format(ExcelConstants.ORDER_DATA_PATH_TEMPLATE, orderMonth), orderDay, sheetDataList);
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

    public static void appendCustomer(Customer customer) {
        appendRow((AppendSheetRow<Customer>) row -> {
            XSSFCell nameCell = row.createCell(CustomerSheetCell.name().getCellNum());
            nameCell.setCellValue(customer.getName());
            XSSFCell addressCell = row.createCell(CustomerSheetCell.address().getCellNum());
            addressCell.setCellValue(customer.getAddress());
            XSSFCell phoneCell = row.createCell(CustomerSheetCell.phone().getCellNum());
            phoneCell.setCellValue(customer.getPhone());
        }, ExcelConstants.CUSTOMER_SHEET_NAME);
    }

    public static void appendProduct(Product product) {
        appendRow((AppendSheetRow<Product>) row -> {
            XSSFCell nameCell = row.createCell(ProductSheetCell.name().getCellNum());
            nameCell.setCellValue(product.getName());
            XSSFCell addressCell = row.createCell(ProductSheetCell.price().getCellNum());
            addressCell.setCellValue(product.getPrice());
        }, ExcelConstants.PRODUCT_SHEET_NAME);
    }

    public static void updateProduct(List<Product> customerList) {
        Map<Integer, Product> customerMap = customerList.stream().collect(
                Collectors.toMap(Product::getRowNumber, t -> t));
        updateRow((UpdateSheetRow<Product>) row -> {
            if (row != null) {
                System.out.println(row.getRowNum());
                int rowNum = row.getRowNum();
                Product product = null;
                if ((product = customerMap.get(rowNum)) != null) {
                    XSSFCell nameCell = row.getCell(ProductSheetCell.name().getCellNum());
                    nameCell.setCellValue(product.getName());
                    XSSFCell addressCell = row.getCell(ProductSheetCell.price().getCellNum());
                    addressCell.setCellValue(product.getPrice());
                }
            } else {

            }

        }, ExcelConstants.PRODUCT_SHEET_NAME);
    }


    public static <T extends SheetRow> void updateRow(UpdateSheetRow<T> updateSheetRow, String sheetName) {
        try {
            FileInputStream fs = new FileInputStream(ExcelConstants.BASIC_DATA_PATH);
            XSSFWorkbook wb = new XSSFWorkbook(fs);
            XSSFSheet sheet = wb.getSheet(sheetName);
            int lastRowNum = sheet.getPhysicalNumberOfRows();
            System.out.println("lastRowNum:" + lastRowNum);
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

    public static <T extends SheetRow> void appendRow(AppendSheetRow<T> appendSheetRow, String sheetName) {
        try {
            FileInputStream fs = new FileInputStream(ExcelConstants.BASIC_DATA_PATH);
            XSSFWorkbook wb = new XSSFWorkbook(fs);
            XSSFSheet sheet = wb.getSheet(sheetName);
            int lastRowNum = sheet.getPhysicalNumberOfRows();
            XSSFRow row = sheet.createRow(lastRowNum + 1);
            appendSheetRow.doAppend(row);
            FileOutputStream out = new FileOutputStream(ExcelConstants.BASIC_DATA_PATH);
            out.flush();
            wb.write(out);
            out.close();
        } catch (Exception e) {
            logger.error("updateRow error.", e);
        }
    }

    public static void deleteRow(String sheetName, int rowNum) {
        try {
            FileInputStream fs = new FileInputStream(ExcelConstants.BASIC_DATA_PATH);
            XSSFWorkbook wb = new XSSFWorkbook(fs);
            XSSFSheet sheet = wb.getSheet(sheetName);
            XSSFRow row = sheet.getRow(rowNum);
            sheet.removeRow(row);
            FileOutputStream out = new FileOutputStream(ExcelConstants.BASIC_DATA_PATH);
            out.flush();
            wb.write(out);
            out.close();
        } catch (Exception e) {
            logger.error("updateRow error.", e);
        }
    }


    public static void createExcel(String filePath, String sheetName, List<SheetData> dataList) {
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

    public static void saveOrUpdateSheet(String filePath, String sheetName, List<SheetData> dataList) {
        try {

            File file = new File(filePath);
            if (file.exists()) {
                updateExcel(filePath, sheetName, dataList);
            } else {
                createExcel(filePath, sheetName, dataList);
            }
            logger.info("save sheet, path:{}, sheetName:{}, dataList:{}", filePath, sheetName, JSONObject.toJSONString(dataList));
        } catch (Exception e) {
            logger.error("saveOrUpdateSheet error.", e);
        }
    }

    public static void updateExcel(String filePath, String sheetName, List<SheetData> dataList) {
        try {
            FileInputStream fs = new FileInputStream(filePath);
            XSSFWorkbook wb = new XSSFWorkbook(fs);
            XSSFSheet sheet = wb.getSheet(sheetName);
            if (sheet == null) {
                sheet = wb.createSheet(sheetName);
            }
            XSSFSheet finalSheet = sheet;
            dataList.forEach(sheetData -> {
                String[] writeStrings = sheetData.getData();
                //将内容写入指定的行号中
                Row row = finalSheet.createRow(sheetData.getRow());
                //遍历整行中的列序号
                for (int j = 0; j < writeStrings.length; j++) {
                    //根据行指定列坐标j,然后在单元格中写入数据
                    Cell cell = row.createCell(j);
                    cell.setCellValue(writeStrings[j]);
                }
            });
            FileOutputStream out = new FileOutputStream(filePath);
            out.flush();
            wb.write(out);
            out.close();
        } catch (Exception e) {
            logger.error("updateExcel error.", e);
        }
    }


}






