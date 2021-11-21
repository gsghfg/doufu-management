package com.zf.product.doufu.model;

import com.zf.product.doufu.model.base.SheetRow;

public class Goods extends SheetRow {
    public Goods(String name, Double price, Double amount) {
        this.name = name;
        this.price = price;
        this.amount = amount;
    }

    public Goods() {
    }

    private String name;
    private Double price;
    private Double amount;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}