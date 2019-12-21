package space.cloud4b.verein.view.mainframe;

import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import space.cloud4b.verein.MainApp;
import space.cloud4b.verein.controller.AdressController;
import space.cloud4b.verein.controller.KalenderController;
import space.cloud4b.verein.einstellungen.Einstellung;
import space.cloud4b.verein.model.verein.meldung.Meldung;
import space.cloud4b.verein.services.Observer;
import space.cloud4b.verein.services.output.ExcelWriter;
import space.cloud4b.verein.view.browser.Browser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class MainFrameController implements Observer {

    int anzMitglieder = 0;
    int anzTermine = 0;

    // Reference to the main application
    private MainApp mainApp;

    @FXML
    private Label titleLabel;
    @FXML
    private Label infoLabel;
    @FXML
    private MenuBar hMenuBarTop;
    @FXML
    private MenuItem exitMenuItem;
    @FXML
    private MenuItem infoMenuItem;
    @FXML
    private MenuItem helpMenuItem;
    @FXML
    private MenuItem linksammlungMenuItem;
    @FXML
    private MenuItem javaDocMenuItem;
    @FXML
    private Label dateLabel;
    @FXML
    private Label sessionLabel;
    @FXML
    private VBox vMenuBarLeftContainer;
    @FXML
    private TextArea meldungAusgabeText;
    @FXML
    private ListView<Meldung> meldungAusgabeListView;
    @FXML
    private Label circleLabelI;
    @FXML
    private Label circleLabelII;
    @FXML
    private Button homeButton;
    @FXML
    private Menu mitgliederMenu;
    @FXML
    private Menu termineMenu;
    @FXML
    private Menu kontrolleMenu;
    @FXML
    private Menu auswertungenMenu;
    @FXML
    private Menu exportMenu;
    @FXML
    private MenuItem mitgliederExcelMenuItem;
    @FXML
    private ImageView clubLogoImage;

    /**
     * Is called by the main application to give a reference back to itself.
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        mainApp.getAdressController().Attach(this);
        mainApp.getKalenderController().Attach(this);
        this.titleLabel.setText(Einstellung.getVereinsName()); // bei Initialize geht es nicht...
        this.dateLabel.setText(LocalDate.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)));
        this.sessionLabel.setText("Session#" + mainApp.getCurrentUser().getSessionId());
        this.anzMitglieder = mainApp.getAdressController().getAnzahlMitglieder();
        circleLabelI.setText(this.anzMitglieder + " Mitglieder");
        circleLabelI.setContentDisplay(ContentDisplay.CENTER);

        this.anzTermine = mainApp.getKalenderController().getAnzahlTermine();
        circleLabelII.setText(this.anzTermine + " Termine");
        circleLabelII.setContentDisplay(ContentDisplay.CENTER);

        infoLabel.setText(mainApp.getCurrentUser().toString());
        meldungAusgabeText.setWrapText(true);
        setMeldungInListView("Herzlich willkommen\n"
                + mainApp.getCurrentUser().getUserName() + "!", "INFO");
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        this.mainApp = mainApp;
        Text iconTxt;

        iconTxt = GlyphsDude.createIcon(FontAwesomeIcon.GROUP, "14px");
        iconTxt.setFill(Color.GRAY);
        mitgliederMenu.setGraphic(iconTxt);
        mitgliederMenu.setText("Mitglieder");

        iconTxt = GlyphsDude.createIcon(FontAwesomeIcon.CALENDAR, "14px");
        iconTxt.setFill(Color.GRAY);
        termineMenu.setGraphic(iconTxt);
        termineMenu.setText("Termine");

        iconTxt = GlyphsDude.createIcon(FontAwesomeIcon.CHECK, "14px");
        iconTxt.setFill(Color.GRAY);
        kontrolleMenu.setGraphic(iconTxt);
        kontrolleMenu.setText("Kontrollen");

        iconTxt = GlyphsDude.createIcon(FontAwesomeIcon.LINE_CHART, "14px");
        iconTxt.setFill(Color.GRAY);
        auswertungenMenu.setGraphic(iconTxt);
        auswertungenMenu.setText("Analyse");

        iconTxt = GlyphsDude.createIcon(FontAwesomeIcon.FILE_EXCEL_ALT, "14px");
        iconTxt.setFill(Color.GRAY);
        exportMenu.setGraphic(iconTxt);
        exportMenu.setText("Exportieren");

        iconTxt = GlyphsDude.createIcon(FontAwesomeIcon.BACKWARD,"14px");
        iconTxt.setFill(Color.GRAY);
       // iconTxt.setStyle("-fx-end-margin: 20px");
        homeButton.setGraphic(iconTxt);
        homeButton.setText("Home");

        iconTxt = GlyphsDude.createIcon(FontAwesomeIcon.USER, "20px");
        iconTxt.setFill(javafx.scene.paint.Color.WHITE);
        infoLabel.setGraphic(iconTxt);
        infoLabel.setText(System.getProperty("user.name"));

        iconTxt = GlyphsDude.createIcon(FontAwesomeIcon.SIGN_OUT, "15px");
        iconTxt.setFill(Color.BLACK);
        exitMenuItem.setGraphic(iconTxt);
        exitMenuItem.setText("beenden");

        iconTxt = GlyphsDude.createIcon(FontAwesomeIcon.INFO, "15px");
        iconTxt.setFill(Color.BLACK);
        infoMenuItem.setGraphic(iconTxt);

        iconTxt = GlyphsDude.createIcon(FontAwesomeIcon.QUESTION, "15px");
        iconTxt.setFill(Color.BLACK);
        helpMenuItem.setGraphic(iconTxt);

        iconTxt = GlyphsDude.createIcon(FontAwesomeIcon.LINK, "15px");
        iconTxt.setFill(Color.BLACK);
        linksammlungMenuItem.setGraphic(iconTxt);

        iconTxt = GlyphsDude.createIcon(FontAwesomeIcon.CHROME, "15px");
        iconTxt.setFill(Color.BLACK);
        javaDocMenuItem.setGraphic(iconTxt);

        iconTxt = GlyphsDude.createIcon(FontAwesomeIcon.CALENDAR, "20px");
        iconTxt.setFill(Color.GRAY);
        this.dateLabel.setGraphic(iconTxt);

        try {
            FileInputStream inputStream = new FileInputStream("ressources/images/logo/ClubLogo01.png");
            Image image = new Image(inputStream);
            clubLogoImage.setImage(image);
        } catch (FileNotFoundException e) {
        }

        meldungAusgabeListView.setCellFactory(param -> new ListCell<Meldung>() {
            protected void updateItem(Meldung item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getMeldungOutputString());
                    switch (item.getMeldungType()) {
                        case "OK":
                            setStyle("-fx-background-color: #badcb3;");
                            break;
                        case "NOK":
                            setStyle("-fx-background-color: #ffdada;");
                            break;
                        default:
                            setStyle("-fx-background-color: #ffffff;");
                            break;
                    }
                }
            }
        });
    }

    // Top-Menu
    /**
     * Opens Einstellungen-Dialog
     */

    /**
     * Opens an about dialog.
     */
    @FXML
    private void showInfo() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Vereins-App");
        alert.setHeaderText("über..");
        //System.getProperty("user.name");
        alert.setContentText(System.getProperty("user.name") + "\nWebsite: https://cloud4b.space");
        alert.showAndWait();
    }



    /**
     * Oeffnet ein Browserfenster mit der Hilfe
     */
    @FXML
    private void handleHilfe() {
        System.out.println("showHilfe");
        Stage stage = new Stage();
        stage.setTitle("Web View");
        Scene scene = new Scene(new Browser("https://www.cloud4b.space/VereinsManager/Hilfe/help.html"), 750, 500, Color.web("#666970"));
        stage.setScene(scene);
        scene.getStylesheets().add("../css/BrowserToolbar.css");
        //TODO Path to stylesheet not correct...
        stage.show();

    }

    /**
     * Oeffnet ein Browserfenster mit der JavaDoc
     */
    @FXML
    private void handleJavaDoc() {
        Stage stage = new Stage();
        stage.setTitle("JavaDoc");
        Scene scene = new Scene(new Browser("https://www.cloud4b.space/VereinsManager/Hilfe/JavaDoc/"), 750, 500, Color.web("#666970"));
        stage.setScene(scene);
        scene.getStylesheets().add("../css/BrowserToolbar.css");
        //TODO Path to stylesheet not correct...
        stage.show();
    }

    /**
     * Oeffnet ein Browserfenster mit der Linksammlung
     */
    @FXML
    private void handleLinksammlung() {
        Stage stage = new Stage();
        stage.setTitle("JavaDoc");
        Scene scene = new Scene(new Browser("https://www.cloud4b.space/VereinsManager/Hilfe/bookmarks.html"), 750, 500, Color.web("#666970"));
        stage.setScene(scene);
        scene.getStylesheets().add("../view/css/BrowserToolbar.css");
        //TODO Path to stylesheet not correct...
        stage.show();
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleBeenden() {
        System.exit(0);
    }


    // Linker Menu-/Navigationsbereich
    @FXML
    private void handleRefresh() {
        System.out.println("Restarting app!");
        mainApp.getPrimaryStage().close();
        Platform.runLater(() -> mainApp.start(new Stage()));
    }

    /* Menupunkte unter Mitglieder
     * Pos#01: handleMitgliederbereich() --> Mitgliederbereich öffnen
     * Pos#02: handleExportMitglieder() --> Export nach Excel
     * Pos#03: handleShowBirthdayStatistics --> Diagramm Geburtstagsstatistik
     * Pos#04: handleShowKatIStatistics --> Kuchendiagramm Mitgliederkategarie I
     */

    /**
     * Oeffnet das Fenster des Mitgliederbereichs
     */
    @FXML
    private void handleMitgliederbereich() {
        mainApp.showMitgliedView(null);
    }

    /**
     * Exportiert die Mitgliederliste nach Excel
     */
    @FXML
    private void handleExportMitglieder() throws IOException {
        ExcelWriter.exportMirgliederToExcel();
    }

    /**
     * Opens the birthday statistics.
     */
    @FXML
    private void handleShowBirthdayStatistics() {
        mainApp.showBirthdayStatistics();
    }

    /**
     * Öffnet die Statistik Kat I
     */
    @FXML
    private void handleShowKatIStatistics() {
        mainApp.showMemberKatIStatistics();
    }


    /* Menupunkte unter Termine
     * Pos#01: handleTerminbereich() --> Mitgliederbereich öffnen
     * handleDoodle() --> Anwesenheits-Matrix öffnen
     * handleKontrolle() --> Kontrollmatrix öffnen
     */

    /**
     * Oeffnet das Fenster des Terminbereichs
     */
    @FXML
    private void handleTerminbereich() {
        mainApp.showTerminEditDialog();
    }

    /**
     * Oeffnet die Kalenderübersicht
     */
    @FXML
    private void handelKalenderansicht() {
        mainApp.showTerminOverview();
    }

    /**
     * Oeffnet ein Browserfenster mit der Terminmatrix
     */
    @FXML
    private void handleDoodle() {
        Stage stage = new Stage();
        stage.setTitle("Doodle");
        Scene scene = new Scene(new Browser("https://www.cloud4b.space/VereinsManager/Doodle/doodle.php"), 750, 500, Color.web("#666970"));
        stage.setScene(scene);
        scene.getStylesheets().add("../view/css/BrowserToolbar.css");
        //TODO Path to stylesheet not correct...
        stage.show();
    }

    /**
     * Oeffnet ein Browserfenster mit der Präsenzkontrolle
     */
    @FXML
    private void handleKontrolle() {
        Stage stage = new Stage();
        stage.setTitle("Präsenzkontrolle");
        Scene scene = new Scene(new Browser("https://www.cloud4b.space/VereinsManager/Kontrolle/kontrolluebersicht.php"), 750, 500, Color.web("#666970"));
        stage.setScene(scene);
        scene.getStylesheets().add("../view/css/BrowserToolbar.css");
        //TODO Path to stylesheet not correct...
        stage.show();
    }

    public void setMeldungInListView(String meldungText, String meldungTyp) {
        meldungAusgabeListView.getItems().add(0, new Meldung(meldungText, meldungTyp));
    }
    public void setInfo(String infoText, String infoTyp) {
        this.meldungAusgabeText.setText(infoText);
        switch (infoTyp) {
            case "OK":
                this.meldungAusgabeText.setStyle("-fx-text-fill: #4FA67B");
                break;
            case "NOK":
                this.meldungAusgabeText.setStyle("-fx-text-fill: #FF5F67");
                break;
            case "Info":
                this.meldungAusgabeText.setStyle("-fx-text-fill: #708ca6");
                break;
            default:
                this.meldungAusgabeText.setStyle("-fx-text-fill: #708ca6");
                break;
        }
    }

    public void setInfo(String infoText, String infoTyp, boolean add) {
        if(add == true){
            this.meldungAusgabeText.setText("*** " +LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + " ***\n" + infoText + "\n\n" + this.meldungAusgabeText.getText());
       // this.meldungAusgabeText.appendText("\n" + LocalTime.now() + " - " + infoText);
       // this.meldungAusgabeText.insertText(LocalTime.now() + " - " + infoText);
        } else {
            this.meldungAusgabeText.setText(infoText);
        }
        switch (infoTyp) {
            case "OK":
                this.meldungAusgabeText.setStyle("-fx-text-fill: #4FA67B");
                break;
            case "NOK":
                this.meldungAusgabeText.setStyle("-fx-text-fill: #FF5F67");
                break;
            case "Info":
                this.meldungAusgabeText.setStyle("-fx-text-fill: #708ca6");
                break;
            default:
                this.meldungAusgabeText.setStyle("-fx-text-fill: #708ca6");
                break;
        }
    }

    /**
     * wird ausgeführt, wenn eine der überwachte Instanzen eine Notify-Benachrichtigung absetzt.
     * @param o das Objekt des überwachten Objekts
     */
    @Override
    public void update(Object o) {

        // wenn die Benachrichtigung vom AdressController kommt
        if (o instanceof AdressController) {
            anzMitglieder = mainApp.getAdressController().getAnzahlMitglieder();
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    circleLabelI.setText(anzMitglieder + " Mitglieder");
                }
            });
        }

        // wenn die Benachrichtigung vom KalenderController kommt
        if (o instanceof KalenderController) {
            anzTermine = mainApp.getKalenderController().getAnzahlTermine();
            Platform.runLater(new Runnable() {
                // Anzahl Termine im UI aktualisieren
                @Override
                public void run() {
                    circleLabelII.setText(anzTermine + " Termine (ab heute)");
                }
            });
        }
    }
}
