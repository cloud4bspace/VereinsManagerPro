package space.cloud4b.verein.view.finanzen;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import space.cloud4b.verein.MainApp;
import space.cloud4b.verein.model.verein.finanzen.Belegkopf;
import space.cloud4b.verein.model.verein.finanzen.Buchungsperiode;

import java.time.Year;

public class HauptjournalViewController {

    private MainApp mainApp;
    private Stage dialogStage;
    private Buchungsperiode buchungsperiode;

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
        buchungsperiode = new Buchungsperiode(Year.now());
    }

    /**
     * Initialisieurng der FXML-Felder (wird automatisch nach dem Konstruktor aufgerufen
     */
    @FXML
    private void initialize() {
        // Initialisierung der Mitglieder-Tabelle und der Spalten
        // Bei Änderung der ausgewählten Zeile werden die Mitgliederdetails im Centerpane angezeigt.
        hauptJournalTabelle.setItems(FXCollections.observableArrayList(buchungsperiode.getHauptjournal()));
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
    }
}
