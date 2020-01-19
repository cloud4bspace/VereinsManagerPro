package space.cloud4b.verein.view.tasks;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import space.cloud4b.verein.MainApp;
import space.cloud4b.verein.model.verein.adressbuch.Mitglied;
import space.cloud4b.verein.model.verein.task.Task;
import space.cloud4b.verein.model.verein.user.User;
import space.cloud4b.verein.services.DatabaseOperation;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Controller zum JavaFX-UI TaskNeuView.fxml (Erfassung eines neuen Tasks)
 * Behandelt die Action-Events
 *
 * @author Bernhard Kämpf und Serge Kaulitz
 * @version 2019-12-17
 */
public class TaskNeuViewController {
    MainApp mainApp;
    Stage dialogStage;
    int indexOfCurrentUser;
    ArrayList<Mitglied> mitgliedArrayList;

    // UI-Variabeln (Verknüpfung mit Elementen des Userinterfaces)
    @FXML
    private TextField bezeichnunglFeld;
    @FXML
    private TextArea detailsTextArea;
    @FXML
    private DatePicker terminDatumPicker;
    @FXML
    private ComboBox<Mitglied> verantwortlichComBox;


    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        mitgliedArrayList = mainApp.getAdressController().getMitgliederListe();
        verantwortlichComBox.getItems().addAll(mitgliedArrayList);
        User currentUser = mainApp.getCurrentUser();
        for (int i = 0; i < mitgliedArrayList.size(); i++) {
            if (mitgliedArrayList.get(i).getId() == currentUser.getUserId()) {
                indexOfCurrentUser = i;
            }
        }
        verantwortlichComBox.getSelectionModel().select(indexOfCurrentUser);
    }

    public void setStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void initialize() {
        terminDatumPicker.setValue(LocalDate.now());
    }

    public void handleSpeichern() throws InterruptedException {
        int neueId = 0;
        Task neuerTask = null;

        if (isValid()) {
            this.dialogStage.close();
            neueId = DatabaseOperation.saveNewTask(bezeichnunglFeld.getText(), detailsTextArea.getText(),
                    terminDatumPicker.getValue().toString(), verantwortlichComBox.getValue(), mainApp.getCurrentUser());
        }

        // mainApp.getMitgliedViewController().setMitglied(neueId);
    }

    private boolean isValid() {
        boolean isValid = true;
        if (bezeichnunglFeld.getText() == null) {
            isValid = false;
        }
        if (detailsTextArea.getText() == null) {
            isValid = false;
        }
        if (terminDatumPicker.getValue() == null) {
            isValid = false;
        }
        if (verantwortlichComBox.getValue() == null) {
            isValid = false;
        }
        return isValid;
    }

    public void handleAbbrechen() {
        this.dialogStage.close();
    }
}
