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
import space.cloud4b.verein.controller.StatusController;
import space.cloud4b.verein.model.verein.status.StatusElement;
import space.cloud4b.verein.services.Observer;

import java.util.ArrayList;

/**
 * Controller zum JavaFX-UI StatusView.fxml (Anzeige der Statuselemente)
 * Versorgt die FXML-Objekte (Felder und Tabellen) mit Daten
 * Erhält Benachrichtigungen der abonnierten Observer-Klasse(n), wenn Datensätze geändert wurden.
 *
 * @author Bernhard Kämpf und Serge Kaulitz
 * @version 2019-12-17
 */
public class StatusViewController implements Observer {

    // die allgemeinen Instanzvariabeln
    private MainApp mainApp;
    private Stage stage;

    // UI-Variabeln (Verknüpfung mit Elementen des Userinterfaces)
    @FXML
    private TreeTableView<StatusElement> statusElementTreeTableView;
    @FXML
    private TreeTableColumn<StatusElement, String> titelSpalte;
    @FXML
    private TreeTableColumn<StatusElement, String> keySpalte;
    @FXML
    private TreeTableColumn<StatusElement, String> symbolSpalte;

    public StatusViewController() {
        // Konstruktor wird nicht benötigt
    }

    /**
     * Initialisierung der Controller-Class. Die Methode wird automatisch aufgerufen, nachdem
     * das fxml-File geladen wurde.
     */
    @FXML
    public void initialize() {
        statusElementTreeTableView.setEditable(false);
        statusElementTreeTableView.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);
        titelSpalte = new TreeTableColumn<>("Titel");
        keySpalte = new TreeTableColumn<>("Key");
        symbolSpalte = new TreeTableColumn<>("Symbol");
        titelSpalte.setCellValueFactory(new TreeItemPropertyValueFactory<>("titel"));
        keySpalte.setCellValueFactory(new TreeItemPropertyValueFactory<>("Key"));
        symbolSpalte.setCellValueFactory(new TreeItemPropertyValueFactory<>("Symbol"));
        titelSpalte.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
    }

    /**
     * Initialisiert die TreeView und generiet die benötigten Knoten und Einträge
     */
    public void setupTreeView() {
        int statusId = 0;
        ArrayList<TreeItem<StatusElement>> statusTreeItems = new ArrayList<>();
        TreeItem statusTreeItem = null;

        ArrayList<StatusElement> statusElementList = mainApp.getStatusController().getStatusElementeListe();
        statusElementTreeTableView.getColumns().setAll(titelSpalte, keySpalte, symbolSpalte);

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

        symbolSpalte.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<StatusElement, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<StatusElement, String> param) {
                if (param.getValue().getValue().getStatusElementSymbol() != null) {
                    return new SimpleStringProperty("" + param.getValue().getValue().getStatusElementSymbol());
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
        if (o instanceof StatusController) {
            StatusController sc = (StatusController) o;
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    setupTreeView();
                }
            });
        }
    }
}
