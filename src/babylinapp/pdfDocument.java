package babylinapp;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class pdfDocument {


    public static void createPDF(String pdfName, String customerName) {
        String path = System.getProperty("user.dir");
        String fileName=path+"\\pdfs\\"+pdfName+".pdf";
        PDDocument pdDocument = new PDDocument();
        PDPage pdPage = new PDPage();
        PDDocumentInformation information = new PDDocumentInformation();
        try {
            information.setTitle("Babylin Consult Receipt - "+customerName);
            information.setAuthor("Babylin Consult");
            information.setCreator("Babylin Consult Stock Management App - PDFBox API");
            information.setSubject("Receipt");
            Calendar date = new GregorianCalendar();
            date.setTime(Date.from(Instant.now()));
            information.setCreationDate(date);
            information.setModificationDate(date);
            information.setKeywords("receipt, invoice");
            pdDocument.addPage(pdPage);
            pdDocument.save(fileName);
            System.out.println("Document Created!");
            pdDocument.close();
            insertContent(fileName);
        } catch (IOException e) {
            jdbcController.printIOExpection(e);
        }
    }
    protected static void insertContent(String pathname){
        File file = new File(pathname);
        try {
            PDDocument document = PDDocument.load(file);
            PDPage pdPage = document.getPage(1);
            PDPageContentStream pdPageContentStream = new PDPageContentStream(document, pdPage);
            pdPageContentStream.beginText();
            pdPageContentStream.setFont(PDType1Font.COURIER,20);
            pdPageContentStream.setLeading(14.5f);
            pdPageContentStream.newLineAtOffset(25, 725);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
