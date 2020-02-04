package space.cloud4b.verein.model.verein.finanzen;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BuchhaltungTest {
    Buchhaltung buchhaltung;

    @Test
    void setupBuchungsperioden() {
        buchhaltung = new Buchhaltung();
        Buchungsperiode buchungsperiode = buchhaltung.getBuchungsperiode(2020);
        buchungsperiode.listHauptjournal();
    }
}