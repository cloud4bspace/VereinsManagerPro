package space.cloud4b.verein.view.tasks;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Callback;
import space.cloud4b.verein.MainApp;
import space.cloud4b.verein.model.verein.task.Task;
import space.cloud4b.verein.services.DatabaseReader;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class TaskViewController {

    private MainApp mainApp;
    private Stage stage;

    @FXML
    private Label titelLabel;
    @FXML
    private TreeTableView<Task> taskTreeTableView;
    @FXML
    private TreeTableColumn<Task, String> idSpalte;
    @FXML
    private TreeTableColumn<Task, String> prioSpalte;
    @FXML
    private TreeTableColumn<Task, String> titelSpalte;
    @FXML
    private TreeTableColumn<Task, String> terminSpalte;
    @FXML
    private TreeTableColumn<Task, String> tageBisTerminSpalte;
    @FXML
    private TreeTableColumn<Task, String> detailsSpalte;
    @FXML
    private TreeTableColumn<Task, String> verantwortlicheSpalte;

    public TaskViewController() {
        // Konstruktor wird nicht benötigt
    }

    public void initialize() {
        titelLabel.setText("Taskliste");
        taskTreeTableView.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);
        idSpalte = new TreeTableColumn<>("Id#");
        prioSpalte = new TreeTableColumn<>("Prio");
        titelSpalte = new TreeTableColumn<>("Titel");
        terminSpalte = new TreeTableColumn<>("Termin");
        tageBisTerminSpalte = new TreeTableColumn<>("Tage bis Termin");
        detailsSpalte = new TreeTableColumn<>("Details");
        verantwortlicheSpalte = new TreeTableColumn<>("Verantwortliche");

        idSpalte.setCellValueFactory(new TreeItemPropertyValueFactory<>("id#"));
        prioSpalte.setCellValueFactory(new TreeItemPropertyValueFactory<>("prio"));
        titelSpalte.setCellValueFactory(new TreeItemPropertyValueFactory<>("titel"));
        tageBisTerminSpalte.setCellValueFactory(new TreeItemPropertyValueFactory<>("tagebis"));
        terminSpalte.setCellValueFactory(new TreeItemPropertyValueFactory<>("termin"));
        detailsSpalte.setCellValueFactory(new TreeItemPropertyValueFactory<>("details"));
        verantwortlicheSpalte.setCellValueFactory(new TreeItemPropertyValueFactory<>("verantwortliche"));

        ArrayList<Task> taskArrayList = DatabaseReader.getTaskList();
        taskTreeTableView.getColumns().setAll(titelSpalte, terminSpalte, tageBisTerminSpalte, prioSpalte, detailsSpalte, verantwortlicheSpalte, idSpalte);

        ArrayList<TreeItem<Task>> pendenteTreeItems = new ArrayList<>();
        ArrayList<TreeItem<Task>> ueberfaelligeTreeItems = new ArrayList<>();
        ArrayList<TreeItem<Task>> inArbeitTreeItems = new ArrayList<>();
        ArrayList<TreeItem<Task>> erledigteTreeItems = new ArrayList<>();
        ArrayList<TreeItem<Task>> weitereTreeItems = new ArrayList<>();
        for (Task task : taskArrayList) {
            if (task.getStatusStatus().getStatusElementKey() == 1) {
                if (task.getTaskDatum().isBefore(LocalDate.now())) {
                    ueberfaelligeTreeItems.add(new TreeItem<Task>(task));
                } else {
                    pendenteTreeItems.add(new TreeItem<Task>(task));
                }
            } else if (task.getStatusStatus().getStatusElementKey() == 2) {
                if (task.getTaskDatum().isBefore(LocalDate.now())) {
                    ueberfaelligeTreeItems.add(new TreeItem<Task>(task));
                } else {
                    inArbeitTreeItems.add(new TreeItem<Task>(task));
                }
                // inArbeitTreeItems.add(new TreeItem<Task>(task));
            } else if (task.getStatusStatus().getStatusElementKey() == 3) {
                erledigteTreeItems.add(new TreeItem<Task>(task));
            } else {
                weitereTreeItems.add(new TreeItem<Task>(task));
            }


        }
        Node pendentGraphic = new Circle(10, Color.rgb(243, 210, 80));
        TreeItem<Task> pendent = new TreeItem<>(new Task("pendent (noch nicht fällig)"), pendentGraphic);

        Node ueberfaelligGraphic = new Circle(10, Color.rgb(247, 136, 136));
        TreeItem<Task> ueberfaellig = new TreeItem<>(new Task("überfällig"), ueberfaelligGraphic);

        Node inArbeitGraphic = new Circle(10, Color.rgb(144, 203, 244));
        TreeItem<Task> inArbeit = new TreeItem<>(new Task("inArbeit"), inArbeitGraphic);
        Node erledigtGraphic = new Circle(10, Color.rgb(143, 173, 136));
        TreeItem<Task> erledigt = new TreeItem<>(new Task("erledigt"), erledigtGraphic);


        TreeItem<Task> weitere = new TreeItem<>(new Task("weitere (Status unklar)"));
        Node rootGraphic = new Circle(10, Color.rgb(144, 204, 244));
        TreeItem<Task> root = new TreeItem<>(new Task("Tasks"), rootGraphic);

        ueberfaellig.getChildren().setAll(ueberfaelligeTreeItems);
        ueberfaellig.setExpanded(true);
        pendent.getChildren().setAll(pendenteTreeItems);
        pendent.setExpanded(true);
        inArbeit.getChildren().setAll(inArbeitTreeItems);
        inArbeit.setExpanded(true);
        weitere.getChildren().setAll(weitereTreeItems);
        weitere.setExpanded(true);
        erledigt.getChildren().setAll(erledigteTreeItems);
        erledigt.setExpanded(false);


        root.getChildren().setAll(ueberfaellig, pendent, inArbeit, weitere, erledigt);
        titelSpalte.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Task, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Task, String> param) {
                return new SimpleStringProperty(param.getValue().getValue().getTaskTitel());
            }
        });

        detailsSpalte.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Task, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Task, String> param) {
                return new SimpleStringProperty(param.getValue().getValue().getTaskText());
            }
        });
        terminSpalte.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Task, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Task, String> param) {
                if (param.getValue().getValue().getTaskDatum() != null) {
                    return new SimpleStringProperty(param.getValue().getValue().getTaskDatum()
                            .format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));
                } else {
                    return new SimpleStringProperty("");
                }
            }
        });
        tageBisTerminSpalte.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Task, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Task, String> param) {

                if (param.getValue().getValue().getTaskDatum() != null) {
                    long daysBetween = ChronoUnit.DAYS.between(LocalDate.now(), param.getValue().getValue().getTaskDatum());
                    // tageBisTerminSpalte.setStyle("-fx-background-color: red;");
                    return new SimpleStringProperty(Long.toString(daysBetween));

                    // return new SimpleStringProperty(Integer.toString(param.getValue().getValue().getTaskDatum().compareTo(LocalDate.now())));
                } else {
                    return null;
                }
            }
        });

        verantwortlicheSpalte.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Task, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Task, String> param) {
                return new SimpleStringProperty(DatabaseReader.getVerantwortlicheAsString(param.getValue().getValue().getTaskId()));
            }
        });
        idSpalte.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Task, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Task, String> param) {
                if (param.getValue().getValue().getTaskId() > 0) {
                    return new SimpleStringProperty("#" + param.getValue().getValue().getTaskId());
                } else {
                    return new SimpleStringProperty("");
                }

            }
        });
        prioSpalte.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Task, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Task, String> param) {
                if (param.getValue().getValue().getPrioStatus() != null) {
                    return new SimpleStringProperty(param.getValue().getValue().getPrioStatus().getStatusElementTextLang());
                } else {
                    return new SimpleStringProperty("");
                }
            }
        });
        root.setExpanded(true);
        taskTreeTableView.setRoot(root);
        taskTreeTableView.setTableMenuButtonVisible(true);
        taskTreeTableView.setShowRoot(false);

    /*    ArrayList<TreeItem> taskTreeItemArrayList = null;
        ArrayList<Task> taskArrayList = mainApp.getTaskController().getTasksAsArrayList();

        pendent.getChildren().setAll((Collection<? extends TreeItem<Task>>) taskTreeItemArrayList);


        for(Task task : taskArrayList) {
            taskTreeItemArrayList.add(new TreeItem<Task>(task));
        }*/
    }

    /**
     * Wird aufgerufen, wenn der User den Button "Task hinzufügen" betätigt
     */
    public void handleErfassenButton() {
        // ruft das Erfassungs-UI über die MainApp auf
        mainApp.showTaskErfassen();
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public void setStage(Stage dialogStage) {
        this.stage = dialogStage;
    }
}
