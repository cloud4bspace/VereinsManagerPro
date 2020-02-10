package space.cloud4b.verein.model.verein.finanzen;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;

import java.math.BigDecimal;
import java.util.Currency;

public class Betrag {
    private BigDecimal betragBuchungsWaehrung;
    private BigDecimal betragBelegWaehrung;
    private Currency belegWaehrung;
    private double umrechnungsKurs;

    /**
     * Für alle Belege in Schweizer Franken
     * @param betragBelegWaehrung
     */
    public Betrag(BigDecimal betragBelegWaehrung) {
        this.betragBelegWaehrung = betragBelegWaehrung;
        this.betragBuchungsWaehrung = betragBelegWaehrung;
        this.belegWaehrung = Currency.getInstance("CHF");
    }

    /**
     * Für Belege in Fremdwährung mit Betrag in CHF (bereits umgerechnet)
     * @param betragBelegWaehrung
     * @param belegWaehrung
     * @param betragBuchungsWaehrung
     */
    public Betrag(BigDecimal betragBelegWaehrung, Currency belegWaehrung, BigDecimal betragBuchungsWaehrung) {
        System.out.println(">>>> neuer Betrag wird erstellt");
        this.betragBelegWaehrung = betragBelegWaehrung;
        this.belegWaehrung = belegWaehrung;
        this.betragBuchungsWaehrung = betragBuchungsWaehrung;
        this.umrechnungsKurs = betragBuchungsWaehrung.doubleValue()/betragBelegWaehrung.doubleValue();
    }

    /**
     * Für Belege in Fremdwährung mit Umrechnungskurs
     * @param betragBelegWaehrung
     * @param belegWaehrung
     * @param umrechnungsKurs;
     */
    public Betrag(BigDecimal betragBelegWaehrung, Currency belegWaehrung, double umrechnungsKurs) {
        System.out.println(">>>> neuer Betrag wird erstellt");
        this.betragBelegWaehrung = betragBelegWaehrung;
        this.belegWaehrung = belegWaehrung;
        this.umrechnungsKurs = umrechnungsKurs;
        this.betragBuchungsWaehrung = new BigDecimal(umrechnungsKurs * betragBelegWaehrung.doubleValue());
    }

    public Currency getWaehrung(){
        return this.belegWaehrung;
    }
    public BigDecimal getBetragBuchungsWaehrung() {
        return this.betragBuchungsWaehrung;
    }

    public String getBetragToString() {
        String string = String.format("%.2f", this.betragBelegWaehrung.doubleValue()) + " " + this.belegWaehrung.getCurrencyCode();
        if(!this.belegWaehrung.getCurrencyCode().equals("CHF")) {
            string += "\n" + String.format("%.2f", this.betragBuchungsWaehrung.doubleValue()) + " CHF";
            string += "\n(" + String.format("%.3f", this.umrechnungsKurs) + ")";
        }
        return string;
    }

    public String getBetragToShortString() {
        return String.format("%.2f", this.betragBelegWaehrung.doubleValue()) + " " + this.belegWaehrung.getCurrencyCode();
    }
    public ObservableValue<String> toColumnString(){
        return new SimpleStringProperty(String.format("%.2f", this.betragBelegWaehrung.doubleValue()));
    }
    public BigDecimal getBetragBelegWaehrung() {
        return this.betragBelegWaehrung;
    }
    public double getUmrechnungsKurs() { return this.umrechnungsKurs; }

    public String toString() {
        String string = this.belegWaehrung.getCurrencyCode() + ": " + String.format("%.2f", this.betragBelegWaehrung.doubleValue());
        if(!this.belegWaehrung.getCurrencyCode().equals("CHF")) {
            string += " --> CHF: " + String.format("%.2f", this.betragBuchungsWaehrung.doubleValue())
                    + " (Kurs: " + String.format("%.3f", this.umrechnungsKurs) + ")";
        }
     return string;
    }

    public ObservableValue<String> toColumnCHFString() {
        return new SimpleStringProperty(String.format("%.2f", this.betragBuchungsWaehrung.doubleValue()));
    }

    public void setWaehrung(Currency newValue) {
        this.belegWaehrung = newValue;
    }

    public void setBetrag(double belegkopfBetrag) {
        this.betragBelegWaehrung = new BigDecimal(belegkopfBetrag);
        this.umrechnungsKurs = this.betragBuchungsWaehrung.doubleValue()/belegkopfBetrag;
    }

    public void setBetragCHF(double belegkopfBetragCHF) {
        this.betragBuchungsWaehrung = new BigDecimal(belegkopfBetragCHF);
        this.umrechnungsKurs = belegkopfBetragCHF/this.betragBelegWaehrung.doubleValue();
    }
}
