package com.zf.product.doufu.model;

import java.util.List;

/**
 * 订单内容
 */
public class OrderContent {
    //订单时间
    private String date;
    //订单概要
    private String brief;
    //订单详情
    private List<Order> orderDetails;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public List<Order> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<Order> orderDetails) {
        this.orderDetails = orderDetails;
    }
}
