package space.cloud4b.verein.model.verein.adressbuch;

import org.junit.Before;
import org.junit.Test;
import space.cloud4b.verein.model.verein.status.Status;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

public class KontaktTest {
    Kontakt kontakt1;
    Kontakt kontakt2;

    public KontaktTest() {

    }

    @Before
    public void before() {
        kontakt1 = new Kontakt(1, "Kämpf", "Bernhard");
        kontakt1.setAdresse("Lindenhof 2");
        kontakt1.setGeburtsdatum(LocalDate.parse("1973-04-18"));
        kontakt2 = new Kontakt(2, "Doe", "John");
    }

    @Test
    public void getKurzbezeichnung() {
        assertEquals(kontakt1.getKurzbezeichnung(), "Kämpf Bernhard");

    }

    @Test
    public void getId() {
        assertEquals(kontakt1.getId(), 1);
        assertEquals(kontakt2.getIdProperty().getValue(), Integer.valueOf(2));
    }

    @Test
    public void getAnredeElement() {
        assertEquals(kontakt1.getAnredeElement(), null);
        kontakt1.setAnredeStatus(new Status(1).getStatusElemente().get(1));
        assertEquals(kontakt1.getAnredeElement().getStatusElementKey(), 1);
    }

    @Test
    public void getNachName() {
        assertEquals(kontakt1.getNachName(), kontakt1.getNachnameProperty().getValue());
    }

    @Test
    public void getAdresse() {
        assertEquals(kontakt2.getAdresse(), null);
    }

    @Test
    public void getPlz() {
        assertEquals(kontakt1.getPlz(), 0);
        kontakt2.setPlz(9999);
        assertEquals(kontakt2.getPlz(), 9999);
    }

    @Test
    public void setPlz() {
        kontakt1.setPlz(1000);
        assertEquals(kontakt1.getPlz(), 1000);
    }
}