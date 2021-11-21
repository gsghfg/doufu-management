package com.zf.product.doufu.model;

import java.util.List;

/**
 * 订单文件
 */
public class OrderFile {
    //文件路径
    private String filePath;
    //订单月份
    private String orderMonth;
    //订单内容
    private List<OrderContent> orderContents;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getOrderMonth() {
        return orderMonth;
    }

    public void setOrderMonth(String orderMonth) {
        this.orderMonth = orderMonth;
    }

    public List<OrderContent> getOrderContents() {
        return orderContents;
    }

    public void setOrderContents(List<OrderContent> orderContents) {
        this.orderContents = orderContents;
    }
}
