package space.cloud4b.verein.model.verein.finanzen;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.*;

class BetragTest {


    @BeforeEach
    void setUp() {
        Betrag betrag = new Betrag(new BigDecimal(100.25), Currency.getInstance("EUR"),1.22);
        System.out.println("Waehrung " + betrag.getWaehrung() + " (" + betrag.getWaehrung().getNumericCode() + ")");
        System.out.println("Betrag CHF: " + betrag.getBetragBelegWaehrung());
        System.out.println("Betrag " + betrag.getWaehrung() + ": " + betrag.getBetragBuchungsWaehrung());

        Betrag betrag1 = new Betrag(new BigDecimal(100),Currency.getInstance("EUR"), new BigDecimal(122));
        System.out.println("Waehrung " + betrag1.getWaehrung() + " (" + betrag1.getWaehrung().getNumericCode() + ")");
        System.out.println("Betrag CHF: " + betrag1.getBetragBelegWaehrung());
        System.out.println("Betrag " + betrag1.getWaehrung() + ": " + betrag1.getBetragBuchungsWaehrung());
    }

    @Test
    void getWaehrung() {

    }
}