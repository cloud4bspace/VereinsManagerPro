package space.cloud4b.verein.model.verein.meldung;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MeldungTest {
    Meldung meldung;

    @Before
    public void setUp() throws Exception {
        meldung = new Meldung("Meldung Text", "NOK");
    }

    @Test
    public void getMeldungType() {
        assertEquals(meldung.getMeldungType(), "NOK");
    }

    @Test
    public void getMeldungOutputString() {
        assertTrue(meldung.getMeldungOutputString().length() > 0);
    }
}