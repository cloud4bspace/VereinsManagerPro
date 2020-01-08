package space.cloud4b.verein.view.termine;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;
import space.cloud4b.verein.MainApp;
import space.cloud4b.verein.controller.KalenderController;
import space.cloud4b.verein.model.verein.kalender.Teilnehmer;
import space.cloud4b.verein.model.verein.kalender.Termin;
import space.cloud4b.verein.model.verein.status.Status;
import space.cloud4b.verein.model.verein.status.StatusElement;
import space.cloud4b.verein.services.DatabaseOperation;
import space.cloud4b.verein.services.DatabaseReader;
import space.cloud4b.verein.services.Observer;
import space.cloud4b.verein.services.output.ExcelWriter;
import space.cloud4b.verein.view.browser.Browser;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Optional;

public class TerminViewController implements Observer {

    // allgemeine Instanzvariabeln
    private Stage dialogStage;
    private MainApp mainApp;
    private ArrayList<Termin> terminListe;
    private ArrayList<Teilnehmer> teilnehmerListe;
    private Termin termin = null;

    // UI-Variabeln (Verknüpfung mit Elementen des Userinterfaces)
    @FXML
    private ComboBox<Termin> terminAuswahlComboBox = new ComboBox<>();
    @FXML
    private Label letzteAenderungLabel;
    @FXML
    private TextField terminText;
    @FXML
    private DatePicker terminDatumPicker;
    @FXML
    private Slider stundenVonSlider = new Slider(0, 23, 12);
    @FXML
    private Slider minutenVonSlider = new Slider(0,59,30);
    @FXML
    private TextField stundenVonFeld = new TextField();
    @FXML
    private TextField minutenVonFeld = new TextField();
    @FXML
    private Slider stundenBisSlider = new Slider(0, 23, 12);
    @FXML
    private Slider minutenBisSlider = new Slider(0,59,30);
    @FXML
    private TextField stundenBisFeld = new TextField();
    @FXML
    private TextField minutenBisFeld = new TextField();
    @FXML
    private ComboBox<StatusElement> comboBoxKategorieI = new ComboBox<StatusElement>();
    @FXML
    private ComboBox<StatusElement> comboBoxKategorieII = new ComboBox<StatusElement>();
    @FXML
    private TextField terminOrt = new TextField();
    @FXML
    private TextArea terminDetails = new TextArea();
    @FXML
    private TableView<Teilnehmer> teilnehmerTabelle;
    @FXML
    private TableColumn<Teilnehmer, String> mitgliedSpalte;
    @FXML
    private TableColumn<Teilnehmer, Number> idSpalte;
    @FXML
    private TableColumn<Teilnehmer, StatusElement> anmeldeStatusSpalte;
    @FXML
    private Hyperlink doodleHyperlink = new Hyperlink("Anwesenheiten bearbeiten");
    @FXML
    private Label angemeldetLabel = new Label();
    @FXML
    private Label vielleichtLabel = new Label();
    @FXML
    private Label neinLabel = new Label();
    @FXML
    private Label anzMitgliederLabel = new Label();

    /**
     * Initialisierung der controller class
     */
    @FXML
    private void initialize() {
        doodleHyperlink.setTooltip(new Tooltip("Anwesenheiten dieses Termins bearbeiten"));
        terminListe = new ArrayList<>();
        terminListe = DatabaseReader.getTermineAsArrayList();

        terminAuswahlComboBox.getItems().addAll(terminListe);
        terminAuswahlComboBox.getSelectionModel().select(getIndexClosestToNow(terminListe));

        // Zeitangabe von/am
        stundenVonSlider.setTooltip(new Tooltip("Hallo"));
        stundenVonSlider.valueProperty().addListener((obs, oldval, newVal) ->
                stundenVonSlider.setValue(Math.round(newVal.doubleValue())));
        stundenVonFeld.textProperty()
                .bindBidirectional(stundenVonSlider.valueProperty(), new NumberStringConverter());
        stundenVonFeld.setTextFormatter(new TextFormatter<Integer>(change -> {
            // Deletion should always be possible.
            if (change.isDeleted()) {
                return change;
            }

            // How would the text look like after the change?
            String txt = change.getControlNewText();

            // Try parsing and check if the result is in [0, 64].
            try {
                int n = Integer.parseInt(txt);
                return 0 <= n && n <= 23 ? change : null;
            } catch (NumberFormatException e) {
                return null;
            }
        }));
        minutenVonSlider.valueProperty().addListener((obs, oldval, newVal) ->
                minutenVonSlider.setValue(Math.round(newVal.doubleValue())));
        minutenVonFeld.textProperty()
                .bindBidirectional(minutenVonSlider.valueProperty(), new NumberStringConverter());
        minutenVonFeld.setTextFormatter(new TextFormatter<Integer>(change -> {
            // Deletion should always be possible.
            if (change.isDeleted()) {
                return change;
            }

            // How would the text look like after the change?
            String txt = change.getControlNewText();

            // Try parsing and check if the result is in [0, 64].
            try {
                int n = Integer.parseInt(txt);
                return 0 <= n && n <= 59 ? change : null;
            } catch (NumberFormatException e) {
                return null;
            }
        }));

        // Zeitangabe bis
        stundenBisSlider.setTooltip(new Tooltip("Hallo"));
        stundenBisSlider.valueProperty().addListener((obs, oldval, newVal) ->
                stundenBisSlider.setValue(Math.round(newVal.doubleValue())));
        stundenBisFeld.textProperty()
                .bindBidirectional(stundenBisSlider.valueProperty(), new NumberStringConverter());
        minutenBisSlider.valueProperty().addListener((obs, oldval, newVal) ->
                minutenBisSlider.setValue(Math.round(newVal.doubleValue())));
        minutenBisFeld.textProperty()
                .bindBidirectional(minutenBisSlider.valueProperty(), new NumberStringConverter());
        setTermin(terminListe.get(getIndexClosestToNow(terminListe)));

        // Teilnehmerkategorien
        Status kategorieI = new Status(2);
        Status kategorieII = new Status(4);
        comboBoxKategorieI.getItems().addAll(kategorieI.getElementsAsArrayList());
        comboBoxKategorieII.getItems().addAll(kategorieII.getElementsAsArrayList());

    }

    /**
     * Setzt die Referenz zur MainApp
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        mainApp.getKalenderController().Attach(this);
        showTeilnehmerListe(terminListe.get(getIndexClosestToNow(terminListe)));
    }

    private int getIndexClosestToNow(ArrayList<Termin> terminLIste) {
        int indexClosestToNow = -1;
        int i = 0;
        while (indexClosestToNow < 0 && this.terminListe.size() > i) {
            // wenn heute, dann dieses, sonst das nächste after
            if (terminListe.get(i).getDatum().isEqual(LocalDate.now())) {
                indexClosestToNow = i;
            }
            if (terminListe.get(i).getDatum().isAfter(LocalDate.now())) {
                indexClosestToNow = i;
            }
            i++;
        }
        return indexClosestToNow;
    }

    // Hyperlink zum Doodle-Formular
    @FXML
    public void handleLinkToDoodle() {
        Stage stage = new Stage();
        stage.setTitle("Doodle");
        Scene scene = new Scene(new Browser("https://www.cloud4b.space/VereinsManager/Doodle/doodleTermin.php" +
                "?TerminId=" + termin.getTerminId()), 750, 500, Color.web("#666970"));
        stage.setScene(scene);
        stage.show();
    }
    public void terminAuswahlComboBoxAction() {
        setTermin(terminAuswahlComboBox.getValue());
        showTeilnehmerListe(terminAuswahlComboBox.getValue());
    }
    public void setTermin(Termin termin) {

        // wenn kein gültiger Termin übergeben wird
        if (termin == null) {
            if (this.termin != null) {
                // den letzten ausgewählten Termin übernehmen
                termin = this.termin;
            } else {
                // den Termin auswählen, der am nächsten am heutigen Datum liegt
                termin = terminListe.get(getIndexClosestToNow(terminListe));
                this.termin = termin;
            }
        } else {
            this.termin = termin;
        }

        terminAuswahlComboBox.getSelectionModel().select(termin);
        letzteAenderungLabel.setText(termin.getLetzteAenderung());
        terminDatumPicker.setValue(termin.getDatum());
        terminText.setText(termin.getTextProperty().getValue());
        terminOrt.setText(termin.getOrtProperty().getValue());
        terminDetails.setText(termin.getDetailsProperty().getValue());
        if (termin.getTerminZeitVon() != null) {
            stundenVonFeld.setText(Integer.toString(termin.getTerminZeitVon().getHour()));
            minutenVonFeld.setText(Integer.toString(termin.getTerminZeitVon().getMinute()));
        } else {
            stundenVonFeld.clear();
            minutenVonFeld.clear();
        }
        if(termin.getTerminZeitBis() != null) {
            stundenBisFeld.setText(Integer.toString(termin.getTerminZeitBis().getHour()));
            minutenBisFeld.setText(Integer.toString(termin.getTerminZeitBis().getMinute()));
        } else {
            stundenBisFeld.clear();
            minutenBisFeld.clear();
        }

        if(termin.getKatIElement()!=null) {
            comboBoxKategorieI.getSelectionModel().select(termin.getKatIElement());
        }
        if(termin.getKatIIElement()!=null) {
            comboBoxKategorieII.getSelectionModel().select(termin.getKatIIElement());
        }


        // Tab Kontrolle/Planung
        angemeldetLabel.setText(Integer.toString(DatabaseReader.getAnzAnmeldungen(termin)));
        vielleichtLabel.setText(Integer.toString(DatabaseReader.getAnzVielleicht(termin)));
        neinLabel.setText(Integer.toString(DatabaseReader.getAnzNein(termin)));
        anzMitgliederLabel.setText(Integer.toString(DatabaseReader.readAnzahlMitglieder()));
     //   comboBoxKategorieI.getSelectionModel().select(termin.getKatIElement().getStatusElementKey());
    }

    public void showTeilnehmerListe(Termin termin) {
        this.teilnehmerListe = DatabaseReader.getTeilnehmer(termin, mainApp.getAdressController().getMitgliederListe());
        teilnehmerTabelle.setItems(FXCollections.observableArrayList(teilnehmerListe));
        idSpalte.setCellValueFactory(
                cellData -> cellData.getValue().getMitglied().getIdProperty());
        mitgliedSpalte.setCellValueFactory(
                cellData -> cellData.getValue().getMitglied().getKurzbezeichnungProperty());
        anmeldeStatusSpalte.setCellValueFactory(
                cellData -> cellData.getValue().getAnmeldungProperty());
        teilnehmerTabelle.setRowFactory(row -> new TableRow<Teilnehmer>() {
            @Override
            public void updateItem(Teilnehmer item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null) {
                    setStyle("");
                } else if (item.getAnmeldungWert() == 1) {
                    setStyle("-fx-background-color: #8FAD88;");
                } else if (item.getAnmeldungWert() == 2) {
                    setStyle("-fx-background-color: #F78888;");
                } else if (item.getAnmeldungWert() == 3) {
                    setStyle("-fx-background-color: #F3D250;");
                }
            }
        });
    }

    /**
     * neuen Termin erfassen
     */
    public void handleErfassenButton() {
        mainApp.showTerminErfassen();
    }

    /**
     * Termin löschen
     */
    public void handleLoeschen() {
        // Warnmeldung anzeigen und Löschung bestätigen lassen
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initOwner(dialogStage);
        alert.setTitle("Löschen bestätigen");
        alert.setHeaderText("Willst Du den Termin wirklich löschen?");
        alert.setContentText("Löschen von\n\n" + termin + "\n\nmit OK bestätigen oder abbrechen");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            DatabaseOperation.deleteTermin(this.termin, mainApp.getCurrentUser());
            this.termin = null;
            setTermin(null);
        }
    }

    /**
     * Den aktuellen Termin neu laden, wenn der Reset-Button betätigt wird.
     */
    public void handleResetButton() {
        setTermin(termin);
    }

    /**
     * überprüft die Daten und speichert sie
     */
    public void handleSpeichernButton() {
        if (isInputValid()) {
           // unsavedChanges = false;
            System.out.println(terminDatumPicker.getValue());
            System.out.println(termin);
            System.out.println("Stunde: " + stundenVonFeld.getText());
            System.out.println("Minute: " + minutenVonFeld.getText());
            termin.setDatum(Date.valueOf(terminDatumPicker.getValue()).toLocalDate());

            if(stundenVonFeld.getText().length() > 0 && minutenVonFeld.getText().length() > 0) {
                termin.setZeit(LocalDateTime.of(terminDatumPicker.getValue(),
                        LocalTime.of(Integer.parseInt(stundenVonFeld.getText()),
                                Integer.parseInt(minutenVonFeld.getText()))));
            } else {
                termin.setZeit(null);
            }

            if(stundenBisFeld.getText().length() > 0 && minutenBisFeld.getText().length() > 0) {
                termin.setZeitBis(LocalDateTime.of(terminDatumPicker.getValue(),
                        LocalTime.of(Integer.parseInt(stundenBisFeld.getText()),
                                Integer.parseInt(minutenBisFeld.getText()))));
            } else {
                termin.setZeitBis(null);
            }
            termin.setTerminText(terminText.getText());
            termin.setTeilnehmerKatI(comboBoxKategorieI.getValue());
            termin.setTeilnehmerKatII(comboBoxKategorieII.getValue());
            termin.setOrt(terminOrt.getText());
            termin.setDetails(terminDetails.getText());
            termin.setTeilnehmerKatI(comboBoxKategorieI.getValue());
            termin.setTeilnehmerKatII(comboBoxKategorieII.getValue());
            termin.setTrackChangeTimestamp(Timestamp.valueOf(LocalDateTime.now()));
            termin.setTrackChangeUsr(System.getProperty("user.name"));
            DatabaseOperation.updateTermin(termin, mainApp.getCurrentUser());
            letzteAenderungLabel.setText(termin.getLetzteAenderung());
            terminAuswahlComboBox.getItems().addAll(mainApp.getKalenderController().getTermineAsArrayList());
            //terminAuswahlComboBox.getSelectionModel().clearAndSelect(termin);
            terminAuswahlComboBox.getSelectionModel().select(termin);
            // mainApp.getMainFrameController().setInfo("Änderungen gespeichert", "OK", true);

            mainApp.getMainFrameController().setMeldungInListView("Änderungen gespeichert", "OK");
        }
    }

    /**
     * exportiere Teilnehmerliste nach excel
     */
    public void handleExportTeilnehmerToExcel() {
        try {
            ExcelWriter.exportTeilnehmerToExcel(termin, mainApp.getCurrentUser(), teilnehmerListe);
        } catch (IOException e) {
            // mainApp.getMainFrameController().setInfo("Export ist fehlgeschlagen", "NOK", true);
            mainApp.getMainFrameController().setMeldungInListView("Export ist fehlgeschlagen"
                    , "NOK");
        }

    }

    /**
     * Validates the user input in the text fields.
     *
     * @return true if the input is valid
     */
    private boolean isInputValid() {
        String errorMeldung = "";
        Boolean isValid = true;

        if (terminDatumPicker == null) {
            errorMeldung += "Datumsangabe ist ungültig!";
            isValid = false;
        }
        if (terminText.getText() == null || terminText.getText().length() == 0) {
            errorMeldung += "Text ungültig!";
            isValid = false;
        }

        if(!isValid && errorMeldung.length()>0) {
            // mainApp.getMainFrameController().setInfo(errorMeldung, "NOK", true);
            mainApp.getMainFrameController().setMeldungInListView(errorMeldung, "NOK");
        }
        return isValid;
    }


    @Override
    public void update(Object o) {
        System.out.println("TerminController Update-Meldung erhalten von " + o);

            KalenderController kc = (KalenderController) o;
            Platform.runLater(new Runnable() { // TODO
                @Override
                public void run() {

                    terminAuswahlComboBox.getItems().remove(0, terminListe.size());
                    terminListe = mainApp.getKalenderController().getTermineAsArrayList();
                    terminAuswahlComboBox.getItems().addAll(terminListe);
                    terminAuswahlComboBox.getSelectionModel().select(termin);
                    teilnehmerTabelle.setItems(FXCollections.observableArrayList(DatabaseReader.getTeilnehmer(termin,
                            mainApp.getAdressController().getMitgliederListe())));
                    if (termin != null) {
                        setTermin(termin);
                    }
                    // Termin ist nicht mehr dasselbe Objekt.. deshalb muss ich mit der ID arbeiten

                }
            });


    }

}
