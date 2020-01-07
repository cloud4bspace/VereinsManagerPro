package space.cloud4b.verein.controller;

import space.cloud4b.verein.MainApp;
import space.cloud4b.verein.model.verein.kontrolle.rangliste.Rangliste;
import space.cloud4b.verein.services.Observer;
import space.cloud4b.verein.services.Subject;

public class MainController implements Subject {

    public MainApp mainApp;
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
