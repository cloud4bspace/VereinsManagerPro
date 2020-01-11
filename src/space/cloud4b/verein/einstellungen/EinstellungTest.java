package space.cloud4b.verein.einstellungen;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class EinstellungTest {

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void getdbURL() {
        assertNotNull(Einstellung.getdbURL());
    }

    @Test
    public void getdbPW() {
        assertNotNull(Einstellung.getdbPW());
    }

    @Test
    public void getdbUser() {
        assertNotNull((Einstellung.getdbUser()));
    }

    @Test
    public void getVereinsName() {
        assertNotNull(Einstellung.getVereinsName());
    }

    @Test
    public void setProperties() {
        Einstellung.setProperties("0",
                "1",
                "2",
                "3",
                "4",
                "5");
        assertEquals(Einstellung.getVereinsName(), "0");
        assertEquals(Einstellung.getdbPort(), "2");
        assertEquals(Einstellung.getdbDatenbank(), "3");
        assertEquals(Einstellung.getdbUser(), "4");
        assertEquals(Einstellung.getdbPW(), "5");

        Einstellung.setProperties("Ruderclub Limmat X",
                "144.hosttech.eu",
                "3306",
                "usr_web116_5",
                "web116",
                "524680_Ab");

    }

    @Test
    public void getdbHost() {
        assertEquals(Einstellung.getdbHost().getClass(), String.class);
    }
}