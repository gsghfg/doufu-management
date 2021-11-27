package com.zf.product.doufu.model;

import com.zf.product.doufu.model.base.SheetRow;
import com.zf.product.doufu.utils.TableUtils;

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
    private String charge;

    public String getCharge() {
        if (price == null || amount == null) {
            return "0";
        } else {
            return TableUtils.formatter.format(price * amount);
        }
    }


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