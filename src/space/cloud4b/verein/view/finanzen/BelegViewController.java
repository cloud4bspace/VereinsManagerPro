package space.cloud4b.verein.view.finanzen;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import space.cloud4b.verein.MainApp;
import space.cloud4b.verein.model.verein.finanzen.Belegkopf;
import space.cloud4b.verein.model.verein.finanzen.Belegposition;
import space.cloud4b.verein.model.verein.finanzen.Konto;
import space.cloud4b.verein.services.DatabaseOperation;
import space.cloud4b.verein.services.DatabaseReader;

import java.text.DecimalFormat;
import java.util.Currency;
import java.util.function.UnaryOperator;
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
    private TableColumn<Belegposition, Konto> positionKontoColumn;
    @FXML
    private TableColumn<Belegposition, String> positionBetragColumn;
    @FXML
    private TableColumn<Belegposition, String> positionWaehrungColumn;
    @FXML
    private TableColumn<Belegposition, String> positionTextColumn;
    @FXML
    private TableColumn<Belegposition, String> positionBetragCHFColumn;


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
        this.belegkopf = beleg;
        belegStatusLabel.setText(beleg.getStatusStringProperty().getValue());
        idLabel.setText("Beleg #" + String.format("%04d",beleg.getBelegNummerProperty().getValue()));
        belegTextFeld.setText(beleg.getBelegTextProperty().getValue());
        belegDatumPicker.setValue(beleg.getBelegDatum());
        buchungsDatumPicker.setValue(beleg.getBuchungsDatum());
        letzteAenderungLabel.setText(beleg.getUserTimestamp());
        belegWaehrungComboBox.getSelectionModel().select(beleg.getBetrag().getWaehrung());
        belegBetragFeld.setText(String.format("%.2f", beleg.getBetrag().getBetragBelegWaehrung().doubleValue()));
        belegBetragCHFFeld.setText(String.format("%.2f", beleg.getBetrag().getBetragBuchungsWaehrung().doubleValue()));
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
                cellData -> cellData.getValue().getKonto());
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

    public void handleAddPosition() {
        Belegposition belegposition = DatabaseOperation.addBelegPosition(belegkopf, mainApp.getCurrentUser());
        belegkopf.getBelegPositionenAsArrayList().add(belegposition);
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
    StringConverter<Double> converter = new StringConverter<Double>() {

        @Override
        public Double fromString(String s) {
            if (s.isEmpty() || "-".equals(s) || ".".equals(s) || "-.".equals(s)) {
                return 0.00 ;
            } else {
                return Double.valueOf(s);
            }
        }

        @Override
        public String toString(Double d) {
            return d.toString();
        }
    };

    UnaryOperator<TextFormatter.Change> filter = c -> {
        String text = c.getControlNewText();
        if (validEditingState.matcher(text).matches()) {
            return c ;
        } else {
            return null ;
        }
    };
    public void handleHauptjournal(){
        mainApp.showHauptjournalView();
    }
    public void setStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
}
