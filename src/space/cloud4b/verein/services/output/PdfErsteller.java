package space.cloud4b.verein.services.output;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class PdfErsteller {
    public static final String DEST = "ressources/files/pdf/einPdf.pdf";

    public static void main(String[] args) throws FileNotFoundException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new PdfErsteller().createPdf(DEST);
    }

    public void createPdf(String destination) throws FileNotFoundException {

        FileOutputStream fos = new FileOutputStream(DEST);
        PdfWriter writer = new PdfWriter(fos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);
        // Creating a table
        float[] pointColumnWidths = {150F, 150F, 150F};
        Table table = new Table(pointColumnWidths);

        // Adding cells to the table
        table.addCell(new Cell().add("Name"));
        table.addCell(new Cell().add("Raju"));
        table.addCell(new Cell().add("Id"));
        table.addCell(new Cell().add("1001"));
        table.addCell(new Cell().add("Designation"));
        table.addCell(new Cell().add("Programmer"));

        // Adding Table to document
        document.add(table);

        document.add(new Paragraph("Hallo Welt"));
        document.add(new Paragraph("Zeile 2"));
        document.close();


    }
}


