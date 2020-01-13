package space.cloud4b.verein.services.output;


import javafx.application.Application;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
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

import java.awt.*;
import java.io.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Die abstrakte Klasse ExcelWriter stellt Methoden zur Verfügung, um Daten im Excel-Format zu exportieren.
 *
 * @author Bernhard Kämpf & Serge Kaulitz
 * @version 2020-01-03
 */
public abstract class ExcelWriter extends Application {


    /**
     * exportiert die übergebene Mitgliederliste in ein Excel-File
     *
     * @param mitgliedArrayList - die Mitgliederliste als ArrayList<Mitglied>
     * @throws IOException
     */
    public static void exportMitgliederToExcel(ArrayList<Mitglied> mitgliedArrayList) throws IOException {
        File file = chooseFile("Mitgliederliste");
        System.out.println(file.getName());

        String[] columns = {"Bild", "Name", "Vorname", "Anrede", "Adresse", "Adresszusatz",
                "PLZ", "Ort", "E-Mail", "Telefon", "Geburtsdatum", "Kat I", "Kat II", "Eintrittsdatum", "Vorstandsmitglied"};
        List<Mitglied> mitgliedList = mitgliedArrayList;

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
        dateCellStyle.setVerticalAlignment(VerticalAlignment.TOP);
        // Create Other rows and cells with employees data
        int rowNum = 1;
        CellStyle cellStyle1 = workbook.createCellStyle();
        cellStyle1.setVerticalAlignment(VerticalAlignment.TOP);
        for(Mitglied mitglied: mitgliedList) {
            Row row = sheet.createRow(rowNum);
            row.setHeightInPoints(62);
            try {
                InputStream inputStream = new FileInputStream("ressources/images/profilbilder/ProfilBild_" +
                        mitglied.getId() + ".png");
                byte[] imageBytes = IOUtils.toByteArray(inputStream);
                int pictureureIdx = workbook.addPicture(imageBytes, Workbook.PICTURE_TYPE_PNG);
                inputStream.close();
                CreationHelper helper = workbook.getCreationHelper();
                Drawing drawing = sheet.createDrawingPatriarch();
                ClientAnchor anchor = helper.createClientAnchor();
                anchor.setCol1(0); //Column B
                anchor.setRow1(rowNum); //Row 3
                anchor.setCol2(1); //Column C
                anchor.setRow2(rowNum + 1); //Row 4
                anchor.setAnchorType(ClientAnchor.AnchorType.MOVE_AND_RESIZE);
                //Creates a picture
                Picture pict = drawing.createPicture(anchor, pictureureIdx);
            } catch (FileNotFoundException e) {
                try {
                    InputStream inputStream = new FileInputStream("ressources/images/profilbilder/ProfilBild_" +
                            mitglied.getId() + ".jpg");
                    byte[] imageBytes = IOUtils.toByteArray(inputStream);
                    int pictureureIdx = workbook.addPicture(imageBytes, Workbook.PICTURE_TYPE_PNG);
                    inputStream.close();
                    CreationHelper helper = workbook.getCreationHelper();
                    Drawing drawing = sheet.createDrawingPatriarch();
                    ClientAnchor anchor = helper.createClientAnchor();
                    anchor.setCol1(0); //Column B
                    anchor.setRow1(rowNum); //Row 3
                    anchor.setCol2(1); //Column C
                    anchor.setRow2(rowNum + 1); //Row 4
                    anchor.setAnchorType(ClientAnchor.AnchorType.MOVE_AND_RESIZE);
                    //Creates a picture
                    Picture pict = drawing.createPicture(anchor, pictureureIdx);
                } catch (FileNotFoundException e1) {
                    InputStream inputStream = new FileInputStream("ressources/images/profilbilder/Dummy.png");
                    byte[] imageBytes = IOUtils.toByteArray(inputStream);
                    int pictureureIdx = workbook.addPicture(imageBytes, Workbook.PICTURE_TYPE_PNG);
                    inputStream.close();
                    CreationHelper helper = workbook.getCreationHelper();
                    Drawing drawing = sheet.createDrawingPatriarch();
                    ClientAnchor anchor = helper.createClientAnchor();
                    anchor.setCol1(0); //Column B
                    anchor.setRow1(rowNum); //Row 3
                    anchor.setCol2(1); //Column C
                    anchor.setRow2(rowNum + 1); //Row 4
                    anchor.setAnchorType(ClientAnchor.AnchorType.MOVE_AND_RESIZE);
                    //Creates a picture
                    Picture pict = drawing.createPicture(anchor, pictureureIdx);
                }
            }

            Cell c01 = row.createCell(1);
            c01.setCellValue(mitglied.getNachName());
            c01.setCellStyle(cellStyle1);

            Cell c02 = row.createCell(2);
            c02.setCellValue(mitglied.getVorname());
            c02.setCellStyle(cellStyle1);

            Cell c03 = row.createCell(3);
            c03.setCellValue(mitglied.getAnredeElement().toString());
            c03.setCellStyle(cellStyle1);

            Cell c04 = row.createCell(4);
            c04.setCellValue(mitglied.getAdresse());
            c04.setCellStyle(cellStyle1);

            Cell c05 = row.createCell(5);
            c05.setCellValue(mitglied.getAdresszusatz());
            c05.setCellStyle(cellStyle1);

            Cell c06 = row.createCell(6);
            c06.setCellValue(mitglied.getPlz());
            c06.setCellStyle(cellStyle1);

            Cell c07 = row.createCell(7);
            c06.setCellValue(mitglied.getOrt());
            c07.setCellStyle(cellStyle1);

            Cell c08 = row.createCell(8);
            c08.setCellValue(mitglied.getEmail());
            c08.setCellStyle(cellStyle1);

            Cell c09 = row.createCell(9);
            c09.setCellValue(mitglied.getTelefon());
            c09.setCellStyle(cellStyle1);

            Cell dateOfBirthCell = row.createCell(10);
            if(mitglied.getGeburtsdatum()!=null) {
                dateOfBirthCell.setCellValue(mitglied.getGeburtsdatum().toString());
            } else {
                dateOfBirthCell.setCellValue("na");
            }
            dateOfBirthCell.setCellStyle(dateCellStyle);

            Cell c11 = row.createCell(11);
            c11.setCellValue(mitglied.getKategorieIElement().toString());
            c11.setCellStyle(cellStyle1);

            Cell c12 = row.createCell(12);
            c12.setCellValue(mitglied.getKategorieIIElement().toString());
            c12.setCellStyle(cellStyle1);

            Cell dateOfEntry = row.createCell(13);
            dateOfEntry.setCellValue(mitglied.getEintrittsdatum().toString());
            dateOfEntry.setCellStyle(dateCellStyle);
            row.createCell(14)
                    .setCellValue(mitglied.getIstVorstandsmitglied().getValue().toString());

            rowNum++;
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
    }

    /**
     * exportiert die Teilnehmerliste zu einem übergebenen Termin
     *
     * @param termin         - das übergebene Termindatum
     * @param currentUser    - der angemeldete User
     * @param teilnehmerList - die Liste der Teilnehmer als ArrayList<Teilnehmer>
     * @throws IOException
     */
    public static void exportTeilnehmerToExcel(Termin termin, User currentUser
            , ArrayList<Teilnehmer> teilnehmerList) throws IOException {
        File file = chooseFile("Teilnehmerliste");
        int rowNumber = 0;
        System.out.println(file.getName());

        String[] columns = {"Mitglied", "Kategorie", "E-Mail", "Anmeldestatus", "Bemerkungen"};

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
        dateCellStyle.setVerticalAlignment(VerticalAlignment.TOP);
        // Create Other rows and cells with employees data
        rowNumber++;
        for (Teilnehmer teilnehmer : teilnehmerList) {
            Row row = sheet.createRow(rowNumber++);
            row.createCell(0).setCellValue("#" + teilnehmer.getMitglied().getId()
                    + " " + teilnehmer.getMitglied().getNachName()
                    + " " + teilnehmer.getMitglied().getVorname()
                    + " " + teilnehmer.getMitglied().getPlz() + " " + teilnehmer.getMitglied().getOrt());
            row.createCell(1)
                    .setCellValue(teilnehmer.getMitglied().getKategorieIElement().toString() + " | "
                            + teilnehmer.getMitglied().getKategorieIIElement().toString());
            row.createCell(2)
                    .setCellValue(teilnehmer.getMitglied().getEmail());
            row.createCell(3)
                    .setCellValue(teilnehmer.getAnmeldungProperty().getValue().toString());
            row.createCell(4)
                    .setCellValue(teilnehmer.getAnmeldungTextProperty().getValue());

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
            cell1.setCellFormula("COUNTIF(D:D,\"" + statusListe.get(zaehler).toString() + "\")");
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
     * @param fileName der standardmässig vorgeschlagene Filename
     * @return gibt ein neues File mit dem vom User gewählten Namen zurück
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

