package space.cloud4b.verein.view.benutzer;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;
import space.cloud4b.verein.MainApp;
import space.cloud4b.verein.controller.BenutzerController;
import space.cloud4b.verein.model.verein.user.User;
import space.cloud4b.verein.services.Observer;

import java.time.LocalDate;

/**
 * der Controller für die BenutzerView
 *
 * @author Bernhard Kämpf & Serge Kaulitz
 * @version 2020-01-07
 */
public class BenutzerViewController implements Observer {

    // allgemeine Instanzvariabeln
    private MainApp mainApp;
    private Stage dialogStage;


    // UI-Variabeln (Verknüpfung mit Elementen des Userinterfaces)
    @FXML
    private TableView<User> userTabelle;
    @FXML
    private TableColumn<User, Number> benutzerIdSpalte;
    @FXML
    private TableColumn<User, Number> mitgliedIdSpalte;
    @FXML
    private TableColumn<User, String> benutzerNameSpalte;
    @FXML
    private TableColumn<User, LocalDate> lastLoginSpalte;
    @FXML
    private TableColumn<User, Number> anzLoginsSpalte;
    @FXML
    private TableColumn<User, Number> sperrcodeSpalte;
    @FXML
    private TableColumn<User, String> benutzerKatSpalte;
    @FXML
    private TableColumn<User, String> benutzerPWSpalte;


    public BenutzerViewController() {

    }

    public void initialize() {
        benutzerIdSpalte.setCellValueFactory(
                cellData -> cellData.getValue().getUserIdProperty());
        mitgliedIdSpalte.setCellValueFactory(
                cellData -> cellData.getValue().getMitgliedIdProperty());
        benutzerNameSpalte.setCellValueFactory(
                cellData -> cellData.getValue().getUserNameProperty());
        lastLoginSpalte.setCellValueFactory(
                cellData -> cellData.getValue().getUserLastLogin());
        anzLoginsSpalte.setCellValueFactory(
                cellData -> cellData.getValue().getUserLoginsCountProperty());
        sperrcodeSpalte.setCellValueFactory(
                cellData -> cellData.getValue().getUserSperrcodeProperty());
        sperrcodeSpalte.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter()));
        benutzerKatSpalte.setCellValueFactory(
                cellData -> cellData.getValue().getUserKatProperty());
        benutzerPWSpalte.setCellValueFactory(
                cellData -> cellData.getValue().getUserPWProperty());
    }

    public void onEditChange(TableColumn.CellEditEvent<User, Number> userNumberCellEditEvent) {
        User user = userTabelle.getSelectionModel().getSelectedItem();
        user.setSperrCode(userNumberCellEditEvent.getNewValue(), mainApp.getCurrentUser());
        initializeTable();
    }

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        initializeTable();
        mainApp.getBenutzerController().Attach(this);
    }

    public void setStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void initializeTable() {
        // Add observable list data to the tables
        userTabelle.setItems(FXCollections.observableArrayList(mainApp.getBenutzerController().getBenutzerListe()));
        userTabelle.setEditable(true);
        userTabelle.setRowFactory(row -> new TableRow<User>() {
            @Override
            public void updateItem(User item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null) {
                    setId("");
                    // setStyle("");
                } else if (item.isUserGesperrt()) {
                    setId("gesperrt");
                    // setStyle("-fx-background-color: #F78888;-fx-text-fill: #000000;");
                } else {
                    setId("");
                    // setStyle("-fx-background-color: #FFFFFF;-fx-text-fill: #000000;");
                }
            }
        });
    }

    @Override
    public void update(Object o) {
        if (o instanceof BenutzerController) {
            BenutzerController bc = (BenutzerController) o;
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    userTabelle.setItems(FXCollections.observableArrayList(bc.getBenutzerListe()));
                }
            });
        }
    }


}
