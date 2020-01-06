package space.cloud4b.verein.view.termine;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import space.cloud4b.verein.MainApp;
import space.cloud4b.verein.model.verein.kalender.Termin;
import space.cloud4b.verein.services.DatabaseOperation;

/**
 * Die Klasse TerminNeuViewController ist verknüpft mit dem JavaFX-Unserinterface TerminNeuView.fxml
 * Sie dient zur Erfassung eines neuen Termins mit wenigen Basisdaten. Weitere Daten können später
 * via TerminView.fxml ergänzt werden.
 *
 * @author Bernhard Kämpf und Serge Kaulitz
 * @version 2019-12-17
 */
public class TerminNeuViewController {

    // allgemeine Instanzvariabeln
    MainApp mainApp;
    Stage dialogStage;

    // UI-Variabeln (Verknüpfung mit Elementen des Userinterfaces)
    @FXML
    private DatePicker terminDatumPicker;
    @FXML
    private TextField terminTextFeld;


    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public void setStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * Schliesst das aktuelle UI, wenn der User den abbrechen-Button betätigt
     */
    public void handleAbbrechen() {
        this.dialogStage.close();
    }

    public void handleSpeichern() throws InterruptedException {
        int neueId = 0;
        Termin neuerTermin = null;
        if (isValid()) {
            this.dialogStage.close();
            neueId = DatabaseOperation.saveNewTermin(terminTextFeld.getText(), terminDatumPicker.getValue().toString()
                    , mainApp.getCurrentUser());
            System.out.println("NeueTerminID: " + neueId);
        }

        // mainApp.getMitgliedViewController().setMitglied(neueId);
    }

    /**
     * Überprüfung die Gültikeit der Eingaben in den Datenfeldern
     *
     * @return gibt true zurück, wenn Daten korrekt sind
     */
    private boolean isValid() {
        String meldung = null;
        boolean isValid = true;
        if (terminTextFeld.getText().isEmpty()) {
            isValid = false;
            meldung = "- Text/Bezeichnung darf nicht leer sein.";
        }
        if (terminDatumPicker.getValue() == null) {
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
}
