package space.cloud4b.verein.view.chart;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import space.cloud4b.verein.model.verein.adressbuch.Mitglied;
import space.cloud4b.verein.services.DatabaseReader;

/**
 * Der Controller für die MitgliederStatisticView
 *
 * @author Bernhard Kämpf & Serge Kaulitz
 * @version 2019-12
 */
public class MemberStatistics01Controller {

    // die allgemeinen Instanzvariabeln
    ObservableList<Mitglied> contactData;
    ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

    // UI-Variabeln (Verknüpfung mit Elementen des Userinterfaces)
    @FXML
    private PieChart pieChart;

    /**
     * Initialisiert die Controller-Class. Die Methode wird automatisch ausgeführt, wenn
     * das fxml-File geladen wird
     */
    @FXML
    private void initialize() {
        pieChartData = DatabaseReader.getDataForPieChart01();
        pieChart.setData(pieChartData);

    }


}
