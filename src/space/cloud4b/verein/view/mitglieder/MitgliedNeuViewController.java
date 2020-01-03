package space.cloud4b.verein.view.mitglieder;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import space.cloud4b.verein.MainApp;
import space.cloud4b.verein.services.DatabaseOperation;

/**
 * Die Klasse MitgliedNeuViewController ist verknüpft mit dem JavaFX-Unserinterface MitgliedNeuView.fxml
 * Sie dient zur Erfassung eines neuen Mitglieds mit wenigen Basisdaten. Weitere Daten können später
 * via MitgliedView.fxml ergänzt werden.
 *
 * @author Bernhard Kämpf und Serge Kaulitz
 * @version 2019-12-17
 */
public class MitgliedNeuViewController {

    // allgemeine Instanzvariabeln
    MainApp mainApp;
    Stage dialogStage;
    int neueId = 0;

    // UI-Variabeln (Verknüpfung mit Elementen des Userinterfaces)
    @FXML
    private TextField nachnameFeld;
    @FXML
    private TextField vornameFeld;
    @FXML
    private DatePicker eintrittsDatumPicker;

    // Standardkonstruktor (wird nicht benötigt)
    public MitgliedNeuViewController() {

    }

    /**
     * Initialisieurng der FXML-Felder (wird automatisch nach dem Konstruktor aufgerufen
     */
    @FXML
    private void initialize() {
        // wird hier nicht benötigt
    }

    /**
     * Setzt die Referenz zur MainApp
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    /**
     * Setzt die Referenz zum aktuellen Stage
     *
     * @param dialogStage die Stage-Referenz des UI
     */
    public void setStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * Wird ausgeführt, wenn der User den Button "speichern" anklickt und ergänzt das Mitglied in
     * der MYSQL-Datenbank
     */
    public void handleSpeichern() throws InterruptedException {

        // Die Gültigkeit der User-Eingaben wird geprüft
        if (isValid()) {
            this.dialogStage.close();
            // Die MYSQL-Tabelle wird mit einem neuen Eintrag ergänzt und gibt die ID des neuen Mitglieds zurück
            this.neueId = DatabaseOperation.saveNewMember(nachnameFeld.getText(),
                    vornameFeld.getText(), eintrittsDatumPicker.getValue().toString());
            // mainApp.getMitgliedViewController().setMitglied(neueId);
        }
    }


    /**
     * Überprüfung die Gültikeit der Eingaben in den Datenfeldern
     *
     * @return gibt true zurück, wenn Daten korrekt sind
     */
    private boolean isValid() {
        String meldung = null;
        boolean isValid = true;
        if (nachnameFeld.getText().isEmpty()) {
            isValid = false;
            meldung = "- Nachname darf nicht leer sein.";
        }
        if (vornameFeld.getText().isEmpty()) {
            isValid = false;
            if (meldung != null) {
                meldung += "\n- Vorname darf nicht leer sein.";
            } else {
                meldung = "- Vorname darf nicht leer sein.";
            }
        }
        if (eintrittsDatumPicker.getValue() == null) {
            isValid = false;
            if (meldung != null) {
                meldung += "\n- Datum darf nicht leer sein.";
            } else {
                meldung = "- Datum darf nicht leer sein.";
            }
        }
        // Meldung anzeigen, wenn Eingaben ungültig sind
        if (!isValid) {
            mainApp.showAlert(meldung, dialogStage);
        }
        return isValid;
    }

    /**
     * Schliesst das aktuelle UI, wenn der User den abbrechen-Button betätigt
     */
    public void handleAbbrechen() {
        this.dialogStage.close();
    }
}
