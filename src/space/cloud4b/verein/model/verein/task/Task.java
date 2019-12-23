package space.cloud4b.verein.model.verein.task;

import space.cloud4b.verein.model.verein.adressbuch.Mitglied;
import space.cloud4b.verein.model.verein.status.StatusElement;

import java.time.LocalDate;

public class Task {

    private int taskId;
    private StatusElement taskStatus;
    private StatusElement taskPrio;
    private LocalDate taskDatum;
    private String taskTitel;
    private String taskText;
    //  private ArrayList<Mitglied> verantwortliche;
    private Mitglied mitglied;

    public Task(int taskId, String taskTitel, String taskText, Mitglied mitglied) {
        this.taskId = taskId;
        this.taskStatus = null;
        this.taskPrio = null;
        this.taskDatum = LocalDate.now().plusMonths(1);
        this.taskTitel = taskTitel;
        this.taskText = taskText;
        this.mitglied = mitglied;
    }

    public Task(int taskId, String taskTitel, String taskText, Mitglied mitglied, LocalDate taskDatum) {
        this.taskId = taskId;
        this.taskStatus = null;
        this.taskPrio = null;
        this.taskDatum = taskDatum;
        this.taskTitel = taskTitel;
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
}
