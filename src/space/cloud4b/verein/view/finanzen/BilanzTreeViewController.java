package space.cloud4b.verein.view.finanzen;

import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableRow;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import space.cloud4b.verein.MainApp;
import space.cloud4b.verein.model.verein.finanzen.Konto;

import java.time.Year;
import java.util.ArrayList;

/**
 * Controller zum JavaFX-UI TaskListView.fxml (Anzeige der Tasks als Liste)
 * Versorgt die FXML-Objekte (Felder und Tabellen) mit Daten
 * Erhält Benachrichtigungen der abonnierten Observer-Klasse(n), wenn Datensätze geändert wurden.
 *
 * @author Bernhard Kämpf und Serge Kaulitz
 * @version 2019-12-17
 */
public class BilanzTreeViewController {
    private Stage dialogStage;
    private MainApp mainApp;

    // UI-Variabeln (Verknüpfung mit Elementen des Userinterfaces)
    @FXML
    private TreeTableView<Konto> bilanzTreeTableView;
    @FXML
    private TreeTableColumn<Konto, String> bezeichnungColumn;
    @FXML
    private TreeTableColumn<Konto, String> betragCHFColumn;

    public BilanzTreeViewController() {
        //Konstruktor wird nicht benötigt
    }

    /**
     * Initialisierung der controller class
     */
    @FXML
    private void initialize() {
        bilanzTreeTableView.setEditable(false);
        bilanzTreeTableView.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);
      /*  bilanzTreeTableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> openKonto(newValue.getValue()));
*/
        bezeichnungColumn = new TreeTableColumn<>("Bezeichnung");
        betragCHFColumn = new TreeTableColumn<>("Betrag");
        bezeichnungColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("bezeichnung"));
        betragCHFColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("betrag"));
        bezeichnungColumn.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
    }


    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        setupTreeView();
    }

    public void setStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setupTreeView() {
        bezeichnungColumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Konto, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Konto, String> param) {
                return new SimpleStringProperty(param.getValue().getValue().getBezeichnungTreeView());
            }
        });
        betragCHFColumn.setStyle( "-fx-alignment: CENTER-RIGHT;");
        betragCHFColumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Konto, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Konto, String> param) {
                return new SimpleStringProperty(param.getValue().getValue().getBetragBilanzTreeView());
            }
        });
        ArrayList<Konto> kontoArrayList = mainApp.getFinanzController().getBuchhalung().getBuchungsperiode(Year.now().getValue()).getKontenplan();
        ArrayList<Konto> bilanzArrayList = new ArrayList<>();
        ArrayList<Konto> klasseArrayList = new ArrayList<>();
        ArrayList<Konto> hauptgruppeArrayList = new ArrayList<>();
        ArrayList<Konto> gruppeArrayList = new ArrayList<>();
        int klasse = 0;
        String klasseString = null;
        int hauptGruppe = 0;
        String hauptGruppeString = null;
        int gruppe = 0;
        String gruppeString = null;
        for(Konto konto : kontoArrayList) {
            System.out.println("Buchungsperiode: " + konto.getBuchungsperiode().getJahr());
            if(konto.getKontoKlasse() == 1 || konto.getKontoKlasse() == 2){
                bilanzArrayList.add(konto);
                if(!konto.getKontoKlasseText().getValue().equals(klasseString)) {
                    klasse = konto.getKontoKlasse();
                    klasseString = konto.getKontoKlasseText().getValue();
                    klasseArrayList.add(new Konto(klasseString, klasse, klasse, konto.getBuchungsperiode()));
                }
                if(!konto.getKontoHauptgruppeText().getValue().equals(hauptGruppeString)) {
                    hauptGruppe = konto.getKontoHauptgruppe();
                    hauptGruppeString = konto.getKontoHauptgruppeText().getValue();
                    hauptgruppeArrayList.add(new Konto(hauptGruppeString, hauptGruppe, klasse, hauptGruppe, konto.getBuchungsperiode()));
                }
                if(!konto.getKontoGruppeText().getValue().equals(gruppeString)) {
                    gruppe = konto.getKontoGruppe();
                    gruppeString = konto.getKontoGruppeText().getValue();
                    gruppeArrayList.add(new Konto(gruppeString, gruppe, klasse, hauptGruppe, gruppe, konto.getBuchungsperiode()));
                }
            }
        }
        Text iconTxt;
        iconTxt = GlyphsDude.createIcon(FontAwesomeIcon.BALANCE_SCALE, "24px");
        iconTxt.setFill(Color.DARKSEAGREEN);
        bilanzTreeTableView.getColumns().setAll(bezeichnungColumn, betragCHFColumn);
        Node rootGraphic = new Circle(10, Color.rgb(144, 204, 244));
        TreeItem<Konto> root = new TreeItem<>(new Konto("Bilanz", kontoArrayList.get(0).getBuchungsperiode()), iconTxt);
        root.setExpanded(true);
       // ArrayList<TreeItem<Konto>> klassenTreeItems = new ArrayList<>();
        TreeItem<Konto> klasseTreeItem;
        TreeItem<Konto> hauptGruppeTreeItem;
        TreeItem<Konto> gruppeTreeItem;
        TreeItem<Konto> kontoTreeItem;
        for(Konto klassenEbene : klasseArrayList) {
            klasseTreeItem = new TreeItem<>(klassenEbene);
            klasseTreeItem.setExpanded(true);
            root.getChildren().add(klasseTreeItem);
            for(Konto hauptgruppenEbene : hauptgruppeArrayList) {
                if(hauptgruppenEbene.getKontoKlasse() == klassenEbene.getKontoKlasse()) {
                    hauptGruppeTreeItem = new TreeItem<>(hauptgruppenEbene);
                    hauptGruppeTreeItem.setExpanded(false);
                    klasseTreeItem.getChildren().add(hauptGruppeTreeItem);
                    for(Konto gruppenEbene : gruppeArrayList) {
                        System.out.println("vergleiche " + gruppenEbene.getKontoHauptgruppe());
                        System.out.println("mit        " + hauptgruppenEbene.getKontoHauptgruppe());
                        if(gruppenEbene.getKontoHauptgruppe() == hauptgruppenEbene.getKontoHauptgruppe()) {
                            gruppeTreeItem = new TreeItem<>(gruppenEbene);
                            gruppeTreeItem.setExpanded(false);
                            hauptGruppeTreeItem.getChildren().add(gruppeTreeItem);
                            for(Konto bilanzKonto : bilanzArrayList) {
                                System.out.println("** vergleiche " + bilanzKonto.getKontoGruppe());
                                System.out.println("              " + bilanzKonto.getKontoGruppeText().getValue());
                                System.out.println("** mit        " + gruppenEbene.getKontoGruppe());
                                System.out.println("              " + gruppenEbene.getKontoBezeichnungProperty().getValue());
                                if(bilanzKonto.getKontoGruppeText().getValue().equals(gruppenEbene.getKontoBezeichnungProperty().getValue())){
                                    kontoTreeItem = new TreeItem<>(bilanzKonto);
                                    kontoTreeItem.setExpanded(false);
                                    gruppeTreeItem.getChildren().add(kontoTreeItem);
                                }
                            }
                        }
                    }
                }
            }
        }
     /*   TreeItem<Task> pendent = new TreeItem<>(new Task("pendent (noch nicht fällig)"), pendentGraphic);
        root.getChildren().setAll(ueberfaellig, pendent, inArbeit, erledigt);
        pendent.getChildren().setAll(pendenteTreeItems);*/
      //  root.getChildren().addAll(klassenTreeItems);
        bilanzTreeTableView.setRoot(root);
        bilanzTreeTableView.setRowFactory(row -> new TreeTableRow<Konto>() {
            @Override
            public void updateItem(Konto item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || item.getKontoNummer() == 0) {
                    setStyle("-fx-background-color: #FFFFFF;-fx-font-size: 2em;");
                    if(isSelected()){
                        setStyle("-fx-text-fill: red;");
                    }
                } else if (item.getKontoNummer() < 10) {
                    setStyle("-fx-background-color: #8FAD88;-fx-font-weight: bolder;-fx-font-size: 1em;");
                } else if (item.getKontoNummer() < 100) {
                    setStyle("-fx-background-color: #ABC9A4;-fx-font-weight: bold;-fx-font-size: 1em;");
                } else if (item.getKontoNummer() < 1000) {
                    setStyle("-fx-background-color: #CDECC6;-fx-font-size: 1em;");
                } else {
                    setStyle("");
                }
            }
        });
    }
}
