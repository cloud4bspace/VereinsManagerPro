package space.cloud4b.verein.view.dashboard;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import space.cloud4b.verein.MainApp;
import space.cloud4b.verein.controller.AdressController;
import space.cloud4b.verein.controller.KalenderController;
import space.cloud4b.verein.model.verein.adressbuch.Mitglied;
import space.cloud4b.verein.model.verein.kalender.Jubilaeum;
import space.cloud4b.verein.model.verein.kalender.Termin;
import space.cloud4b.verein.model.verein.kontrolle.rangliste.Position;
import space.cloud4b.verein.model.verein.status.StatusElement;
import space.cloud4b.verein.services.Observer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class DashBoardController implements Observer {

    private MainApp mainApp;

    // UI-Variabeln (Verknüpfung mit Elementen des Userinterfaces)
    @FXML
    private TableView<Mitglied> mitgliederTabelle;
    @FXML
    private Label mitgliederLabel;
    @FXML
    private TableColumn<Mitglied, Number> idSpalte;
    @FXML
    private TableColumn<Mitglied, String> nachNameSpalte;
    @FXML
    private TableColumn<Mitglied, String> vorNameSpalte;
    @FXML
    private TableColumn<Mitglied, StatusElement> anredeSpalte;
    @FXML
    private TableColumn<Mitglied, StatusElement> kategorieSpalte;

    @FXML
    private TableView<Termin> termineTabelle;
    @FXML
    private Label terminLabel;
    @FXML
    private TableColumn<Termin, String> terminDatumSpalte;
    @FXML
    private TableColumn<Termin, String> terminZeitSpalte;
    @FXML
    private TableColumn<Termin, String> terminTextSpalte;

    @FXML
    private TableView<Jubilaeum> jubilaeumTabelle;
    @FXML
    private Label jubilaeumLabel;
    @FXML
    private TableColumn<Jubilaeum, String> jubilaeumDatumStringSpalte;
    @FXML
    private TableColumn<Jubilaeum, String> jubilaeumTextSpalte;

    @FXML
    private TableView<Position> ranglisteTabelle;
    @FXML
    private Label anwesenheitenLabel;
    @FXML
    private TableColumn<Position, Number> rangSpalte;
    @FXML
    private TableColumn<Position, String> mitgliedSpalte;
    @FXML
    private TableColumn<Position, Number> anzTermineSpalte;
    @FXML
    private TableColumn<Position, Number> anzAnwesenheiten;
    @FXML
    private TableColumn<Position, Number> anwesenheitsAnteilSpalte;


    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        mainApp.getAdressController().Attach(this);
        mainApp.getKalenderController().Attach(this);

        // Add observable list data to the tables
        mitgliederTabelle.setItems(FXCollections.observableArrayList(mainApp.getAdressController().getMitgliederListe()));
        termineTabelle.setItems(FXCollections.observableArrayList(mainApp.getKalenderController().getNaechsteTerminListe()));
        jubilaeumTabelle.setItems(FXCollections.observableArrayList(mainApp.getKalenderController().getJubilaeumsListe()));
        ranglisteTabelle.setItems(mainApp.getMainController().getRangliste().getRanglistenListe());
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        // Mitgliederliste initialisieren
        mitgliederLabel.setText("Mitglieder (Stand "
                + LocalDate.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)) + ")");
        idSpalte.setCellValueFactory(
                cellData -> cellData.getValue().getIdProperty());
        vorNameSpalte.setCellValueFactory(
                cellData -> cellData.getValue().getVornameProperty());
        nachNameSpalte.setCellValueFactory(
                cellData -> cellData.getValue().getNachnameProperty());
        anredeSpalte.setCellValueFactory(
                cellData -> cellData.getValue().getAnredeProperty());
        kategorieSpalte.setCellValueFactory(
                cellData -> cellData.getValue().getKategorieIElementProperty());


        // Terminliste initialisieren
        terminDatumSpalte.setCellValueFactory(
                cellData -> cellData.getValue().getDateAsLocalStringMedium());
       terminZeitSpalte.setCellValueFactory(
                cellData -> cellData.getValue().getZeitTextProperty());
        terminTextSpalte.setCellValueFactory(
                cellData -> cellData.getValue().getTextProperty());


        // Jubiläumsliste initialisieren
      //  jubilaeumDatumSpalte.setCellValueFactory(
           //     cellData -> cellData.getValue().getDatumProperty());
        jubilaeumTextSpalte.setCellValueFactory(
                cellData -> cellData.getValue().getTextProperty());
        jubilaeumDatumStringSpalte.setCellValueFactory(
                cellData -> cellData.getValue().getDateAsNiceString());

        // Rangliste initialisieren
        anwesenheitenLabel.setText("Rangliste der Anwesenheiten (Jahr " + LocalDate.now().getYear() + ")");
        rangSpalte.setCellValueFactory(
                cellData -> cellData.getValue().getRangProperty());
        mitgliedSpalte.setCellValueFactory(
                cellData -> cellData.getValue().getKurzbezeichnungProperty());
        anzTermineSpalte.setCellValueFactory(
                cellData -> cellData.getValue().getAnzahlTermineProperty());
        anzAnwesenheiten.setCellValueFactory(
                cellData -> cellData.getValue().getAnzahlAnwesenheitenProperty());
        anwesenheitsAnteilSpalte.setCellValueFactory(
                cellData -> cellData.getValue().getAnwesenheitsAnteilProperty());

    }


    @Override
    public void update(Object o) {
        System.out.println("DashBoardController hat Update-Meldung erhalten");
        if (o instanceof AdressController) {
            AdressController ac = (AdressController) o;
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    mitgliederTabelle.setItems(FXCollections.observableArrayList(mainApp.getAdressController().getMitgliederListe()));
                }

                // FXCollections.observableArrayList(mitgliederListe);
            });
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    jubilaeumTabelle.setItems(FXCollections.observableArrayList(mainApp.getAdressController().getJubilaeumsListe()));
                }
            });
        }
        if (o instanceof KalenderController) {
            KalenderController kc = (KalenderController) o;
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    termineTabelle.setItems(FXCollections.observableArrayList(mainApp.getKalenderController().getNaechsteTerminListe()));
                }
            });
        }

    }
}
