package com.zf.product.doufu.excel;

import com.zf.product.doufu.model.base.SheetRow;
import org.apache.poi.xssf.usermodel.XSSFRow;

interface UpdateSheetRow<T extends SheetRow> {
    void doUpdate(XSSFRow row);
}