package space.cloud4b.verein.view.finanzen;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import space.cloud4b.verein.MainApp;
import space.cloud4b.verein.model.verein.finanzen.Belegkopf;
import space.cloud4b.verein.model.verein.finanzen.Buchungsperiode;
import space.cloud4b.verein.model.verein.finanzen.Konto;
import space.cloud4b.verein.services.DatabaseOperation;

import java.time.Year;
import java.util.HashMap;

public class HauptjournalViewController {

    private MainApp mainApp;
    private Stage dialogStage;
    private Buchungsperiode buchungsperiode;
    private HashMap<Integer, Konto> kontenPlan;
    private int jahr;

    // UI-Variabeln (Verknüpfung mit Elementen des Userinterfaces)
    @FXML
    private TableView<Belegkopf> hauptJournalTabelle;
    @FXML
    private TableColumn<Belegkopf, String> belegDatumSpalte;
    @FXML
    private TableColumn<Belegkopf, Number> belegNummerSpalte;
    @FXML
    private TableColumn<Belegkopf, String> belegTextSpalte;
    @FXML
    private TableColumn<Belegkopf, String> buchungsDatumSpalte;
    @FXML
    private TableColumn<Belegkopf, String> belegBetragStringSpalte;
    @FXML
    private TableColumn<Belegkopf, String> belegPositionSpalte;

    public HauptjournalViewController() {
        System.out.println("HauptjournalViewController erzeugt");
        jahr = Year.now().getValue();
    }

    /**
     * Initialisieurng der FXML-Felder (wird automatisch nach dem Konstruktor aufgerufen
     */
    @FXML
    private void initializeData() {
        buchungsperiode = mainApp.getFinanzController().getBuchhalung().getBuchungsperiode(jahr);
        // Initialisierung der Mitglieder-Tabelle und der Spalten
        // Bei Änderung der ausgewählten Zeile werden die Mitgliederdetails im Centerpane angezeigt.
        hauptJournalTabelle.setItems(FXCollections.observableArrayList(buchungsperiode.getHauptjournal()));
        hauptJournalTabelle.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> openBeleg(newValue));

        belegDatumSpalte.setCellValueFactory(
                cellData -> cellData.getValue().getBelegDatumStringProperty());
        buchungsDatumSpalte.setCellValueFactory(
                cellData -> cellData.getValue().getBuchungsDatumStringProperty());
        belegNummerSpalte.setCellValueFactory(
                cellData -> cellData.getValue().getBelegNummerProperty());
        belegTextSpalte.setCellValueFactory(
                cellData -> cellData.getValue().getBelegTextProperty());
        belegBetragStringSpalte.setCellValueFactory(
                cellData -> cellData.getValue().getBetragStringProperty());
        belegPositionSpalte.setCellValueFactory(
                cellData -> cellData.getValue().getBelegPositionen());
    }

    public void setMainApp(MainApp mainApp) {
        // setzt die Referenz zur MainApp
        this.mainApp = mainApp;
        initializeData();
    }

    public void handleBelegErfassen() {
        Belegkopf neuerBeleg = DatabaseOperation.addBelegkopf(buchungsperiode, mainApp.getCurrentUser(), mainApp.getFinanzController().getBuchhalung().getBuchungsperiode(buchungsperiode.getJahr()).getKontenPlanHashMap());
        mainApp.showBelegkopfEdit(neuerBeleg);
    }
    public void openBeleg(Belegkopf beleg) {
        if (beleg != null) {
            mainApp.showBelegkopfEdit(beleg);
        }
    }
}
