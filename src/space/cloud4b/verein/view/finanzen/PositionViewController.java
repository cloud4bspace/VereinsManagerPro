package space.cloud4b.verein.view.finanzen;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import space.cloud4b.verein.MainApp;
import space.cloud4b.verein.model.verein.finanzen.Belegkopf;
import space.cloud4b.verein.model.verein.finanzen.Belegposition;
import space.cloud4b.verein.model.verein.finanzen.Betrag;
import space.cloud4b.verein.model.verein.finanzen.Konto;
import space.cloud4b.verein.services.DatabaseOperation;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.Year;
import java.util.Currency;
import java.util.regex.Pattern;

public class PositionViewController {
    private MainApp mainApp;
    private Stage dialogStage;
    private Belegkopf belegkopf;
    private Belegposition position;
    private static DecimalFormat df2 = new DecimalFormat("#.##");
    private Pattern validEditingState = Pattern.compile("-?(([1-9][0-9]*)|0)?(\\.[0-9]*)?");

    // UI-Variabeln (Verknüpfung mit Elementen des Userinterfaces)
    @FXML
    private Label idLabel;
    @FXML
    private TextField betragFeld;
    @FXML
    private TextField betragCHFFeld;
    @FXML
    private TextField kursFeld;
    @FXML
    private Label letzteAenderungLabel;
    @FXML
    private ComboBox<Currency> waehrungComboBox = new ComboBox<>();
    @FXML
    private ComboBox<Konto> kontoComboBox = new ComboBox<>();
    @FXML
    private ComboBox<String> sollHabenComboBox = new ComboBox<>();
    @FXML
    private TextField positionTextFeld;
    @FXML
    private Button loeschenButton;
    @FXML
    private Button abbrechenButton;
    @FXML
    private Button speichernButton;
    private Stage fromStage;


    /**
     * Initialisieurng der FXML-Felder (wird automatisch nach dem Konstruktor aufgerufen
     */
    @FXML
    private void initializeData() {
        betragFeld.setAlignment(Pos.CENTER_RIGHT);
        betragCHFFeld.setAlignment(Pos.CENTER_RIGHT);
        kursFeld.setAlignment(Pos.CENTER_RIGHT);
       // waehrungComboBox.getItems().addAll(Currency.getAvailableCurrencies());
        waehrungComboBox.getItems().addAll(mainApp.getFinanzController().getBuchhalung().getWaehrungen());
        kontoComboBox.getItems().addAll(mainApp.getFinanzController().getBuchhalung().getBuchungsperiode(Year.now().getValue()).getKontenplan());
        sollHabenComboBox.getItems().addAll("S", "H");

    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        initializeData();
      //  belegNummerFeld.setAlignment(Pos.CENTER_LEFT);
        //belegNummerFeld.setStyle("-fx-background-color: #BADCB3");
       // belegUserFeld.setStyle("-fx-background-color: #BADCB3");

    }

    public void setBelegposition(Belegposition position, Belegkopf belegkopf) {
        this.position = position;
        this.belegkopf = belegkopf;
        idLabel.setText("Position #" + position.getPositionNummerProperty().getValue());
        positionTextFeld.setText(position.getPositionTextProperty().getValue());

        letzteAenderungLabel.setText(position.getUserTimestamp());
        System.out.println("diese Waehrung wird gesetzt: " + position.getBetrag().getValue().getWaehrung());
       // waehrungComboBox.getSelectionModel().select(position.getBetrag().getValue().getWaehrung());
        waehrungComboBox.getSelectionModel().select(position.getWaehrung());
        kontoComboBox.getSelectionModel().select(position.getKonto().getValue());
        sollHabenComboBox.getSelectionModel().select(position.getSH().getValue());
       // TextFormatter<Double> textFormatter = new TextFormatter<>(converter, 0.00, filter);
        betragFeld.setText(String.format("%,.2f", position.getBetrag().getValue().getBetragBelegWaehrung().doubleValue()));
        betragCHFFeld.setText(String.format("%,.2f", position.getBetrag().getValue().getBetragBuchungsWaehrung().doubleValue()));
        kursFeld.setText(String.format("%.4f", position.getBetrag().getValue().getUmrechnungsKurs()));
        betragFeld.textProperty().addListener((obs, oldText, newText) -> {
            handleBetragChanged();
        });

        if(belegkopf.getBelegStatus().getStatusElementKey() == 3 || belegkopf.getBelegStatus().getStatusElementKey() == 4){
            positionTextFeld.setDisable(true);
            waehrungComboBox.setDisable(true);
            kontoComboBox.setDisable(true);
            sollHabenComboBox.setDisable(true);
            betragFeld.setDisable(true);
            betragCHFFeld.setDisable(true);
            kursFeld.setDisable(true);
            loeschenButton.setDisable(true);
            speichernButton.setDisable(true);
        } else {
            positionTextFeld.setDisable(false);
            waehrungComboBox.setDisable(false);
            kontoComboBox.setDisable(false);
            sollHabenComboBox.setDisable(false);
            betragFeld.setDisable(false);
            betragCHFFeld.setDisable(false);
            kursFeld.setDisable(false);
            loeschenButton.setDisable(false);
            speichernButton.setDisable(false);
        }


    }

    public void handleBetragChanged() {

        if(betragFeld.getText().isEmpty()) {
            betragFeld.setText(String.format("%,.2f", 0.00));
        }
        double kurs = Double.valueOf(kursFeld.getText().replace("'", ""));
        double betrag = Double.valueOf(betragFeld.getText().replace("'", ""));
        double betragCHF = Double.valueOf(betragCHFFeld.getText().replace("'", ""));
        if(waehrungComboBox.getValue().equals(Currency.getInstance("CHF"))){
                    betragCHFFeld.setText(String.format("%,.2f", betrag));
                    kursFeld.setText(String.format("%.4f",1.000));
        } else {
                    betragCHFFeld.setText(String.valueOf(kurs*betrag));
        }
    }

    public void handleHauptjournal(){
        mainApp.showHauptjournalView();
    }
    public void handleLoeschen() {
        System.out.println("Belegposition löschen");
        this.dialogStage.close();
        belegkopf.getBelegPositionenAsArrayList().remove(position);
        DatabaseOperation.deleteBelegposision(belegkopf, position, mainApp.getCurrentUser());

        mainApp.showBelegkopfEdit(belegkopf);
    }
    public void handleAbbrechen() { this.dialogStage.close(); }
    public void handleSpeichern() {
        if (isValid()) {

            //position.setBelegPositionId();
           // position.setPositionNummer();
            position.setSollHaben(sollHabenComboBox.getValue());
            position.setKonto(kontoComboBox.getValue());
            double betrag = new Double(betragFeld.getText().replace("'", ""));
            double betragCHF = Double.parseDouble(betragCHFFeld.getText().replace("'",""));
            position.setBetrag(new Betrag(new BigDecimal(betrag), waehrungComboBox.getValue(), new BigDecimal(betragCHF)));
            position.setPositionsText(positionTextFeld.getText());

            if(!belegkopf.getBelegPositionenAsArrayList().contains(position)) {
                belegkopf.getBelegPositionenAsArrayList().add(position);
            }
            DatabaseOperation.updateBelegPosition(position, mainApp.getCurrentUser());
            DatabaseOperation.updateBelegKopf(belegkopf, mainApp.getCurrentUser());
            this.dialogStage.close();
            mainApp.showBelegkopfEdit(belegkopf);
        }
    }

    public boolean isValid() {
        String errorMeldung = null;
        boolean isValid = true;
        if (betragFeld.getText() == null || betragFeld.getText().length() == 0) {
            errorMeldung += "Ungültiger Betrag!";
            isValid = false;
        } else {
            // versuchen, die PLZ zu Integer zu parsen
            try {
                Integer.parseInt(betragFeld.getText());
            } catch (NumberFormatException e) {
                errorMeldung += "Betrag muss eine Zahl sein!";
            }
        }
        if(kontoComboBox.getValue() == null) {
            errorMeldung += "Konto auswählen!";
            isValid = false;
        }
        // Meldung anzeigen, wenn Eingaben ungültig sind
        if (!isValid) {
            mainApp.showAlert(errorMeldung, dialogStage);
        }
        return isValid;
    }
    public void setStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setFromStage(Stage fromDialogStage) {
        this.fromStage = fromDialogStage;
    }
}
