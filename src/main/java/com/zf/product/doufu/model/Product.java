package com.zf.product.doufu.model;

import com.zf.product.doufu.model.base.SheetRow;

public class Product extends SheetRow {
    private String name;
    private Double price;

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
}