package com.zf.product.doufu.constants;

import java.text.SimpleDateFormat;

public class ExcelConstants {
    public static final String CUSTOMER_SHEET_NAME = "客户";
    public static final String PRODUCT_SHEET_NAME = "商品";
    public static final String BASIC_DATA_PATH = "/Users/jhyang/IdeaProjects/doufu/doufu-management/src/main/resources/FirstTests.xlsx";
    public static final String ORDER_DATA_PATH_TEMPLATE = "/Users/jhyang/IdeaProjects/doufu/doufu-management/src/main/resources/order/order-%s.xlsx";
    public static final String ORDER_DIRECTORY_PATH = "/Users/jhyang/IdeaProjects/doufu/doufu-management/src/main/resources/order";

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat MONTH_FORMAT = new SimpleDateFormat("yyyyMM");

    public static void main(String[] args) {
        System.out.println(String.format(ORDER_DATA_PATH_TEMPLATE,"2021-11"));
    }
}
