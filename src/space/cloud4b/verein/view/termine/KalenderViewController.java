package space.cloud4b.verein.view.termine;

import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import space.cloud4b.verein.MainApp;
import space.cloud4b.verein.model.verein.kalender.Termin;
import space.cloud4b.verein.services.DatabaseReader;
import space.cloud4b.verein.services.Observer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.Locale;

public class KalenderViewController implements Observer {

    private Stage dialogStage;
    private MainApp mainApp;
    private LocalDate todaydate = LocalDate.now();
    private LocalDate firstOfMonth = todaydate.withDayOfMonth(1);
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd. LLL");
    private DateTimeFormatter formatterMonth = DateTimeFormatter.ofPattern("MMMM yyyy");

    // UI-Variabeln (Verknüpfung mit Elementen des Userinterfaces)
    @FXML
    private GridPane kalenderGrid;
    @FXML
    private Label topRowLabel = new Label();
    @FXML
    private Button backButton;
    @FXML
    private Button nextButton;



    public KalenderViewController() {

        //Konstruktor wird nicht (zwingend) benötigt
        //Konstruktor hat noch keinen Zugriff auf @FXML-Variabeln
    }

    /**
     * Initialisierung der controller class
     */
    @FXML
    private void initialize() {
        // alle Elemnte aus der GridPane entfernen
        kalenderGrid.getChildren().clear();

        // Monat und Jahr als Titel setzen
        topRowLabel.setText(formatterMonth.format(firstOfMonth));

        // Button für vorhergehenden Monat erstellen
        Text iconTxt = GlyphsDude.createIcon(FontAwesomeIcon.ANGLE_DOUBLE_LEFT, "14px");
        iconTxt.setFill(Color.GRAY);
        backButton.setText(formatterMonth.format(firstOfMonth.minusMonths(1)));
        backButton.setGraphic(iconTxt);

        // Button für nächsten Monat erstellen
        iconTxt = GlyphsDude.createIcon(FontAwesomeIcon.ANGLE_DOUBLE_RIGHT, "14px");
        iconTxt.setFill(Color.GRAY);
        nextButton.setText(formatterMonth.format(firstOfMonth.plusMonths(1)));
        nextButton.setGraphic(iconTxt);

        // die Header-Zeile mit den Wochentagen füllen
        kalenderGrid.add(new Label("Mo."), 1, 0);
        kalenderGrid.add(new Label("Di."), 2, 0);
        kalenderGrid.add(new Label("Mi."), 3, 0);
        kalenderGrid.add(new Label("Do."), 4, 0);
        kalenderGrid.add(new Label("Fr."), 5, 0);
        kalenderGrid.add(new Label("Sa."), 6, 0);
        kalenderGrid.add(new Label("So."), 7, 0);

        // die aktuelle Woche und das Startdatum ermitteln
        int weekNumber = firstOfMonth.get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear());
        LocalDate currentDate = firstOfMonth;


        int cellCount = 0; // Zähler für die Zellen der GridPane

        // die Wochen des Monats durchlaufen (maximal 6)
        for (int week = 1; week <= 6; week++) {

            // die Wochennummer in die erste Spalte schreiben
            kalenderGrid.add(new Label("Wo " + weekNumber++), 0, week);

            // die Tage der Woche durchlaufen
            for (int tag = 1; tag <= 7; tag++) {
                cellCount++;
                int dow = currentDate.getDayOfWeek().getValue();

                // jede Zelle erhält ein VBox mit einem Label und einer ListView
                VBox vbox = new VBox();
                Label label = new Label();
                label.setId("kalenderLabel");

                // die ListView erstellen und die anzuzeigenden Daten bestimmen
                ListView<Termin> listView = new ListView<>();
                listView.setCellFactory(param -> new ListCell<Termin>() {
                    protected void updateItem(Termin item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null || item.getTerminId() < 1) {
                            setText(null);
                        } else {
                            setText(item.getKalenderText());
                        }
                    }
                });

                // in der ersten Woche sind ggf. noch Daten des Vormonats anzuzeigen
                if (week == 1) {
                    label.setText(formatter.format(currentDate.minusDays(dow - tag)));
                    listView.setItems(FXCollections.observableArrayList(DatabaseReader
                            .getTermineFromLocalDate(currentDate.minusDays(dow - tag))));
                } else {
                    label.setText(formatter.format(currentDate.plusDays(cellCount - dow)));
                    listView.setItems(FXCollections.observableArrayList(DatabaseReader
                            .getTermineFromLocalDate(currentDate.plusDays(cellCount - dow))));
                }
                vbox.getChildren().add(0, label);
                vbox.getChildren().add(1, listView);

                // die Ansicht für einen Tag wird an der richtigen Position ins GridPane eingefügt
                kalenderGrid.add(vbox, tag, week);
            }
        }
    }

    /**
     * ermittelt den vorangegangenen Monat und initialisiert die Ansicht mit dem neuen Monat
     */
    @FXML
    public void handleNextMonth() {
        this.firstOfMonth = this.firstOfMonth.plusMonths(1);
        initialize();
    }

    /**
     * ermittelt den nächsten Monat und initialisiert die Ansicht mit dem neuen Monat
     */
    @FXML
    public void handlePreviousMonth() {
        this.firstOfMonth = this.firstOfMonth.minusMonths(1);
        initialize();

    }

    /**
     * verknüpft den Controller mit der MainApp und fügt ihn zur Observerliste des
     * KalenderControllers hinzu
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        mainApp.getKalenderController().Attach(this);
    }


    @Override
    public void update(Object o) {

    }

    public void setStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
}
