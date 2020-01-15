package space.cloud4b.verein.view.chart;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;
import space.cloud4b.verein.model.verein.adressbuch.Mitglied;

import java.text.DateFormatSymbols;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Der Controller für die BirthdayStatisticsView
 *
 * @author Bernhard Kämpf & Serge Kaulitz
 * @version 2019-12
 */
public class BirthdayStatisticsController {

    // allgemeine Instanzvariabeln
    private ObservableList<String> monthNames = FXCollections.observableArrayList();

    // UI-Variabeln (Verknüpfung mit Elementen des Userinterfaces)
    @FXML
    private BarChart<String, Integer> barChart;
    @FXML
    private CategoryAxis xAxis;

    /**
     * Initialisierung der Controller-Class (wird automatisch beim Laden des fxml-Files
     * ausgeführt
     */
    @FXML
    private void initialize() {
        String[] months = DateFormatSymbols.getInstance(Locale.GERMAN).getMonths();
        monthNames.addAll(Arrays.asList(months));
        xAxis.setCategories(monthNames);
    }

    /**
     * Setzt die Person, deren Statistik angezeigt werden soll
     *
     * @param persons
     */
    public void setPersonData(List<Mitglied> persons) {
        // Anzahl Personen mit Geburtsdatum in diesem Monat zählen
        int[] monthCounter = new int[12];
        for (Mitglied p : persons) {
            int month = p.getGeburtsdatum().getMonthValue() - 1;
            monthCounter[month]++;
        }

        XYChart.Series<String, Integer> series = new XYChart.Series<>();

        // Erstelle ein XYChart.Data object for jeden Monat und füge diese als Chart-Element hinzu.
        for (int i = 0; i < monthCounter.length; i++) {
            series.getData().add(new XYChart.Data<>(monthNames.get(i), monthCounter[i]));
        }
        barChart.getData().add(series);
    }
}
