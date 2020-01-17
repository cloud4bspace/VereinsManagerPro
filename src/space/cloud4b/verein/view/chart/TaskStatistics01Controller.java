package space.cloud4b.verein.view.chart;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import space.cloud4b.verein.services.DatabaseReader;

public class TaskStatistics01Controller {

    /**
     * Der Controller für die TaskStatisticView
     *
     * @author Bernhard Kämpf & Serge Kaulitz
     * @version 2019-12
     */
    ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

    @FXML
    private PieChart pieChart;

    /**
     * Initialisiert die Controller-Class. Die Methode wird automatisch ausgeführt, wenn
     * das fxml-File geladen wird
     */
    @FXML
    private void initialize() {
        pieChartData = DatabaseReader.getDataForTaskPieChart01();
        pieChart.setData(pieChartData);
    }
}
