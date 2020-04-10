package space.cloud4b.verein.view.finanzen;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import space.cloud4b.verein.MainApp;
import space.cloud4b.verein.model.verein.finanzen.*;

import java.time.Year;
import java.util.ArrayList;

public class KontoAuszugViewController {

    private MainApp mainApp;
    private Stage dialogStage;
    private Buchungsperiode buchungsperiode;
    private Konto konto;
    private ArrayList<Kontoposition> kontoPositionen = new ArrayList<>();

    // UI-Variabeln (Verknüpfung mit Elementen des Userinterfaces)
    @FXML
    private TableView<Kontoposition> kontoAuszugTabelle;
    @FXML
    private TableColumn<Kontoposition, String> belegNummerColumn;
    @FXML
    private TableColumn<Kontoposition, String> belegDatumColumn;
    @FXML
    private TableColumn<Kontoposition, String> belegTextColumn;
    @FXML
    private TableColumn<Kontoposition, String> sollHabenColumn;
    @FXML
    private TableColumn<Kontoposition, String> sollBetragCHFColumn;
    @FXML
    private TableColumn<Kontoposition, String> habenBetragCHFColumn;
    @FXML
    private TableColumn<Kontoposition, String> saldoCHFColumn;
    @FXML
    private ComboBox<Konto> kontoComboBox;

    public KontoAuszugViewController() {
        System.out.println("KontoAuszugViewController erzeugt");
    }

    /**
     * Initialisieurng der FXML-Felder (wird automatisch nach dem Konstruktor aufgerufen
     */
    @FXML
    private void initializeData() {

    }

    /**
     * Initialisieurng der FXML-Felder (wird automatisch nach dem Konstruktor aufgerufen
     */
    @FXML
    private void initialize() {
        sollBetragCHFColumn.setStyle( "-fx-alignment: CENTER-RIGHT;");
        habenBetragCHFColumn.setStyle( "-fx-alignment: CENTER-RIGHT;");
        saldoCHFColumn.setStyle( "-fx-alignment: CENTER-RIGHT;");
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        buchungsperiode = mainApp.getFinanzController().getBuchhalung().getBuchungsperiode(Year.now().getValue());
        kontoComboBox.getItems().addAll(mainApp.getFinanzController().getBuchhalung().getBuchungsperiode(buchungsperiode
                .getJahr()).getKontenplan());
    }

    public void fillKontopositionen() {
        double saldo = 0;
        for(Belegkopf belegkopf : mainApp.getFinanzController().getBuchhalung().getBuchungsperiode(
                this.buchungsperiode.getJahr()).getHauptjournal()) {
            for(Belegposition belegposition: belegkopf.getBelegPositionenAsArrayList()) {
                if(belegposition.getKontoObject().equals(konto)){
                    if(belegposition.getSH().getValue().equals("S")){
                        saldo += belegposition.getBetrag().getValue().getBetragBuchungsWaehrung().doubleValue();
                    } else {
                        saldo -= belegposition.getBetrag().getValue().getBetragBuchungsWaehrung().doubleValue();
                    }
                    kontoPositionen.add(new Kontoposition(belegkopf, belegposition, konto, saldo));
                }
            }
        }
        kontoAuszugTabelle.setItems(FXCollections.observableArrayList(this.kontoPositionen));

        belegNummerColumn.setCellValueFactory(
                cellData -> cellData.getValue().getBelegkopf().getBelegNummerStringProperty());
        belegDatumColumn.setCellValueFactory(
                cellData -> cellData.getValue().getBelegkopf().getBelegDatumStringProperty());
        belegTextColumn.setCellValueFactory(
                cellData -> cellData.getValue().getBelegkopf().getBelegTextProperty(cellData.getValue()
                        .getBelegposition()));
        sollHabenColumn.setCellValueFactory(
                cellData -> cellData.getValue().getBelegposition().getSH());
        sollBetragCHFColumn.setCellValueFactory(
                cellData -> cellData.getValue().getBelegposition().getBetragCHFSoll());
        habenBetragCHFColumn.setCellValueFactory(
                cellData -> cellData.getValue().getBelegposition().getBetragCHFHaben());
        saldoCHFColumn.setCellValueFactory(
                cellData -> cellData.getValue().getSaldo());

    }

    public void setKonto(Konto konto) {
        this.konto = konto;
        this.fillKontopositionen();
        kontoComboBox.getSelectionModel().select(konto);
    }

    public void setKontoComboBoxAction() {
       // kontoAuszugTabelle.getItems().clear();
        konto = kontoComboBox.getValue();
       // this.fillKontopositionen();
       // setKonto(kontoComboBox.getValue());
        mainApp.showKontoauszug(konto);
    }


    public void setStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
}
