package space.cloud4b.verein.services.output;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import space.cloud4b.verein.model.verein.user.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class HeaderFooterPageEvent extends PdfPageEventHelper {
    /**
     * The template with the total number of pages.
     */
    PdfTemplate total;
    /**
     * The header text.
     */
    private String header;
    private User user;

    /**
     * Allows us to change the content of the header.
     *
     * @param header The new header String
     */
    public void setHeader(String header) {
        this.header = header;
    }

    /**
     * Creates the PdfTemplate that will hold the total number of pages.
     *
     * @see com.itextpdf.text.pdf.PdfPageEventHelper#onOpenDocument(
     *com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
     */
    public void onOpenDocument(PdfWriter writer, Document document) {
        total = writer.getDirectContent().createTemplate(30, 16);
    }

    /**
     * Adds a header to every page
     *
     * @see com.itextpdf.text.pdf.PdfPageEventHelper#onEndPage(
     *com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
     */
    public void onEndPage(PdfWriter writer, Document document) {
        PdfContentByte cb = writer.getDirectContent();
        FontSelector fs = new FontSelector();
        Font fontTitel = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
        Font fontSubHeader = FontFactory.getFont(FontFactory.HELVETICA, 8);
        fontTitel.setColor(new BaseColor(247, 136, 136));
        fs.addFont(fontTitel);
        Phrase titel = new Phrase(header, fontTitel);
        PdfPTable table = new PdfPTable(2);
        try {
            table.setWidths(new int[]{24, 24});
            // table.setWidths(new int[]{100, 100, 100});
            table.setTotalWidth(527);
            table.setLockedWidth(true);
            table.getDefaultCell().setFixedHeight(20);
            table.getDefaultCell().setBorder(0);
            table.addCell(titel);
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
            //  table.addCell(String.format("Seite %d von ", writer.getPageNumber()));
            table.addCell(new Paragraph(String.format("Seite %d", writer.getPageNumber()), fontSubHeader));
            //   PdfPCell cell = new PdfPCell(Image.getInstance(total));
            //  cell.setBorder(0);
            //  table.addCell(cell);
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(new Paragraph("erstellt von " + user.getUserName() + " am " + LocalDate.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)), fontSubHeader));
            table.addCell(" ");
            // table.addCell(" ");
            //  table.writeSelectedRows(0, -1, 34, 803, writer.getDirectContent());
            // table.writeSelectedRows(0, 1, 30, 800, writer.getDirectContent());
            table.writeSelectedRows(0, 2, 30, 800, writer.getDirectContent());
        } catch (DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }

    /**
     * Fills out the total number of pages before the document is closed.
     *
     * @see com.itextpdf.text.pdf.PdfPageEventHelper#onCloseDocument(
     *com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
     */
    public void onCloseDocument(PdfWriter writer, Document document) {
        ColumnText.showTextAligned(total, Element.ALIGN_LEFT,
                new Phrase(String.valueOf(writer.getPageNumber() - 1)),
                2, 2, 0);
    }

    public void setUser(User user) {
        this.user = user;
    }
}