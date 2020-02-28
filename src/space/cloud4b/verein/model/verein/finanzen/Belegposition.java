package space.cloud4b.verein.model.verein.finanzen;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import space.cloud4b.verein.model.verein.user.User;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Currency;

public class Belegposition {
    private int belegPositionId;
    private int positionNummer;
    private char sollHaben;
    private Konto konto;
    private Betrag betrag;
    private String positionsText;
    private Belegkopf belegKopf;

    public Belegposition(Belegkopf belegkopf, int belegPositionId, int positionNummer, char sollHaben, Konto konto, Betrag betrag
            , String positionsText) {
        System.out.println(">>>> neue Belegposition wird erstellt ***");
        this.belegPositionId = belegPositionId;
        this.positionNummer = positionNummer;
        this.sollHaben = sollHaben;
        this.betrag = betrag;
        this.positionsText = positionsText;
        this.konto = konto;
        this.belegKopf = belegkopf;
    }

    public Belegposition(int newKey, int belegkopfId, int positionsnummer, User currentUser, Timestamp valueOf) {
        this.belegPositionId = newKey;
        this.positionNummer = positionsnummer;
        this.sollHaben = 'S';
        this.konto = null;
        this.betrag = new Betrag(new BigDecimal(0), Currency.getInstance("CHF"),new BigDecimal(0) );
        this.positionsText = "neue Position";
    }

    public String toHauptjournalString() {
        if(this.konto != null) {
            return "#" + positionNummer + " " + sollHaben + " " + konto.toString()
                    + " " + betrag.getBetragToShortString() + " " + this.positionsText;
        } else {
            return "#" + positionNummer + " " + sollHaben
                    + " " + betrag.getBetragToShortString() + " " + this.positionsText;
        }
    }
    public String toString() {
        if(this.konto != null) {
            return "#" + positionNummer + " " + sollHaben + " " + konto.toString() + " " + betrag.toString();
        } else {
            return "#" + positionNummer + " " + sollHaben +  " " + betrag.toString();
        }
    }

    public ObservableValue<Number> getPosition() {
        return new SimpleIntegerProperty(this.positionNummer);
    }


    public ObservableValue<String> getSH() {
        return new SimpleStringProperty(String.valueOf(this.sollHaben));
    }

    public ObservableValue<Konto> getKonto() {
        return new SimpleObjectProperty<>(this.konto);
    }

    public ObservableValue<Betrag> getBetrag() {
        return new SimpleObjectProperty<>(this.betrag);
    }

    public ObservableValue<String> getTextProperty() {
        return new SimpleObjectProperty<>(this.positionsText);
    }
    public String getText(){
        return this.positionsText;
    }

    public void setText(String newValue) {
        this.positionsText = newValue;
    }

    public void setCurrency(Currency newValue) {
        this.betrag.setWaehrung(newValue);
    }

    public ObservableValue<String> getPositionNummerProperty() {
        return new SimpleStringProperty(Integer.toString(this.positionNummer));
    }

    public ObservableValue<String> getPositionTextProperty() {
        return new SimpleStringProperty(this.positionsText);
    }

    public String getUserTimestamp() {
        return "TODO...";
    }

    public void setSollHaben(String sollHaben) {
        this.sollHaben = sollHaben.toCharArray()[0];
    }

    public void setKonto(Konto konto) {
        this.konto = konto;
    }

    public void setBetrag(Betrag betrag) {
        this.betrag = betrag;
    }

    public void setPositionsText(String positionsText) {
        this.positionsText = positionsText;
    }

    public int getPositionId() {
        return this.belegPositionId;
    }

    public Konto getKontoObject() {
        if(konto == null) {
            konto = new Konto("ungültiges Konto", 9999, 9, this.getBelegKopf().getBuchungsPeriode());
        }
        return konto;
    }

    public ObservableValue<String> getBetragCHFSoll() {
        if(this.sollHaben == 'S') {
            return new SimpleStringProperty(String.format("%,.2f"
                    , this.betrag.getBetragBuchungsWaehrung().doubleValue()));
        } else {
            return null;
        }
    }

    public ObservableValue<String> getBetragCHFHaben() {
        if(this.sollHaben == 'H') {
            return new SimpleStringProperty(String.format("%,.2f"
                    , this.betrag.getBetragBuchungsWaehrung().doubleValue()));
        } else {
            return null;
        }
    }

    public Currency getWaehrung() {
        if(betrag != null) {
            return betrag.getWaehrung();
        } else {
            return Currency.getInstance("CHF");
        }
    }

    public Belegkopf getBelegKopf() {
        return belegKopf;
    }

}
