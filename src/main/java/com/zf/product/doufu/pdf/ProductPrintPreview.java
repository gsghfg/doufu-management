//package com.zf.product.doufu.pdf;
//
//import java.awt.*;
//import java.awt.print.*;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//import table.PackageDetailTable;
//import utils.PrinterSet;
//
//import static com.microsoft.schemas.vml.STExt.Enum.table;
//
////----------------------------
////PrintPreview子类  实现Printable重写print绘制打印内容
//public class ProductPrintPreview extends PrintPreview implements Printable{
//    private static final long serialVersionUID = 1L;
//    PackageDetailTable table=new PackageDetailTable();
//
//    //packageDetailTable数据内容，visible=false时不显示预览窗口
//    public ProductPrintPreview(PackageDetailTable packageDetailTable, boolean visible) {
//
//        setPrintable(this);
//        printPreviewCanvas=new PrintPreviewCanvas(this);
//        if(getPaperSize().equals("A4")){
//            setPaper(595,842,PageFormat.LANDSCAPE);
//        }else{
//            setPaper(480,620,PageFormat.LANDSCAPE);
//        }
//        this.setLocationRelativeTo(null);//居中
//
//        this.table=packageDetailTable;
//        super.printerName=PrinterSet.getBigPrinter();
//
//        if(visible) setVisible(true);
//        else print();
//    }
//
//    @Override
//    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
//        /*        int up=Integer.parseInt(PrinterSet.getPrinter1Up());
//        int left=Integer.parseInt(PrinterSet.getPrinter1Left());*/
//        int startX;
//        int startY;
//        if(getPaperSize().equals("A4")){
//            startX=120;
//            startY=120;
//        }else{
//            startX=36;
//            startY=55;
//        }
//        int tableTop=startY+50;
//        int productRow=Integer.parseInt(table.getName());//要打印的产品在表中的行
//
//        Graphics2D g2 = (Graphics2D) graphics;
//        g2.setStroke(new BasicStroke(0.8f,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));//设置画笔粗细的
//
//        Font font=new Font("黑体",Font.PLAIN,18);
//        g2.setFont(font);
//        g2.drawString("配 件 清 单 ",startX+240,startY+18);
//
//        font=new Font("宋体",Font.BOLD,8);
//        g2.setFont(font);
//
//        String order=table.getValueAt(0, 1).toString().substring(5);
//        g2.drawString("单号:"+order,startX+10,tableTop-10);
//
//
//        int tableLeft=startX;
//        int cw1=30;//类别
//        int cw2=160;//型号
//        int cw3=210;//规格
//        int cw4=25;//单位
//        int cw5=25;//数量
//        int cw6=120;//附注
//
//        int tableWidth=cw1+cw2+cw3+cw4+cw5+cw6;
//        int headeHeightt=28;
//        int lineHeight=14;
//        int bodyLineCount=13;
//        int tableHeight=headeHeightt+lineHeight*(bodyLineCount+2);
//
//        //表头
//        int x=tableLeft;
//        drawCellOnCenter(g2,"类别",x,tableTop,cw1,headeHeightt);
//        drawCellOnCenter(g2,"产品型号",x+=cw1,tableTop,cw2,headeHeightt);
//        drawCellOnCenter(g2,"产品规格",x+=cw2,tableTop,cw3,headeHeightt);
//        drawCellOnCenter(g2,"单位",x+=cw3,tableTop,cw4,headeHeightt);
//        drawCellOnCenter(g2,"数量",x+=cw4,tableTop,cw5,headeHeightt);
//        drawCellOnCenter(g2,"附注",x+=cw5,tableTop,cw6,headeHeightt);
//
//        int y=tableTop;
//        g2.drawLine(tableLeft,y,tableLeft+tableWidth,y);//横线0
//        g2.drawLine(tableLeft,y+=headeHeightt,tableLeft+tableWidth,y);//横线1
//
//        tableTop+=headeHeightt;
//        x=tableLeft;
//        int rows=2,row1,row2;
//        String text1=table.getValueAt(productRow, 3).toString();//型号
//        String text2=table.getValueAt(productRow, 4).toString();
//        int productHight=lineHeight*rows;
//        if(!isLineFlow(g2,text1,cw2)&&!isLineFlow(g2,text2,cw3)) {
//            drawCellOnCenter(g2,text1,x+=cw1,tableTop,cw2,headeHeightt);
//            drawCellOnCenter(g2,text2,x+=cw2,tableTop,cw3,headeHeightt);
//            productHight=lineHeight*rows;
//        }
//        else{
//            row1=drawCellOnCenterRows(g2,text1,x+=cw1,tableTop,cw2,lineHeight);
//            row2=drawCellOnCenterRows(g2,text2,x+=cw2,tableTop,cw3,lineHeight);
//            rows=row1>row2?row1:row2;
//            productHight=lineHeight*rows;
//        }
//
//        drawCellOnCenter(g2,"主机",tableLeft,tableTop,cw1,productHight);
//        drawCellOnCenter(g2,"台",x+=cw3,tableTop,cw4,productHight);
//        drawCellOnCenter(g2,"1",x+=cw4,tableTop,cw5,productHight);
//
//        g2.drawLine(tableLeft,y+=productHight,tableLeft+tableWidth,y);//横线end
//
//        //表框架
//        tableTop-=headeHeightt;
//        x=tableLeft;
//        tableHeight+=productHight;
//        g2.drawLine(x,tableTop,x,tableTop+tableHeight);//竖0
//        g2.drawLine(x+=cw1,tableTop,x,tableTop+tableHeight);//竖1
//        g2.drawLine(x+=cw2,tableTop,x,tableTop+tableHeight);//竖2
//        g2.drawLine(x+=cw3,tableTop,x,tableTop+tableHeight);//竖2
//        g2.drawLine(x+=cw4,tableTop,x,tableTop+tableHeight);//竖2
//        g2.drawLine(x+=cw5,tableTop,x,tableTop+tableHeight);//竖2
//        g2.drawLine(x+=cw6,tableTop,x,tableTop+tableHeight);//竖3
//           Date date=new Date();
//           String time;
//           SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");//设置日期格式
//           time=df.format(date);
//        g2.drawString("装配员："+time,startX+50,tableTop+tableHeight+lineHeight);
//        g2.drawString("装配日期："+time,startX+450,tableTop+tableHeight+lineHeight);
//        tableTop+=headeHeightt;
//
//        //表身
//        tableTop+=productHight;
//        for(int i=0;i<bodyLineCount;i++){
//            g2.drawLine(tableLeft+cw1,tableTop+lineHeight*i,tableLeft+tableWidth,tableTop+lineHeight*i);
//        }
//        tableHeight=lineHeight*bodyLineCount;
//        g2.drawLine(tableLeft,tableTop+tableHeight,tableLeft+tableWidth,tableTop+tableHeight);//横线end
//
//        x=tableLeft;
//        drawCellOnCenter(g2,"配件",x,tableTop,cw1,tableHeight);
//
//        String list[][]={{"C1","1/2NPT引压过渡焊接头","套",""},
//                {"C2","M20X1.5丁字接头","套","配:M10X25螺栓2个，油封1个"},
//                {"C3","1/2NPT锥管阴螺纹接头","套","配:M10X35螺栓2个，油封1个"},
//                {"B1","管装弯支架","套","配:M10X16螺栓4个，U型卡1个"},
//                {"B2","盘装弯支架","套","配:M10X16螺栓4个"},
//                {"B3","管装平支架","套","配:M10X16螺栓4个，U型卡1个"},
//                {"下套","","件",""},
//                {"垫片","","片",""},
//                {"盲法兰","","片",""},
//                {"安装法兰","","片",""},
//                {"紧固件","","套",""},
//        };
//        for(int i=0;i<11;i++){
//            x=tableLeft;
//            drawCellOnCenter(g2,list[i][0],x+=cw1,tableTop+lineHeight*i,cw2,lineHeight);
//            drawCellOnCenter(g2,list[i][1],x+=cw2,tableTop+lineHeight*i,cw3,lineHeight);
//            drawCellOnCenter(g2,list[i][2],x+=cw3,tableTop+lineHeight*i,cw4,lineHeight);
//            drawCellOnLeft(g2,list[i][3],x+=(cw4+cw5),tableTop+lineHeight*i,cw6,lineHeight);
//        }
//        //随机文件
//
//        tableTop+=tableHeight;
//        tableHeight=lineHeight*2;
//        g2.drawLine(tableLeft+cw1,tableTop+lineHeight,tableLeft+tableWidth,tableTop+lineHeight);
//        g2.drawLine(tableLeft,tableTop+tableHeight,tableLeft+tableWidth,tableTop+tableHeight);//横线end
//
//        String plist[][]={{"随机","使用说明书","本","1"},
//                {"文件","合格证/检测报告","张","1"},
//        };
//        for(int i=0;i<2;i++){
//            x=tableLeft;
//            drawCellOnCenter(g2,plist[i][0],x,tableTop+lineHeight*i,cw1,lineHeight);
//            drawCellOnCenter(g2,plist[i][1],x+=cw1,tableTop+lineHeight*i,cw2,lineHeight);
//            drawCellOnCenter(g2,plist[i][2],x+=(cw2+cw3),tableTop+lineHeight*i,cw4,lineHeight);
//            drawCellOnCenter(g2,plist[i][3],x+=(cw4),tableTop+lineHeight*i,cw5,lineHeight);
//        }
//
//        return Printable.PAGE_EXISTS;
//
//    }
//}