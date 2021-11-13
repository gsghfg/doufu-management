package com.zf.product.doufu.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zf.product.doufu.excel.SheetData;
import com.zf.product.doufu.model.Order;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WriteExcel {
    private static final Logger logger = LoggerFactory.getLogger(WriteExcel.class);
    static private String pathname;
    static private Workbook workbook;
    static private Sheet sheet1;

    /**
     * 使用栗子
     * WriteExcel excel = new WriteExcel("D:\\myexcel.xlsx");
     * excel.write(new String[]{"1","2"}, 0);//在第1行第1个单元格写入1,第一行第二个单元格写入2
     */
    public void write(String[] writeStrings, int rowNumber) throws Exception {
        //将内容写入指定的行号中
        Row row = sheet1.createRow(rowNumber);
        //遍历整行中的列序号
        for (int j = 0; j < writeStrings.length; j++) {
            //根据行指定列坐标j,然后在单元格中写入数据
            Cell cell = row.createCell(j);
            cell.setCellValue(writeStrings[j]);
        }
        OutputStream stream = new FileOutputStream(pathname);
        workbook.write(stream);
        stream.close();
    }


    public void writeNewExcel(List<SheetData> dataList) throws Exception {
        dataList.forEach(sheetData -> {
            String[] writeStrings = sheetData.getData();
            //将内容写入指定的行号中
            Row row = sheet1.createRow(sheetData.getRow());
            //遍历整行中的列序号
            for (int j = 0; j < writeStrings.length; j++) {
                //根据行指定列坐标j,然后在单元格中写入数据
                Cell cell = row.createCell(j);
                cell.setCellValue(writeStrings[j]);
            }
        });
        OutputStream stream = new FileOutputStream(pathname);
        workbook.write(stream);
        stream.close();
    }


    public WriteExcel(String excelPath, String sheetName) throws Exception {
        //在excelPath中需要指定具体的文件名(需要带上.xls或.xlsx的后缀)
        this.pathname = excelPath;
        String fileType = excelPath.substring(excelPath.lastIndexOf(".") + 1, excelPath.length());
        //创建文档对象
        if (fileType.equals("xls")) {
            //如果是.xls,就new HSSFWorkbook()
            workbook = new HSSFWorkbook();
        } else if (fileType.equals("xlsx")) {
            //如果是.xlsx,就new XSSFWorkbook()
            workbook = new XSSFWorkbook();
        } else {
            throw new Exception("文档格式后缀不正确!!！");
        }
        // 创建表sheet
        sheet1 = workbook.getSheet(sheetName) == null ? workbook.createSheet(sheetName) : workbook.getSheet(sheetName);
    }

    public static void main(String[] args) {
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

//            WriteExcel excel = new WriteExcel(path, today);
//            excel.writeNewExcel(sheetDataList);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}

