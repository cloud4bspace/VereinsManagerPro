package space.cloud4b.verein.view.tasks;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import space.cloud4b.verein.MainApp;
import space.cloud4b.verein.model.verein.task.Task;

public class TaskViewController {

    private MainApp mainApp;
    private Stage stage;

    @FXML
    private TreeTableView<Task> taskTreeTableView;
    @FXML
    private TreeTableColumn<Task, String> titelSpalte;
    @FXML
    private TreeTableColumn<Task, String> detailsSpalte;

    public TaskViewController() {
        // Konstruktor wird nicht benötigt
    }

    public void initialize() {
        taskTreeTableView.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);
        titelSpalte = new TreeTableColumn<>("Titel");
        detailsSpalte = new TreeTableColumn<>("Details");

        titelSpalte.setCellValueFactory(new TreeItemPropertyValueFactory<>("titel"));
        detailsSpalte.setCellValueFactory(new TreeItemPropertyValueFactory<>("details"));

        taskTreeTableView.getColumns().setAll(titelSpalte, detailsSpalte);

        TreeItem<Task> item1 = new TreeItem<>(new Task("Task 1.1", "Details zu 1.1"));
        TreeItem<Task> item2 = new TreeItem<>(new Task("Task 1.2", "Details zu 1.2"));
        TreeItem<Task> pendent = new TreeItem<>(new Task("pendent"));

        TreeItem<Task> item21 = new TreeItem<>(new Task("Task 2.1", "Details zu 2.1"));
        TreeItem<Task> item22 = new TreeItem<>(new Task("Task 2.2", "Details zu 2.2"));
        TreeItem<Task> inArbeit = new TreeItem<>(new Task("inArbeit"));

        TreeItem<Task> root = new TreeItem<>(new Task("Tasks"));

        pendent.getChildren().setAll(item1, item2);
        pendent.setExpanded(true);
        inArbeit.getChildren().setAll(item21, item22);
        inArbeit.setExpanded(true);

        root.getChildren().setAll(pendent, inArbeit);
        titelSpalte.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Task, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Task, String> param) {
                return new SimpleStringProperty(param.getValue().getValue().getTaskTitel());
            }
        });

        detailsSpalte.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Task, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Task, String> param2) {
                return new SimpleStringProperty(param2.getValue().getValue().getTaskText());
            }
        });
        root.setExpanded(true);
        taskTreeTableView.setRoot(root);
        taskTreeTableView.setShowRoot(false);
    /*    ArrayList<TreeItem> taskTreeItemArrayList = null;
        ArrayList<Task> taskArrayList = mainApp.getTaskController().getTasksAsArrayList();

        pendent.getChildren().setAll((Collection<? extends TreeItem<Task>>) taskTreeItemArrayList);


        for(Task task : taskArrayList) {
            taskTreeItemArrayList.add(new TreeItem<Task>(task));
        }*/
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public void setStage(Stage dialogStage) {
        this.stage = dialogStage;
    }
}
