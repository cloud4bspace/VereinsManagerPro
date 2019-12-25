package space.cloud4b.verein.services.output;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.text.Anchor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Section;
import space.cloud4b.verein.model.verein.adressbuch.Mitglied;
import space.cloud4b.verein.services.DatabaseReader;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public abstract class PdfOutput {
    public static final String DEST = "ressources/files/pdf/einPdf.pdf";


    public static void mitgliederListePdf(ArrayList<Mitglied> mitgliederListe) {

        File file = new File(DEST);
        file.getParentFile().mkdirs();
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(DEST);
            PdfWriter writer = new PdfWriter(fos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);
            // Creating a table
            float[] pointColumnWidths = {150F, 150F, 150F};
            Table table = new Table(pointColumnWidths);

            // Adding cells to the table
            for (Mitglied mitglied : mitgliederListe) {
                table.addCell(new Cell().add(mitglied.getKurzbezeichnung()));
                table.addCell(new Cell().add(mitglied.getEintrittsdatum().toString()));
                table.addCell(new Cell().add(mitglied.getLetzteAenderung()));
            }
      /*      table.addCell(new Cell().add("Name"));
            table.addCell(new Cell().add("Raju"));
            table.addCell(new Cell().add("Id"));
            table.addCell(new Cell().add("1001"));
            table.addCell(new Cell().add("Designation"));
            table.addCell(new Cell().add("Programmer"));*/

            // Adding Table to document
            document.add(table);

            document.add(new Paragraph("Hallo Welt"));
            document.add(new Paragraph("Zeile 2"));

            Anchor anchor = new Anchor("First Chapter");
            anchor.setName("First Chapter");
            Chapter catPart = new Chapter(new com.itextpdf.text.Paragraph(anchor), 1);
            Paragraph subPara = new Paragraph("Subcategory 1");
            Section subCatPart = catPart.addSection(subPara.toString());
            subCatPart.add(new com.itextpdf.text.Paragraph("hallo"));
            subPara = new Paragraph("Subcategory 2");
            subCatPart = catPart.addSection(subPara.toString());
            subCatPart.add(new com.itextpdf.text.Paragraph("Paragraph 1"));
            subCatPart.add(new com.itextpdf.text.Paragraph("Paragraph 2"));
            subCatPart.add(new com.itextpdf.text.Paragraph("Paragraph 3"));
            document.add(subPara);
            //document.add(anchor);


            document.add(new Paragraph(DatabaseReader.getUserNameVorname(1)));
            document.close();
            try {
                Desktop.getDesktop().open(file);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}

