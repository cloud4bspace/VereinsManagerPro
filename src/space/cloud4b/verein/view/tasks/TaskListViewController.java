package space.cloud4b.verein.view.tasks;

import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import space.cloud4b.verein.MainApp;
import space.cloud4b.verein.model.verein.adressbuch.Mitglied;
import space.cloud4b.verein.model.verein.task.Task;
import space.cloud4b.verein.services.DatabaseReader;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;

public class TaskListViewController {
    private Stage dialogStage;
    private MainApp mainApp;

    // UI-Variabeln (Verknüpfung mit Elementen des Userinterfaces)
    @FXML
    private ListView<Task> taskListView;
    @FXML
    private Label titelLabel;
    @FXML
    private ScrollPane scrollPane;
    private ObservableList<Task> masterData = FXCollections.observableArrayList();

    public TaskListViewController() {
        //Konstruktor wird nicht benötigt
    }

    /**
     * Initialisierung der controller class
     */
    @FXML
    private void initialize() {
        titelLabel.setText("Taskliste (Stand "
                + LocalDate.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)) + ")");
        /*
        taskListView.setCellFactory(param -> new ListCell<Task>() {
            protected void updateItem(Task item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item.getTaskId() < 1) {
                    setText(null);
                } else {
                    setText(item.getOutputText());
                }
            }
        });
        taskListView.setItems(FXCollections.observableArrayList(DatabaseReader
                            .getTaskList()));
*/
// alle Elemnte aus der GridPane entfernen
        scrollPane.setPadding(new Insets(10));
        GridPane gridPane = new GridPane();
        gridPane.getChildren().clear();
        gridPane.setId("taskGridPane");
        RowConstraints rc = new RowConstraints();
        rc.setMinHeight(50);
        rc.setValignment(VPos.TOP);


        // gridPane.setStyle("-fx-background-color: #e9fff4;-fx-horizontal-grid-lines-visible: true;");
        ArrayList<Task> taskList = DatabaseReader.getTaskList();

        ArrayList<Mitglied> mitgliederListe = DatabaseReader.getMitgliederAsArrayList();
        Text iconUserTxt = GlyphsDude.createIcon(FontAwesomeIcon.USER, "16px");
        //iconTxt.setFill(Color.color(0.969, 0.535, 0.535));
        iconUserTxt.setFill(Color.BLACK);
        //  label.setId("overDue");

        for (int i = 0; i < taskList.size(); i++) {

            // gridPane.setGridLinesVisible(true);

            Label label = new Label("#" + taskList.get(i).getTaskId());
            // label.setPadding(new Insets(5, 10, 5, 10));
            label.setId("taskId");
            VBox vbox = new VBox();
            vbox.setId("taskCell");
            vbox.getChildren().add(label);
            gridPane.add(vbox, 0, i);


            label = new Label(taskList.get(i).getTaskTitel());
            label.setId("taskTitel");
            label.setWrapText(true);

            Label label2 = new Label(taskList.get(i).getTaskText());
            label2.setId("taskDetails");
            label2.setWrapText(true);

            vbox = new VBox();
            vbox.setId("taskCell");
            vbox.getChildren().addAll(label, label2);
            gridPane.add(vbox, 1, i);


            label = new Label(taskList.get(i).getTaskDatum().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));
            label.setWrapText(true);
            label.setId("taskTitel");
            if (taskList.get(i).getTaskDatum().isBefore(LocalDate.now())) {
                Text iconTxt = GlyphsDude.createIcon(FontAwesomeIcon.EXCLAMATION_TRIANGLE, "16px");
                //iconTxt.setFill(Color.color(0.969, 0.535, 0.535));
                iconTxt.setFill(Color.DARKRED);
                //  label.setId("overDue");
                label.setGraphic(iconTxt);
                ;
            }

            vbox = new VBox();
            vbox.setId("taskCell");
            vbox.getChildren().addAll(label);
            gridPane.add(vbox, 2, i);

            label2 = new Label(taskList.get(i).getPrioStatus().getStatusElementTextLang());
            label2.setWrapText(true);
            label2.setId("taskTitel");

            Label label3 = new Label(taskList.get(i).getStatusStatus().getStatusElementTextLang());
            label3.setWrapText(true);
            label3.setId("taskTitel");

            vbox = new VBox();
            vbox.setId("taskCell");
            vbox.getChildren().addAll(label2, label3);
            gridPane.add(vbox, 3, i);


            vbox = new VBox();
            vbox.setId("taskCell");

            label = new Label(taskList.get(i).getVerantwortlichesMitglied().getKurzbezeichnung());
            label.setWrapText(true);
            label.setId("taskTitel");
            iconUserTxt = GlyphsDude.createIcon(FontAwesomeIcon.USER, "16px");
            //iconTxt.setFill(Color.color(0.969, 0.535, 0.535));
            iconUserTxt.setFill(Color.BLACK);
            label.setGraphic(iconUserTxt);

            vbox.getChildren().add(label);

            gridPane.add(vbox, 4, i);


            gridPane.getRowConstraints().add(i, rc);
            Separator sepHor = new Separator();
            sepHor.setValignment(VPos.CENTER);
            GridPane.setConstraints(sepHor, 0, i);
            GridPane.setColumnSpan(sepHor, 7);
            gridPane.getChildren().add(sepHor);

        }

        // gridPane.setGridLinesVisible( true );

        ColumnConstraints col0 = new ColumnConstraints();


        ColumnConstraints col1 = new ColumnConstraints();
        col1.setHgrow(Priority.ALWAYS);
        col1.setPrefWidth(200);

        ColumnConstraints col2 = new ColumnConstraints();
        col2.setHgrow(Priority.ALWAYS);
        col2.setPrefWidth(200);

        gridPane.getColumnConstraints().addAll(new ColumnConstraints(50), col1, col2);

        scrollPane.setContent(gridPane);


    }


    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public void setStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
}
