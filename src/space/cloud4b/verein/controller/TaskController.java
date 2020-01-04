package space.cloud4b.verein.controller;

import space.cloud4b.verein.model.verein.task.Task;
import space.cloud4b.verein.services.DatabaseReader;
import space.cloud4b.verein.services.Observer;
import space.cloud4b.verein.services.Subject;

import java.sql.Timestamp;
import java.util.ArrayList;

public class TaskController implements Subject {

    int anzahlTasks = 0;
    private Timestamp timestamp = null; // Zeitstempel der letzten Änderung im der Mitglieder-Datenbank
    private ArrayList<Observer> observerList;
    private ArrayList<Task> taskListe;


    public TaskController() {
        observerList = new ArrayList<>();
        taskListe = DatabaseReader.getTaskList();
        startTasksObserver(); //TODO hier noch zu fürh
    }

    /**
     * aktualisiert die Anzahl der Termine und benachrichtigt die Observer
     * @param anzahlTasks
     */
    public void updateAnzahlTasks(int anzahlTasks) {
        this.anzahlTasks = anzahlTasks;
        Notify();
    }


    public int getAnzahlTasks() {
        return anzahlTasks;
    }

    public ArrayList<Task> getTasksAsArrayList() {
        return taskListe;
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
        System.out.println("Aenderungen bei den Tasks mit neuem Zeitstempel (" + neuerZeitstempel + ") festgestellt");
        Notify();
    }
    public void setAnzahlTasks(ArrayList<Task> taskListe) {
        this.taskListe = taskListe;
    }
    private void startTasksObserver() {
        Runnable observerTask = () -> {
            int zaehler = 0;
            while (true) {
                if(DatabaseReader.readAnzahlTasks() != anzahlTasks) {
                    updateAnzahlTasks(DatabaseReader.readAnzahlTasks());
                    this.taskListe = DatabaseReader.getTaskList();
                    updateListen();
                }
                // hat sich der Zeitstempel der letzten Äenderung verändert?
              /*  else if(this.timestamp == null) {
                    updateLetzeAenderung(DatabaseReader.readLetzteAenderung());
                    this.taskListe = DatabaseReader.getTaskList();
                    updateListen();
                }*/
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {

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

    public int getanzahlTasks() {
        return anzahlTasks;
    }
}
