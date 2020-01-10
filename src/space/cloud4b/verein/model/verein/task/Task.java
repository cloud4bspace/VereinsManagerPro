package space.cloud4b.verein.model.verein.task;

import space.cloud4b.verein.model.verein.adressbuch.Mitglied;
import space.cloud4b.verein.model.verein.status.StatusElement;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class Task {

    private int taskId;
    private StatusElement taskStatus;
    private StatusElement taskPrio;
    private LocalDate taskDatum;
    private String taskTitel;
    private String taskText;
    private Mitglied verantwortlichesMitglied;

    public Task(String taskTitel) {
        this.taskTitel = taskTitel;
    }

    public Task(String taskTitel, String taskText) {
        this.taskTitel = taskTitel;
        this.taskText = taskText;
    }

    public Task(int taskId, String taskTitel, String taskText, Mitglied mitglied) {
        this.taskId = taskId;
        this.taskStatus = null;
        this.taskPrio = null;
        this.taskDatum = LocalDate.now().plusMonths(1);
        this.taskTitel = taskTitel;
        this.taskText = taskText;
        this.verantwortlichesMitglied = mitglied;
    }

    public Task(int taskId, String taskTitel, String taskText, Mitglied mitglied, LocalDate taskDatum) {
        this.taskId = taskId;
        this.taskStatus = null;
        this.taskPrio = null;
        this.taskDatum = taskDatum;
        this.taskTitel = taskTitel;
        this.taskText = taskText;
        this.verantwortlichesMitglied = mitglied;
    }

    // Task-Id
    public int getTaskId() {
        return taskId;
    }

    // Titel/Bezeichnung
    public String getTaskTitel() {
        return this.taskTitel;
    }

    public void setTitel(String titel) {
        this.taskTitel = titel;
    }

    // Text/Details
    public void setDetails(String taskText) {
        this.taskText = taskText;
    }

    public String getTaskText() {
        return this.taskText;
    }

    // Termin
    public LocalDate getTaskDatum() {
        return this.taskDatum;
    }

    // verantwortliches Mitglied
    public void setVerantwortliches(Mitglied mitglied) {
        this.verantwortlichesMitglied = mitglied;
    }

    public Mitglied getVerantwortlichesMitglied() {
        return this.verantwortlichesMitglied;
    }

    // Priorität
    public StatusElement getPrioStatus() {
        return this.taskPrio;
    }

    public void setPrioStatus(StatusElement taskPrio) {
        this.taskPrio = taskPrio;
    }

    // Status
    public StatusElement getStatusStatus() {
        return this.taskStatus;
    }

    public void setStatusStatus(StatusElement taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String toString() {
        return this.taskTitel + ": " + taskPrio.getStatusElementTextLang() + " (" + taskDatum.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)) + ")";
    }
}
