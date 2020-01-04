package space.cloud4b.verein.controller;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import space.cloud4b.verein.MainApp;
import space.cloud4b.verein.model.verein.kontrolle.rangliste.Rangliste;
import space.cloud4b.verein.services.Observer;
import space.cloud4b.verein.services.Subject;

import java.io.File;
import java.time.LocalDate;
import java.util.Optional;

public class MainController implements Subject {

    private MainApp mainApp;
    private Rangliste rangliste;

    public MainController(MainApp mainApp) {
        System.out.println("MainController erzeugt");
        this.rangliste = new Rangliste("Anwesenheitsstatistik", this);
        this.mainApp = mainApp;
    }

    public Rangliste getRangliste() {
        return this.rangliste;
    }

    /**
     * öffnet einen "Save as"-Dialog
     * @return
     */
    public File chooseImageFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        Window stage = new Stage();
        fileChooser.setTitle("Bilddatei auswählen..");
        //fileChooser.setInitialFileName(fileName + LocalDate.now() + ".xlsx");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG-Images", "*.png"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JPG-Images", "*.jpg"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JPEG-Images", "*.jpeg"));
        File file = fileChooser.showOpenDialog(stage);
        return file;
    }

    /**
     * öffnet einen "Save as"-Dialog
     * @param fileName gibt den vom
     * @return
     */
    public File saveAsFile(String fileName) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        Window stage = new Stage();
        fileChooser.setTitle("Speichern unter..");
        fileChooser.setInitialFileName(fileName + LocalDate.now() + ".xlsx");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Excel File", "*.xlsx"));
        File file = fileChooser.showSaveDialog(stage);
        return file;
    }

    /**
     * Methode extrahiert aus dem übergebenen Filenamen die Erweiterung (Extension)
     * und gibt diese als String zurück
     *
     * @param filename
     * @return
     */
    public Optional<String> getExtensionByStringHandling(String filename) {
        return Optional.ofNullable(filename).filter(f -> f.contains(".")).map(f
                -> f.substring(filename.lastIndexOf(".") + 1));
    }


    /**
     * Methode fügt das übergebene Objekt zur Observer-Liste hinzu
     */
    @Override
    public void Attach(Observer o) {

    }

    /**
     * Methode löscht das übergebene Objekt aus der Observer-Liste
     */
    @Override
    public void Dettach(Observer o) {

    }

    /**
     * Methode durchläuft die in der Observerliste eingetragenen Klassen und ruft dort die
     * update-Methode auf.
     */
    @Override
    public void Notify() {

    }
}
