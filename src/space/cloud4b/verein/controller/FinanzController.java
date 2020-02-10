package space.cloud4b.verein.controller;

import space.cloud4b.verein.MainApp;
import space.cloud4b.verein.model.verein.finanzen.Buchhaltung;
import space.cloud4b.verein.services.DatabaseOperation;
import space.cloud4b.verein.services.DatabaseReader;
import space.cloud4b.verein.services.Observer;
import space.cloud4b.verein.services.Subject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Die Klasse AdressController stellt den fxml-Controllern die benötigten Listen und Daten zur
 * Verfügung, wie z.B. die Mitglieder- oder Jubiläumsliste.
 * Zudem überwacht die Klasse die zugrundeliegenden MYSQL-Tabelle und gibt Änderungen, Ergänzungen und
 * Löschungen an die in der Observer-Liste (observerList) eingetragenen fxml-Controllern weiter.
 * Dazu implementiert die Klasse das Subject-Interface
 *
 * @author Bernhard Kämpf und Serge Kaulitz
 * @version 2019-12-17
 * @see Subject
 */
public class FinanzController implements Subject {

    private MainApp mainApp;
    private Buchhaltung buchhaltung;
    private int anzahlBelege;
    private Timestamp timestamp = null; // Zeitstempel der letzten Änderung im der Mitglieder-Datenbank
    private ArrayList<Observer> observerList;

    public FinanzController(MainApp mainApp) {
        this.mainApp = mainApp;
        System.out.println("Finanzcontroller erzeugt..");
        // Der Mitgliederstatus in der MYSQL-Tabelle kontakt wird für jedes Element überprüft
        DatabaseOperation.checkMitgliederStatus();

        // Die benötigten Listen werden instanziert
        observerList = new ArrayList<>();
        buchhaltung = new Buchhaltung();

        // der Observer-Thread wird gestartet
        startFinanzObserver();
    }

    /**
     * Aktualisiert die übergebene Anzahl der Mitglieder in der entsprechenden
     * Instanzvariabel.
     * Mittels Notify() werden die in der Observer-Liste eingetragenen Klassen über die Änderung
     * orientiert (Start der Methode update()) bei den Observer-Klassen
     *
     * @param anzahlBelege Anzahl der Mitglieder gemäss MYSQL-Tabelle
     */
    public void updateAnzahlBelege(int anzahlBelege) {
        this.anzahlBelege = anzahlBelege;
    }

    /**
     * Aktualisiert die in den Instanzvaribeln geführten Listen.
     * Mittels Notify() werden die in der Observer-Liste eingetragenen Klassen über die Änderung
     * orientiert (Start der Methode update()) bei den Observer-Klassen
     */
    public void updateListen() {
    }

    /**
     * aktualisiert den Zeitstempel für die letzte Änderung in der Adress-Tabelle der Datenbank.
     * Mittels Notify() werden die in der Observer-Liste eingetragenen Klassen über die Änderung
     * orientiert (Start der Methode update()) bei den Observer-Klassen
     *
     * @param neuerZeitstempel aktueller (letzter) Zeitstempel in der MYSQL-Tabelle kontakt
     */
    public void updateLetzeAenderung(Timestamp neuerZeitstempel) {
        this.timestamp = neuerZeitstempel;
    }

    /**
     * Methode gibt die jeweils aktuelle Anzahl der Mitglieder gem. MYSQL-Tabelle kontakt zurück
     *
     * @return aktuelle Anzahl der Mitglieder gemäss MYSQL-Tabelle
     */
    public int getAnzahlBelege() {
        return anzahlBelege;
    }


    /**
     * Methode startet den Observer-Thread "AdressObserver" und überprüft in einem ständigen
     * Loop alle 2 Sekunden die zugrundeliegenden MYSQL-Tabellen auf folgende Änderungen:
     * - Anzahl der Datensätze hat sich geändert
     * - Der Zeitstempel der letzen Änderung bei einem Datensatz hat sich geändert
     * Festgestellte Änderungen werden in den Instanzvariabeln nachgeführt.
     */
    private void startFinanzObserver() {
        Runnable observeMitglieder = () -> {
            while (true) {
                boolean update = false;
                // hat sich die Anzahl der Einträge in der Tabelle Kontakt verändert?
                if (DatabaseReader.readAnzahlBelege() != anzahlBelege) {
                    updateAnzahlBelege(DatabaseReader.readAnzahlMitglieder());
                    update = true;
                }
                // hat sich der Zeitstempel der letzten Äenderung verändert?
                if (this.timestamp == null) {
                    updateLetzeAenderung(DatabaseReader.readLetzteAenderung());
                    update = true;
                } else if (Objects.requireNonNull(DatabaseReader.readLetzteAenderung()).after(this.timestamp)) {
                    updateLetzeAenderung(DatabaseReader.readLetzteAenderung());
                    update = true;
                }
                if (update) {
                    updateListen();
                    Notify();
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    System.out.println("es gibt ein Problem");
                }
            }
        };
        Thread thread = new Thread(observeMitglieder);
        thread.setName("AdressObserver");
      //  thread.setDaemon(true);
        thread.start();
    }

    /**
     * Methode fügt das übergebene Objekt zur Observer-Liste hinzu
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
     * Methode durchläuft die in der Observerliste
     * eingetragenen Klassen und ruft dort die
     * update-Methode auf.
     */
    @Override
    public void Notify() {
        for (Observer observer : observerList) {
            observer.update(this);
        }
    }


    public Buchhaltung getBuchhalung() {
        return this.buchhaltung;
    }
}
