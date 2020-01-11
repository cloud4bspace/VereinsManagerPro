package space.cloud4b.verein.controller;

import org.junit.Before;
import org.junit.Test;

import java.sql.Timestamp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BenutzerControllerTest {
    BenutzerController bc;

    @Before
    public void setUp() throws Exception {
        bc = new BenutzerController();
    }

    @Test
    public void updateAnzahlBenutzer() {
        bc.updateAnzahlBenutzer(33);
        assertEquals(bc.getAnzahlBenutzer(), 33);
    }

    @Test
    public void updateListen() {
        bc.updateListen();
        assertTrue(bc.getBenutzerListe().size() > 0);
    }

    @Test
    public void updateLetzeBenutzerAenderung() {
        bc.updateLetzeBenutzerAenderung(Timestamp.valueOf("2020-01-01 22:22:22"));
    }

    @Test
    public void getAnzahlBenutzer() {
        updateAnzahlBenutzer();
        assertTrue(bc.getAnzahlBenutzer() > 0);
    }

    @Test
    public void getBenutzerListe() {
        bc.updateListen();
        assertTrue(bc.getBenutzerListe().size() > 0);
    }
}