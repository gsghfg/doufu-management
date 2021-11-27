package com.zf.product.doufu.pdf;

import com.itextpdf.text.Font;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.zf.product.doufu.constants.PdfConstants;
import com.zf.product.doufu.model.Order;
import com.zf.product.doufu.utils.ListUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * @author clh
 */
public class PDFUtils {
    private static final Logger logger = LoggerFactory.getLogger(PDFUtils.class);

    public static BaseFont bfCN = null;
    public static Font titleFont = null;
    public static Font customerFont = null;
    public static Font contentFont = null;
    public static Font smallBoldFont = null;
    private static final ArrayList<Integer> DEFAULT_LAYOUT;
    private static final ArrayList<Integer> COUNT_COL_LAYOUT;
    private static final String[] TITLE_VALUES;
    private static final String[] HEADER_VALUES;
    //单元格跨行
    private static final Integer[] TITLE_ROW_LAYOUT;
    private static final Integer[] BRIEF_ROW_LAYOUT;
    private static final Integer[] HEADER_ROW_LAYOUT;
    //单元格跨列
    private static final Integer[] TITLE_COL_LAYOUT;
    private static final Integer[] BRIEF_COL_LAYOUT;
    private static final Integer[] HEADER_COL_LAYOUT;


    static {
        //
        HEADER_COL_LAYOUT = new Integer[]{1, 1, 1, 1, 1};
        BRIEF_COL_LAYOUT = new Integer[]{3, 2};
        TITLE_COL_LAYOUT = new Integer[]{5};
        //
        TITLE_ROW_LAYOUT = new Integer[]{1, 1, 1, 1, 1};
        BRIEF_ROW_LAYOUT = new Integer[]{1, 1, 1, 1, 1};
        HEADER_ROW_LAYOUT = new Integer[]{1, 1, 1, 1, 1};
        //
        COUNT_COL_LAYOUT = ListUtils.asList(new Integer[]{4, 1});
        DEFAULT_LAYOUT = ListUtils.asList(new Integer[]{1, 1, 1, 1, 1});
        //
        HEADER_VALUES = new String[]{"序号", "商品", "数量(斤)", "单价(元)", "金额(元)"};
        TITLE_VALUES = new String[]{PdfConstants.PDF_HEADER_CONTENT};
        try {
            bfCN = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", false);
            titleFont = new Font(bfCN, 20f);
            customerFont = new Font(bfCN, 14f);
            contentFont = new Font(bfCN, 12f);
            smallBoldFont = new Font(bfCN, 8f);
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] fontNames = e.getAvailableFontFamilyNames();
        for (String fontName : fontNames) {
            System.out.println(fontName);
        }
    }

    public static PdfPCell getAlignRightPdfCell(String value, Font font, float height) {
        Paragraph p = new Paragraph(value, font);
        p.setAlignment(Paragraph.ALIGN_CENTER);
        PdfPCell cellN = new PdfPCell(p);
        cellN.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        cellN.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cellN.setMinimumHeight(height);
        return cellN;
    }

    public static PdfPCell getAlignLeftPdfCell(String value, Font font, float height) {
        Paragraph p = new Paragraph(value, font);
        p.setAlignment(Paragraph.ALIGN_CENTER);
        PdfPCell cellN = new PdfPCell(p);
        cellN.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cellN.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cellN.setMinimumHeight(height);
        return cellN;
    }

    /**
     * 获取单行表格
     *
     * @param widthArr 单行表格内的单元格宽度
     * @param values   单行表格内的单元格内容
     * @param font     字体样式
     * @param height   单元格最小高度
     * @return
     */
    public static PdfPTable getExplain(float[] widthArr, String[] values, Font font, float height) {
        PdfPTable table = new PdfPTable(widthArr);
        for (int i = 0; i < widthArr.length; i++) {
            table.addCell(getPdfCellNoBorder(values[i], font, height));
        }
        return table;
    }

    /**
     * 普通单元格
     *
     * @param value  单元格内容
     * @param font   字体
     * @param height 单元格最小高度
     * @return
     */
    public static PdfPCell getNormalPdfCell(String value, Font font, float height) {
        Paragraph p = new Paragraph(value, font);
        p.setAlignment(Paragraph.ALIGN_CENTER);
        PdfPCell cellN = new PdfPCell(p);
        cellN.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cellN.setVerticalAlignment(PdfPCell.ALIGN_JUSTIFIED_ALL);
        cellN.setMinimumHeight(height);
        return cellN;
    }

    public static PdfPCell getTitlePdfCell(String value, Font font, float height) {
        Paragraph p = new Paragraph(value, font);
        p.setAlignment(Paragraph.ALIGN_CENTER);
        PdfPCell cellN = new PdfPCell(p);
        cellN.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cellN.setVerticalAlignment(PdfPCell.ALIGN_TOP);
        cellN.setMinimumHeight(height);
        return cellN;
    }


    /**
     * 普通单元格
     *
     * @param value  单元格内容
     * @param font   字体
     * @param height 单元格最小高度
     * @return
     */
    public static PdfPCell getPdfCellNoBorder(String value, Font font, float height) {
        Paragraph p = new Paragraph(value, font);
        p.setAlignment(Paragraph.ALIGN_CENTER);
        PdfPCell cellN = new PdfPCell(p);
        cellN.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        cellN.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cellN.setMinimumHeight(height);
        cellN.setBorder(0);
        return cellN;
    }


    public static PdfPTable getRowColTable(float[] f, ArrayList<ArrayList<String>> values, ArrayList<ArrayList<Integer>> rows, ArrayList<ArrayList<Integer>> cols, float height) {
        PdfPTable table = new PdfPTable(f);
        for (int i = 0; i < values.size(); i++) {
            ArrayList<String> v = values.get(i);
            ArrayList<Integer> r = rows.get(i);
            ArrayList<Integer> c = cols.get(i);
            for (int j = 0; j < v.size(); j++) {
                PdfPCell cell = null;
                if (i == 0) {
                    cell = getTitlePdfCell(v.get(j), titleFont, height + 2);
                } else if (i == 1 && j == 0) {
                    cell = getAlignLeftPdfCell(v.get(j), customerFont, height);
                } else if (i == 1 && j == 1) {
                    cell = getAlignRightPdfCell(v.get(j), customerFont, height);
                } else {
                    cell = getNormalPdfCell(v.get(j), contentFont, height);
                }
                if (r.get(j) > 1) {
                    cell.setRowspan(r.get(j));
                }
                if (c.get(j) > 1) {
                    cell.setColspan(c.get(j));
                }
                table.addCell(cell);
            }
        }
        return table;
    }

    public static PdfPTable getRowColTable(float[] f, ArrayList<ArrayList<PdfPCell>> values, ArrayList<ArrayList<Integer>> rows, ArrayList<ArrayList<Integer>> cols) {
        PdfPTable table = new PdfPTable(f);
        for (int i = 0; i < values.size(); i++) {
            ArrayList<PdfPCell> v = values.get(i);
            ArrayList<Integer> r = rows.get(i);
            ArrayList<Integer> c = cols.get(i);
            for (int j = 0; j < v.size(); j++) {
                PdfPCell cell = v.get(j);
                if (r.get(j) > 1) {
                    cell.setRowspan(r.get(j));
                }
                if (c.get(j) > 1) {
                    cell.setColspan(c.get(j));
                }
                table.addCell(cell);
            }
        }
        return table;
    }


    public static void createPdf(String path, ArrayList<ArrayList<String>> contentList, String[] briefValue) throws FileNotFoundException, DocumentException {
        Document document = new Document(new RectangleReadOnly(340f, 396f),28,28,28,28);
        File file = new File(path);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        PdfWriter.getInstance(document, new FileOutputStream(new File(path)));

        document.open();
        float[] f1 = {80f, 110f, 120f, 120f, 120f};

        //添加内容
        //values list
        ArrayList<ArrayList<String>> values = new ArrayList<>();
        values.addAll(getTableValues(contentList, briefValue));

        //单元格行
        ArrayList<ArrayList<Integer>> rows = new ArrayList<>();
        rows.addAll(getTableRows(contentList));
        //单元格列
        ArrayList<ArrayList<Integer>> cols = new ArrayList<>();
        cols.addAll(getTableCols(contentList));
        //添加内容
        document.add(getRowColTable(f1, values, rows, cols, 20f));

        //添加备注
        ArrayList<ArrayList<PdfPCell>> remarkValues = new ArrayList<>();
        remarkValues.add(ListUtils.asList(new PdfPCell[]{getRemarkPefCell()}));
        ArrayList<ArrayList<Integer>> remarkRows = new ArrayList<>();
        remarkRows.addAll(ListUtils.asList(2, new Integer[]{1, 1, 1, 1, 1}));
        ArrayList<ArrayList<Integer>> remarkCols = new ArrayList<>();
        remarkCols.addAll(ListUtils.asList(2, new Integer[]{5}));
        document.add(getRowColTable(f1, remarkValues, remarkRows, remarkCols));

        //添加表底
        String[] e2 = {"本单据一式三份(白,红,黄)"};
        float[] e3 = {300f};
        document.add(getExplain(e3, e2, smallBoldFont, 12f));

        logger.info("create pdf:{}", path);
        document.close();
    }


    private static PdfPCell getRemarkPefCell() {
        Paragraph p = new Paragraph("备注: \n电话：133xxxxxxxx", smallBoldFont);
        p.setAlignment(Paragraph.ALIGN_CENTER);
        PdfPCell cellN = new PdfPCell(p);
        cellN.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cellN.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cellN.setMinimumHeight(16f);
        return cellN;
    }


    private static ArrayList<ArrayList<Integer>> getTableCols(ArrayList<ArrayList<String>> contentList) {
        ArrayList<ArrayList<Integer>> colsLayout = new ArrayList<>();
        colsLayout.add(ListUtils.asList(TITLE_COL_LAYOUT));
        colsLayout.add(ListUtils.asList(BRIEF_COL_LAYOUT));
        colsLayout.add(ListUtils.asList(HEADER_COL_LAYOUT));
        colsLayout.addAll(getContentColsLayout(contentList));
        return colsLayout;
    }

    private static ArrayList<ArrayList<Integer>> getTableRows(ArrayList<ArrayList<String>> contentList) {
        ArrayList<ArrayList<Integer>> rowsLayout = new ArrayList<>();
        rowsLayout.add(ListUtils.asList(TITLE_ROW_LAYOUT));
        rowsLayout.add(ListUtils.asList(BRIEF_ROW_LAYOUT));
        rowsLayout.add(ListUtils.asList(HEADER_ROW_LAYOUT));
        rowsLayout.addAll(getContentRowsLayout(contentList));
        return rowsLayout;
    }

    private static ArrayList<ArrayList<String>> getTableValues(ArrayList<ArrayList<String>> contentList, String[] briefValue) {
        ArrayList<ArrayList<String>> values = new ArrayList<>();
        // add title
        values.add(ListUtils.asList(TITLE_VALUES));
        // add brief
        values.add(ListUtils.asList(briefValue));
        // add header
        values.add(ListUtils.asList(HEADER_VALUES));
        // add content
        if (contentList != null && contentList.size() > 0) {
            values.addAll(contentList);
        }
        return values;
    }

    private static ArrayList<ArrayList<Integer>> getContentColsLayout(ArrayList<ArrayList<String>> contentList) {
        ArrayList<ArrayList<Integer>> contentCols = new ArrayList<>();
        if (contentList != null && contentList.size() > 0) {
            contentCols.addAll(ListUtils.asList(contentList.size() - 1, DEFAULT_LAYOUT));
        }
        contentCols.add(COUNT_COL_LAYOUT);
        return contentCols;
    }

    private static ArrayList<ArrayList<Integer>> getContentRowsLayout(ArrayList<ArrayList<String>> contentList) {
        ArrayList<ArrayList<Integer>> contentRows = new ArrayList<>();
        if (contentList != null && contentList.size() > 0) {
            contentRows.addAll(ListUtils.asList(contentList.size(), DEFAULT_LAYOUT));
        } else {
            contentRows.add(DEFAULT_LAYOUT);
        }
        return contentRows;
    }


    private static final NumberFormat formatter = new DecimalFormat("0.00");

    public static void printOrder(String orderDay, Order order) {
        String[] briefValue = {"客户：" + order.getCustomerName(), orderDay};
        ArrayList<ArrayList<String>> contentList = new ArrayList<>();
        final double[] charge = {0d};
        final int[] index = {1};
        order.getGoodsList().forEach(goods -> {
            Double produceCharge = goods.getPrice() * goods.getAmount();
            ArrayList<String> data = ListUtils.asList(new String[]{String.valueOf(index[0]), goods.getName(), String.valueOf(goods.getPrice()), String.valueOf(goods.getAmount()), formatter.format(produceCharge)});
            charge[0] += produceCharge;
            index[0]++;
            contentList.add(data);
        });
        contentList.add(ListUtils.asList(new String[]{"合计:", formatter.format(charge[0])}));
        try {
            String tempPdfPath = PdfConstants.TEMP_DIRECT + File.separator + orderDay + "." + order.getCustomerName() + ".pdf";
            createPdf(tempPdfPath, contentList, briefValue);
//            PDFPrintUtils.printPdf(tempPdfPath, PdfConstants.PRINTER_NAME);
        } catch (Exception e) {
            logger.error("print orderDay:" + orderDay + " ,customerName:" + order.getCustomerName() + " order error", e);
        }
    }

}



