package space.cloud4b.verein.controller;

import space.cloud4b.verein.model.verein.user.User;
import space.cloud4b.verein.services.DatabaseReader;
import space.cloud4b.verein.services.Observer;
import space.cloud4b.verein.services.Subject;

import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Die Klasse BenutzerController stellt den fxml-Controllern die Benutzerliste zur Verfügung.
 * Zudem überwacht die Klasse die zugrundeliegenden MYSQL-Tabelle und gibt Änderungen, Ergänzungen und
 * Löschungen an die in der Observer-Liste (observerList) eingetragenen fxml-Controllern weiter.
 * Dazu implementiert die Klasse das Subject-Interface
 *
 * @author Bernhard Kämpf und Serge Kaulitz
 * @version 2019-12-17
 * @see Subject
 */
public class BenutzerController implements Subject {

    private int anzahlBenutzer;
    private Timestamp timestamp = null; // Zeitstempel der letzten Änderung im der Mitglieder-Datenbank
    private ArrayList<Observer> observerList;
    private ArrayList<User> benutzerListe;

    public BenutzerController() {

        // Die benötigten Listen werden instanziert
        observerList = new ArrayList<>();
        benutzerListe = new ArrayList<>();

        // der Observer-Thread wird gestartet
        startBenutzerObserver();
    }

    /**
     * Aktualisiert die übergebene Anzahl der Mitglieder in der entsprechenden
     * Instanzvariabel.
     * Mittels Notify() werden die in der Observer-Liste eingetragenen Klassen über die Änderung
     * orientiert (Start der Methode update()) bei den Observer-Klassen
     *
     * @param anzahlBenutzer Anzahl der Mitglieder gemäss MYSQL-Tabelle
     */
    public void updateAnzahlBenutzer(int anzahlBenutzer) {
        this.anzahlBenutzer = anzahlBenutzer;
    }

    /**
     * Aktualisiert die in den Instanzvaribeln geführten Listen.
     * Mittels Notify() werden die in der Observer-Liste eingetragenen Klassen über die Änderung
     * orientiert (Start der Methode update()) bei den Observer-Klassen
     */
    public void updateListen() {
        this.benutzerListe = DatabaseReader.getBenutzerAsArrayList();
    }

    /**
     * aktualisiert den Zeitstempel für die letzte Änderung in der Tabelle der Datenbank.
     * Mittels Notify() werden die in der Observer-Liste eingetragenen Klassen über die Änderung
     * orientiert (Start der Methode update()) bei den Observer-Klassen
     *
     * @param neuerZeitstempel aktueller (letzter) Zeitstempel in der MYSQL-Tabelle kontakt
     */
    public void updateLetzeBenutzerAenderung(Timestamp neuerZeitstempel) {
        this.timestamp = neuerZeitstempel;
    }

    /**
     * Methode gibt die jeweils aktuelle Anzahl der Benutzer gem. MYSQL-Tabelle kontakt zurück
     *
     * @return aktuelle Anzahl der Mitglieder gemäss MYSQL-Tabelle
     */
    public int getAnzahlBenutzer() {
        return anzahlBenutzer;
    }

    /**
     * Methode gibt die Benutzerliste als ArrayList<User> zurück
     *
     * @return Benutzerliste als ArrayList
     */
    public ArrayList<User> getBenutzerListe() {
        return this.benutzerListe;
    }


    /**
     * Methode startet den Observer-Thread "BenutzerObserver" und überprüft in einem ständigen
     * Loop alle 2 Sekunden die zugrundeliegenden MYSQL-Tabelle auf folgende Änderungen:
     * - Anzahl der Datensätze hat sich geändert
     * - Der Zeitstempel der letzen Änderung bei einem Datensatz hat sich geändert
     * Festgestellte Änderungen werden in den Instanzvariabeln nachgeführt.
     */
    private void startBenutzerObserver() {
        Runnable observeBenutzer = () -> {
            while (true) {
                boolean update = false;
                // hat sich die Anzahl der Einträge in der Tabelle Benutzer verändert?
                if (DatabaseReader.readAnzahlBenutzer() != anzahlBenutzer) {
                    updateAnzahlBenutzer(DatabaseReader.readAnzahlBenutzer());
                    update = true;
                }
                // hat sich der Zeitstempel der letzten Äenderung verändert?
                if (this.timestamp == null) {
                    updateLetzeBenutzerAenderung(DatabaseReader.readLetzteBenutzerAenderung());
                    update = true;
                } else if (DatabaseReader.readLetzteBenutzerAenderung().after(this.timestamp)) {
                    updateLetzeBenutzerAenderung(DatabaseReader.readLetzteBenutzerAenderung());
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
        Thread thread = new Thread(observeBenutzer);
        thread.setName("BenutzerObserver");
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * Methode fügt das übergebene Objekt zur Observer-Liste hinzu
     */
    @Override
    public void Attach(Observer o) {
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
