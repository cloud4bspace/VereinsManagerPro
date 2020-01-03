package space.cloud4b.verein.controller;

import space.cloud4b.verein.MainApp;
import space.cloud4b.verein.model.verein.adressbuch.Mitglied;
import space.cloud4b.verein.model.verein.kalender.Jubilaeum;
import space.cloud4b.verein.services.DatabaseOperation;
import space.cloud4b.verein.services.DatabaseReader;
import space.cloud4b.verein.services.Observer;
import space.cloud4b.verein.services.Subject;
import space.cloud4b.verein.view.dashboard.DashBoardController;
import space.cloud4b.verein.view.mitglieder.MitgliedViewController;

import java.sql.Timestamp;
import java.util.ArrayList;

/**
 *
 */
public class AdressController implements Subject {

    private MainApp mainApp;
    private int anzahlMitglieder;
    private Timestamp timestamp = null; // Zeitstempel der letzten Änderung im der Mitglieder-Datenbank
    private DashBoardController dashBoardController;
    private MitgliedViewController mitgliedViewController;
    private ArrayList<Observer> observerList;
    private ArrayList<Mitglied> mitgliederListe;
    private ArrayList<Jubilaeum> jubilaeumsListe;

    public AdressController() {
        System.out.println("AdressController erzeugt");
        observerList = new ArrayList<>();
        // mitgliederListe = DatabaseReader.mitgliederLaden();
        mitgliederListe = DatabaseReader.getMitgliederAsArrayList();
        jubilaeumsListe = DatabaseReader.getJubilaeenAsArrayList();
        DatabaseOperation.checkMitgliederStatus();
        startAdressObserver();
    }


    public void updateAnzahlMitglieder(int anzahlMitglieder) {
        this.anzahlMitglieder = anzahlMitglieder;
        System.out.println("Anzahl Mitglieder geändert auf " + anzahlMitglieder);
        Notify();
    }

    public void updateListen() {
        this.mitgliederListe = DatabaseReader.getMitgliederAsArrayList();
        this.jubilaeumsListe = DatabaseReader.getJubilaeenAsArrayList();
        Notify();
    }

    /**
     * aktualisiert den Zeitstempel für die letzte Änderung in der Adress-Tabelle der Datenbank
     * @param neuerZeitstempel
     */
    public void updateLetzeAenderung(Timestamp neuerZeitstempel){
        this.timestamp = neuerZeitstempel;
        System.out.println("Aenderungen bei den Mitgliedern mit neuem Zeitstempel (" + neuerZeitstempel + ") festgestellt");
        Notify();
    }

    public int getAnzahlMitglieder() {
        return anzahlMitglieder;
    }


    public ArrayList<Mitglied> getMitgliederListe() {
        return this.mitgliederListe;
    }

    public ArrayList<Jubilaeum> getJubilaeumsListe() {
        return this.jubilaeumsListe;
    }

    private void startAdressObserver() {
        Runnable observeMitglieder = () -> {
            int zaehler = 0;
            while (true) {
                // hat sich die Anzahl der Einträge in der Tabelle Kontakt verändert?
                if(DatabaseReader.readAnzahlMitglieder() != anzahlMitglieder) {
                    updateAnzahlMitglieder(DatabaseReader.readAnzahlMitglieder());
                    updateListen();
                }
                // hat sich der Zeitstempel der letzten Äenderung verändert?
                if(this.timestamp == null) {
                    updateLetzeAenderung(DatabaseReader.readLetzteAenderung());
                    updateListen();
                } else if (DatabaseReader.readLetzteAenderung().after(this.timestamp)){
                    updateLetzeAenderung(DatabaseReader.readLetzteAenderung());
                    updateListen();
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    System.out.println("Problem mit Runnable: " + this);
                }
            }
        };
        Thread thread = new Thread(observeMitglieder);
        thread.setName("AdressObserver");
        thread.setDaemon(true);
        thread.start();
    }
    @Override
    public void Attach(Observer o) {
        observerList.add(o);
    }

    @Override
    public void Dettach(Observer o) {
        observerList.remove(o);
    }

    @Override
    public void Notify() {
        for(int i = 0; i <observerList.size(); i++)
        {
            observerList.get(i).update(this);
        }
    }
}
