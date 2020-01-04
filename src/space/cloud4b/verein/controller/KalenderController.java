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
    private Timestamp timestamp = null; // Zeitstempel der letzten Änderung im der Mitglieder-Datenbank
    private ArrayList<Observer> observerList;
    private ArrayList<Termin> terminListe;
    private ArrayList<Termin> kommendeTermineListe = new ArrayList<>();
    private ArrayList<Jubilaeum> jubilaeumsListe;
    // TODO die Anmeldeliste sollte konsequenterweise auch hier geführt werden..

    public KalenderController() {
        System.out.println("KalenderController erzeugt");
        observerList = new ArrayList<>();
        jubilaeumsListe = DatabaseReader.jubilaenLaden();
        terminListe = DatabaseReader.getTermineAsArrayList();
        kommendeTermineListe = DatabaseReader.getKommendeTermineAsArrayList();
        startKalenderObserver(); //TODO hier noch zu fürh
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
        Notify();
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
    public void updateListen() {
        Notify();
    }

    /**
     * aktualisiert den Zeitstempel für die letzte Änderung in der Termin-Tabelle der Datenbank
     * @param neuerZeitstempel
     */
    public void updateLetzeAenderung(Timestamp neuerZeitstempel){
        this.timestamp = neuerZeitstempel;
        System.out.println("Aenderungen bei den Mitgliedern mit neuem Zeitstempel (" + neuerZeitstempel + ") festgestellt");
        Notify();
    }

    public void setTerminliste(ArrayList<Termin> terminListe) {
        this.terminListe = terminListe;
    }

    private void startKalenderObserver() {
        Runnable observerKalender = () -> {
            int zaehler = 0;
            while (true) {
                if(DatabaseReader.readAnzahlTermine() != anzahlTermine) {
                    updateAnzahlTermine(DatabaseReader.readAnzahlTermine());
                    this.terminListe = DatabaseReader.getTermineAsArrayList();
                    this.kommendeTermineListe = DatabaseReader.getKommendeTermineAsArrayList();
                    updateListen();
                }
                // hat sich der Zeitstempel der letzten Äenderung verändert?
                else if(this.timestamp == null) {
                    updateLetzeAenderung(DatabaseReader.readLetzteAenderung());
                    this.terminListe = DatabaseReader.getTermineAsArrayList();
                    this.kommendeTermineListe = DatabaseReader.getKommendeTermineAsArrayList();
                    updateListen();
                } else if (DatabaseReader.readLetzteAenderung().after(this.timestamp)){
                    this.terminListe = DatabaseReader.getTermineAsArrayList();
                    this.kommendeTermineListe = DatabaseReader.getKommendeTermineAsArrayList();
                    updateLetzeAenderung(DatabaseReader.readLetzteAenderung());
                    updateListen();
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {

                }
            }
        };
        Thread thread = new Thread(observerKalender);
        thread.setDaemon(true);
        thread.start();
    }

    private void startAnmeldungenObserver() {
        Runnable observerAnmeldungen = () -> {
            int zaehler = 0;
            while (true) {
                if(DatabaseReader.getAnzMeldungen() != anzahlMeldungen) {
                    updateAnzahlMeldungen(DatabaseReader.getAnzMeldungen());
                    updateListen();
                }
                // hat sich der Zeitstempel der letzten Äenderung verändert?
                // TODO muss man noch implementieren
                /*
                else if(this.timestamp == null) {
                    updateLetzeAenderung(DatabaseReader.readLetzteAenderung());
                    this.terminListe = DatabaseReader.getTermineAsArrayList();
                    this.kommendeTermineListe = DatabaseReader.getKommendeTermineAsArrayList();
                    updateListen();
                } else if (DatabaseReader.readLetzteAenderung().after(this.timestamp)){
                    this.terminListe = DatabaseReader.getTermineAsArrayList();
                    this.kommendeTermineListe = DatabaseReader.getKommendeTermineAsArrayList();
                    updateLetzeAenderung(DatabaseReader.readLetzteAenderung());
                    updateListen();
                }*/
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {

                }
            }
        };
        Thread thread = new Thread(observerAnmeldungen);
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
