package space.cloud4b.verein.model.verein.finanzen;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import space.cloud4b.verein.services.DatabaseReader;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;

public class Belegkopf {
    private int belegId;
    private int belegNummer;
    private LocalDate belegDatum;
    private LocalDate buchungsDatum;
    private Buchungsperiode buchungsPeriode;
    private String belegKopfText;
    private Betrag betrag;
    private ArrayList<Belegposition> belegPositionen;

    public Belegkopf(int belegId, int belegNummer, LocalDate belegDatum, LocalDate buchungsDatum,
                     Buchungsperiode buchungsPeriode, String belegKopfText, Betrag betrag) {
        System.out.println(">>> neuer Belegkopf wird erstellt ***");
        this.belegId = belegId;
        this.belegNummer = belegNummer;
        this.belegDatum = belegDatum;
        this.buchungsDatum = buchungsDatum;
        this.buchungsPeriode = buchungsPeriode;
        this.belegKopfText = belegKopfText;
        this.betrag = betrag;
        belegPositionen = new ArrayList<>();
        fillBelegPositionen();
    }

    public String toString() {
        return "#" + belegNummer + ": " + belegDatum.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)) + " " + belegKopfText + " " + betrag.toString();
    }

    public void fillBelegPositionen() {
        belegPositionen = DatabaseReader.getBelegPositionen(this);
    }

    public int getBelegkopfId() {
        return this.belegId;
    }

    public Buchungsperiode getBuchungsPeriode(){
        return buchungsPeriode;
    }

    public ObservableValue<LocalDate> getBelegDatumProperty() {
        return new SimpleObjectProperty(this.belegDatum);
    }

    public SimpleStringProperty getBelegDatumStringProperty() {
        return new SimpleStringProperty(belegDatum.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));
    }

    public ObservableValue<Number> getBelegNummerProperty() {
        return new SimpleIntegerProperty(this.belegNummer);
    }

    public SimpleStringProperty getBelegBetragProperty() {
        DecimalFormat df = new DecimalFormat("0.00");
        String formatted = df.format(betrag.getBetragBelegWaehrung());
        return new SimpleStringProperty(formatted);
    }
    public SimpleStringProperty getBetragCHFProperty() {
        DecimalFormat df = new DecimalFormat("0.00");
        String formatted = df.format(betrag.getBetragBuchungsWaehrung());
        return new SimpleStringProperty(formatted);
    }

    public SimpleStringProperty getKursStringProperty() {
        DecimalFormat df = new DecimalFormat("0.000");
        String formatted = df.format(betrag.getUmrechnungsKurs());
        return new SimpleStringProperty(formatted);
    }

    public ObservableValue<LocalDate> getBuchungsDatumProperty() {
        return new SimpleObjectProperty<LocalDate>(this.buchungsDatum);
    }


    public ObservableValue<String> getBelegWaehrungProperty() {
        return new SimpleStringProperty(this.betrag.getWaehrung().getCurrencyCode() + " (" + this.betrag.getWaehrung().getSymbol() + ")");
    }

    public ObservableValue<String> getBelegTextProperty() {
        return new SimpleStringProperty(this.belegKopfText);
    }
    public ObservableValue<String> getBetragStringProperty() {
        return new SimpleStringProperty(this.betrag.getBetragToString());
    }

    public ObservableValue<String> getBuchungsDatumStringProperty() {
        return new SimpleStringProperty(buchungsDatum.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));
    }

    public ObservableValue<String> getBelegPositionen() {
        String string = new String();
        int count = 1;
        for(Belegposition position : belegPositionen) {
            if(count>1) {
                string += "\n";
            }
            string += position.toHauptjournalString();
            count++;
        }
        return new SimpleStringProperty(string);
    }
}
