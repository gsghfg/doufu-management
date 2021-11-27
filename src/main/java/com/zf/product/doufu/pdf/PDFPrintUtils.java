package com.zf.product.doufu.pdf;

import com.alibaba.fastjson.JSONObject;
import com.zf.product.doufu.constants.PdfConstants;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;
import org.apache.pdfbox.printing.PDFPrintable;
import org.apache.pdfbox.printing.Scaling;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.standard.Media;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.MediaTray;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterJob;
import java.io.File;

public class PDFPrintUtils {
    public static void main(String[] args) {
        try {
//            printPdf("D:\\workspace\\doufu\\doufu-management\\src\\main\\java\\com\\zf\\product\\doufu\\pdf\\test.pdf",PdfConstants.PRINTER_NAME);
//            PrinterJob printerJob = getPrintServiceByName(PdfConstants.PRINTER_NAME);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        PrintService printer = PrintServiceLookup.lookupDefaultPrintService();
//        Media[] objs = (Media[]) printer.getSupportedAttributeValues(Media.class, null, null);
//        for (Media obj : objs) {
//            if (obj instanceof MediaSizeName) {
//
//                obj.getValue();
//                System.out.println("纸张型号：" + JSONObject.toJSONString(obj)+", value:"+obj.getValue());
//            } else if (obj instanceof MediaTray) {
//                System.out.println("纸张来源：" + obj);
//            }
//        }

        try {
            getPrintServiceByName(PdfConstants.PRINTER_NAME);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static PrinterJob getPrintServiceByName(String printerName) throws Exception {
        PrinterJob job = PrinterJob.getPrinterJob();
        System.out.println(JSONObject.toJSON(job.defaultPage()));
        System.out.println(job.defaultPage().getHeight()+", "+job.defaultPage().getWidth());
        System.out.println(job.defaultPage().getImageableX()+", "+job.defaultPage().getImageableY());
        System.out.println(job.defaultPage().getImageableHeight()+", "+job.defaultPage().getImageableWidth());
        // 遍历查询打印机名称
        boolean flag = false;
        for (PrintService ps : PrinterJob.lookupPrintServices()) {
            String psName = ps.toString();
            System.out.println(psName);
            // 选用指定打印机，需要精确查询打印机就用equals，模糊查询用contains
            if (psName.contains(printerName)) {
                flag = true;
                job.setPrintService(ps);
                break;
            }
        }
        if (!flag) {
            throw new RuntimeException("打印失败，未找到名称为" + printerName + "的打印机，请检查。");
        }
        return job;
    }

    public static void setPageStyle(PDDocument document, PrinterJob job) {
        job.setPageable(new PDFPageable(document));
        Paper paper = new Paper();
        int width = 340;
        int height = 392;
//        // 设置打印纸张大小
        paper.setSize(width, height); // 1/72 inch
//        // 设置边距，单位是像素，10mm边距，对应 28px
        int marginLeft = 0;
        int marginRight = 0;
        int marginTop = 0;
        int marginBottom = 0;
//        PageFormat format = job.defaultPage();
//        Paper paper = format.getPaper();
        // 设置打印位置 坐标
        paper.setImageableArea(marginLeft, marginRight, width - (marginLeft + marginRight), height - (marginTop + marginBottom));
//        paper.setImageableArea(marginLeft, marginRight, 339, 384);

        // custom page format
        PageFormat pageFormat = new PageFormat();
        pageFormat.setPaper(paper);
        // override the page format
        Book book = new Book();
        // append all pages 设置一些属性 是否缩放 打印张数等
        book.append(new PDFPrintable(document, Scaling.SCALE_TO_FIT), pageFormat, 1);
        job.setPageable(book);
    }

    public static PDDocument printPdf(String pdfPath, String printerName) throws Exception {
        File file = new File(pdfPath);
        PDDocument document = PDDocument.load(file);
        PrinterJob job = getPrintServiceByName(printerName);
        setPageStyle(document, job);
        // 开始打印
        job.print();
        return document;
    }


//    private static Map<String, Object> settingForm = parseFile("src/config/setting.txt");


//    public static boolean print(){
//
//        PrintService printer = PrintServiceLookup.lookupDefaultPrintService();
//        Media[] objs = (Media[]) printer.getSupportedAttributeValues(Media.class, null, null);
//        for (Media obj : objs) {
//            if (obj instanceof MediaSizeName) {
//                System.out.println("纸张型号：" + obj);
//            } else if (obj instanceof MediaTray) {
//                System.out.println("纸张来源：" + obj);
//            }
//        }
//
//        // 通俗理解就是书、文档
//        Book book = new Book();
//        // 设置成竖打
//        PageFormat pf = new PageFormat();
//        pf.setOrientation(PageFormat.PORTRAIT);
//        // 通过Paper设置页面的空白边距和可打印区域。必须与实际打印纸张大小相符。
////        int ImageableAreaWidth = (int)(2.833*(int)settingForm.get("width"));
////        int ImageableAreaHeight = (int)(2.833*(int)settingForm.get("heigth"));
////        int ImageableAreaX = (int)(2.833*(int)settingForm.get("x"));
////        int ImageableAreaY = (int)(2.833*(int)settingForm.get("y"));
//
//        Paper p = new Paper();
//        p.setSize(120, 140);// 纸张大小
////        p.setImageableArea(ImageableAreaX, ImageableAreaY, ImageableAreaWidth, ImageableAreaHeight);
//        pf.setPaper(p);
//        // 把 PageFormat 和 Printable 添加到书中，组成一个页面
////        book.append(new QrPrint(), pf);
//        // 获取打印服务对象
//        //查找所有打印服务
//        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
//        System.out.println("*****从配置文件读取的打印机型号*****");
//        System.out.println(services.length);
////        System.out.println(parseFile("src/config/printer.txt").get("printer"));
//        System.out.println("**********");
//        // this step is necessary because I have several printers configured
//        //将所有查找出来的打印机与自己想要的打印机进行匹配，找出自己想要的打印机
//        PrintService myPrinter = null;
//        for (int i = 0; i < services.length; i++) {
//            System.out.println("service found: " + services[i]);
////            if (services[i].getName().equals(parseFile("src/config/printer.txt").get("printer"))) {
////                myPrinter = services[i];
////                System.out.println("当前使用打印机:"+myPrinter.getName());
////                break;
////            }else {
//                myPrinter=PrintServiceLookup.lookupDefaultPrintService();
////            }
//        }
//
//        PrinterJob job = PrinterJob.getPrinterJob();
//        try {
//            // 设置打印类
//            job.setPageable(book);
//            job.setPrintService(myPrinter);
//            job.print();
//            return true;
//        } catch (PrinterException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//            return false;
//        }
//    }

    /* 读文件(json) */
//    public static Map<String, Object> parseFile(String path) {
//        try {
//            File file = new File(path);
//            if (file.isFile() && file.exists() && file.canRead()) {
//                String encoding = "GBK";
//                InputStreamReader in;
//                in = new InputStreamReader(new FileInputStream(file), encoding);
//                BufferedReader bufferedReader = new BufferedReader(in);
//                String lineTxt = "";
//                StringBuilder sb = new StringBuilder(lineTxt);
//                while ((lineTxt = bufferedReader.readLine()) != null) {
//                    if (!lineTxt.trim().equals("")) {
//                        sb.append(lineTxt);
//                    }
//                }
//                lineTxt = sb.toString();
//                in.close();
//                return JSON.parseObject(lineTxt);
//            }else {
//                System.out.println("找不到指定文件");
//            }
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        return null;
//    }
}
