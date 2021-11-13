//package com.zf.product.doufu.test.pdf;
//
//import org.apache.pdfbox.pdmodel.PDDocument;
//import org.apache.pdfbox.pdmodel.PDPage;
//import org.apache.pdfbox.pdmodel.PDPageContentStream;
//import org.apache.pdfbox.pdmodel.font.PDFont;
//import org.apache.pdfbox.pdmodel.font.PDType1Font;
//
//public class createHelloPDF {
//
//    public static void createPdf(){
//    PDDocument doc = null;
//    PDPage page = null;
//
//     try {
//         doc = new PDDocument();
//         page = new PDPage();
//         doc.addPage(page);
//         PDFont font = PDType1Font.HELVETICA_BOLD;
//         PDPageContentStream content = new PDPageContentStream(doc, page);
//         content.beginText();
//         content.setFont(font, 12);
//         content.moveTextPositionByAmount(100, 700);
//         String txt = "Faith can move mountains";
////         String txt = "道可道，非常道；名可名，非常名。"
////                        +"无名，万物之始，有名，万物之母。"
////                        +"故常无欲，以观其妙，常有欲，以观其徼。"
////                        +"此两者，同出而异名，同谓之玄，玄之又玄，众妙之门。";
//         content.drawString(txt);
//
//         content.endText();
//         content.close();
//         doc.save("/Users/jhyang/IdeaProjects/doufu/doufu-management/src/main/resources/crt.pdf");
//         doc.close();
//     } catch (Exception e) {
//         System.out.println(e);
//     }
//    }
//
//    public static void main(String[] args) {
//        createPdf();
//        System.err.println("success");
//    }
//}