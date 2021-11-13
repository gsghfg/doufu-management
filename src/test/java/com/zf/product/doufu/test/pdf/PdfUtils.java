package com.zf.product.doufu.test.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.Iterator;
import java.util.List;


/**
 * @ClassName PdfUtils
 * @Deacription TODO
 * @Author alex 
 * @Date 2020/2/26 15:19
 * @Version 1.0
 **/
public class PdfUtils {
    /** 
     * @Author alex 
     * @Description //TODO 创建简单表格
     * @Date  2020/2/27 14:19
     * @Param [d :, pName 表格标题, cellWidth :表格列宽, col：表格列头, list ：需要的数据]
     * @return void
     **/
    
    public static void createPdf(Document d, String pName, float[] cellWidth, String[] col, List<List<String>> list) {
        try {
            /** pdf文档中中文字体的设置，注意一定要添加iTextAsian.jar包 */

            Font FontChinese10 = createChineseFont(10,Font.NORMAL,BaseColor.BLACK);//加入document：
            Font FontChinese6 = createChineseFont(6,Font.NORMAL,BaseColor.BLACK);//加入document：
            /** 向文档中添加内容，创建段落对象 */
            Paragraph p1 = createParagraph(pName,FontChinese10);
            p1.setAlignment(Paragraph.ALIGN_CENTER);
            p1.setSpacingAfter(10);//设置段落后间距
            d.add(p1);// Paragraph添加文本
            /** 创建表格对象（包含行列矩阵的表格） */
            PdfPTable t = createPdfPTable(cellWidth.length);// 设置总列宽queryCollegePartner
            t.setTotalWidth(cellWidth);
//            t.setPaddingTop(50);
            if (col == null || col.length < 1) {
                throw new Exception("列头为空");
            }
            for (String c : col) {
                PdfPCell c1 = createPdfPCell(createPhrase(c,FontChinese6));
                c1.setBackgroundColor(new BaseColor(180, 202, 220));//淡蓝色
                t.addCell(c1);
            }
            if (list != null && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    Iterator<String> it = list.get(i).iterator();
                    while (it.hasNext()) {
                        t.addCell(createPdfPCell(createPhrase(it.next(), FontChinese6)));
                    }

                }
            }
            d.add(t);
            d.newPage();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * @return com.itextpdf.text.Font
     * @Author cl
     * @Description //TODO 创造字体格式
     * @Date 2020/2/27 13:21
     * @Param [fontname , size 字体大小, style：字体风格, color：颜色]
     **/

    public static Font createFont(String fontname, float size, int style, BaseColor color) {
        Font font = FontFactory.getFont(fontname, size, style, color);
        return font;
    }

    /**
     * 功能： 返回支持中文的字体
     *
     * @param size    字体大小
     * @param style   字体风格
     * @param color   字体 颜色
     * @return 字体格式
     */
    public static Font createChineseFont(float size, int style, BaseColor color) {
        BaseFont bfChinese = null;
        try {
            bfChinese = BaseFont.createFont("STSong-Light",
                    "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Font(bfChinese, size, style, color);

    }

    /**
     * 功能：向PDF文档中添加段落
     * @param text 内容
     * @param font 内容对应的字体
     * @return Paraggraph 指定字体格式的内容
     */
    public static Paragraph createParagraph(String text,Font font) {
        Paragraph paragraph = new Paragraph(text,font);
        return paragraph;
    }


    /**
     * 创建表格
     * @param colnum 列数
     * @return 列数为colnum的表格
     */
    public static PdfPTable createPdfPTable(int colnum){
        PdfPTable table=new PdfPTable(colnum);
        return table;
    }


    /**
     * 创建表格的单元格
     * @param p 短语
     * @return 单元格
     */
    public static PdfPCell createPdfPCell(Phrase p){
        PdfPCell c = new PdfPCell(p);
        return c;
    }

    /**
     * 功能：向PDF文档中添加短语
     * @param text 内容
     * @param font 内容对应的字体
     * @return phrase 指定字体格式的内容
     */
    public static Phrase createPhrase(String text,Font font) {
        Phrase phrase = new Phrase(text,font);
        return phrase;
    }



    /**
     * 功能：创建导出数据的目标文档
     * @param fileName 存储文件的临时路径
     */
    public static Document createDocument(String fileName) {
        Document document=null;
        File file = new File(fileName);
        File dir = file.getParentFile();
        //如果文件夹不存在则创建
        if  (!dir .exists())
            dir .mkdirs();
        FileOutputStream out = null;
        PdfWriter pdfWriter=null;
        document = new Document(PageSize.A4,50,50,50,50);
        try {
            if  (!file .exists())//不存在文件则创建文件
                file.createNewFile();
            out = new FileOutputStream(file);
            pdfWriter= PdfWriter.getInstance(document, out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        // 打开文档准备写入内容
        pdfWriter.setPageEvent(new CreateHeaderFooter());
        document.open();

        return document;
    }
    /**
     * 最后关闭PDF文档
     */
    public static void closeDocument(Document document) {
        if(document != null) {
            document.close();
        }
    }
}

