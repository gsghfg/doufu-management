package com.zf.product.doufu.excel.data;

import java.util.HashMap;
import java.util.Map;

public class ProductSheetCell implements ReadCell {
    private Map<String, CellData> readCell = new HashMap<>();

    private static CellData name = new CellData("name", "名称", String.class, "");
    private static CellData price = new CellData("price", "价格", String.class, "");

    public ProductSheetCell() {
        readCell.put(name.getCellSheetName(), name);
        readCell.put(price.getCellSheetName(), price);
    }


    public static CellData name() {
        return name;
    }

    public static CellData price() {
        return price;
    }

    @Override
    public CellData get(String excelCellName) {
        return readCell.get(excelCellName);
    }
}
