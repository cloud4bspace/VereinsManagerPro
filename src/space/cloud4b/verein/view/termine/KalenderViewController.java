package space.cloud4b.verein.view.termine;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import space.cloud4b.verein.MainApp;
import space.cloud4b.verein.model.verein.kalender.Termin;
import space.cloud4b.verein.services.DatabaseReader;
import space.cloud4b.verein.services.Observer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class KalenderViewController implements Observer {

    private Stage dialogStage;
    private MainApp mainApp;
    private ArrayList<Termin> terminListe;
    private LocalDate todaydate = LocalDate.now();
    private LocalDate firstOfMonth = todaydate.withDayOfMonth(1);
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd. LLL");
    private DateTimeFormatter formatterMonth = DateTimeFormatter.ofPattern("MMMM yyyy");

    @FXML
    private VBox topRowVBox = new VBox();
    @FXML
    private Label topRowLabel = new Label();
    @FXML
    private Label weekRow1Label = new Label();
    @FXML
    private Label weekRow2Label = new Label();
    @FXML
    private Label weekRow3Label = new Label();
    @FXML
    private Label weekRow4Label = new Label();
    @FXML
    private Label weekRow5Label = new Label();
    @FXML
    private Label weegRow6Label = new Label();
    @FXML
    private Label day01Label = new Label();
    @FXML
    private ListView<Termin> day01ListView = new ListView<>();
    @FXML
    private Label day02Label = new Label();
    @FXML
    private ListView<String> day02ListView = new ListView<>();
    @FXML
    private Label day03Label = new Label();
    @FXML
    private Label day04Label = new Label();
    @FXML
    private Label day05Label = new Label();
    @FXML
    private Label day06Label = new Label();
    @FXML
    private Label day07Label = new Label();


    public KalenderViewController() {

        //Konstruktor wird nicht (zwingend) benötigt
        //Konstruktor hat noch keinen Zugriff auf @FXML-Variabeln
    }

    /**
     * Initialisierung der controller class
     */
    @FXML
    private void initialize() {

        day01ListView.setCellFactory(param -> new ListCell<Termin>() {
            protected void updateItem(Termin item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null || item.getTerminId() < 1) {
                    setText(null);
                } else {
                    setText(Integer.toString(item.getTerminId()));
                }
            }
        });
        // ich fülle zuerst eine Array mit allen Tagen der Matrix...
        Map<Integer, LocalDate> datenDesMonats = new HashMap<>();
        int weekNumber = firstOfMonth.get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear());
        LocalDate currentDate = firstOfMonth;

        // Woche 1 durchlaufen
        int position = 0; // Positionen innerhalb der Matrix von 0 bis 41 (42 Felder = 7 X 6)
        weekRow1Label.setText("Wo " + weekNumber++);
        // Zelle 1
        int dow = currentDate.getDayOfWeek().getValue();
        int dowCount = 1;
        if (dow == dowCount) {
            day01Label.setText(formatter.format(currentDate) + "(" + currentDate.getDayOfWeek() + ")");
            datenDesMonats.put(position, currentDate);
        } else {
            day01Label.setText(formatter.format(currentDate.minusDays(dow - dowCount)));
            day01Label.setStyle("-fx-text-fill: grey; -fx-background-color: #c8cdcc");
            datenDesMonats.put(position, currentDate.minusDays(dow - dowCount));
        }
        day01ListView.setItems(FXCollections.observableArrayList(DatabaseReader.getKommendeTermineAsArrayList()));
        // Zelle 2
        currentDate.plusDays(1);
        dowCount++;
        if (dow == dowCount) {
            day02Label.setText(formatter.format(currentDate) + "(" + currentDate.getDayOfWeek() + ")");
            datenDesMonats.put(position, currentDate);
        } else {
            day02Label.setText(formatter.format(currentDate.minusDays(dow - dowCount)));
            day02Label.setStyle("-fx-text-fill: grey; -fx-background-color: #c8cdcc");
            datenDesMonats.put(position, currentDate.minusDays(dow - dowCount));
        }
        // Zelle 3
        currentDate.plusDays(1);
        dowCount++;
        if (dow == dowCount) {
            day03Label.setText(formatter.format(currentDate) + "(" + currentDate.getDayOfWeek() + ")");
            datenDesMonats.put(position, currentDate);
        } else {
            day03Label.setText(formatter.format(currentDate.minusDays(dow - dowCount)));
            day03Label.setStyle("-fx-text-fill: grey; -fx-background-color: #c8cdcc");
            datenDesMonats.put(position, currentDate.minusDays(dow - dowCount));
        }


        //  }

        topRowLabel.setText(formatterMonth.format(firstOfMonth));
        // do first week

        weekRow2Label.setText("Wo " + weekNumber++);
        weekRow3Label.setText("Wo " + weekNumber++);


        // day01ListView.setItems();
    }

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
