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
import space.cloud4b.verein.model.verein.status.StatusElement;
import space.cloud4b.verein.services.DatabaseOperation;

import java.time.Year;
import java.util.Currency;
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
    private TableColumn<Belegkopf, StatusElement> belegStatusSpalte;
    @FXML
    private TableColumn<Belegkopf, String> belegDatumSpalte;
    @FXML
    private TableColumn<Belegkopf, String> belegNummerSpalte;
    @FXML
    private TableColumn<Belegkopf, String> belegTextSpalte;
    @FXML
    private TableColumn<Belegkopf, String> buchungsDatumSpalte;
    @FXML
    private TableColumn<Belegkopf, String> belegBetragFWStringSpalte;
    @FXML
    private TableColumn<Belegkopf, Currency> belegCurrencySpalte;
    @FXML
    private TableColumn<Belegkopf, String> belegBetragCHFStringSpalte;
    @FXML
    private TableColumn<Belegkopf, String> belegKursStringSpalte;

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
        belegStatusSpalte.setCellValueFactory(
                cellData -> cellData.getValue().getBelegStatusObservableValue());
        belegDatumSpalte.setCellValueFactory(
                cellData -> cellData.getValue().getBelegDatumStringProperty());
        buchungsDatumSpalte.setCellValueFactory(
                cellData -> cellData.getValue().getBuchungsDatumStringProperty());
        belegNummerSpalte.setCellValueFactory(
                cellData -> cellData.getValue().getBelegNummerStringProperty());
        belegTextSpalte.setCellValueFactory(
                cellData -> cellData.getValue().getBelegTextProperty());
        belegBetragFWStringSpalte.setCellValueFactory(
                cellData -> cellData.getValue().getBelegBetragProperty());
        belegBetragFWStringSpalte.setStyle( "-fx-alignment: CENTER-RIGHT;");
        belegCurrencySpalte.setCellValueFactory(
                cellData -> cellData.getValue().getBelegWaehrungCurrency());
        belegBetragCHFStringSpalte.setCellValueFactory(
                cellData -> cellData.getValue().getBetragCHFProperty());
        belegBetragCHFStringSpalte.setStyle( "-fx-alignment: CENTER-RIGHT;");
        belegKursStringSpalte.setCellValueFactory(
                cellData -> cellData.getValue().getKursStringProperty());
        belegKursStringSpalte.setStyle( "-fx-alignment: CENTER-RIGHT;");
      /*  belegPositionSpalte.setCellValueFactory(
                cellData -> cellData.getValue().getBelegPositionen());*/
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
