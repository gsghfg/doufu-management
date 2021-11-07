package com.zf.product.doufu.excel.data;

public class CellData {
    private CellData() {
    }

    public CellData(String cellDataName, String cellSheetName, Class cellType, Object defaultValue) {
        this.cellDataName = cellDataName;
        this.cellSheetName = cellSheetName;
        this.cellType = cellType;
        this.defaultValue = defaultValue;
    }

    private String cellDataName;
    private String cellSheetName;
    private Class cellType;
    private Object defaultValue;
    private Integer cellNum;

    public String getCellDataName() {
        return cellDataName;
    }

    public void setCellDataName(String cellDataName) {
        this.cellDataName = cellDataName;
    }

    public String getCellSheetName() {
        return cellSheetName;
    }

    public void setCellSheetName(String cellSheetName) {
        this.cellSheetName = cellSheetName;
    }

    public Class getCellType() {
        return cellType;
    }

    public void setCellType(Class cellType) {
        this.cellType = cellType;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
    }

    public Integer getCellNum() {
        return cellNum;
    }

    public void setCellNum(Integer cellNum) {
        this.cellNum = cellNum;
    }
}
