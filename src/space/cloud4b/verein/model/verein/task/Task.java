package space.cloud4b.verein.model.verein.task;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
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
    private SimpleStringProperty taskTitelProperty;
    private String taskText;
    //  private ArrayList<Mitglied> verantwortliche;
    private Mitglied mitglied;

    public Task(String taskTitel) {
        this.taskTitel = taskTitel;
    }

    public Task(String taskTitel, String taskText) {
        this.taskTitel = taskTitel;
        this.taskTitelProperty = new SimpleStringProperty(taskTitel);
        this.taskText = taskText;
    }

    public Task(int taskId, String taskTitel, String taskText, Mitglied mitglied) {
        this.taskId = taskId;
        this.taskStatus = null;
        this.taskPrio = null;
        this.taskDatum = LocalDate.now().plusMonths(1);
        this.taskTitel = taskTitel;
        this.taskTitelProperty = new SimpleStringProperty(taskTitel);
        this.taskText = taskText;
        this.mitglied = mitglied;
    }

    public Task(int taskId, String taskTitel, String taskText, Mitglied mitglied, LocalDate taskDatum) {
        this.taskId = taskId;
        this.taskStatus = null;
        this.taskPrio = null;
        this.taskDatum = taskDatum;
        this.taskTitel = taskTitel;
        this.taskTitelProperty = new SimpleStringProperty(taskTitel);
        this.taskText = taskText;
        this.mitglied = mitglied;
    }

    public String getOutputText() {
        return taskTitel + "\n" + taskText + "\n" + taskDatum;
    }

    public String getTaskTitel() {
        return this.taskTitel;
    }

    public String getTaskText() {
        return this.taskText;
    }

    public LocalDate getTaskDatum() {
        return this.taskDatum;
    }

    public int getTaskId() {
        return taskId;
    }

    public StatusElement getPrioStatus() {
        return this.taskPrio;
    }

    public void setPrioStatus(StatusElement taskPrio) {
        this.taskPrio = taskPrio;
    }

    public StatusElement getStatusStatus() {
        return this.taskStatus;
    }

    public void setStatusStatus(StatusElement taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String toString() {
        return this.taskTitel + ": " + taskPrio.getStatusElementTextLang() + " (" + taskDatum.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)) + ")";
    }

    public void setTitel(String titel) {
        this.taskTitel = titel;
        this.taskTitelProperty = new SimpleStringProperty(titel);
        System.out.println("Task Titel-Update");
    }

    public ObservableValue<String> getTaskTitelProperty() {
        return this.taskTitelProperty;
    }
}
