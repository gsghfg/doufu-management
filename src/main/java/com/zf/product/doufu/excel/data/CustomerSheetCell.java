package com.zf.product.doufu.excel.data;

import java.util.HashMap;
import java.util.Map;

public class CustomerSheetCell implements ReadCell {
    private Map<String, CellData> readCell = new HashMap<>();

    private static CellData name = new CellData("name", "姓名", String.class, "");
    private static CellData phone = new CellData("phone", "电话", String.class, "");
    private static CellData address = new CellData("address", "地址", String.class, "");

    public CustomerSheetCell() {
        readCell.put(name.getCellSheetName(), name);
        readCell.put(phone.getCellSheetName(), phone);
        readCell.put(address.getCellSheetName(), address);
    }


    public static CellData name() {
        return name;
    }

    public static CellData phone() {
        return phone;
    }

    public static CellData address() {
        return address;
    }

    @Override
    public CellData get(String excelCellName) {
        return readCell.get(excelCellName);
    }
}
