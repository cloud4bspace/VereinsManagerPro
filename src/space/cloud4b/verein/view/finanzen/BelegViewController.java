package space.cloud4b.verein.view.finanzen;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import space.cloud4b.verein.MainApp;
import space.cloud4b.verein.model.verein.finanzen.Belegkopf;
import space.cloud4b.verein.model.verein.finanzen.Belegposition;
import space.cloud4b.verein.model.verein.status.Status;
import space.cloud4b.verein.services.DatabaseOperation;
import space.cloud4b.verein.services.DatabaseReader;

import java.text.DecimalFormat;
import java.util.Currency;
import java.util.regex.Pattern;

public class BelegViewController {
    private MainApp mainApp;
    private Stage dialogStage;
    private Belegkopf belegkopf;
    private static DecimalFormat df2 = new DecimalFormat("#.##");
    private Pattern validEditingState = Pattern.compile("-?(([1-9][0-9]*)|0)?(\\.[0-9]*)?");

    // UI-Variabeln (Verknüpfung mit Elementen des Userinterfaces)
    @FXML
    private Label idLabel;
    @FXML
    private DatePicker belegDatumPicker;
    @FXML
    private DatePicker buchungsDatumPicker;
    @FXML
    private TextField belegBetragFeld;
    @FXML
    private TextField belegBetragCHFFeld;
    @FXML
    private TextField belegKursFeld;
    @FXML
    private TextField belegPeriodeFeld;
    @FXML
    private Label letzteAenderungLabel;
    @FXML
    private ComboBox<Currency> belegWaehrungComboBox;
    @FXML
    private Label belegStatusLabel;
    @FXML
    private TextField belegTextFeld;
    @FXML
    private TableView<Belegposition> belegPositionTabelle;
    @FXML
    private TableColumn<Belegposition, Number> positionPositionColumn;
    @FXML
    private TableColumn<Belegposition, String> positionSHColumn;
    @FXML
    private TableColumn<Belegposition, String> positionKontoColumn;
    @FXML
    private TableColumn<Belegposition, String> positionBetragColumn;
    @FXML
    private TableColumn<Belegposition, String> positionWaehrungColumn;
    @FXML
    private TableColumn<Belegposition, String> positionTextColumn;
    @FXML
    private TableColumn<Belegposition, String> positionBetragCHFColumn;
    @FXML
    private Button speichernButton;
    @FXML
    private Button addPositionButton;
    @FXML
    private Button abschliessenButton;
    @FXML
    private Button stornoButten;


    /**
     * Initialisieurng der FXML-Felder (wird automatisch nach dem Konstruktor aufgerufen
     */
    @FXML
    private void initialize() {
        belegBetragFeld.setAlignment(Pos.CENTER_RIGHT);
        belegBetragCHFFeld.setAlignment(Pos.CENTER_RIGHT);
        belegKursFeld.setAlignment(Pos.CENTER_RIGHT);
        belegWaehrungComboBox.getItems().addAll(Currency.getAvailableCurrencies());
        positionBetragCHFColumn.setStyle( "-fx-alignment: CENTER-RIGHT;");
        positionBetragColumn.setStyle( "-fx-alignment: CENTER-RIGHT;");
        positionPositionColumn.setStyle( "-fx-alignment: CENTER-RIGHT;");
        positionTextColumn.setCellFactory(
                TextFieldTableCell.forTableColumn());
        positionTextColumn.setOnEditCommit(
                (TableColumn.CellEditEvent<Belegposition, String> t) ->
                        ( t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setText(t.getNewValue())
        );
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
      //  belegNummerFeld.setAlignment(Pos.CENTER_LEFT);
        //belegNummerFeld.setStyle("-fx-background-color: #BADCB3");
       // belegUserFeld.setStyle("-fx-background-color: #BADCB3");

    }

    public void setBelegkopf(Belegkopf beleg) {
        switch(beleg.getBelegStatus().getStatusElementKey()) {
            case 1: // unvollständig
            case 9: // fehler
                buchungsDatumPicker.setDisable(false);
                belegDatumPicker.setDisable(false);
                addPositionButton.setDisable(false);
                speichernButton.setDisable(false);
                abschliessenButton.setDisable(true);
                stornoButten.setDisable(true);
                belegTextFeld.setDisable(false);
                belegTextFeld.setEditable(true);
                belegPeriodeFeld.setDisable(false);
                break;
            case 2: // bereit zur Verbuchung
                buchungsDatumPicker.setDisable(false);
                belegDatumPicker.setDisable(false);
                addPositionButton.setDisable(false);
                speichernButton.setDisable(false);
                abschliessenButton.setDisable(false);
                stornoButten.setDisable(true);
                belegTextFeld.setDisable(false);
                belegTextFeld.setEditable(true);
                belegPeriodeFeld.setDisable(false);
                break;
            case 3: // verbucht
                buchungsDatumPicker.setDisable(true);
                belegDatumPicker.setDisable(true);
                addPositionButton.setDisable(true);
                speichernButton.setDisable(true);
                abschliessenButton.setDisable(true);
                stornoButten.setDisable(false);
                belegTextFeld.setDisable(true);
                belegTextFeld.setEditable(false);
                belegPeriodeFeld.setDisable(true);
                break;
            case 4: // archiviert
                buchungsDatumPicker.setDisable(true);
                belegDatumPicker.setDisable(true);
                addPositionButton.setDisable(true);
                speichernButton.setDisable(true);
                abschliessenButton.setDisable(true);
                stornoButten.setDisable(true);
                belegTextFeld.setDisable(true);
                belegTextFeld.setEditable(false);
                belegPeriodeFeld.setDisable(true);
                break;
            default:
                break;
        }
        this.belegkopf = beleg;
        belegStatusLabel.setText(beleg.getStatusStringProperty().getValue());
        idLabel.setText("Beleg #" + String.format("%04d",beleg.getBelegNummerProperty().getValue()));
        belegTextFeld.setText(beleg.getBelegTextProperty().getValue());
        belegDatumPicker.setValue(beleg.getBelegDatum());
        buchungsDatumPicker.setValue(beleg.getBuchungsDatum());
        letzteAenderungLabel.setText(beleg.getUserTimestamp());
        belegWaehrungComboBox.getSelectionModel().select(beleg.getBetrag().getWaehrung());
        belegBetragFeld.setText(String.format("%,.2f", beleg.getBetrag().getBetragBelegWaehrung().doubleValue()));
        belegBetragCHFFeld.setText(String.format("%,.2f", beleg.getBetrag().getBetragBuchungsWaehrung().doubleValue()));
        belegKursFeld.setText(String.format("%.4f", beleg.getBetrag().getUmrechnungsKurs()));
        belegPeriodeFeld.setText(Integer.toString(beleg.getBuchungsPeriode().getJahr()));

        belegPositionTabelle.setItems(FXCollections.observableArrayList(beleg.getBelegPositionenAsArrayList()));
        belegPositionTabelle.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> openPosition(newValue));
        positionPositionColumn.setCellValueFactory(
                cellData -> cellData.getValue().getPosition());
        positionSHColumn.setCellValueFactory(
                cellData -> cellData.getValue().getSH());
        positionKontoColumn.setCellValueFactory(
                cellData -> cellData.getValue().getKontoObject().toTableString());
        positionBetragColumn.setCellValueFactory(
                cellData -> cellData.getValue().getBetrag().getValue().toColumnString());
        positionTextColumn.setCellValueFactory(
                cellData -> cellData.getValue().getTextProperty());
        positionBetragCHFColumn.setCellValueFactory(
                cellData -> cellData.getValue().getBetrag().getValue().toColumnCHFString());
        positionWaehrungColumn.setCellValueFactory(
                cellData -> new SimpleStringProperty(cellData.getValue().getBetrag().getValue().getWaehrung().getCurrencyCode()));
           // projectTrackingService.save(edit.getRowValue());

    }

    public void openPosition(Belegposition position) {
            if (position != null) {
                mainApp.showBelegpositionEdit(position, this.dialogStage, belegkopf);
            }
    }

    public void handleStornieren(){
        //TODO
    }
    public void handleAbschliessen() {
        if(isValid()){
            belegkopf.setBelegDatum(belegDatumPicker.getValue());
            belegkopf.setBuchungsDatum(buchungsDatumPicker.getValue());
            belegkopf.setBuchungsPeriode(mainApp.getFinanzController().getBuchhalung().getBuchungsperiode(Integer.parseInt(belegPeriodeFeld.getText())));
            belegkopf.setBelegKopfText(belegTextFeld.getText());
            // belegkopf.setBetrag(new Betrag(new BigDecimal(0), Currency.getInstance("CHF"),new BigDecimal(0)));
            belegkopf.setBetrag(DatabaseReader.readBelegkopfBetrag(belegkopf));
            belegkopf.setBetragCHF(DatabaseReader.readBelegkopfBetragCHF(belegkopf));
            belegkopf.setWaehrung(DatabaseReader.readBelegkopfWaehrung(belegkopf));
            belegkopf.setStatus(new Status(9).getStatusElemente().get(3));
            DatabaseOperation.bookBelegKopf(belegkopf, mainApp.getCurrentUser());
            belegkopf.setBelegNummer(DatabaseReader.getLastBelegnummer(belegkopf.getBuchungsPeriode())+1);
            setBelegkopf(belegkopf);
        }
    }
    public void handleAddPosition() {
        handleSpeichern();
        Belegposition belegposition = DatabaseOperation.addBelegPosition(belegkopf, mainApp.getCurrentUser());
       // belegkopf.getBelegPositionenAsArrayList().add(belegposition);
        // erst hinzufügen, wenn Position komplett ist (inkl. Kontonummer)
        belegPositionTabelle.setItems(FXCollections.observableArrayList(belegkopf.getBelegPositionenAsArrayList()));
        openPosition(belegposition);
    }

    public void handleSpeichern() {
        if(isValid()){
            belegkopf.setBelegDatum(belegDatumPicker.getValue());
            belegkopf.setBuchungsDatum(buchungsDatumPicker.getValue());
            belegkopf.setBuchungsPeriode(mainApp.getFinanzController().getBuchhalung().getBuchungsperiode(Integer.parseInt(belegPeriodeFeld.getText())));
            belegkopf.setBelegKopfText(belegTextFeld.getText());
           // belegkopf.setBetrag(new Betrag(new BigDecimal(0), Currency.getInstance("CHF"),new BigDecimal(0)));
            belegkopf.setBetrag(DatabaseReader.readBelegkopfBetrag(belegkopf));
            belegkopf.setBetragCHF(DatabaseReader.readBelegkopfBetragCHF(belegkopf));
            belegkopf.setWaehrung(DatabaseReader.readBelegkopfWaehrung(belegkopf));
            DatabaseOperation.updateBelegKopf(belegkopf, mainApp.getCurrentUser());
        }
    }
    public boolean isValid() {
        return true;
    }

    public void handleHauptjournal(){
        mainApp.showHauptjournalView();
    }
    public void setStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
}
