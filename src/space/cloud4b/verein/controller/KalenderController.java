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
import java.util.Objects;

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

    public KalenderController() {
        observerList = new ArrayList<>();
        jubilaeumsListe = DatabaseReader.getJubilaeenAsArrayList();
        startKalenderObserver();
        startAnmeldungenObserver();
    }

    /**
     * aktualisiert die Anzahl der Termine und benachrichtigt die Observer
     *
     * @param anzahlTermine - Anzahl der Termine gem. MYSQL-Datenbank
     */
    public void updateAnzahlTermine(int anzahlTermine) {
        this.anzahlTermine = anzahlTermine;
        Notify();
    }

    /**
     * aktualisiert die Anzahl der Rückmeldungen und benachrichtigt die Observer
     * @param anzahlMeldungen - Die Anzahl
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
    }

    /**
     * aktualisiert den Zeitstempel für die letzte Änderung in der Termin-Tabelle der Datenbank
     *
     * @param neuerZeitstempel Der neue (höchste) Zeitstempel aus der Termin-Tabelle
     */
    public void updateLetzeTerminAenderung(Timestamp neuerZeitstempel) {
        this.timestamp = neuerZeitstempel;
    }

    /**
     * aktualisiert den Zeitstempel für die letzte Änderung in der Termin-Tabelle der Datenbank
     *
     * @param neuerZeitstempel - der neue (höchste) Zeitstemptel gem. MYSQL-Tabelle
     */
    public void updateLetzeAnmeldungAenderung(Timestamp neuerZeitstempel) {
        this.timestampAnmeldungen = neuerZeitstempel;
    }

    /**
     * Methode startet den Observer-Thread "Kalender" und überprüft in einem ständigen
     * Loop alle 2 Sekunden die zugrundeliegenden MYSQL-Tabelle auf folgende Änderungen:
     * - Anzahl der Datensätze hat sich geändert
     * - Der Zeitstempel der letzen Änderung bei einem Datensatz hat sich geändert
     * Festgestellte Änderungen werden in den Instanzvariabeln nachgeführt.
     */
    private void startKalenderObserver() {
        Runnable observerKalender = () -> {
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
                } else if (Objects.requireNonNull(DatabaseReader.readLetzteAenderung()).after(this.timestamp)) {
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
                    e.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(observerKalender);
        thread.setName("TerminObserver");
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * Methode startet den Observer-Thread "Terminkontrolle" und überprüft in einem ständigen
     * Loop alle 2 Sekunden die zugrundeliegenden MYSQL-Tabelle auf folgende Änderungen:
     * - Anzahl der Datensätze hat sich geändert
     * - Der Zeitstempel der letzen Änderung bei einem Datensatz hat sich geändert
     * Festgestellte Änderungen werden in den Instanzvariabeln nachgeführt.
     */
    private void startAnmeldungenObserver() {
        Runnable observerAnmeldungen = () -> {
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
                } else if (Objects.requireNonNull(DatabaseReader.readLetzteAnmeldungAenderung())
                        .after(this.timestampAnmeldungen)) {
                    updateLetzeAnmeldungAenderung(DatabaseReader.readLetzteAnmeldungAenderung());
                    update = true;
                }
                if (update) {
                    Notify();
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(observerAnmeldungen);
        thread.setName("TerminkontrollObserver");
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * Methode fügt das übergebene Objekt zur Observer-Liste hinzu
     */
    @Override
    public void Attach(Observer o) {
        // überprüfen, ob ein Objekt derselben Klasse bereits vorhanden ist und ggf. löschen
        for (int i = 0; i < observerList.size(); i++) {
            System.out.println("O#" + i + ": " + observerList.get(i));
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
