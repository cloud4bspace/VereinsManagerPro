package space.cloud4b.verein.view.finanzen;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import javafx.util.StringConverter;
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
import java.util.function.UnaryOperator;
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
    private ComboBox<Currency> waehrungComboBox;
    @FXML
    private ComboBox<Konto> kontoComboBox;
    @FXML
    private ComboBox<String> sollHabenComboBox;
    @FXML
    private TextField positionTextFeld;
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
        idLabel.setText("Beleg #" + position.getPositionNummerProperty().getValue());
        positionTextFeld.setText(position.getPositionTextProperty().getValue());

        letzteAenderungLabel.setText(position.getUserTimestamp());
        waehrungComboBox.getSelectionModel().select(position.getBetrag().getValue().getWaehrung());
        kontoComboBox.getSelectionModel().select(position.getKonto().getValue());
        sollHabenComboBox.getSelectionModel().select(position.getSH().getValue());
       // TextFormatter<Double> textFormatter = new TextFormatter<>(converter, 0.00, filter);
        betragFeld.setText(String.format("%.2f", position.getBetrag().getValue().getBetragBelegWaehrung().doubleValue()));
        betragCHFFeld.setText(String.format("%.2f", position.getBetrag().getValue().getBetragBuchungsWaehrung().doubleValue()));
        kursFeld.setText(String.format("%.4f", position.getBetrag().getValue().getUmrechnungsKurs()));
        betragFeld.textProperty().addListener((obs, oldText, newText) -> {
            handleBetragChanged();
        });

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

    public void handleBetragChanged() {
        if(betragFeld.getText().isEmpty()) {
            betragFeld.setText(String.format("%.2f", 0.00));
        }
        double kurs = Double.valueOf(kursFeld.getText());
        double betrag = Double.valueOf(betragFeld.getText());
        double betragCHF = Double.valueOf(betragCHFFeld.getText());
        if(waehrungComboBox.getValue().getCurrencyCode().equals("CHF")){
                    betragCHFFeld.setText(String.format("%.2f", Double.valueOf(betragFeld.getText())));
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
        DatabaseOperation.deleteBelegposision(position, mainApp.getCurrentUser());

        mainApp.showBelegkopfEdit(belegkopf);
    }
    public void handleAbbrechen() { this.dialogStage.close(); }
    public void handleSpeichern() {
        if (isValid()) {
            this.dialogStage.close();
            //position.setBelegPositionId();
           // position.setPositionNummer();
            position.setSollHaben(sollHabenComboBox.getValue());
            position.setKonto(kontoComboBox.getValue());
            position.setBetrag(new Betrag(new BigDecimal(betragFeld.getText()), waehrungComboBox.getValue(), new BigDecimal(betragCHFFeld.getText())));
            position.setPositionsText(positionTextFeld.getText());
            DatabaseOperation.updateBelegPosition(position, mainApp.getCurrentUser());
            DatabaseOperation.updateBelegKopf(belegkopf, mainApp.getCurrentUser());
            mainApp.showBelegkopfEdit(belegkopf);
        }
    }

    public boolean isValid() {
        String errorMeldung = null;
        boolean isValid = true;
        if (betragFeld.getText() == null || betragFeld.getText().length() == 0) {
            errorMeldung += "Ungültige PLZ!";
        } else {
            // versuchen, die PLZ zu Integer zu parsen
            try {
                Integer.parseInt(betragFeld.getText());
            } catch (NumberFormatException e) {
                errorMeldung += "PLZ muss eine Zahl sein!";
            }
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
