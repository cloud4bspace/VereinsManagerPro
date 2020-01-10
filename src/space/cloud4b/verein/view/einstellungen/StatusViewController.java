package space.cloud4b.verein.view.einstellungen;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import space.cloud4b.verein.MainApp;
import space.cloud4b.verein.controller.TaskController;
import space.cloud4b.verein.model.verein.status.StatusElement;
import space.cloud4b.verein.model.verein.task.Task;
import space.cloud4b.verein.services.Observer;

import java.util.ArrayList;

public class StatusViewController implements Observer {

    private MainApp mainApp;
    private Stage stage;

    // UI-Variabeln (Verknüpfung mit Elementen des Userinterfaces)
    @FXML
    private TreeTableView<StatusElement> statusElementTreeTableView;
    @FXML
    private TreeTableColumn<StatusElement, String> titelSpalte;
    @FXML
    private TreeTableColumn<StatusElement, String> keySpalte;

    public StatusViewController() {
        // Konstruktor wird nicht benötigt
    }

    public void initialize() {
        statusElementTreeTableView.setEditable(false);
        statusElementTreeTableView.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);
        //  statusElementTreeTableView.getSelectionModel().selectedItemProperty().addListener(
        //         (observable, oldValue, newValue) -> openTask(newValue.getValue()));

        titelSpalte = new TreeTableColumn<>("Titel");
        keySpalte = new TreeTableColumn<>("Key");


        titelSpalte.setCellValueFactory(new TreeItemPropertyValueFactory<>("titel"));
        keySpalte.setCellValueFactory(new TreeItemPropertyValueFactory<>("Key"));

        titelSpalte.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());

    }

    public void setupTreeView() {
        int statusId = 0;
        ArrayList<TreeItem<StatusElement>> statusTreeItems = new ArrayList<>();
        TreeItem statusTreeItem = null;

        ArrayList<StatusElement> statusElementList = mainApp.getStatusElementController().getStatusElementeListe();
        statusElementTreeTableView.getColumns().setAll(titelSpalte, keySpalte);

        TreeItem<StatusElement> root = new TreeItem<>(new StatusElement(0, "StatusElemente"));

        for (StatusElement statusElement : statusElementList) {
            if (statusId != statusElement.getStatusId()) {
                statusId = statusElement.getStatusId();
                //neuer Knoten auf Ebene Status
                statusTreeItem = new TreeItem<StatusElement>(new StatusElement(statusElement.getStatusId(),
                        "Status#" + statusElement.getStatusId() + " «" + statusElement.getStatusText() + "»"));
                statusTreeItems.add(statusTreeItem);
            }
            statusTreeItem.getChildren().add(new TreeItem<StatusElement>(statusElement));

        }


        root.getChildren().setAll(statusTreeItems);
        titelSpalte.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<StatusElement, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<StatusElement, String> param) {
                return new SimpleStringProperty(param.getValue().getValue().getStatusElementTextLang());
            }
        });

        keySpalte.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<StatusElement, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<StatusElement, String> param) {
                if (param.getValue().getValue().getStatusElementKey() > 0) {
                    return new SimpleStringProperty("" + param.getValue().getValue().getStatusElementKey());
                } else {
                    return new SimpleStringProperty("");
                }
            }
        });

        root.setExpanded(true);
        statusElementTreeTableView.setRoot(root);
        statusElementTreeTableView.setTableMenuButtonVisible(true);
        statusElementTreeTableView.setShowRoot(false);

    }

    public void openTask(Task task) {
        if (task.getPrioStatus() != null) {
            mainApp.showTaskEdit(task);
        }
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
        setupTreeView();
        mainApp.getTaskController().Attach(this);
    }

    public void setStage(Stage dialogStage) {
        this.stage = dialogStage;
    }

    @Override
    public void update(Object o) {
        System.out.println("StatusViewController hat Update-Meldung erhalten von " + o);
        if (o instanceof TaskController) { // TODO noch falscher Kontroller
            TaskController tc = (TaskController) o;
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    setupTreeView();
                    System.out.println(this + ": update...");
                }
            });

        }
    }
}
