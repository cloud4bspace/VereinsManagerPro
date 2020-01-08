package space.cloud4b.verein.view.mitglieder;


import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import space.cloud4b.verein.MainApp;
import space.cloud4b.verein.controller.AdressController;
import space.cloud4b.verein.model.verein.adressbuch.Mitglied;
import space.cloud4b.verein.model.verein.status.Status;
import space.cloud4b.verein.model.verein.status.StatusElement;
import space.cloud4b.verein.services.DatabaseOperation;
import space.cloud4b.verein.services.Observer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.Optional;


/**
 * Die Klasse MitgleidViewController ist verknüpft mit dem JavaFX-Unserinterfac MitgliedView.fxml
 * Sie versorgt die Felder und Tabellen mit Daten und behandelt die Action-Events, welche aus
 * dem Userinterface ausgelöst werden.
 * Sie wird von der Controller-Klasse AdressController benachrichtigt, wenn Datensätze geändert wurden
 * und aktualisiert werden müssen.
 *
 * @author Bernhard Kämpf und Serge Kaulitz
 * @version 2019-12-17
 */
// TODO - nach dem Erfassen eines neuen Kontakts sollte dieser angezeigt werden
// TODO - nach dem Speichern von Änderungen funktioniert der Filter nicht mehr..
public class MitgliedViewController implements Observer {

    // allgemeine Instanzvariabeln
    private MainApp mainApp;
    private Stage dialogStage;
    private Mitglied aktuellesMitglied = null;
    private ArrayList<Mitglied> mitgliedArrayList;
    boolean unsavedChanges = false;

    // UI-Variabeln (Verknüpfung mit Elementen des Userinterfaces)
    @FXML
    public ComboBox<StatusElement> comboBoxAnrede = new ComboBox<StatusElement>();
    @FXML
    public ComboBox<StatusElement> comboBoxKategorieI = new ComboBox<StatusElement>();
    @FXML
    public ComboBox<StatusElement> comboBoxKategorieII = new ComboBox<StatusElement>();
    @FXML
    private TextField filterField;
    @FXML
    private TableView<Mitglied> mitgliedTabelle;
    @FXML
    private TableColumn<Mitglied, Number> idSpalte;
    @FXML
    private TableColumn<Mitglied, String> vornameSpalte;
    @FXML
    private TableColumn<Mitglied, String> nachnameSpalte;
    @FXML
    private TableColumn<Mitglied, String> ortSpalte;
    @FXML
    private Label idLabel;
    @FXML
    private Label letzteAenderungLabel;
    @FXML
    private TextField nachNameFeld;
    @FXML
    private TextField vorNameFeld;
    @FXML
    private TextField adresseFeld;
    @FXML
    private TextField adressZusatzFeld;
    @FXML
    private TextField plzFeld;
    @FXML
    private TextField ortFeld;
    @FXML
    private TextArea bemerkungsFeld;
    @FXML
    private TextField mobileFeld;
    @FXML
    private TextField telefonFeld;
    @FXML
    private TextField eMailFeld;
    @FXML
    private TextField eMailIIFeld;
    @FXML
    private DatePicker geburtsdatumPicker;
    @FXML
    private Label alterLabel;
    @FXML
    private DatePicker eintrittsDatumPicker;
    @FXML
    private DatePicker austrittsDatumPicker;
    @FXML
    private Label mitgliedSeitLabel;
    @FXML
    private ImageView profilBild;
    @FXML
    private CheckBox istVorstandsmitgliedCheckBox;
    @FXML
    private Button nextMitgliedButton;

    // Standardkonstruktor (wird nicht benötigt)
    public MitgliedViewController() {
    }

    /**
     * Initialisieurng der FXML-Felder (wird automatisch nach dem Konstruktor aufgerufen
     */
    @FXML
    private void initialize() {
        // Initialisierung der Mitglieder-Tabelle und der Spalten
        // Bei Änderung der ausgewählten Zeile werden die Mitgliederdetails im Centerpane angezeigt.
        mitgliedTabelle.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> setMitglied(newValue));

        // ComboBox-Elemente mit den dazugehörigen Auswahlmöglichkeiten füllen
        Status anrede = new Status(1);
        comboBoxAnrede.getItems().addAll(anrede.getElementsAsArrayList());
        Status kategorieI = new Status(2);
        comboBoxKategorieI.getItems().addAll(kategorieI.getElementsAsArrayList());
        Status kategorieII = new Status(4);
        comboBoxKategorieII.getItems().addAll(kategorieII.getElementsAsArrayList());
    }

    /**
     * setzt die Referenz zur MainApp und führt weitere Schritte aus:
     * - Eintrag in die Observerliste der zu überwachenden Controller
     * - Die im FX-UI enthaltenen Liste mit Daten befüllen
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        // setzt die Referenz zur MainApp
        this.mainApp = mainApp;
        // in Obseverliste des/der relevanten Controller eintragen
        mainApp.getAdressController().Attach(this);

        // die benötigten Listen mit Daten füllen
        //  mitgliedTabelle.setItems(FXCollections.observableArrayList(mainApp.getAdressController().getMitgliederListe()));
        mitgliedTabelle.getSelectionModel().selectFirst();
        mitgliedArrayList = new ArrayList<>(mainApp.getAdressController().getMitgliederListe());
        aktuellesMitglied = mitgliedArrayList.get(0);
        setSuchFilter();
        setMitglied(aktuellesMitglied);

        mitgliedTabelle.getSelectionModel().selectFirst();
        mitgliedTabelle.getFocusModel().focus(0);
    }

    public void setStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void handleGetNextMitglied() {
        int i = mitgliedArrayList.indexOf(aktuellesMitglied) + 1;

        if (i < mitgliedArrayList.size()) {
            mitgliedTabelle.scrollTo(i);

            mitgliedTabelle.getSelectionModel().select(i);
            mitgliedTabelle.getFocusModel().focus(i);
            setMitglied(mitgliedArrayList.get(mitgliedTabelle.getSelectionModel().getFocusedIndex()));

        } else {
            // mainFrameController.setInfo("Blättern nicht möglich..", "NOK", true);
            // TODO löschen  mainApp.getMainFrameController().setInfo("Blättern nicht möglich..", "NOK", true);
            mainApp.getMainFrameController().setMeldungInListView("Blättern nicht möglich..", "NOK");
        }
    }


    /**
     * soll ein neueröffnetes Mitglied direkt selektieren aufgrund der neuen Key-ID
     * TODO funktioniert nicht...
     * @param mitgliedId
     */
    public void setMitglied(int mitgliedId) throws InterruptedException {
        int i = 0;
        Mitglied mitglied = null;
        while(mitgliedArrayList.size() > i){
            mitglied = mitgliedArrayList.get(i);
            if(mitglied.getId() == mitgliedId){
                setMitglied(mitglied);
                return;
            }
            i++;
        }
    }

    /**
     * Setzt das in der Tabelle selektierte/ausgewählte Mitglied in den Dialog
     *
     * @param mitglied
     */
    public void setMitglied(Mitglied mitglied) {
        // wenn es ungespeicherte Änderungen gibt wird der User vor dem Wechsel informiert und
        // kann bei Bedarf noch abbrechen
        if(unsavedChanges) {
            // Zeige Warnmeldung
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initOwner(dialogStage);
            alert.setTitle("ungespeicherte Änderungen");
            alert.setHeaderText("ungespeicherte Aenderungen missachten?");
            alert.setContentText("bei\n\n" + aktuellesMitglied + "\n\nmit OK bestätigen oder abbrechen");
            Optional<ButtonType> result = alert.showAndWait();
            if(result.get() != ButtonType.OK) {
                // wenn nicht OK gedrückt wird ist die Methode zu verlassen
                return;
            } else {
                // mainApp.getMainFrameController().setInfo("Reset durchgeführt", "OK", true);
                mainApp.getMainFrameController().setMeldungInListView("Reset durchgeführt"
                        , "OK");
            }
        }

        if(mitglied == null){
            mitglied = aktuellesMitglied;
        }
        // mainApp.getMainFrameController().setInfo("Mitglied #" + mitglied.getId() + " geladen", "OK", true);
        // die Felder des ausgewählten Mitglieds mit Daten füllen
        this.aktuellesMitglied = mitglied;
        idLabel.setText(("Details zu Mitglied #" + mitglied.getId()));
        letzteAenderungLabel.setText(mitglied.getLetzteAenderung());
        comboBoxAnrede.getSelectionModel().select(mitglied.getAnredeElement().getStatusElementKey());
        nachNameFeld.setText(mitglied.getNachName());
        vorNameFeld.setText(mitglied.getVorname());
        adresseFeld.setText(mitglied.getAdresse());
        adressZusatzFeld.setText(mitglied.getAdresszusatz());
        plzFeld.setText(Integer.toString(mitglied.getPlz()));
        ortFeld.setText(mitglied.getOrt());
        bemerkungsFeld.setText(mitglied.getBemerkungen());
        mobileFeld.setText(mitglied.getMobile());
        telefonFeld.setText(mitglied.getTelefon());
        eMailFeld.setText(mitglied.getEmail());
        eMailIIFeld.setText(mitglied.getEmailII());
        // beim Geburtsdatum in Klammern zusätzlich das aktuelle Alter anzeigen
        geburtsdatumPicker.setValue(mitglied.getGeburtsdatum());
        if(mitglied.getGeburtsdatum()!=null) {
            alterLabel.setText("Geburtsdatum (" + Period.between(mitglied.getGeburtsdatum(),
                    LocalDate.now()).getYears() + ")");
            alterLabel.setTooltip(new Tooltip("in Klammern: aktuelles Alter"));
        } else {
            alterLabel.setText("Geburtsdatum");
        }
        // beim Eintrittsdatum wird in Klammern die Anzahl der Mitgliedsjahre angezeigt
        mitgliedSeitLabel.setText("Eintritt (" + Period.between(mitglied.getEintrittsdatum(),
                LocalDate.now()).getYears() + ")");
        mitgliedSeitLabel.setTooltip(new Tooltip("in Klammern: Mitgliedsjahre"));
        eintrittsDatumPicker.setValue(mitglied.getEintrittsdatum());
        austrittsDatumPicker.setValue(mitglied.getAustrittsDatum());
        comboBoxKategorieI.getSelectionModel().select(mitglied.getKategorieIElement().getStatusElementKey());
        comboBoxKategorieII.getSelectionModel().select(mitglied.getKategorieIIElement().getStatusElementKey());
        istVorstandsmitgliedCheckBox.setSelected(mitglied.getIstVorstandsmitglied().getValue());

        // Profilbild des Mitglieds wird gesucht im Ordner "ressources/images/profilbilder/"
        // zwei Formate sind möglich (.jpg oder .png)
        try {
            // Suche nach .jpg-Bild
            FileInputStream inputStream = new FileInputStream("ressources/images/profilbilder/ProfilBild_"
                    + mitglied.getId() + ".jpg");
            Image image = new Image(inputStream);
            profilBild.setImage(image);
        } catch (FileNotFoundException e) {
            // Suche nach .png-Bild
            try {
                FileInputStream inputStream = new FileInputStream("ressources/images/profilbilder/ProfilBild_"
                        + mitglied.getId() + ".png");
                Image image = new Image(inputStream);
                profilBild.setImage(image);
            } catch (FileNotFoundException ex) {
                // wenn weder .jpg noch .png-Bild vorhanden sind, wird ein Dummy-Bild geladen
                try {
                    FileInputStream inputStream = new FileInputStream("ressources/images/profilbilder/Dummy.png");
                    Image image = new Image(inputStream);
                    profilBild.setImage(image);
                } catch (FileNotFoundException exe) {
                    System.out.println("Keine Bilddateien gefunden (" + exe + ")");
                }
            }
        }
        unsavedChanges = false;
    }

    /**
     * Methode wird ausgeführt, wenn der User neben dem Profilbild den Button
     * "Bild ändern/hinzufügen" anklickt.
     * Aus dem MainApp wird der Dialog zur Auswahl einer Bilddatei aufgerufen,
     * welcher ein File zurückgibt.
     */
    public void handelProfilbildButton() {
        File selectedFile = mainApp.chooseImageFile();
        if (selectedFile != null) {
            saveNeuesProfilbild(selectedFile);
        } else {
            System.out.println("File selection cancelled.");
        }
    }

    /**
     * speichert die übergebene Bilddatei als Profilbild des Users im dafür
     * vorgesehenen Ordner ab
     * @param file
     */
    public void saveNeuesProfilbild(File file) {
        // die File-Extension des übergebenen Filenamens wird extrahiert
        //Optional<String> ext = getExtensionByStringHandling(file.getName());
        Optional<String> ext = mainApp.getExtensionByStringHandling(file.getName());
        String extStr = ext.get();
        // die Pfade der Ursprungsdatei und der Zieldatei werden gelesen/generiert
        Path src = Paths.get(file.getAbsolutePath());
        Path dst = Paths.get("../VereinsManager/ressources/images/profilbilder/ProfilBild_"
                + aktuellesMitglied.getId() + "." + extStr);
        try {
            // das übergebene File wird mit dem neuen Namen in den Zielordner kopiert
            java.nio.file.Files.copy(
                    src, dst, StandardCopyOption.COPY_ATTRIBUTES,
                    StandardCopyOption.REPLACE_EXISTING
            );
            setMitglied(this.aktuellesMitglied);

        } catch (IOException e) {
            System.out.println("File konnte nicht gespeicher twerden (" + e + ")");
        }
    }

    /**
     * Methode extrahiert aus dem übergebenen Filenamen die Erweiterung (Extension)
     * und gibt diese als String zurück
     * @param filename
     * @return
     */
    public Optional<String> getExtensionByStringHandling(String filename) {
        return Optional.ofNullable(filename).filter(f -> f.contains(".")).map(f
                -> f.substring(filename.lastIndexOf(".") + 1));
    }


    /**
     * initiert den Suchfilter auf der Mitglieder-Tabelle
     */
    public void setSuchFilter() {
        mitgliedArrayList = new ArrayList<>(mainApp.getAdressController().getMitgliederListe());
        mitgliedTabelle.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> setMitglied(newValue));
        ObservableList<Mitglied> masterData = FXCollections.observableArrayList(mainApp.getAdressController().getMitgliederListe());
        // 1. Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<Mitglied> filteredData = new FilteredList<>(masterData, p -> true);
        // 2. Set the filter Predicate whenever the filter changes.
        //  filterField.setPromptText("Nachname Vorname");

        filterField.getStyleClass().add("search-field");
        filterField.setPromptText("Nachname Vorname");

        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(mitglied -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();
/*
                if (mitglied.getVorname().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.
                } else if (mitglied.getNachName().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                }*/
                if (mitglied.getKurzbezeichnung().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false; // Does not match.
            });
        });
        // 3. Wrap the FilteredList in a SortedList.
        SortedList<Mitglied> sortedData = new SortedList<>(filteredData);
        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(mitgliedTabelle.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        mitgliedTabelle.setItems(sortedData);

        idSpalte.setCellValueFactory(
                cellData -> cellData.getValue().getIdProperty());
        vornameSpalte.setCellValueFactory(
                cellData -> cellData.getValue().getVornameProperty());
        nachnameSpalte.setCellValueFactory(
                cellData -> cellData.getValue().getNachnameProperty());
        ortSpalte.setCellValueFactory(
                cellData -> cellData.getValue().getOrtProperty()
        );

        // mitgliedTabelle.getSelectionModel().selectFirst();
        // mitgliedTabelle.getFocusModel().focus(0);
    }
    /**
     * Wenn der User den Reset-Button betätigt, wird das aktuelle Mitglied nochmals neu geladen.
     */
    public void handleResetButton() {
        setMitglied(aktuellesMitglied);
    }

    /**
     * Wird aufgerufen, wenn der User den Button "Mitglied hinzufügen" betätigt
     */
    public void handleErfassenButton() {
        // ruft das Erfassungs-UI über die MainApp auf
        mainApp.showMitgliedErfassen();
    }

    /**
     * Wird ausgeführt, wenn der User den Button "löschen" betätigt
     */
    public void handleLoeschenButton() {

        // Warnmeldung anzeigen und Löschung bestätigen lassen
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initOwner(dialogStage);
        alert.setTitle("Löschen bestätigen");
        alert.setHeaderText("Willst Du den Kontakt wirklich löschen?");
        alert.setContentText("Löschen von\n\n" + aktuellesMitglied + "\n\nmit OK bestätigen oder abbrechen");
        Optional<ButtonType> result = alert.showAndWait();

        if(result.get() == ButtonType.OK) {
            // Löschung in der Datenbank umsetzen
            DatabaseOperation.deleteMitglied(aktuellesMitglied, mainApp.getCurrentUser());
            // Löschung in der ArrayList umsetzen
            mitgliedTabelle.getItems().remove(mitgliedTabelle.getSelectionModel().getSelectedItem());
            // TODO: Profilbild könnte in diesem Fall auch noch gelöscht werden
        }
    }

    /**
     * Wird aufgerufen, wenn der Inhalt eines Eingabefelds verändert wird und ein Speichern
     * notwendig wird.
     */
    public void onValueChanged() {
        if(!unsavedChanges) {
            unsavedChanges = true;
            //mainApp.getMainFrameController().setInfo("Änderungen wurden noch nicht gespeichert!", "INFO", true);
            // mainApp.getMainFrameController().setMeldungInListView("Änderungen wurden noch nicht gespeichert"
            //         , "INFO");
        }
    }

    /**
     * wird ausgeführt, wenn der User den "Speichern"-Button betätigt hat
     */
    public void handleSpeichernButton() throws InterruptedException {
        //TODO eigentlich nur auszuführen, wenn überhaupt Daten geändert wurden.
        //TODO nach dem Speichern funktioniert der Filter nicht mehr

        // Die Gültigkeit der Daten wird überprüft. Falls die Daten gültig sind wird gespeichert.
        if (isInputValid()) {
            // die Datenfelder des Objekts werden aktualisiert
            aktuellesMitglied.setNachName(nachNameFeld.getText());
            aktuellesMitglied.setVorName(vorNameFeld.getText());
            aktuellesMitglied.setAdresse(adresseFeld.getText());
            aktuellesMitglied.setAdresszusatz(adressZusatzFeld.getText());
            aktuellesMitglied.setPlz(Integer.parseInt(plzFeld.getText()));
            aktuellesMitglied.setOrt(ortFeld.getText());
            aktuellesMitglied.setGeburtsdatum(geburtsdatumPicker.getValue());
            aktuellesMitglied.setAnredeStatus(comboBoxAnrede.getValue());
            aktuellesMitglied.setMobile(mobileFeld.getText());
            aktuellesMitglied.setTelefon(telefonFeld.getText());
            aktuellesMitglied.setEmail(eMailFeld.getText());
            aktuellesMitglied.setEmailII(eMailIIFeld.getText());
            aktuellesMitglied.setBemerkungen(bemerkungsFeld.getText());
            aktuellesMitglied.setEintrittsDatum(eintrittsDatumPicker.getValue());
            aktuellesMitglied.setAustrittsDatum(austrittsDatumPicker.getValue());
            aktuellesMitglied.setKategorieIStatus(comboBoxKategorieI.getValue());
            aktuellesMitglied.setKategorieIIStatus(comboBoxKategorieII.getValue());
            aktuellesMitglied.setIstVorstandsmitglied(istVorstandsmitgliedCheckBox.isSelected());
            aktuellesMitglied.setLetzteAenderungTimestamp(Timestamp.valueOf(LocalDateTime.now()));

            // die Änderungen werden an die Datenbank weitergegeben
            DatabaseOperation.updateMitglied(aktuellesMitglied, mainApp.getCurrentUser());
            // Eine Bestätigung wird im Infofeld ausgegeben
            //mainApp.getMainFrameController().setInfo("Änderungen gespeichert!", "OK", true);
            mainApp.getMainFrameController().setMeldungInListView("Änderungen gespeichert", "OK");
            //   mitgliedArrayList = new ArrayList<>(mainApp.getAdressController().getMitgliederListe());

            unsavedChanges = false;

            // initialize();
        }
    }

    /**
     * Überprüfung die Gültikeit der Eingaben in den Datenfeldern
     * @return gibt true zurück, wenn Daten korrekt sind
     */
    private boolean isInputValid() {
        String errorMeldung = "";
        Boolean isValid = true;

        if (nachNameFeld.getText() == null || nachNameFeld.getText().length() == 0) {
            errorMeldung += "Nachname ist ungültig!";
            isValid = false;
        }
        if (vorNameFeld.getText() == null || vorNameFeld.getText().length() == 0) {
            errorMeldung += "Vorname ist ungültig!";
            isValid = false;
        }
        if (geburtsdatumPicker.getValue() == null) {
            errorMeldung += "Geburtsdatum darf nicht leer sein.";
            isValid = false;
        }
        if (plzFeld.getText() == null || plzFeld.getText().length() == 0) {
            errorMeldung += "Ungültige PLZ!";
        } else {
            // versuchen, die PLZ zu Integer zu parsen
            try {
                Integer.parseInt(plzFeld.getText());
            } catch (NumberFormatException e) {
                errorMeldung += "PLZ muss eine Zahl sein!";
            }
        }
        if(!isValid && errorMeldung.length()>0) {
            // mainApp.getMainFrameController().setInfo(errorMeldung, "NOK", true);
            mainApp.getMainFrameController().setMeldungInListView(errorMeldung, "NOK");
        }
        return isValid;
    }


    @Override
    public void update(Object o) {
        System.out.println("MitgliedViewController hat Update-Meldung erhalten von " + o);
        if (o instanceof AdressController) {
            AdressController ac = (AdressController) o;
            Platform.runLater(new Runnable() { // TODO
                @Override
                public void run() {
                    mitgliedTabelle.setItems(FXCollections.observableArrayList(((AdressController) o).getMitgliederListe()));
                    mitgliedArrayList = ac.getMitgliederListe();
                }
            });

        }
    }


}
