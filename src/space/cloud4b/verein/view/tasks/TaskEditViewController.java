package space.cloud4b.verein.view.tasks;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import space.cloud4b.verein.MainApp;
import space.cloud4b.verein.model.verein.adressbuch.Mitglied;
import space.cloud4b.verein.model.verein.status.Status;
import space.cloud4b.verein.model.verein.status.StatusElement;
import space.cloud4b.verein.model.verein.task.Task;
import space.cloud4b.verein.services.DatabaseOperation;

import java.util.ArrayList;
import java.util.Optional;

public class TaskEditViewController {

    MainApp mainApp;
    Stage dialogStage;
    int indexOfCurrentUser;
    private Task task;


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
    @FXML
    private ComboBox<StatusElement> prioComBox;
    @FXML
    private ComboBox<StatusElement> statusComBox;
    @FXML
    private VBox taskEditVBox;


    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        mitgliedArrayList = mainApp.getAdressController().getMitgliederListe();


    }

    public void setStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void initialize() {


    }

    public void handleSpeichern() {
        if (isValid()) {
            this.dialogStage.close();
            // die Änderungen beim Task-Objekt vornehmen
            task.setTitel(bezeichnunglFeld.getText());
            task.setDetails(detailsTextArea.getText());
            task.setPrioStatus(prioComBox.getValue());
            task.setStatusStatus(statusComBox.getValue());
            task.setVerantwortliches(verantwortlichComBox.getValue());

            // die Änderungen werden an die Datenbank weitergegeben
            DatabaseOperation.updateTask(task, mainApp.getCurrentUser());
            mainApp.getMainFrameController().setMeldungInListView(
                    "Änderungen gespeichert", "OK");
        }
    }

    private boolean isValid() {
        String errorMeldung = null;
        boolean isValid = true;
        if (bezeichnunglFeld.getText() == null || bezeichnunglFeld.getText().length() < 1) {
            errorMeldung = "- Bezeichnung ausfüllen";
            isValid = false;
        }
        if (detailsTextArea.getText() == null || detailsTextArea.getText().length() < 1) {
            if (errorMeldung != null) {
                errorMeldung += "\n- Details ausfüllen";
            } else {
                errorMeldung = "- Details ausfüllen";
            }
            isValid = false;
        }
        if (terminDatumPicker.getValue() == null) {
            if (errorMeldung != null) {
                errorMeldung += "\n- Termin ungültig";
            } else {
                errorMeldung = "- Termin ungültig";
            }
            isValid = false;
        }

        if (!isValid) {
            mainApp.getMainFrameController().setMeldungInListView(errorMeldung, "NOK");
            mainApp.showAlert(errorMeldung, dialogStage);
        }
        return isValid;
    }

    public void handleAbbrechen() {
        this.dialogStage.close();
    }

    public void handleReset() {
        setTask(this.task);
    }

    public void handleDelete() {
        this.dialogStage.close();
        // Warnmeldung anzeigen und Löschung bestätigen lassen
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initOwner(dialogStage);
        alert.setTitle("Löschen bestätigen");
        alert.setHeaderText("Willst Du den Task wirklich löschen?");
        alert.setContentText("Löschen von\n\n" + task + "\n\nmit OK bestätigen oder abbrechen");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            // Löschung in der Datenbank umsetzen
            DatabaseOperation.deleteTask(task, mainApp.getCurrentUser());
            mainApp.getMainFrameController().setMeldungInListView("Task gelöscht", "OK");
            dialogStage.close();
        }
    }

    public void setTask(Task task) {
        this.task = task;
        terminDatumPicker.setValue(task.getTaskDatum());
        bezeichnunglFeld.setText(task.getTaskTitel());
        detailsTextArea.setText(task.getTaskText());
        detailsTextArea.setWrapText(true);
        Status prioStatus = new Status(7);
        prioComBox.getItems().addAll(prioStatus.getElementsAsArrayList());
        prioComBox.getSelectionModel().select(task.getPrioStatus());
        Status statusStatus = new Status(8);
        statusComBox.getItems().addAll(statusStatus.getElementsAsArrayList());
        statusComBox.getSelectionModel().select(task.getStatusStatus());
        verantwortlichComBox.getItems().addAll(mitgliedArrayList);
        verantwortlichComBox.getSelectionModel().select(task.getVerantwortlichesMitglied());
    }
}
