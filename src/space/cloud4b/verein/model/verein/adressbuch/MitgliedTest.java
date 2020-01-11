package space.cloud4b.verein.model.verein.adressbuch;

import javafx.beans.property.SimpleBooleanProperty;
import org.junit.Before;
import org.junit.Test;
import space.cloud4b.verein.model.verein.status.Status;
import space.cloud4b.verein.model.verein.status.StatusElement;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import static org.junit.Assert.assertEquals;

public class MitgliedTest extends Kontakt {
    Mitglied mitglied;
    Mitglied mitglied2;
    Mitglied mitglied3;

    public MitgliedTest() {
        super(1, "Kämpf", "Bernhard");
    }

    @Before
    public void newMitglied() {
        mitglied = new Mitglied(1, "Kämpf", "Bernhard");
        mitglied2 = new Mitglied(2, "Kunz", "Marlene", "2019-02-02");
        mitglied2.setKategorieIStatus(new Status(2).getStatusElemente().get(1));
        mitglied.setGeburtsdatum(LocalDate.now().minusYears(30));
        mitglied.setAustrittsDatum(LocalDate.now());
    }

    @Test
    public void testNewMitglied() {
        mitglied3 = new Mitglied(1, "A", "B", "2029-01-03");
    }

    @Test(expected = NullPointerException.class)
    public void testGetKurzbezeichnung() throws Exception {
        mitglied.getKurzbezeichnung();
    }


    @Test
    public void test2GetKurzbezeichnung() {
        assertEquals(mitglied2.getKurzbezeichnung(), mitglied2.getNachName() + " " + mitglied2.getVorname() +
                " | " + new Status(2).getStatusElemente().get(1).getStatusElementTextLang());
    }

    @Test
    public void testGetId() {
        assertEquals(mitglied.getId(), 1, 0.01);
    }

    @Test
    public void testGetEintrittsdatum() {
        assertEquals(mitglied.getEintrittsdatum(), null);
        assertEquals(mitglied2.getEintrittsdatum(), LocalDate.parse("2019-02-02"));
    }


    @Test
    public void testGetGeburtsdatum() {
        assertEquals(mitglied.getGeburtsdatum(), LocalDate.now().minusYears(30));
        assertEquals(mitglied2.getGeburtsdatum(), null);
    }

    @Test(expected = DateTimeParseException.class)
    public void testSetGeburtsdatum() throws Exception {
        mitglied2.setGeburtsdatum(LocalDate.parse("1987-02-31"));
    }

    @Test
    public void testGetAustrittsDatum() {
        assertEquals(mitglied2.getAustrittsDatum(), null);
        assertEquals(mitglied.getAustrittsDatum(), LocalDate.now());
    }

    @Test
    public void testSetAustrittsDatum() {
        mitglied.setAustrittsDatum(LocalDate.parse("1900-01-01"));
    }

    @Test
    public void testSetLetzteAenderungUser() {
        mitglied.setLetzteAenderungUser("Kämpf");
    }

    @Test
    public void getKategorieIIElementProperty() {
        assertEquals(mitglied2.getKategorieIIElement(), null);
    }

    @Test
    public void getKategorieIElemen() {
        assertEquals(mitglied2.getKategorieIElement().getClass(), StatusElement.class);
    }

    @Test
    public void setIstVorstandsmitglied() {
        assertEquals(mitglied2.getIstVorstandsmitglied().getValue(), new SimpleBooleanProperty(false).getValue());
    }
}