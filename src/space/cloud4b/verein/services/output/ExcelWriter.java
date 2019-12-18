package space.cloud4b.verein.services.output;


import javafx.application.Application;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.DefaultIndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import space.cloud4b.verein.model.verein.adressbuch.Mitglied;
import space.cloud4b.verein.model.verein.kalender.Teilnehmer;
import space.cloud4b.verein.model.verein.kalender.Termin;
import space.cloud4b.verein.model.verein.status.Status;
import space.cloud4b.verein.model.verein.status.StatusElement;
import space.cloud4b.verein.model.verein.user.User;
import space.cloud4b.verein.services.DatabaseReader;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class ExcelWriter extends Application {


    /**
     * exportiert die aktuelle Mitgliederliste in ein Excel-File
     * @throws IOException
     */
    public static void exportMirgliederToExcel() throws IOException {
        File file = chooseFile("Mitgliederliste");
        System.out.println(file.getName());

        String[] columns = {"Name", "Vorname", "Anrede", "Adresse", "Adresszusatz",
                "PLZ", "Ort", "E-Mail", "Telefon", "Geburtsdatum", "Kat I", "Kat II", "Eintrittsdatum", "Vorstandsmitglied"};
        List<Mitglied> mitgliedList = new ArrayList<>(DatabaseReader.getMitgliederAsArrayList());

        Workbook workbook = new XSSFWorkbook();
        CreationHelper createHelper = workbook.getCreationHelper();

        // Create a Sheet
        Sheet sheet = workbook.createSheet("Mitglieder");
        // Create a Font for styling header cells
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);
        headerFont.setColor(IndexedColors.RED.getIndex());

        // Create a CellStyle with the font
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);

        // Create a Row
        Row headerRow = sheet.createRow(0);

        // Create cells
        for(int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerCellStyle);
        }

        // Create Cell Style for formatting Date
        CellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd.MM.yyyy"));
        // Create Other rows and cells with employees data
        int rowNum = 1;
        for(Mitglied mitglied: mitgliedList) {
            Row row = sheet.createRow(rowNum++);

            row.createCell(0)
                    .setCellValue(mitglied.getNachName());
            row.createCell(1)
                    .setCellValue(mitglied.getVorname());
            row.createCell(2)
                    .setCellValue(mitglied.getAnredeElement().toString());
            row.createCell(3)
                    .setCellValue(mitglied.getAdresse());
            row.createCell(4)
                    .setCellValue(mitglied.getAdresszusatz());
            row.createCell(5)
                    .setCellValue(mitglied.getPlz());
            row.createCell(6)
                    .setCellValue(mitglied.getOrt());
            row.createCell(7)
                    .setCellValue(mitglied.getEmail());
            row.createCell(8)
                    .setCellValue(mitglied.getTelefon());
            Cell dateOfBirthCell = row.createCell(9);
            if(mitglied.getGeburtsdatum()!=null) {
                dateOfBirthCell.setCellValue(mitglied.getGeburtsdatum().toString());
            } else {
                dateOfBirthCell.setCellValue("na");
            }
            dateOfBirthCell.setCellStyle(dateCellStyle);
            row.createCell(10)
                    .setCellValue(mitglied.getKategorieIElement().toString());
            row.createCell(11)
                    .setCellValue(mitglied.getKategorieIIElement().toString());
            Cell dateOfEntry = row.createCell(12);
            dateOfEntry.setCellValue(mitglied.getEintrittsdatum().toString());
            dateOfEntry.setCellStyle(dateCellStyle);
            row.createCell(13)
                    .setCellValue(mitglied.getIstVorstandsmitglied().getValue().toString());
        }

        // Resize all columns to fit the content size
        for(int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Write the output to a file
        FileOutputStream fileOut = new FileOutputStream(file.getParentFile() + "/" + file.getName());
        workbook.write(fileOut);
        fileOut.close();
        workbook.setForceFormulaRecalculation(true);
        Desktop.getDesktop().open(file);
        // Closing the workbook
        //workbook.close();
    }

    /**
     * exportiert die Teilnehmerliste zu einem übergebenen Termin
     *
     * @throws IOException
     */
    public static void exportTeilnehmerToExcel(Termin termin, User currentUser) throws IOException {
        File file = chooseFile("Teilnehmerliste");
        int rowNumber = 0;
        System.out.println(file.getName());

        String[] columns = {"MitgliedId", "Mitglied", "Kategorie", "E-Mail", "Anmeldestatus"};
        List<Teilnehmer> teilnehmerList = new ArrayList<>(DatabaseReader.getTeilnehmer(termin));

        Workbook workbook = new XSSFWorkbook();
        CreationHelper createHelper = workbook.getCreationHelper();

        // Create a Sheet
        Sheet sheet = workbook.createSheet("Teilnehmer");

        // mergedCells for Header Row
        CellRangeAddress mergedRegion = new CellRangeAddress(0, 0, 0, 4); // row 1 col A to C
        sheet.addMergedRegion(mergedRegion);
        CellRangeAddress mergedRegion2 = new CellRangeAddress(1, 1, 0, 4);
        sheet.addMergedRegion(mergedRegion2);

        // Create a Font for styling header cells
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);

        // Create a CellStyle with the font and specific color
        XSSFCellStyle style1 = (XSSFCellStyle) workbook.createCellStyle();
        style1.setFont(headerFont);
        //  style1.setFillBackgroundColor(IndexedColors.RED.getIndex());
        style1.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.index);
        style1.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style1.getFont().setColor(new XSSFColor(new java.awt.Color(243, 210, 80),
                new DefaultIndexedColorMap()));
        // gelb: 243, 210, 80
        // rot: 247, 136, 136

        Row terminRow = sheet.createRow(rowNumber++);
        Cell terminDatum = terminRow.createCell(0);
        if (termin.getZeitText() != null) {
            terminDatum.setCellValue("Termin vom " + termin.getDateAsLocalStringLong().getValue()
                    + " " + termin.getTerminText().getValue() + " (" + termin.getZeitText() + ")");
        } else {
            terminDatum.setCellValue("Termin vom " + termin.getDateAsLocalStringLong().getValue()
                    + " " + termin.getTerminText().getValue() + " (ganztags)");
        }
        terminDatum.setCellStyle(style1);


        Row termin2ndRow = sheet.createRow(rowNumber++);
        Cell terminDetails = termin2ndRow.createCell(0);
        terminDetails.setCellValue(termin.getOrt());
        terminDetails.setCellStyle(style1);

        // Create Row for User-Info
        Row userRow = sheet.createRow(rowNumber++);
        Cell userInfo = userRow.createCell(0);
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH.mm");
        Timestamp timestamp = new Timestamp(date.getTime());
        userInfo.setCellValue("erstellt: User#" + currentUser.getUserTxt() + " | " + sdf.format(timestamp));
        rowNumber++;

        // Create a Row
        Row headerRow = sheet.createRow(rowNumber++);
        // Create cells
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(style1);
        }

        // Create Cell Style for formatting Date
        CellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd.MM.yyyy"));
        // Create Other rows and cells with employees data
        rowNumber++;
        for (Teilnehmer teilnehmer : teilnehmerList) {
            Row row = sheet.createRow(rowNumber++);
            row.createCell(0).setCellValue("#" + teilnehmer.getMitglied().getId());
            row.createCell(1)
                    .setCellValue(teilnehmer.getMitglied().getNachName() + " " + teilnehmer.getMitglied().getVorname());
            row.createCell(2)
                    .setCellValue(teilnehmer.getMitglied().getKategorieIElement().toString() + " | "
                            + teilnehmer.getMitglied().getKategorieIIElement().toString());
            row.createCell(3)
                    .setCellValue(teilnehmer.getMitglied().getEmail());
            row.createCell(4)
                    .setCellValue(teilnehmer.getAnmeldungProperty().getValue().toString());
        }

        rowNumber++;

        Status teilnahmeStatus = new Status(5);
        ArrayList<StatusElement> statusListe = teilnahmeStatus.getElementsAsArrayList();
        int zaehler = 0;
        while (statusListe.size() > zaehler) {
            Row footerRow = sheet.createRow(rowNumber++);
            Cell cell = footerRow.createCell(0);
            cell.setCellValue("Total " + statusListe.get(zaehler).toString());
            cell.setCellStyle(style1);

            Cell cell1 = footerRow.createCell(1);
            cell1.setCellFormula("COUNTIF(E:E,\"" + statusListe.get(zaehler).toString() + "\")");
            cell1.setCellStyle(style1);

            zaehler++;
        }




        // Resize all columns to fit the content size
        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Write the output to a file
        FileOutputStream fileOut = new FileOutputStream(file.getParentFile() + "/" + file.getName());
        workbook.write(fileOut);
        fileOut.close();
        workbook.setForceFormulaRecalculation(true);
        Desktop.getDesktop().open(file);
        // Closing the workbook
        //workbook.close();
    }

    /**
     * öffnet einen "Save as"-Dialog
     * @param fileName gibt den vom
     * @return
     */
    public static File chooseFile(String fileName) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        Window stage = new Stage();
        fileChooser.setTitle("Speichern unter..");
        fileChooser.setInitialFileName(fileName + LocalDate.now() + ".xlsx");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Excel File", "*.xlsx"));
        File file = fileChooser.showSaveDialog(stage);
        return file;
    }

}

