package space.cloud4b.verein.controller;

import space.cloud4b.verein.MainApp;
import space.cloud4b.verein.model.verein.kontrolle.rangliste.Position;
import space.cloud4b.verein.services.DatabaseReader;
import space.cloud4b.verein.services.Observer;
import space.cloud4b.verein.services.Subject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Die Klasse RanglisteController stellt den fxml-Controllern die benötigten Listen und Daten zur
 * Verfügung, wie z.B. die Rangliste (ArrayList<Position>)
 * Zudem überwacht die Klasse die zugrundeliegenden MYSQL-Tabelle und gibt Änderungen, Ergänzungen und
 * Löschungen an die in der Observer-Liste (observerList) eingetragenen fxml-Controllern weiter.
 * Dazu implementiert die Klasse das Subject-Interface
 *
 * @author Bernhard Kämpf und Serge Kaulitz
 * @version 2020-01-07
 * @see space.cloud4b.verein.services.Subject
 */
public class RanglisteController implements Subject {

    private MainApp mainApp;
    private int anzahlMitglieder;
    private int anzahlAnwesenheiten;
    private int anzahlTermine; // im aktuellen Jahr
    private Timestamp timestampMitglieder = null;
    private ArrayList<Observer> observerList;
    private ArrayList<Position> rangliste;

    public RanglisteController(MainApp mainApp) {
        this.mainApp = mainApp;
        System.out.println("RanglisteController erzeugt");

        // Die benötigten Listen werden instanziert
        observerList = new ArrayList<>();
        rangliste = new ArrayList<>();
        // rangliste muss hier bereits gefüllt werden, damit UI nicht leer angezeigt wird...
        this.rangliste = DatabaseReader.fuelleRangliste(mainApp.getAdressController().getMitgliederListe(),
                mainApp.getKalenderController().getTermineAsArrayList());

        // der Observer-Thread wird gestartet
        startRanglisteObserver();
    }


    /**
     * Gibt die Rangliste als ArrayList zurück
     *
     * @return die Rangliste als ArrayList<Position>
     */
    public ArrayList<Position> getRanglisteAsArrayList() {
        return this.rangliste;
    }

    /**
     * Aktualisiert die in den Instanzvaribeln geführten Listen.
     * Mittels Notify() werden die in der Observer-Liste eingetragenen Klassen über die Änderung
     * orientiert (Start der Methode update()) bei den Observer-Klassen
     */
    public void updateListen() {
        this.rangliste = DatabaseReader.fuelleRangliste(mainApp.getAdressController().getMitgliederListe(),
                mainApp.getKalenderController().getTermineAsArrayList());
    }

    /**
     * Methode startet den Observer-Thread "AdressObserver" und überprüft in einem ständigen
     * Loop alle 2 Sekunden die zugrundeliegenden MYSQL-Tabellen auf folgende Änderungen:
     * - Anzahl der Datensätze hat sich geändert
     * - Der Zeitstempel der letzen Änderung bei einem Datensatz hat sich geändert
     * Festgestellte Änderungen werden in den Instanzvariabeln nachgeführt.
     */
    private void startRanglisteObserver() {
        Runnable observeRangliste = () -> {
            while (true) {
                boolean update = false;
                // hat sich die Anzahl der Einträge in der Tabelle Kontakt verändert?
                if (DatabaseReader.readAnzahlMitglieder() != anzahlMitglieder) {
                    updateAnzahlMitglieder(DatabaseReader.readAnzahlMitglieder());
                    update = true;
                }
                // hat sich der Zeitstempel der letzten Äenderung verändert?
                if (this.timestampMitglieder == null) {
                    updateLetzeAenderung(DatabaseReader.readLetzteAenderung());
                    update = true;
                } else if (Objects.requireNonNull(DatabaseReader.readLetzteAenderung())
                        .after(this.timestampMitglieder)) {
                    updateLetzeAenderung(DatabaseReader.readLetzteAenderung());
                    update = true;
                }
                // hat sich die Anzahl der Einträge in der Tabelle terminkontrolle verändert?
                if (this.anzahlAnwesenheiten != DatabaseReader.getAnzAnwesenheiten()) {
                    updateAnzahlAnwesenheiten(DatabaseReader.getAnzAnwesenheiten());
                    update = true;
                }
                if (this.anzahlTermine != DatabaseReader.readAnzahlTermine()) {
                    updateAnzahlTermine(DatabaseReader.readAnzahlTermine());
                    update = true;
                }
                if (update) {
                    updateListen();
                    Notify();
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    System.out.println("Problem mit Runnable: " + this);
                }
            }
        };
        Thread thread = new Thread(observeRangliste);
        thread.setName("RanglisteObserver");
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * aktualisiert die Anzahl der Termine gem. MYSQL-Datenbank
     *
     * @param anzahlTermine die Anzahl der Termine
     */
    private void updateAnzahlTermine(int anzahlTermine) {
        this.anzahlTermine = anzahlTermine;
    }

    /**
     * aktualisiert die Anzahl der Anwesenheiten gem. MYSQL-Datenbank
     *
     * @param anzAnwesenheiten die Anzahl der Anwesenheiten
     */
    private void updateAnzahlAnwesenheiten(int anzAnwesenheiten) {
        this.anzahlAnwesenheiten = anzAnwesenheiten;
    }

    /**
     * aktualisiert den Timestamp der letzten Änderung eines Datensatzes
     *
     * @param timestampMitglieder der Timestamp der letzten Änderung in der Mitglieder-Tabelle
     */
    private void updateLetzeAenderung(Timestamp timestampMitglieder) {
        this.timestampMitglieder = timestampMitglieder;
    }

    /**
     * aktualisiert die Anzahl der Mitglieder gem. MYSQL-Datenbank
     *
     * @param anzahlMitglieder die Anzahl der Mitglieder
     */
    private void updateAnzahlMitglieder(int anzahlMitglieder) {
        this.anzahlMitglieder = anzahlMitglieder;
    }

    /**
     * Methode fügt das übergebene Objekt zur Observer-Liste hinzu.
     * Falles bereits andere Objekte derselben Klasse vorhanden sind, werden diese gelöscht.
     */
    @Override
    public void Attach(Observer o) {
        // überprüfen, ob ein Objekt derselben Klasse bereits vorhanden ist und ggf. löschen
        for (int i = 0; i < observerList.size(); i++) {
            if (observerList.get(i).getClass().equals(o.getClass())) {
                observerList.remove(i);
            }
        }
        // neuen Observer hinzufügen
        observerList.add(o);
    }

    /**
     * Methode löscht das übergebene Objekt aus der Observer-Liste
     */
    @Override
    public void Dettach(Observer o) {
        observerList.remove(o);
    }

    /**
     * Methode durchläuft die in der Observerliste eingetragenen Klassen und ruft dort die
     * update-Methode auf.
     */
    @Override
    public void Notify() {
        for (Observer observer : observerList) {
            observer.update(this);
        }
    }
}
