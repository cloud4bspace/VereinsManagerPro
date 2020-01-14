package space.cloud4b.verein.model.verein.task;

import org.junit.Before;
import org.junit.Test;
import space.cloud4b.verein.model.verein.adressbuch.Mitglied;

import java.time.LocalDate;

import static org.junit.Assert.assertTrue;

public class TaskTest {
    Task task;

    @Before
    public void setUp() throws Exception {
        task = new Task(99, "Titel", "Text", new Mitglied(1, "Kämpf"
                , "Bernhard"), LocalDate.now());
    }

    @Test
    public void getTaskId() {
        assertTrue(task.getTaskId() == 99);
    }

    @Test
    public void getTaskTitel() {
        assertTrue(task.getTaskTitel() == "Titel");
    }

    @Test
    public void setTitel() {
        task.setTitel("neuer Titel");
        assertTrue(task.getTaskTitel() == "neuer Titel");
    }

    @Test
    public void setDetails() {
        task.setDetails("Details neu");
        assertTrue(task.getTaskText() == "Details neu");
    }

    @Test
    public void setVerantwortliches() {
        task.setVerantwortliches(new Mitglied(2, "Kaulitz", "Serge"));
        assertTrue(task.getVerantwortlichesMitglied().getId() == 2);
    }
}