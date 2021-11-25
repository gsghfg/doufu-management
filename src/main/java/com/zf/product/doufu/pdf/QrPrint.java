//package com.zf.product.doufu.pdf;
//
//import java.awt.print.Book;
//import java.awt.print.PageFormat;
//import java.awt.print.Paper;
//import java.awt.print.Printable;
//import java.awt.print.PrinterException;
//import java.awt.print.PrinterJob;
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.util.Map;
//
//import javax.imageio.ImageIO;
//import javax.print.PrintService;
//import javax.print.PrintServiceLookup;
//import javax.print.attribute.standard.Media;
//import javax.print.attribute.standard.MediaSizeName;
//import javax.print.attribute.standard.MediaTray;
//
//import com.alibaba.fastjson.JSON;
//
//import java.awt.*;
//import java.awt.image.BufferedImage;
//
//public class QrPrint implements Printable {
//	/**
//	 *
//	 * @param Graphic指明打印的图形环境
//	 *
//	 * @param PageFormat指明打印页格式（页面大小以点为计量单位，1点为1英才的1/72，1英寸为25.4毫米。A4纸大致为595×842点）
//	 *
//	 * @param pageIndex指明页号
//	 *
//	 **/
//	//读取参数文件
//	Map<String, Object> settingForm = parseFile("src/config/setting.txt");
//
//	public int print(Graphics gra, PageFormat pf, int pageIndex ) throws PrinterException {
//
//		System.out.println("pageIndex=" + pageIndex);
//		Component c = null;
//
//		// 转换成Graphics2D
//		Graphics2D g2 = (Graphics2D) gra;
//
//		// 设置打印颜色为黑色
//		g2.setColor(Color.black);
//
//		// 设置打印字体（字体名称、样式和点大小）（字体名称可以是物理或者逻辑名称）五种字体系列：Serif、SansSerif、Monospaced、Dialog
//		// 和 DialogInput
//
//		int fontSize = (int)settingForm.get("font");
//
//		Font font = new Font("Monospaced", Font.BOLD, fontSize);
//		g2.setFont(font);// 设置字体
//		float line_heigth = font.getSize2D();// 字体高度
//
//		pf.getPaper();
//
//		System.out.println("打印页面的宽度=>>" + pf.getWidth());
//		System.out.println("打印页面的高度=>>" + pf.getHeight());
//		System.out.println("可成像页面的宽度=>>" + pf.getImageableWidth());
//		System.out.println("可成像页面的高度=>>" + pf.getImageableHeight());
//		System.out.println("可成像页面的坐标=>> (" + pf.getImageableX() + " , " + pf.getImageableY() + ")");
//
//		// 打印起点坐标
//		double x = pf.getImageableX();
//		double y = pf.getImageableY();
//		double width = pf.getImageableWidth();
//		double height = pf.getImageableHeight();
//		// logo位置大小
//		int logo_width = (int) (0.225 * width);
//		int logo_height = (int) (0.59 * logo_width);
//		int logo_x = (int) (0.0375 * width);
//		int logo_y = (int) (0.5 * (height - logo_height));
//		// 二维码位置大小
//		int init_width = (int) (0.25 * width);
//		int init_height = init_width;
//		int init_x = (int) (0.725 * width);
//		int init_y = (int) (0.5 * (height - init_height));
//		// 文字位置大小
//		float line_x = (float) (0.3 * width);
//		float lineOne_y = init_y + line_heigth;
//		float lineTwo_y = init_y + 3 * line_heigth;
//		float lineThree_y = init_y + 5 * line_heigth;
//
//		switch (pageIndex) {
//		case 0:
//
//			Map<String,Object> form =  parseFile("src/config/line.txt");
//			String lineOne = form.get("lineOne").toString();
//			String lineTwo = form.get("lineTwo").toString();
//			String lineThree = form.get("lineThree").toString();
//			String qrText = new StringBuilder().append(lineOne).append(lineTwo).append(lineThree).toString();
//
//			Image src = Toolkit.getDefaultToolkit().getImage("src/icon/logo.png");
//			Image init = new QRCodeImage1(qrText, 80).getImage();
//
//			g2.drawString(lineOne, line_x, lineOne_y);
//			g2.drawString(lineTwo, line_x, lineTwo_y);
//			g2.drawString(lineThree, line_x, lineThree_y);
//			g2.drawImage(src, logo_x, logo_y, logo_width, logo_height, c);
//			g2.drawImage(init, init_x, init_y, init_width, init_height, c);
//
//			return PAGE_EXISTS;
//
//		default:
//
//			return NO_SUCH_PAGE;
//
//		}
//
//	}
//
//	public boolean isPrint() {
//
//		PrintService printer = PrintServiceLookup.lookupDefaultPrintService();
//		Media[] objs = (Media[]) printer.getSupportedAttributeValues(Media.class, null, null);
//		for (Media obj : objs) {
//			if (obj instanceof MediaSizeName) {
//				System.out.println("纸张型号：" + obj);
//			} else if (obj instanceof MediaTray) {
//				System.out.println("纸张来源：" + obj);
//			}
//		}
//
//		// 通俗理解就是书、文档
//		Book book = new Book();
//		// 设置成竖打
//		PageFormat pf = new PageFormat();
//		pf.setOrientation(PageFormat.PORTRAIT);
//		// 通过Paper设置页面的空白边距和可打印区域。必须与实际打印纸张大小相符。
//		int ImageableAreaWidth = (int)(2.833*(int)settingForm.get("width"));
//		int ImageableAreaHeight = (int)(2.833*(int)settingForm.get("heigth"));
//		int ImageableAreaX = (int)(2.833*(int)settingForm.get("x"));
//		int ImageableAreaY = (int)(2.833*(int)settingForm.get("y"));
//
//		Paper p = new Paper();
//		p.setSize(240, 90);// 纸张大小
//		p.setImageableArea(ImageableAreaX, ImageableAreaY, ImageableAreaWidth, ImageableAreaHeight);
//		pf.setPaper(p);
//		// 把 PageFormat 和 Printable 添加到书中，组成一个页面
//		book.append(new QrPrint(), pf);
//		// 获取打印服务对象
//		//查找所有打印服务
//		PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
//		System.out.println("*****从配置文件读取的打印机型号*****");
//		System.out.println(services.length);
//		System.out.println(parseFile("src/config/printer.txt").get("printer"));
//		System.out.println("**********");
//		// this step is necessary because I have several printers configured
//		//将所有查找出来的打印机与自己想要的打印机进行匹配，找出自己想要的打印机
//		PrintService myPrinter = null;
//		for (int i = 0; i < services.length; i++) {
//			System.out.println("service found: " + services[i]);
//			if (services[i].getName().equals(parseFile("src/config/printer.txt").get("printer"))) {
//				myPrinter = services[i];
//				System.out.println("当前使用打印机:"+myPrinter.getName());
//				break;
//			}else {
//				myPrinter=PrintServiceLookup.lookupDefaultPrintService();
//			}
//		}
//
//		PrinterJob job = PrinterJob.getPrinterJob();
//		try {
//			// 设置打印类
//			job.setPageable(book);
//			job.setPrintService(myPrinter);
//			job.print();
//			return true;
//		} catch (PrinterException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return false;
//		}
//	}
//	/* 读文件(json) */
//	public static Map<String, Object> parseFile(String path) {
//		try {
//			File file = new File(path);
//			if (file.isFile() && file.exists() && file.canRead()) {
//				String encoding = "GBK";
//				InputStreamReader in;
//				in = new InputStreamReader(new FileInputStream(file), encoding);
//				BufferedReader bufferedReader = new BufferedReader(in);
//				String lineTxt = "";
//				StringBuilder sb = new StringBuilder(lineTxt);
//				while ((lineTxt = bufferedReader.readLine()) != null) {
//					if (!lineTxt.trim().equals("")) {
//						sb.append(lineTxt);
//					}
//				}
//				lineTxt = sb.toString();
//				in.close();
//				return JSON.parseObject(lineTxt);
//			}else {
//				System.out.println("找不到指定文件");
//			}
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return null;
//	}
//
//}
//
