package com.zf.product.doufu.pdf;

import com.itextpdf.text.pdf.PdfPCell;

class ContentCell {
    private String value;
    private PdfPCell cell;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public PdfPCell getCell() {
        return cell;
    }

    public void setCell(PdfPCell cell) {
        this.cell = cell;
    }

    public ContentCell(String value, PdfPCell cell) {
        this.value = value;
        this.cell = cell;
    }
}