package space.cloud4b.verein.view.finanzen;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import space.cloud4b.verein.MainApp;
import space.cloud4b.verein.model.verein.finanzen.Buchungsperiode;
import space.cloud4b.verein.model.verein.finanzen.Konto;

import java.time.Year;

public class KontenplanViewController {
    private MainApp mainApp;
    private Stage dialogStage;
    private Buchungsperiode buchungsperiode;
    private Konto konto;

    // UI-Variabeln (Verknüpfung mit Elementen des Userinterfaces)
    @FXML
    private TableView<Konto> kontenplanTabelle;
    @FXML
    private TableColumn<Konto, Number> kontoNummerSpalte;
    @FXML
    private TableColumn<Konto, String> kontoSaldoSpalte;
    @FXML
    private TableColumn<Konto, String> kontoBezeichnungSpalte;
    @FXML
    private TableColumn<Konto, String>  kontoHauptgruppeSpalte;
    @FXML
    private TableColumn<Konto, String> kontoGruppeSpalte;
    @FXML
    private TableColumn<Konto, String> kontoKlasseSpalte;
    @FXML
    private TableColumn<Konto, String> kontoUmgiederungSpalte;


    public KontenplanViewController() {

    }

    /**
     * Initialisieurng der FXML-Felder (wird automatisch nach dem Konstruktor aufgerufen
     */
    @FXML
    private void initializeData() {
        // Initialisierung der Mitglieder-Tabelle und der Spalten
        // Bei Änderung der ausgewählten Zeile werden die Mitgliederdetails im Centerpane angezeigt.
        kontenplanTabelle.setItems(FXCollections.observableArrayList(buchungsperiode.getKontenplan()));
        kontenplanTabelle.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> openKontoauszug(newValue));
        kontoNummerSpalte.setCellValueFactory(
                cellData -> cellData.getValue().getKontoNummerProperty());
        kontoBezeichnungSpalte.setCellValueFactory(
                cellData -> cellData.getValue().getKontoBezeichnungProperty());
        kontoSaldoSpalte.setCellValueFactory(
                cellData -> cellData.getValue().getSaldo().toColumnString());
        kontoSaldoSpalte.setStyle( "-fx-alignment: CENTER-RIGHT;");
        kontoHauptgruppeSpalte.setCellValueFactory(
                cellData -> cellData.getValue().getKontoHauptgruppeText());
        kontoGruppeSpalte.setCellValueFactory(
                cellData -> cellData.getValue().getKontoGruppeText());
        kontoKlasseSpalte.setCellValueFactory(
                cellData -> cellData.getValue().getKontoKlasseText());
        kontoUmgiederungSpalte.setCellValueFactory(
                cellData -> cellData.getValue().getUmgliederungText());

    }

    private void openKontoauszug(Konto konto) {
        if (konto != null) {
            mainApp.showKontoauszug(konto);
        }
    }

    public void setMainApp(MainApp mainApp) {
        // setzt die Referenz zur MainApp
        this.mainApp = mainApp;
        buchungsperiode = mainApp.getFinanzController().getBuchhalung().getBuchungsperiode(Year.now().getValue());
        initializeData();
    }
}
