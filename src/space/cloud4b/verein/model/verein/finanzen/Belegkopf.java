package space.cloud4b.verein.model.verein.finanzen;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import space.cloud4b.verein.model.verein.status.Status;
import space.cloud4b.verein.model.verein.status.StatusElement;
import space.cloud4b.verein.model.verein.user.User;
import space.cloud4b.verein.services.DatabaseOperation;
import space.cloud4b.verein.services.DatabaseReader;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;

public class Belegkopf {
    private int belegId;
    private int belegNummer;
    private StatusElement belegStatus;
    private LocalDate belegDatum;
    private LocalDate buchungsDatum;
    private Buchungsperiode buchungsPeriode;
    private String belegKopfText;
    private Betrag betrag;
    private String letzteAenderungUser;
    private Timestamp letzteAenderungTimestamp;
    private ArrayList<Belegposition> belegPositionen;
    private HashMap<Integer, Konto> kontenPlan;

    public Belegkopf(StatusElement belegStatus, int belegId, int belegNummer, LocalDate belegDatum, LocalDate buchungsDatum,
                     Buchungsperiode buchungsPeriode, String belegKopfText, Betrag betrag,
                     String letzteAenderungUser, Timestamp letzteAenderungTimestamp, HashMap<Integer, Konto> kontenPlan) {
        System.out.println(">>> neuer Belegkopf wird erstellt ***");
        this.belegStatus = belegStatus;
        this.belegId = belegId;
        this.belegNummer = belegNummer;
        this.belegDatum = belegDatum;
        this.buchungsDatum = buchungsDatum;
        this.buchungsPeriode = buchungsPeriode;
        this.belegKopfText = belegKopfText;
        this.betrag = betrag;
        this.letzteAenderungUser = letzteAenderungUser;
        this.letzteAenderungTimestamp = letzteAenderungTimestamp;
        belegPositionen = new ArrayList<>();
        this.kontenPlan = kontenPlan;
        fillBelegPositionen();
    }

    public String toString() {
        return "#" + belegNummer + ": " + belegDatum.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)) + " " + belegKopfText + " " + betrag.toString();
    }

    public void fillBelegPositionen() {
        belegPositionen = DatabaseReader.getBelegPositionen(this, kontenPlan);
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
    public SimpleStringProperty getBelegNummerStringProperty() {
        String string = String.format("%04d", this.belegNummer);
        return new SimpleStringProperty(string);
    }

    public SimpleStringProperty getStatusStringProperty() {
        return new SimpleStringProperty(this.belegStatus.getStatusElementTextLang());
    }

    public SimpleStringProperty getBelegBetragProperty() {
        return new SimpleStringProperty(String.format("%,.2f", betrag.getBetragBelegWaehrung()));
    }
    public SimpleStringProperty getBetragCHFProperty() {
        return new SimpleStringProperty(String.format("%,.2f", this.betrag.getBetragBuchungsWaehrung()));
    }

    public SimpleStringProperty getKursStringProperty() {
        return new SimpleStringProperty(String.format("%,.3f",betrag.getUmrechnungsKurs()));
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

    public LocalDate getBelegDatum() {
        return belegDatum;
    }

    public LocalDate getBuchungsDatum() {
        return buchungsDatum;
    }

    public ArrayList<Belegposition> getBelegPositionenAsArrayList() {
        return belegPositionen;
    }

    public Betrag getBetrag() {
        return betrag;
    }

    public String getUserTimestamp() {
        return letzteAenderungUser + " (" + letzteAenderungTimestamp + ")";
    }

    public void setBelegDatum(LocalDate belegDatum) {
        this.belegDatum = belegDatum;
    }

    public void setBuchungsDatum(LocalDate buchungsDatum) {
        this.buchungsDatum = buchungsDatum;
    }

    public void setBuchungsPeriode(Buchungsperiode periode) {
        this.buchungsPeriode = periode;
    }

    public void setBelegKopfText(String belegKopfText) {
        this.belegKopfText = belegKopfText;
    }

    public void setBetrag(double belegkopfBetrag) {
        this.betrag.setBetrag(belegkopfBetrag);
    }

    public void setBetragCHF(double belegkopfBetragCHF) {
        this.betrag.setBetragCHF(belegkopfBetragCHF);
    }

    public void setWaehrung(String belegkopfWaehrung) {
        this.betrag.setWaehrung(Currency.getInstance(belegkopfWaehrung));
    }

    public ObservableValue<String> getBelegTextProperty(Belegposition belegposition) {
        if(belegposition.getText() == null) {
            return new SimpleStringProperty(this.belegKopfText);
        } else {
            return new SimpleStringProperty(this.belegKopfText + " (" + belegposition.getText() + ")");
        }

    }

    public void setStatus(StatusElement statusElement) {
        this.belegStatus = statusElement;
    }

    public StatusElement getBelegStatus() {
        return this.belegStatus;
    }

    public ObservableValue<String> getBelegStatusProperty() {
        return new SimpleStringProperty(this.belegStatus.getStatusElementSymbol());
    }

    public void setBelegNummer(int i) {
        belegNummer = i;
    }

    public void updateStatus(int i) {
        setStatus(new Status(9).getStatusElemente().get(i));
    }

    public ObservableValue<StatusElement> getBelegStatusObservableValue() {
        return new SimpleObjectProperty<StatusElement>(this.getBelegStatus());
    }

    public ObservableValue<Currency> getBelegWaehrungCurrency() {
        return new SimpleObjectProperty<Currency>(this.betrag.getWaehrung());
    }

    public void selfUpdateStatus(User currentUser) {
        StatusElement neuerStatus = DatabaseReader.checkBelegkopfStatus(this, currentUser);
        DatabaseOperation.updateBelegStatus(this, neuerStatus, currentUser);
    }
}
