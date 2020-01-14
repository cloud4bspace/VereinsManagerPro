package space.cloud4b.verein.model.verein.kalender;

import org.junit.Before;
import org.junit.Test;
import space.cloud4b.verein.model.verein.adressbuch.Mitglied;
import space.cloud4b.verein.model.verein.status.StatusElement;

import static junit.framework.TestCase.assertEquals;

public class TeilnehmerTest {
    Teilnehmer teilnehmer;

    public TeilnehmerTest() {

    }

    @Before
    public void setUp() {
        teilnehmer = new Teilnehmer(new Mitglied(1, "Kämpf", "Bernhard"));
    }

    @Test
    public void testSetAnmeldeStatus() {
        teilnehmer.setAnmeldeStatus(new StatusElement(9, "Test"));
        teilnehmer.setAnmeldungText("Hallo");
        assertEquals(teilnehmer.getAnmeldungProperty().getValue().getStatusElementKey(), 0);
        assertEquals(teilnehmer.getAnmeldungText(), "Hallo");
    }

    @Test
    public void getAnmeldungWert() {
        assertEquals(teilnehmer.getMitglied().getId(), 1);
    }
}