package space.cloud4b.verein.view.projekte;


import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import space.cloud4b.verein.MainApp;
import space.cloud4b.verein.controller.ProjektController;
import space.cloud4b.verein.model.verein.projekt.Projekt;
import space.cloud4b.verein.model.verein.status.StatusElement;
import space.cloud4b.verein.services.Observer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;


/**
 * Controller zum JavaFX-UI MitgliedView.fxml (Anzeige Mitgliederbereich)
 * Versorgt die FXML-Objekte (Felder und Tabellen) mit Daten und behandelt die Action-Events
 * Erhält Benachrichtigungen der abonnierten Observer-Klasse(n), wenn Datensätze geändert wurden.
 *
 * @author Bernhard Kämpf
 * @version 2020-04-10
 */
public class ProjektViewController implements Observer {

    // allgemeine Instanzvariabeln
    private MainApp mainApp;
    private Stage dialogStage;
    private Projekt aktuellesProjekt = null;
    private ArrayList<Projekt> projektArrayList;
    boolean unsavedChanges = false;

    // UI-Variabeln (Verknüpfung mit Elementen des Userinterfaces)
    @FXML
    private TableView<Projekt> projektTableView;
    @FXML
    private TableColumn<Projekt, Number> projektIdColumn;
    @FXML
    private TableColumn<Projekt, String> projektTitelColumn;
    @FXML
    private TableColumn<Projekt, String> projektDetailsColumn;
    @FXML
    private TableColumn<Projekt, StatusElement> projektKategorieColumn;
    @FXML
    private TableColumn<Projekt, StatusElement> projektPhaseColumn;
    @FXML
    private TableColumn<Projekt, LocalDate> projektStartDatumColumn;
    @FXML
    private TableColumn<Projekt, LocalDate> projektEndeDatumColumn;

    // Standardkonstruktor (wird nicht benötigt)
    public ProjektViewController() {
    }

    /**
     * Initialisieurng der FXML-Felder (wird automatisch nach dem Konstruktor aufgerufen
     */
    @FXML
    private void initialize() {

        // Initialisierung der Mitglieder-Tabelle und der Spalten
        // Bei Änderung der ausgewählten Zeile werden die Mitgliederdetails im Centerpane angezeigt.
        projektTableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> setProjekt(newValue));
        projektIdColumn.setCellValueFactory(
                cellData -> cellData.getValue().getProjektIdProperty());
        projektTitelColumn.setCellValueFactory(
                cellData -> cellData.getValue().getTitelProperty());
        projektTitelColumn.setPrefWidth(200);
        projektDetailsColumn.setCellValueFactory(
                cellData -> cellData.getValue().getProjektDetailsProperty());
        projektDetailsColumn.setPrefWidth(400);

        /* TODO Problem gelöst mit der Darstellung und Sortierung von Daten
        *   Sortiert wird nach Localdate (2020-01-02) und dargestellt wird ein
        *   anderer String (02.01.2020) */
        projektStartDatumColumn.setCellValueFactory(
                cellData -> cellData.getValue().getProjektStartDatumProperty());
        projektStartDatumColumn.setPrefWidth(100);
        projektStartDatumColumn.setCellFactory(column -> {
            return new TableCell<Projekt, LocalDate>() {
                @Override
                protected void updateItem(LocalDate item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(item.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));
                    }
                }
            };
        });
        projektEndeDatumColumn.setCellValueFactory(
                cellData -> cellData.getValue().getProjektEndeDatumProperty());
        projektEndeDatumColumn.setPrefWidth(100);
        projektEndeDatumColumn.setCellFactory(column -> {
            return new TableCell<Projekt, LocalDate>() {
                @Override
                protected void updateItem(LocalDate item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(item.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));
                    }
                }
            };
        });
        projektPhaseColumn.setCellValueFactory(
                cellData -> cellData.getValue().getProjektPhaseProperty());
        projektPhaseColumn.setPrefWidth(110);

        projektKategorieColumn.setCellValueFactory(
                cellData -> cellData.getValue().getProjektKategorieProperty());
        projektKategorieColumn.setPrefWidth(110);

    }

    private void setProjekt(Projekt newValue) {
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

        // die benötigten Listen mit Daten füllen
        projektTableView.setItems(FXCollections.observableArrayList(mainApp.getProjektController().getProjektListe()));
        projektTableView.getSelectionModel().selectFirst();
        projektTableView.getFocusModel().focus(0);
        // in Obseverliste des/der relevanten Controller eintragen
        mainApp.getProjektController().Attach(this);
        projektTableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> openProjekt(newValue, oldValue));
    }

    public void setStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void openProjekt(Projekt newProjekt, Projekt oldProjekt) {
            if(newProjekt != null) {
                mainApp.showProjektEdit(newProjekt);
            }
    }

    @Override
    public void update(Object o) {
       // mainApp.getMainFrameController().setMeldungInListView("Update-Meldung erhalten", "INFO");
        if (o instanceof ProjektController) {
            ProjektController pc = (ProjektController) o;
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    projektTableView.setItems(FXCollections.observableArrayList(((ProjektController) o).getProjektListe()));

                }
            });

        }
    }


}
