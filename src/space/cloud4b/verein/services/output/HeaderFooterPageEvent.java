package space.cloud4b.verein.services.output;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.FontSelector;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import space.cloud4b.verein.model.verein.user.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

/**
 * Die Klasse stellt bei der Erstellung von Pdf-Dokumenten Methoden zur Verfügung für
 * Kopfzeile inkl. Seitennummerierung
 *
 * @author Bernhard Kämpf und Serge Kaulitz
 * @version 2019-12-20
 */
public class HeaderFooterPageEvent extends PdfPageEventHelper {

    /**
     * Der Titel für den Header
     */
    private String header;
    /**
     * Der angemeldete User für den Header
     */
    private User user;

    /**
     * Ermöglicht die Übergabe eines individuellen Titels
     *
     * @param header Der Text für den Titel
     */
    public void setHeader(String header) {
        this.header = header;
    }

    /**
     * Setzt den angemeldeten User
     *
     * @param user Der angemeldete User
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Fügt jeder Seite einen Header hinzu (bei onEndPage-Event)
     * @see com.itextpdf.text.pdf.PdfPageEventHelper#onEndPage(com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
     */
    public void onEndPage(PdfWriter writer, Document document) {
        FontSelector fs = new FontSelector();
        Font fontTitel = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
        Font fontSubHeader = FontFactory.getFont(FontFactory.HELVETICA, 8);
        fontTitel.setColor(new BaseColor(247, 136, 136));
        fs.addFont(fontTitel);
        Phrase titel = new Phrase(header, fontTitel);
        PdfPTable table = new PdfPTable(2);
        try {
            table.setWidths(new int[]{24, 24});
            table.setTotalWidth(527);
            table.setLockedWidth(true);
            table.getDefaultCell().setFixedHeight(20);
            table.getDefaultCell().setBorder(0);
            table.addCell(titel);
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(new Paragraph(String.format("Seite %d", writer.getPageNumber()), fontSubHeader));
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(new Paragraph("erstellt von " + user.getUserName() + " am " + LocalDate.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)), fontSubHeader));
            table.addCell(" ");
            table.writeSelectedRows(0, 2, 30, 800, writer.getDirectContent());
        } catch (DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }
}