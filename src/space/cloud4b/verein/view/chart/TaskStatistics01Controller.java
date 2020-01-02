package space.cloud4b.verein.view.chart;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import space.cloud4b.verein.services.DatabaseReader;

public class TaskStatistics01Controller {

    ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
    @FXML
    private PieChart pieChart;

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {

        pieChartData = DatabaseReader.getDataForTaskPieChart01();
        pieChart.setData(pieChartData);

    }


}
