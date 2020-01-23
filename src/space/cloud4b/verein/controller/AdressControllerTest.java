package space.cloud4b.verein.controller;

import org.junit.Before;

import org.junit.Test;
import space.cloud4b.verein.services.DatabaseReader;

import java.lang.annotation.Annotation;
import java.sql.Timestamp;

import static org.junit.Assert.assertTrue;

public class AdressControllerTest {
    AdressController ac;

    @Before
    public void setUp() throws Exception {
        ac = new AdressController();
    }

    @Test
    public void updateAnzahlMitglieder() {
        ac.updateAnzahlMitglieder(0);

    }

    @Test
    public void updateListen() {
        ac.updateListen();
    }

    @Test
    public void updateLetzeAenderung() {
        ac.updateLetzeAenderung(Timestamp.valueOf("2019-01-10 22:22:22"));
    }

    @Test
    public void testGetAnzahlMitglieder() {
        ac.updateAnzahlMitglieder(DatabaseReader.readAnzahlMitglieder());
        assertTrue(ac.getAnzahlMitglieder() > 0);
    }

    @Test
    public void getMitgliederListe() {
        ac.updateListen();
        assertTrue(ac.getMitgliederListe().size() > 0);
    }

    @Test
    public void getJubilaeumsListe() {
        assertTrue(ac.getJubilaeumsListe().size() == 0);
        ac.updateListen();
        assertTrue(ac.getJubilaeumsListe().size() > 0);
    }


    @org.junit.jupiter.api.Test
    void getAnzahlMitglieder() {
    }
}