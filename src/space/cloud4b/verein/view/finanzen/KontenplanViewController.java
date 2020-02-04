package space.cloud4b.verein.view.finanzen;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
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

    // UI-Variabeln (Verknüpfung mit Elementen des Userinterfaces)
    @FXML
    private TableView<Konto> kontenplanTabelle;
    @FXML
    private TableColumn<Konto, Number> kontoNummerSpalte;
    @FXML
    private TableColumn<Konto, String> kontoBezeichnungSpalte;

    public KontenplanViewController() {
        buchungsperiode = new Buchungsperiode(Year.now());
    }

    /**
     * Initialisieurng der FXML-Felder (wird automatisch nach dem Konstruktor aufgerufen
     */
    @FXML
    private void initialize() {
        // Initialisierung der Mitglieder-Tabelle und der Spalten
        // Bei Änderung der ausgewählten Zeile werden die Mitgliederdetails im Centerpane angezeigt.
        kontenplanTabelle.setItems(FXCollections.observableArrayList(buchungsperiode.getKontenplan()));
        kontoNummerSpalte.setCellValueFactory(
                cellData -> cellData.getValue().getKontoNummerProperty());
        kontoBezeichnungSpalte.setCellValueFactory(
                cellData -> cellData.getValue().getKontoBezeichnungProperty());
    }

    public void setMainApp(MainApp mainApp) {
        // setzt die Referenz zur MainApp
        this.mainApp = mainApp;
    }
}
