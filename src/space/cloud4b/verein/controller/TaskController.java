package space.cloud4b.verein.controller;

import space.cloud4b.verein.model.verein.task.Task;
import space.cloud4b.verein.services.DatabaseReader;
import space.cloud4b.verein.services.Observer;
import space.cloud4b.verein.services.Subject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Die Klasse TaskController stellt den fxml-Controllern die benötigten Listen und Daten zur
 * Verfügung, wie z.B. die Taskliste (ArrayList<Task>)
 * Zudem überwacht die Klasse die zugrundeliegenden MYSQL-Tabelle und gibt Änderungen, Ergänzungen und
 * Löschungen an die in der Observer-Liste (observerList) eingetragenen fxml-Controllern weiter.
 * Dazu implementiert die Klasse das Subject-Interface
 *
 * @author Bernhard Kämpf und Serge Kaulitz
 * @version 2020-01-07
 * @see space.cloud4b.verein.services.Subject
 */
public class TaskController implements Subject {

    int anzahlTasks = 0;
    int anzahlOpenTasks = 0;
    private Timestamp timestamp = null; // Zeitstempel der letzten Änderung im der Mitglieder-Datenbank
    private ArrayList<Observer> observerList;
    private ArrayList<Task> taskListe;


    public TaskController() {

        // Die benötigten Listen werden instanziert
        observerList = new ArrayList<>();
        taskListe = DatabaseReader.getTaskList();

        // der Observer-Thread wird gestartet
        startTasksObserver();
    }

    /**
     * Gibt die Anzahl Tasks als int zurück
     */
    public int getAnzahlOpenTasks() {
        return anzahlOpenTasks;
    }

    public void setAnzahlOpenTasks(int anzahlOpenTasks) {
        this.anzahlOpenTasks = anzahlOpenTasks;
    }

    /**
     * Gibt eine Taskliste als ArrayList zurück
     */
    public ArrayList<Task> getTasksAsArrayList() {
        return taskListe;
    }

    /**
     * die Listen (ArrayList) werden nach einer Änderung in der Datenbank aktualisiert und die
     * Observer werden benachrichtigt.
     */
    public void updateListen() {
        this.taskListe = DatabaseReader.getTaskList();
        System.out.println("Aenderungen bei Listen umgesetzt");
    }

    /**
     * aktualisiert den Zeitstempel für die letzte Änderung in der Termin-Tabelle der Datenbank
     *
     * @param neuerZeitstempel der aktuelle (höchste) Zeitstempel
     */
    public void updateLetzeAenderung(Timestamp neuerZeitstempel) {
        this.timestamp = neuerZeitstempel;
        System.out.println("Aenderungen bei den Tasks mit neuem Zeitstempel (" + neuerZeitstempel + ") festgestellt");
    }

    public void setAnzahlTasks(int anzahlTasks) {
        this.anzahlTasks = anzahlTasks;
    }

    private void startTasksObserver() {
        Runnable observerTask = () -> {
            boolean update = false;
            while (true) {
                if (DatabaseReader.readTotalAnzahlTasks() != anzahlTasks) {
                    setAnzahlTasks(DatabaseReader.readTotalAnzahlTasks());
                    setAnzahlOpenTasks(DatabaseReader.readAnzahlTasks());
                    update = true;
                }
                // hat sich der Zeitstempel der letzten Äenderung verändert?
                else if (this.timestamp == null || Objects.requireNonNull(DatabaseReader.readLetzteAenderungTask())
                        .after(this.timestamp)) {
                    updateLetzeAenderung(DatabaseReader.readLetzteAenderungTask());
                    update = true;
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (update) {
                    update = false;
                    updateListen();
                    Notify();
                }
            }
        };
        Thread thread = new Thread(observerTask);
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
