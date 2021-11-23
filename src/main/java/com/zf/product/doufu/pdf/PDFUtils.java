package com.zf.product.doufu.pdf;

import com.alibaba.fastjson.JSONObject;
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
    //    public static Font titleFont2 = null;
    public static Font normalFont = null;
    public static Font remarkFont = null;
    public static Font smallBoldFont = null;
    public static float[] width100 = {100f};
    public static float minHeight = 30f;
    private static final ArrayList<Integer> DEFAULT_LAYOUT;
    private static final ArrayList<Integer> COUNT_COL_LAYOUT;
    private static final String[] TITLE_VALUES;
    private static final String[] HEADER_VALUES;
    //    private static final String[] zero_count;
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
            titleFont = new Font(bfCN, 14f, Font.BOLD);
//            titleFont2 = new Font(bfCN, 18f, Font.BOLD);
            normalFont = new Font(bfCN, 8f);
            remarkFont = new Font(bfCN, 6f);
            smallBoldFont = new Font(bfCN, 6f);
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    /**
     * 获取标题
     *
     * @param value 标题内容
     * @param font  字体样式
     * @return
     */
//    public static PdfPTable getPdfTitle(String value, Font font) {
//        float[] width = {100f};
//        PdfPTable table = new PdfPTable(width);
//        Paragraph content = new Paragraph(value, font);
//        PdfPCell cellN = new PdfPCell(content);
//        cellN.setPadding(10f);
//        cellN.setBorder(0);
//        cellN.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
//        cellN.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
//        cellN.setMinimumHeight(30f);
//        table.addCell(cellN);
//        return table;
//    }
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
        cellN.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
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

    /**
     * 没有上下边框的单元格
     *
     * @param value
     * @param font
     * @return
     */
//    public static PdfPCell getPdfCellNoTB(String value, Font font) {
//        Paragraph p = new Paragraph(value, font);
//        p.setAlignment(Paragraph.ALIGN_CENTER);
//        PdfPCell cellN = new PdfPCell(p);
//        cellN.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
//        cellN.setMinimumHeight(30f);
//        cellN.setBorderWidthBottom(0);
//        cellN.setBorderWidthTop(0);
//        return cellN;
//    }

    /**
     * 没有上边框的单元格
     *
     * @param value
     * @param font
     * @return
     */
//    public static PdfPCell getPdfCellNoT(String value, Font font) {
//        Paragraph p = new Paragraph(value, font);
//        p.setAlignment(Paragraph.ALIGN_CENTER);
//        PdfPCell cellN = new PdfPCell(p);
//        cellN.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
//        cellN.setMinimumHeight(30f);
//        cellN.setBorderWidthTop(0);
//        return cellN;
//    }

    /**
     * 没有上边框的单元格
     *
     * @param value
     * @param font
     * @return
     */
//    public static PdfPCell getPdfCellNoB(String value, Font font) {
//        Paragraph p = new Paragraph(value, font);
//        p.setAlignment(Paragraph.ALIGN_CENTER);
//        PdfPCell cellN = new PdfPCell(p);
//        cellN.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
//        cellN.setMinimumHeight(30f);
//        cellN.setBorderWidthBottom(0);
//        return cellN;
//    }

    /**
     * 单元格内增加表格
     *
     * @param width
     * @param content
     * @param height
     * @param border
     * @return
     * @throws Exception
     */
//    public static PdfPTable getCellTable(float[] width, String[] content, float height, int border) throws Exception {
//        PdfPTable table = new PdfPTable(width);
//        for (int i = 0; i < width.length; i++) {
//            Paragraph p = new Paragraph(content[i], normalFont);
//            PdfPCell c = new PdfPCell(p);
//            c.setMinimumHeight(height);
//            c.setBorder(border);
//            table.addCell(c);
//        }
//        return table;
//    }

    /**
     * 生成跨行跨列的表格
     *
     * @param f
     * @param values
     * @param rows
     * @param cols
     * @return
     */
//    public static PdfPTable getRowColTable(float[] f, String[][] values, int[][] rows, int[][] cols, float height) {
//        PdfPTable table = new PdfPTable(f);
//        for (int i = 0; i < values.length; i++) {
//            String[] v = values[i];
//            int[] r = rows[i];
//            int[] c = cols[i];
//            for (int j = 0; j < v.length; j++) {
//                PdfPCell cell = getNormalPdfCell(v[j], normalFont, height);
//                if (r[j] > 1) {
//                    cell.setRowspan(r[j]);
//                }
//                if (c[j] > 1) {
//                    cell.setColspan(c[j]);
//                }
//                table.addCell(cell);
//            }
//        }
//        return table;
//    }
    public static PdfPTable getRowColTable(float[] f, ArrayList<ArrayList<String>> values, ArrayList<ArrayList<Integer>> rows, ArrayList<ArrayList<Integer>> cols, float height) {
        PdfPTable table = new PdfPTable(f);
        for (int i = 0; i < values.size(); i++) {
            ArrayList<String> v = values.get(i);
            ArrayList<Integer> r = rows.get(i);
            ArrayList<Integer> c = cols.get(i);
            for (int j = 0; j < v.size(); j++) {
                PdfPCell cell = null;
                if (i == 0) {
                    cell = getNormalPdfCell(v.get(j), titleFont, height);
                } else if (i == 1 && j == 0) {
                    cell = getAlignLeftPdfCell(v.get(j), normalFont, height);
                } else if (i == 1 && j == 1) {
                    cell = getAlignRightPdfCell(v.get(j), normalFont, height);
                } else {
                    cell = getNormalPdfCell(v.get(j), normalFont, height);
                }
                System.out.println("i:" + i + ", j:" + j + ", value:" + v.get(j));
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

//    public static void getPdf(String path) throws FileNotFoundException, DocumentException {
//        Document document = new Document(PageSize.A6.rotate());
//        PdfWriter.getInstance(document, new FileOutputStream(new File(path)));
//
//        document.open();
//        float[] f1 = {20f, 40f, 30f, 30f, 30f};
//
//        //单元格的值
//        String[] title = {"豆制品批发"};
//        String[] brief = {"订单编号:", "日期：2021-10-21"};
//        String[] header = {"编号", "商品", "数量(斤)", "单价(元)", "金额(元)"};
//        String[] v1 = {"1", "xx1", "xx1", "xx1", "xx1"};
//        String[] v2 = {"2", "xx1", "xx1", "xx1", "xx1"};
//        String[] v3 = {"3", "xx1", "xx1", "xx1", "xx1"};
//        String[] v4 = {"4", "xx1", "xx1", "xx1", "xx1"};
//        String[] v5 = {"5", "xx1", "xx1", "xx1", "xx1"};
//        String[] v6 = {"6", "xx1", "xx1", "xx1", "xx1"};
//        String[] v7 = {"7", "xx1", "xx1", "xx1", "xx1"};
//        String[] v8 = {"8", "xx1", "xx1", "xx1", "xx1"};
//        String[] v9 = {"9", "xx1", "xx1", "xx1", "xx1"};
//        String[] v10 = {"10", "xx1", "xx1", "xx1", "xx1"};
//        String[] count = {"总计:", "xxx"};
//        ;
//
//        //单元格跨行
//        int[] titleR = {1, 1, 1, 1, 1};
//        int[] briefR = {1, 1, 1, 1, 1};
//        int[] headerR = {1, 1, 1, 1, 1};
//        int[] r1 = {1, 1, 1, 1, 1};
//        int[] r2 = {1, 1, 1, 1, 1};
//        int[] r3 = {1, 1, 1, 1, 1};
//        int[] r4 = {1, 1, 1, 1, 1};
//        int[] r5 = {1, 1, 1, 1, 1};
//        int[] r6 = {1, 1, 1, 1, 1};
//        int[] r7 = {1, 1, 1, 1, 1};
//        int[] r8 = {1, 1, 1, 1, 1};
//        int[] r9 = {1, 1, 1, 1, 1};
//        int[] r10 = {1, 1, 1, 1, 1};
//        int[] countR = {1, 1, 1, 1, 1};
//        //单元格跨列
//        int[] titleC = {5};
//        int[] briefC = {2, 3};
//        int[] headerC = {1, 1, 1, 1, 1};
//        int[] c1 = {1, 1, 1, 1, 1};
//        int[] c2 = {1, 1, 1, 1, 1};
//        int[] c3 = {1, 1, 1, 1, 1};
//        int[] c4 = {1, 1, 1, 1, 1};
//        int[] c5 = {1, 1, 1, 1, 1};
//        int[] c6 = {1, 1, 1, 1, 1};
//        int[] c7 = {1, 1, 1, 1, 1};
//        int[] c8 = {1, 1, 1, 1, 1};
//        int[] c9 = {1, 1, 1, 1, 1};
//        int[] c10 = {1, 1, 1, 1, 1};
//        int[] countC = {4, 1};
//        String[][] values = {title, brief, header, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, count};
//        int[][] rows = {titleR, briefR, headerR, r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, countR};
//        int[][] cols = {titleC, briefC, headerC, c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, countC};
//        //四行一起添加
//        document.add(getRowColTable(f1, values, rows, cols, 10f));
//
//        //表底
//        String[] e2 = {"联系单位：xxxxxxxxxxx", "", "联系电话：xxxxxxxxxxxxx"};
//        float[] e3 = {50f, 50f, 50f};
//        document.add(getExplain(e3, e2, smallBoldFont, 15f));
//
//        System.out.println(JSONObject.toJSONString(document));
//        document.close();
//    }

    public static void createPdf(String path, ArrayList<ArrayList<String>> contentList, String[] briefValue) throws FileNotFoundException, DocumentException {
        Document document = new Document(PageSize.A6.rotate());
        PdfWriter.getInstance(document, new FileOutputStream(new File(path)));

        document.open();
        float[] f1 = {20f, 40f, 30f, 30f, 30f};

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
        document.add(getRowColTable(f1, values, rows, cols, 10f));

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
        float[] e3 = {150f};
        document.add(getExplain(e3, e2, smallBoldFont, 6f));

        System.out.println(JSONObject.toJSONString(document));
        document.close();
    }

    private static PdfPCell getRemarkPefCell() {
        Paragraph p = new Paragraph("备注: \n电话：133xxxxxxxx", remarkFont);
        p.setAlignment(Paragraph.ALIGN_CENTER);
        PdfPCell cellN = new PdfPCell(p);
        cellN.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cellN.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cellN.setMinimumHeight(8f);
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


    public static void main(String[] args) throws FileNotFoundException, DocumentException {
        String path = "/Users/jhyang/IdeaProjects/doufu/doufu-management/src/main/java/com/zf/product/doufu/pdf/test.pdf";
//        getPdf(path);
        ArrayList<ArrayList<String>> contentList = new ArrayList<>();
        int count = 0;
        for (int i = 1; i <= 5; i++) {
            ArrayList<String> data = ListUtils.asList(new String[]{String.valueOf(i), "商品商品商品商品商品" + String.valueOf(i), "xx1", "xx1", String.valueOf(i)});
            count += i;
            contentList.add(data);
        }

        String[] briefValue = {"订单编号:", "日期：2021-10-21"};
        createPdf(path, contentList, briefValue);
    }

    private static final NumberFormat formatter = new DecimalFormat("0.00");
    static String path = "/Users/jhyang/IdeaProjects/doufu/doufu-management/src/main/java/com/zf/product/doufu/pdf/test.pdf";

    public static void printOrder(String orderDay, Order order) {
        String[] briefValue = {"客户：" + order.getCustomerName(), "日期：" + orderDay};
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
            createPdf(path, contentList, briefValue);
        } catch (Exception e) {
            logger.error("print pdf error", e);
        }
    }

}



