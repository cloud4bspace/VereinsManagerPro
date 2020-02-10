package space.cloud4b.verein.model.verein.finanzen;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import space.cloud4b.verein.services.DatabaseReader;

import java.util.ArrayList;
import java.util.Random;

public class Konto {
    private int kontoId;
    private int kontoNummer;
    private int kontoZuordnung;
    private Betrag saldo;
    private Buchungsperiode buchungsperiode;
    private String kontoBezeichnung;
    private String kontoKlasseText;
    private String kontoHauptgruppeText;
    private String kontoGruppeText;
    private ArrayList<Kontoposition> kontoPositionen;

    public Konto(int kontoNummer, Buchungsperiode periode) {
        System.out.println(">>>> neues Konto wird erstellt *** (" + kontoNummer + ")");
        this.kontoId = DatabaseReader.getKontoId(kontoNummer, periode);
        this.kontoNummer = kontoNummer;
        this.kontoZuordnung = DatabaseReader.getKontoZuordnung(this.kontoNummer, periode);
        this.kontoBezeichnung = DatabaseReader.getKontobezeichnung(this.kontoNummer, periode);
        this.kontoHauptgruppeText = DatabaseReader.getKontoHauptgruppeText(this.kontoZuordnung);
        this.kontoGruppeText = DatabaseReader.getKontoGruppeText(this.kontoZuordnung);
        this.kontoKlasseText = DatabaseReader.getKontoKlasseText(this.kontoZuordnung);
        this.buchungsperiode = periode;

        this.updateSaldo();
    }

    public void updateSaldo() {
        this.saldo = DatabaseReader.getSaldo(this, buchungsperiode);
        System.out.println("Saldo Konto " + this + ": " + saldo);
    }

    public Betrag getSaldo() {
        return this.saldo;
    }
    public String toString() {
        return kontoNummer + " " + kontoBezeichnung;
    }
    public SimpleIntegerProperty getKontoNummerProperty() {
        return new SimpleIntegerProperty(this.kontoNummer);
    }
    public SimpleStringProperty getKontoBezeichnungProperty(){
        return new SimpleStringProperty(this.kontoBezeichnung);
    }

    public SimpleStringProperty getKontoHauptgruppeText(){
        return new SimpleStringProperty((this.kontoHauptgruppeText));
    }

    public ObservableValue<String> getKontoGruppeText() {
        return new SimpleStringProperty(this.kontoGruppeText);
    }

    public ObservableValue<String> getKontoKlasseText() {
        return new SimpleStringProperty(this.kontoKlasseText);
    }

    public ObservableValue<String> getUmgliederungText() {
        if(this.kontoNummer != this.kontoZuordnung) {
            return new SimpleStringProperty(Integer.toString(this.kontoZuordnung));
        } else {
            return new SimpleStringProperty("-");
        }
    }

    public int getKontoNummer() {
        return this.kontoNummer;
    }
    public Buchungsperiode getPeriode() {
        return this.buchungsperiode;
    }

    @Override
    public boolean equals(Object obj) {
        // checking if both the object references are
        // referring to the same object.
        if(this == obj)
            return true;

        // it checks if the argument is of the
        // type Geek by comparing the classes
        // of the passed argument and this object.
        // if(!(obj instanceof Geek)) return false; ---> avoid.
        if(obj == null || obj.getClass()!= this.getClass())
            return false;

        // type casting of the argument.
        Konto konto = (Konto) obj;

        // comparing the state of argument with
        // the state of 'this' Object.
        return (konto.getKontoNummer() == this.kontoNummer);
    }

    @Override
    public int hashCode()
    {
        Random r = new Random();
        return this.kontoNummer * r.nextInt();
    }
}
