package space.cloud4b.verein.model.verein.kontrolle.rangliste;

import org.junit.Before;
import org.junit.Test;
import space.cloud4b.verein.model.verein.adressbuch.Mitglied;

import static org.junit.Assert.assertEquals;

public class PositionTest {
    Position position;

    public PositionTest() {
        position = new Position(new Mitglied(1, "Kämpf", "Bernhard")
                , "Bernhard Kämpf", 10, 2, 2 / 10);
    }

    @Before
    public void before() {

    }

    @Test
    public void getAnzahlTermineProperty() {
        assertEquals(position.getAnzahlTermineProperty().getValue().intValue(), 10);
    }


    @Test
    public void getAnwesenheitsAnteil() {
        assertEquals(position.getAnwesenheitsAnteil(), 2 / 10, 0.01);
    }
}