package com.zf.product.doufu.model;

import com.zf.product.doufu.model.base.SheetRow;

import java.util.List;

public class Order extends SheetRow {
    private String customerName;
    private String orderBrief;
    private List<Goods> goodsList;

    public List<Goods> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<Goods> goodsList) {
        this.goodsList = goodsList;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getOrderBrief() {
        return orderBrief;
    }

    public void setOrderBrief(String orderBrief) {
        this.orderBrief = orderBrief;
    }
}