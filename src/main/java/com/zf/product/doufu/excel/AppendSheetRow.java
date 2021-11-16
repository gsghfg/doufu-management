package com.zf.product.doufu.excel;

import com.zf.product.doufu.model.base.SheetRow;
import org.apache.poi.xssf.usermodel.XSSFRow;

interface AppendSheetRow<T extends SheetRow> {
    void doAppend(XSSFRow row);
}