//package com.zf.product.doufu.pdf;
//
//import java.awt.*;
//import java.awt.print.*;
//
//import javax.print.PrintService;
//import javax.print.PrintServiceLookup;
//import javax.print.attribute.HashAttributeSet;
//import javax.print.attribute.standard.PrinterName;
//import javax.swing.*;
//
//import utils.PrinterSet;
//
////打印预览父类
//public class PrintPreview extends JFrame {
//    private static final long serialVersionUID = 1L;
//
//    Printable printable;
//    PageFormat pageFormat;
//    PrintPreviewCanvas printPreviewCanvas;
//    String printerName;
//
//    public PrintPreview() {
//        JButton print=new JButton("打印");
//        this.add(print,BorderLayout.SOUTH);
//        this.setLocationRelativeTo(null);//居中
//        //打印
//        print.addMouseListener(new java.awt.event.MouseAdapter() {
//            public void mousePressed(java.awt.event.MouseEvent ev) {
//                print();
//            }
//        });
//    }
//
//    public void setPrintable(Printable printable) {
//        this.printable = printable;
//    }
//    //打印
//    protected void print() {
//        PrinterJob job=PrinterJob.getPrinterJob();
//        //job.setPrintable(printable);
//
//        Book book = new Book();
//        book.append(printable, pageFormat);
//        job.setPageable(book);
//
//        HashAttributeSet hs = new HashAttributeSet();
//        hs.add(new PrinterName(printerName,null));//通过打印机名称选择打印机
//        PrintService[] pss = PrintServiceLookup.lookupPrintServices(null, hs);
//        if(pss.length==0)
//        {
//            JOptionPane.showMessageDialog(null, "请检查打印机设置\n");
//            return ;
//        }
//           // System.out.println("打印机:"+printerName);
//        try {
//             // if (!job.printDialog())return;//取消   此方法会将页边距变为0 原因未知
//            job.setPrintService(pss[0]);
//            job.print();
//        } catch (Exception e) {
//            e.printStackTrace();
//            JOptionPane.showMessageDialog(null, "打印失败\n"+e.toString());
//        }
//    }
//    private void setPreviewWindow(int x,int y) {
//
//        add(new JPanel(),BorderLayout.EAST);//边框
//        add(new JPanel(),BorderLayout.WEST);
//        add(new JPanel(),BorderLayout.NORTH);
//        setSize(x+40,y+40);
//    }
//    //纸张大小设置
//    protected void setPaper(int x,int y,int orientation) {
//        Paper paper=new Paper();
//        pageFormat=new PageFormat();
//        paper.setSize(x,y);//设置一页纸的大小
//        paper.setImageableArea(0,0,x,y) ;//设置打印区域的大小
//        pageFormat.setPaper(paper);
//        pageFormat.setOrientation(orientation);
//
//        if(orientation==PageFormat.LANDSCAPE) setPreviewWindow(y,x);
//        else setPreviewWindow(x,y);
//
//        //printPreviewCanvas.setPreferredSize(new Dimension(y,x));
//        add(printPreviewCanvas);//预览内容
//    }
//    protected void setPaper(int x,int y) {
//        setPaper(x,y,PageFormat.PORTRAIT);
//    }
//
//    //以下是在子类中调用，用于绘制表格
//    //居中
//    static void drawCellOnCenter(Graphics2D g2,String text,int x, int y,int width,int height){
//        FontMetrics fm = g2.getFontMetrics();//用于获取字符串宽高
//        g2.drawString(text,x + (width - fm.stringWidth(text)) / 2,
//                y+ fm.getAscent() + (height - fm.getHeight()) / 2);
//    }
//    //左对齐
//    static void drawCellOnLeft(Graphics2D g2,String text,int x, int y,int width,int height){
//        FontMetrics fm = g2.getFontMetrics();//用于获取字符串宽高
//        g2.drawString(text,x +fm.getHeight()/3,
//                y+ fm.getAscent() + (height - fm.getHeight()) / 4);
//    }
//    //是否超出列宽
//    static boolean isLineFlow(Graphics2D g2,String text,int width){
//         FontMetrics fm =g2.getFontMetrics();//用于获取字符串宽高
//         if(fm.stringWidth(text)>width-fm.getHeight()) return true;
//         return false;
//    }
//    //带自动换行
//    static int drawCellOnCenterRows(Graphics2D g2,String text,int x, int y,int width,int height){
//         FontMetrics fm =g2.getFontMetrics();//用于获取字符串宽高
//         int stringWidth;
//         String s;
//         int rowCount=0;
//         for(int i=1;i<=text.length();i++){
//             s=text.substring(0,i);//i不包括
//             stringWidth=fm.stringWidth(s);
//             if(stringWidth>width-fm.getHeight()){//换行
//                 s=text.substring(0,i-1);
//                 drawCellOnCenter(g2,s,x,y,width,height);
//                 rowCount++;
//                 text=text.substring(i-1,text.length());//取剩下的
//                 y+=height;
//                 i=1;
//             }
//             else{
//                 if(i==text.length()) {
//                     drawCellOnCenter(g2,s,x,y,width,height);
//                     rowCount++;
//                     break;
//                 }
//             }
//
//         }
//         return rowCount;
//     }
//
//
//}
//
////----------------------------
////PrintPreview内部类  用于绘制打印内容，在setPaper中调用
//class PrintPreviewCanvas extends Canvas {
//    private static final long serialVersionUID = 1L;
//    Printable printable;
//
//    public PrintPreviewCanvas(Printable printable) {
//        this.printable=printable;
//        this.setBackground(Color.white);
//    }
//
//    public void paint(Graphics g){
//        try {
//            printable.print(g, null, 0);
//        } catch (PrinterException e) {
//            e.printStackTrace();
//        }
//    }
//}
//
//
//
