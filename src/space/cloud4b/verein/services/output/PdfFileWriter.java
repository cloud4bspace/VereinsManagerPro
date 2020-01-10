package space.cloud4b.verein.services.output;

import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import org.apache.poi.util.IOUtils;
import space.cloud4b.verein.model.verein.adressbuch.Mitglied;
import space.cloud4b.verein.model.verein.user.User;

import java.awt.*;
import java.io.*;
import java.net.MalformedURLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;

/**
 * Die Klasse bietet Methoden zur Erstellung von PDF-Dokumenten
 *
 * @author Bernhard Kämpf und Serge Kaulitz
 * @version 2019-12-20
 */
public abstract class PdfFileWriter {
    public static final String DEST = "ressources/files/pdf/Mitgliederliste_" + LocalDate.now().toString() + ".pdf";
    public static Font font = new Font(Font.FontFamily.UNDEFINED, 8, Font.NORMAL);
    public static Font fontBold = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD);


    public static void writePdf(User user, ArrayList<Mitglied> mitgliederListe) throws IOException {

        File file = new File(DEST);
        file.getParentFile().mkdirs();
        FileOutputStream fos = null;
        Document document = new Document(PageSize.A4);
        // document.newPage();
        document.setMargins(20F, 10F, 80F, 20F);
        try {
            try {
                fos = new FileOutputStream(DEST);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            com.itextpdf.text.pdf.PdfWriter writer = com.itextpdf.text.pdf.PdfWriter.getInstance(document, fos);

            // HeaderFooterPageEvent reagiert auf
            HeaderFooterPageEvent event = new HeaderFooterPageEvent();
            event.setHeader("Mitgliederliste ");
            event.setUser(user);
            writer.setPageEvent(event);
            document.open();
            document.addCreationDate();
            document.addTitle("Mitgliederliste");
            document.addAuthor(user.getUserName());
            document.addCreator("VereinsManagerPro (www.cloud4b.space)");
            document.addSubject("Mitgliederliste");
            //setHeader(writer, document);

            // document.setMargins(50F,20F,20F,20F);
            float[] pointColumnWidths = {40F, 150F, 150F, 150F, 80F};
            PdfPTable table = new PdfPTable(5);
            table.setHeaderRows(1);
            table.setHorizontalAlignment(0);
            table.setTotalWidth(pointColumnWidths);
            table.setWidthPercentage(100f);
            // Header Row
            Paragraph paraHeader01 = new Paragraph("ID", fontBold);
            PdfPCell paraCell01 = new PdfPCell(paraHeader01);
            paraCell01.setPadding(5);
            paraCell01.setBorderWidthLeft(0);
            paraCell01.setBorderWidthRight(0);
            paraCell01.setBorderColor(new BaseColor(247, 136, 136));
            paraCell01.setBackgroundColor(new BaseColor(247, 136, 136));
            paraCell01.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(paraCell01);

            Paragraph paraHeader02 = new Paragraph("Mitglied", fontBold);
            PdfPCell paraCell02 = new PdfPCell(paraHeader02);
            paraCell02.setPadding(5);
            paraCell02.setBorderWidthLeft(0);
            paraCell02.setBorderWidthRight(0);
            paraCell02.setBorderColor(new BaseColor(247, 136, 136));
            paraCell02.setBackgroundColor(new BaseColor(247, 136, 136));
            paraCell02.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(paraCell02);

            Paragraph paraHeader03 = new Paragraph("Kontaktdaten, Geburtstag", fontBold);
            paraHeader03.setLeading(5);
            PdfPCell paraCell03 = new PdfPCell(paraHeader03);
            paraCell03.setPadding(5);
            paraCell03.setBorderWidthLeft(0);
            paraCell03.setBorderWidthRight(0);
            paraCell03.setBorderColor(new BaseColor(247, 136, 136));
            paraCell03.setBackgroundColor(new BaseColor(247, 136, 136));
            paraCell03.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(paraCell03);

            Paragraph paraHeader04 = new Paragraph("Mitgliederstatus, Eintritt", fontBold);
            paraHeader04.setLeading(5);
            PdfPCell paraCell04 = new PdfPCell(paraHeader04);
            paraCell04.setPadding(5);
            paraCell04.setBorderWidthLeft(0);
            paraCell04.setBorderWidthRight(0);
            paraCell04.setBorderColor(new BaseColor(247, 136, 136));
            paraCell04.setBackgroundColor(new BaseColor(247, 136, 136));
            paraCell04.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(paraCell04);

            Paragraph paraHeader05 = new Paragraph("Profilbild", fontBold);
            paraHeader05.setLeading(5);
            PdfPCell paraCell05 = new PdfPCell(paraHeader05);
            paraCell05.setPadding(5);
            paraCell05.setBorderWidthLeft(0);
            paraCell05.setBorderWidthRight(0);
            paraCell05.setBorderColor(new BaseColor(247, 136, 136));
            paraCell05.setBackgroundColor(new BaseColor(247, 136, 136));
            paraCell05.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(paraCell05);


            for (Mitglied mitglied : mitgliederListe) {
                // Spalte 1
                Paragraph paragraph1CellCol01 = new Paragraph("#" + Integer.toString(mitglied.getId()), font);
                paragraph1CellCol01.setLeading(5);
                paragraph1CellCol01.setAlignment(Element.ALIGN_RIGHT);
                PdfPCell cellCol01 = new PdfPCell();
                cellCol01.setBorderWidthLeft(0);
                cellCol01.setBorderWidthRight(0);
                cellCol01.setBorderColor(new BaseColor(247, 136, 136));
                cellCol01.addElement(paragraph1CellCol01);
                cellCol01.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cellCol01.setBackgroundColor(new BaseColor(247, 136, 136));
                cellCol01.setPadding(5);
                table.addCell(cellCol01);


                // Spalte 2
                Paragraph paragraph1CellCol02 = new Paragraph(mitglied.getNachName() + " "
                        + mitglied.getVorname(), fontBold);
                paragraph1CellCol02.setLeading(5);
                Paragraph paragraph2CellCol02 = new Paragraph(mitglied.getAdresse(), font);
                Paragraph paragraph3CellCol02 = new Paragraph(mitglied.getPlz() + " " + mitglied.getOrt(), font);
                paragraph3CellCol02.setSpacingAfter(0);

                PdfPCell cellCol02 = new PdfPCell();
                cellCol02.setBorderWidthLeft(0);
                cellCol02.setBorderWidthRight(0);
                cellCol02.setBorderColor(new BaseColor(247, 136, 136));
                cellCol02.setPadding(5);
                cellCol02.addElement(paragraph1CellCol02);
                cellCol02.addElement(paragraph2CellCol02);
                cellCol02.addElement(paragraph3CellCol02);
                table.addCell(cellCol02);

                // Spalte 3
                Paragraph paragraph1CellCol03 = new Paragraph(mitglied.getEmail(), font);
                paragraph1CellCol03.setLeading(5);
                Paragraph paragraph2CellCol03 = new Paragraph(mitglied.getMobile(), font);
                PdfPCell cellCol03 = new PdfPCell();
                Paragraph paragraph3CellCol03 = new Paragraph(mitglied.getGeburtsdatum()
                        .format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)), font);
                cellCol03.setBorderWidthLeft(0);
                cellCol03.setBorderWidthRight(0);
                cellCol03.setBorderColor(new BaseColor(247, 136, 136));
                cellCol03.setPadding(5);
                cellCol03.addElement(paragraph1CellCol03);
                cellCol03.addElement(paragraph2CellCol03);
                cellCol03.addElement(paragraph3CellCol03);
                table.addCell(cellCol03);

                // Spalte 4
                Paragraph paragraph1CellCol04 = new Paragraph(mitglied.getKategorieIElement()
                        .getStatusElementTextLang(), font);
                paragraph1CellCol04.setLeading(5);
                Paragraph paragraph2CellCol04 = new Paragraph(mitglied.getKategorieIIElement()
                        .getStatusElementTextLang(), font);
                Paragraph paragraph3CellCol04 = new Paragraph(mitglied.getEintrittsdatum()
                        .format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)), font);
                PdfPCell cellCol04 = new PdfPCell();
                cellCol04.setBorderWidthLeft(0);
                cellCol04.setBorderWidthRight(0);
                cellCol04.setBorderColor(new BaseColor(247, 136, 136));
                cellCol04.setPadding(5);
                cellCol04.addElement(paragraph1CellCol04);
                cellCol04.addElement(paragraph2CellCol04);
                cellCol04.addElement(paragraph3CellCol04);
                table.addCell(cellCol04);

                // Spalte 5
                try {
                    InputStream inputStream = new FileInputStream("ressources/images/profilbilder/ProfilBild_"
                            + mitglied.getId() + ".png");
                    byte[] imageBytes = IOUtils.toByteArray(inputStream);
                    Image image = Image.getInstance(imageBytes);
                    image.setWidthPercentage(100f);
                    inputStream.close();
                    PdfPCell cellCol05 = new PdfPCell();
                    cellCol05.setBorderWidthLeft(0);
                    cellCol05.setBorderWidthRight(0);
                    cellCol05.setBorderColor(new BaseColor(247, 136, 136));
                    cellCol05.setPadding(5);
                    cellCol05.addElement(image);
                    table.addCell(cellCol05);
                } catch (IOException e) {
                    try {
                        InputStream inputStream = new FileInputStream("ressources/images/profilbilder/ProfilBild_"
                                + mitglied.getId() + ".jpg");
                        byte[] imageBytes = IOUtils.toByteArray(inputStream);
                        Image image = Image.getInstance(imageBytes);
                        image.setWidthPercentage(100f);
                        inputStream.close();
                        PdfPCell cellCol05 = new PdfPCell();
                        cellCol05.setBorderWidthLeft(0);
                        cellCol05.setBorderWidthRight(0);
                        cellCol05.setBorderColor(new BaseColor(247, 136, 136));
                        cellCol05.setPadding(5);
                        cellCol05.addElement(image);
                        table.addCell(cellCol05);
                    } catch (FileNotFoundException ex) {
                        InputStream inputStream = new FileInputStream("ressources/images/profilbilder/Dummy"
                                + ".png");
                        byte[] imageBytes = IOUtils.toByteArray(inputStream);
                        Image image = Image.getInstance(imageBytes);
                        image.setWidthPercentage(100f);
                        inputStream.close();
                        PdfPCell cellCol05 = new PdfPCell();
                        cellCol05.setBorderWidthLeft(0);
                        cellCol05.setBorderWidthRight(0);
                        cellCol05.setBorderColor(new BaseColor(247, 136, 136));
                        cellCol05.setPadding(5);
                        cellCol05.addElement(image);
                        table.addCell(cellCol05);

                    } catch (MalformedURLException ex) {
                        ex.printStackTrace();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                }

            }
            document.add(table);

            document.close();
            try {
                Desktop.getDesktop().open(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        }

    }

}
