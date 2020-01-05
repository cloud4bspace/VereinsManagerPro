package space.cloud4b.verein.controller;

import space.cloud4b.verein.model.verein.kalender.Jubilaeum;
import space.cloud4b.verein.model.verein.kalender.Sortbydate;
import space.cloud4b.verein.model.verein.kalender.Termin;
import space.cloud4b.verein.services.DatabaseReader;
import space.cloud4b.verein.services.Observer;
import space.cloud4b.verein.services.Subject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Die Klasse KalenderController stellt den fxml-Controllern die benötigten Listen und Daten zur
 * Verfügung, wie z.B. die Termin- oder Teilnehmerliste.
 * Zudem überwacht die Klasse die zugrundeliegenden MYSQL-Tabelle und gibt Änderungen, Ergänzungen und
 * Löschungen an die in der Observer-Liste (observerList) eingetragenen fxml-Controllern weiter.
 * Dazu implementiert die Klasse das Subject-Interface
 *
 * @author Bernhard Kämpf und Serge Kaulitz
 * @version 2019-12-17
 * @see space.cloud4b.verein.services.Subject
 */
public class KalenderController implements Subject {

    int anzahlTermine;
    int anzahlMeldungen;
    private Timestamp timestamp = null; // Zeitstempel der letzten Änderung im der Termin-Tabelle
    private Timestamp timestampAnmeldungen; // Zeitstempel der letzten Änderung im der Anmeldungen-Tabelle
    private ArrayList<Observer> observerList;
    private ArrayList<Termin> terminListe;
    private ArrayList<Termin> kommendeTermineListe = new ArrayList<>();
    private ArrayList<Jubilaeum> jubilaeumsListe;
    // TODO die Anmeldeliste sollte auch irgendwo zentral geführt werden? oder bei den Terminen?

    public KalenderController() {
        observerList = new ArrayList<>();
        jubilaeumsListe = DatabaseReader.jubilaenLaden();
        startKalenderObserver();
        startAnmeldungenObserver();
    }

    /**
     * aktualisiert die Anzahl der Termine und benachrichtigt die Observer
     * @param anzahlTermine
     */
    public void updateAnzahlTermine(int anzahlTermine) {
        this.anzahlTermine = anzahlTermine;
        Notify();
    }

    /**
     * aktualisiert die Anzahl der Rückmeldungen und benachrichtigt die Observer
     * @param anzahlMeldungen
     */
    public void updateAnzahlMeldungen(int anzahlMeldungen) {
        this.anzahlMeldungen = anzahlMeldungen;
    }

    public int getAnzahlTermine() {
        return anzahlTermine;
    }

    public ArrayList<Termin> getNaechsteTerminListe() {
        return kommendeTermineListe;
    }

    public ArrayList<Termin> getTermineAsArrayList() {
        return terminListe;
    }

    public ArrayList<Jubilaeum> getJubilaeumsListe() {
        Collections.sort(jubilaeumsListe, new Sortbydate());
        return jubilaeumsListe;
    }

    /**
     * die Listen (ArrayList) werden nach einer Änderung in der Datenbank aktualisiert und die
     * Observer werden benachrichtigt.
     */
    public void updateTerminListen() {
        this.terminListe = DatabaseReader.getTermineAsArrayList();
        this.kommendeTermineListe = DatabaseReader.getKommendeTermineAsArrayList();
        //Notify ist nötig, wenn Aenderungen Zeitstempel zu update geführt haben.
    }

    /**
     * aktualisiert den Zeitstempel für die letzte Änderung in der Termin-Tabelle der Datenbank
     *
     * @param neuerZeitstempel
     */
    public void updateLetzeTerminAenderung(Timestamp neuerZeitstempel) {
        this.timestamp = neuerZeitstempel;
        System.out.println("Aenderungen bei den Terminen mit neuem Zeitstempel (" + neuerZeitstempel + ") festgestellt");
    }

    /**
     * aktualisiert den Zeitstempel für die letzte Änderung in der Termin-Tabelle der Datenbank
     *
     * @param neuerZeitstempel
     */
    public void updateLetzeAnmeldungAenderung(Timestamp neuerZeitstempel) {
        this.timestampAnmeldungen = neuerZeitstempel;
        System.out.println("Aenderungen bei den Anmeldungen mit neuem Zeitstempel (" + neuerZeitstempel + ") festgestellt");
    }

    public void setTerminliste(ArrayList<Termin> terminListe) {
        this.terminListe = terminListe;
    }

    private void startKalenderObserver() {
        Runnable observerKalender = () -> {
            int zaehler = 0;
            while (true) {
                boolean update = false;
                if (DatabaseReader.readAnzahlTermine() != anzahlTermine) {
                    updateAnzahlTermine(DatabaseReader.readAnzahlTermine());
                    update = true;
                }
                // hat sich der Zeitstempel der letzten Äenderung verändert?
                else if (this.timestamp == null) {
                    updateLetzeTerminAenderung(DatabaseReader.readLetzteAenderung());
                    update = true;
                } else if (DatabaseReader.readLetzteAenderung().after(this.timestamp)) {
                    updateLetzeTerminAenderung(DatabaseReader.readLetzteAenderung());
                    update = true;
                }
                if (update) {
                    updateTerminListen();
                    Notify();
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {

                }
            }
        };
        Thread thread = new Thread(observerKalender);
        thread.setName("TerminObserver");
        thread.setDaemon(true);
        thread.start();
    }

    private void startAnmeldungenObserver() {
        Runnable observerAnmeldungen = () -> {
            int zaehler = 0;
            while (true) {
                boolean update = false;
                if (DatabaseReader.getAnzMeldungen() != anzahlMeldungen) {
                    updateAnzahlMeldungen(DatabaseReader.getAnzMeldungen());
                    update = true;
                }
                // hat sich der Zeitstempel der letzten Äenderung verändert?
                else if (this.timestampAnmeldungen == null) {
                    updateLetzeAnmeldungAenderung(DatabaseReader.readLetzteAnmeldungAenderung());
                    update = true;
                } else if (DatabaseReader.readLetzteAnmeldungAenderung().after(this.timestampAnmeldungen)) {
                    updateLetzeAnmeldungAenderung(DatabaseReader.readLetzteAnmeldungAenderung());
                    update = true;
                }
                if (update) {
                    Notify();
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {

                }
            }
        };
        Thread thread = new Thread(observerAnmeldungen);
        thread.setName("AnmeldungenObserver");
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
        for (int i = 0; i < observerList.size(); i++) {
            observerList.get(i).update(this);
        }

    }
}
