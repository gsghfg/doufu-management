package com.zf.product.doufu.excel;

import com.alibaba.fastjson.JSONObject;
import com.zf.product.doufu.constants.ExcelConstants;
import com.zf.product.doufu.excel.data.CellData;
import com.zf.product.doufu.excel.data.CustomerSheetCell;
import com.zf.product.doufu.excel.data.ProductSheetCell;
import com.zf.product.doufu.excel.data.ReadCell;
import com.zf.product.doufu.model.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

public class SheetReader {
    private static final Logger logger = LoggerFactory.getLogger(SheetReader.class);

    private XSSFWorkbook sheets;

    public XSSFWorkbook getSheets() {
        return sheets;
    }

    SheetReader(String filePath) {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(filePath);
            sheets = new XSSFWorkbook(fileInputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<OrderFile> readOrderFiles(String directoryPath) {
        File directory = new File(directoryPath);
        File[] files = directory.listFiles();
        List<OrderFile> orderFileList = new ArrayList<>();
        for (File file : files) {
            if (!file.isDirectory()) {
                String fileName = file.getName();
                String type = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
                String name = fileName.substring(0, fileName.lastIndexOf("."));
                if ("xlsx".equals(type) && name.startsWith("order-")) {
                    orderFileList.add(readOrderFile(file));
                }
            }
        }
        logger.info("readOrderFiles result:{}", JSONObject.toJSONString(orderFileList));
        return orderFileList;
    }

    /**
     * 读取订单目录下的所有订单日期
     *
     * @param directoryPath
     * @return
     */
    public static Map<String, String[]> readOrderDays(String directoryPath) {
        File directory = new File(directoryPath);
        File[] files = directory.listFiles();
        Map<String, String[]> result = new HashMap<>();
        for (File file : files) {
            if (!file.isDirectory()) {
                String fileName = file.getName();
                String type = fileName.substring(fileName.lastIndexOf(".") + 1);
                String name = fileName.substring(0, fileName.lastIndexOf("."));
                if ("xlsx".equals(type) && name.startsWith("order-")) {
                    String orderMonth = name.substring(fileName.lastIndexOf("-") + 1);
                    SheetReader data = new SheetReader(file.getPath());
                    XSSFWorkbook workbook = data.getSheets();
                    Iterator<XSSFSheet> sheetIterator = workbook.xssfSheetIterator();
                    List<String> dayList = new ArrayList<>();
                    while (sheetIterator.hasNext()) {
                        XSSFSheet sheet = sheetIterator.next();
                        dayList.add(sheet.getSheetName());
                    }
                    result.put(orderMonth, dayList.toArray(new String[0]));
                }
            }
        }
        logger.info("readOrderDays result:{}", JSONObject.toJSONString(result));
        return result;
    }

    private static OrderFile readOrderFile(File file) {
        OrderFile orderFile = new OrderFile();
        orderFile.setFilePath(file.getPath());
        String fileName = file.getName().substring(0, file.getName().lastIndexOf("."));
        String orderMonth = fileName.substring(fileName.lastIndexOf("-") + 1, fileName.length());
        orderFile.setOrderMonth(orderMonth);
        SheetReader data = new SheetReader(file.getPath());
        XSSFWorkbook workbook = data.getSheets();
        Iterator<XSSFSheet> sheetIterator = workbook.xssfSheetIterator();
        List<OrderContent> orderContents = new ArrayList<>();
        while (sheetIterator.hasNext()) {
            XSSFSheet sheet = sheetIterator.next();
            orderContents.add(readOrderContent(sheet));
        }
        orderFile.setOrderContents(orderContents);
        return orderFile;
    }


    public static OrderContent readOrderContent(Date date) {
        String orderDay = ExcelConstants.DATE_FORMAT.format(date);
        String orderMonth = ExcelConstants.MONTH_FORMAT.format(date);
        SheetReader data = new SheetReader(String.format(ExcelConstants.ORDER_DATA_PATH_TEMPLATE, orderMonth));
        XSSFWorkbook workbook = data.getSheets();
        return readOrderContent(workbook.getSheet(orderDay));
    }

    public static OrderContent readOrderContent(String orderDay) {
        String[] array = orderDay.split("-");
        String orderMonth = array[0] + array[1];
        SheetReader data = new SheetReader(String.format(ExcelConstants.ORDER_DATA_PATH_TEMPLATE, orderMonth));
        XSSFWorkbook workbook = data.getSheets();
        return readOrderContent(workbook.getSheet(orderDay));
    }

    private static OrderContent readOrderContent(XSSFSheet sheet) {
        OrderContent orderContent = new OrderContent();
        orderContent.setDate(sheet.getSheetName());
        List<Order> orderDetails = new ArrayList<>();
        Order order = null;
        for (int rowNum = 0; rowNum <= sheet.getPhysicalNumberOfRows(); rowNum++) {
            XSSFRow row = sheet.getRow(rowNum);
            if (row != null) {
                //判断这行记录是否存在
                if (row.getLastCellNum() < 1 || "".equals(getValue(row.getCell(1)))) {
                    continue;
                }
                //获取每一行
                order = new Order();
                order.setRowNumber(rowNum);
                order.setCustomerName(getValue(row.getCell(0)));
                List<Goods> goodsList = new ArrayList<>();
                Goods goods = null;
                for (int cellNum = 1; cellNum <= row.getPhysicalNumberOfCells(); cellNum++) {
                    int index = cellNum % 3;
                    if (index == 1) {
                        goods = new Goods();
                        String name = getValue(row.getCell(cellNum), String.class);
                        goods.setName(name);
                    } else if (index == 2) {
                        Double price = Double.valueOf(row.getCell(cellNum).getStringCellValue());
                        goods.setPrice(price);
                    } else if (index == 0) {
                        Double amount = Double.valueOf(row.getCell(cellNum).getStringCellValue());
                        goods.setAmount(amount);
                        goodsList.add(goods);
                    }
                }
                order.setGoodsList(goodsList);
                orderDetails.add(order);
            }
        }
        orderContent.setOrderDetails(orderDetails);
        orderContent.setBrief(getOrderBrief(orderDetails));
        return orderContent;
    }

    private static String getOrderBrief(List<Order> orderDetails) {
        List<Goods> goodsList = new ArrayList<>();
        orderDetails.stream().map(Order::getGoodsList).forEach(list -> {
            goodsList.addAll(list);
        });
        //商品，总数量，总金额
        Map<String, Double[]> briefMap = new HashMap<>();
        goodsList.stream().forEach(goods -> {
            String name = goods.getName();
            Double price = goods.getPrice();
            Double amount = goods.getAmount();
            Double[] amountChargeArray = null;
            if ((amountChargeArray = briefMap.get(name)) == null) {
                amountChargeArray = new Double[]{amount, amount * price};
            } else {
                amountChargeArray[0] += amount;
                amountChargeArray[1] += (amount * price);
            }
            briefMap.put(name, amountChargeArray);
        });
        logger.info("briefMap:{}", JSONObject.toJSONString(briefMap));
        StringBuffer buffer = new StringBuffer();
        final Double[] charge = {0d};
        briefMap.forEach((productName, amountChargeArray) -> {
            buffer.append(productName).append(" 下单：").append(amountChargeArray[0]).append("斤，应收款：").append(formatter.format(amountChargeArray[1])).append("\n");
            charge[0] += amountChargeArray[1];
        });
        buffer.append("共计：").append(charge[0]).append("元");
        logger.info("brief:{}", buffer.toString());
        return buffer.toString();
    }

    private static final NumberFormat formatter = new DecimalFormat("0.00");

    public static void main(String[] args) {
//        String path = "/Users/jhyang/IdeaProjects/doufu/doufu-management/src/main/resources/order-202111.xlsx";
//        String path = "/Users/jhyang/IdeaProjects/doufu/doufu-management/src/main/resources/order";

//        readOrderFiles(path);

//        SimpleDateFormat ORDER_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
//        try {
//            Date date = ORDER_DATE_FORMAT.parse("2021-11-23");
//            System.out.println(JSONObject.toJSONString(readOrderContent(date)));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
        System.out.println(SheetReader.readOrderDays(ExcelConstants.ORDER_DIRECTORY_PATH));

    }

    public static void readBaseDataTest() {
        String path = "/Users/jhyang/IdeaProjects/doufu/doufu-management/src/main/resources/FirstTests.xlsx";
        SheetReader data = new SheetReader(path);
        XSSFWorkbook workbook = data.getSheets();
        Iterator<XSSFSheet> sheetIterator = workbook.xssfSheetIterator();
        List<Customer> customerList = null;
        List<Product> productList = null;
        while (sheetIterator.hasNext()) {
            XSSFSheet sheet = sheetIterator.next();
            String name = sheet.getSheetName();
            System.out.println("name:" + name);
            if (ExcelConstants.CUSTOMER_SHEET_NAME.equals(name)) {
                customerList = readCustomer(sheet);
                System.out.println("customerList:" + JSONObject.toJSONString(customerList));
            } else if (ExcelConstants.PRODUCT_SHEET_NAME.equals(name)) {
                productList = readProduct(sheet);
                System.out.println("productList:" + JSONObject.toJSONString(productList));
            }
        }
    }

    public static List<Customer> readCustomer() {
        SheetReader data = new SheetReader(ExcelConstants.BASIC_DATA_PATH);
        XSSFSheet sheet = data.getSheets().getSheet(ExcelConstants.CUSTOMER_SHEET_NAME);
        return readCustomer(sheet);
    }

    public static List<Product> readProduct() {
        SheetReader data = new SheetReader(ExcelConstants.BASIC_DATA_PATH);
        XSSFSheet sheet = data.getSheets().getSheet(ExcelConstants.PRODUCT_SHEET_NAME);
        return readProduct(sheet);
    }


    public static List<Customer> readCustomer(XSSFSheet sheet) {
        Customer a = null;
        List<Customer> aList = new ArrayList<Customer>();
        initHeader(sheet.getRow(0), new CustomerSheetCell());
        for (int rowNum = 1; rowNum <= sheet.getPhysicalNumberOfRows(); rowNum++) {
            XSSFRow row = sheet.getRow(rowNum);
            if (row != null) {
                //判断这行记录是否存在
                if (row.getLastCellNum() < 1 || "".equals(getValue(row.getCell(1)))) {
                    continue;
                }
                //获取每一行
                a = new Customer();
                a.setName(getValue(row.getCell(CustomerSheetCell.name().getCellNum())));
                a.setPhone(getValue(row.getCell(CustomerSheetCell.phone().getCellNum())));
                a.setAddress(getValue(row.getCell(CustomerSheetCell.address().getCellNum())));
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
        for (int rowNum = 1; rowNum <= sheet.getPhysicalNumberOfRows(); rowNum++) {
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

    public static <T> T getValue(Cell cell, Class<T> tClass) {
        if (cell == null) {
            return null;
        }
        CellType cellType = cell.getCellTypeEnum();
        T value = (T) "";
        switch (cellType) {
            case NUMERIC:
                value = (T) String.valueOf(cell.getNumericCellValue());
                break;
            case _NONE:
            case ERROR:
            case BLANK:
            case FORMULA:
                break;
            case STRING:
                value = (T) cell.getStringCellValue().trim();
                break;
            case BOOLEAN:
                value = (T) String.valueOf(cell.getBooleanCellValue());
                break;
        }
        return value;
    }
}
