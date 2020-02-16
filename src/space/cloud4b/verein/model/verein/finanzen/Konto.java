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
    private int kontoKlasse;
    private String kontoKlasseText;
    private int kontoHauptgruppe;
    private String kontoHauptgruppeText;
    private int kontoGruppe;
    private String kontoGruppeText;
    private ArrayList<Kontoposition> kontoPositionen;

    public Konto(int kontoNummer, Buchungsperiode periode) {
        System.out.println(">>>> neues Konto wird erstellt *** (" + kontoNummer + ")");
        this.kontoId = DatabaseReader.getKontoId(kontoNummer, periode);
        this.kontoNummer = kontoNummer;
        this.kontoZuordnung = DatabaseReader.getKontoZuordnung(this.kontoNummer, periode);
        this.kontoBezeichnung = DatabaseReader.getKontobezeichnung(this.kontoNummer, periode);
        this.kontoHauptgruppeText = DatabaseReader.getKontoHauptgruppeText(this.kontoZuordnung);
        this.kontoHauptgruppe = kontoNummer/100;
        this.kontoGruppeText = DatabaseReader.getKontoGruppeText(this.kontoZuordnung);
        this.kontoGruppe = kontoNummer/10;
        this.kontoKlasseText = DatabaseReader.getKontoKlasseText(this.kontoZuordnung);
        this.kontoKlasse = kontoNummer/1000;
        this.buchungsperiode = periode;

        this.updateSaldo();
    }

    /**
     * Konstruktur für eine Kontoklasse (wird in der TreeView benötigt)
     * @param kontoBezeichnung
     * @param nummer
     * @param kontoKlasse
     */
    public Konto(String kontoBezeichnung, int nummer, int kontoKlasse, Buchungsperiode buchungsperiode) {
        this.kontoBezeichnung = kontoBezeichnung;
        this.kontoNummer = nummer;
        this.kontoKlasse = kontoKlasse;
        this.buchungsperiode = buchungsperiode;
        this.saldo = DatabaseReader.getSaldoKontoKlasse(this);

    }

    /**
     * Konstruktor für Hauptgruppe
     * @param kontoBezeichnung
     * @param nummer
     * @param kontoKlasse
     * @param kontoHauptgruppe
     * @param buchungsperiode
     */
    public Konto(String kontoBezeichnung, int nummer, int kontoKlasse, int kontoHauptgruppe
            , Buchungsperiode buchungsperiode) {
        this.kontoBezeichnung = kontoBezeichnung;
        this.kontoNummer = nummer;
        this.kontoKlasse = kontoKlasse;
        this.kontoHauptgruppe = kontoHauptgruppe;
        this.buchungsperiode = buchungsperiode;
        this.saldo = DatabaseReader.getSaldoKontoHauptgruppe(this);
    }

    /**
     * Konstruktor für Gruppe
     * @param kontoBezeichnung
     * @param nummer
     * @param kontoKlasse
     * @param kontoHauptGruppe
     * @param kontoGruppe
     * @param buchungsperiode
     */
    public Konto(String kontoBezeichnung, int nummer, int kontoKlasse, int kontoHauptGruppe, int kontoGruppe
            , Buchungsperiode buchungsperiode) {
        this.kontoBezeichnung = kontoBezeichnung;
        this.kontoNummer = nummer;
        this.kontoKlasse = kontoKlasse;
        this.kontoHauptgruppe = kontoHauptGruppe;
        this.kontoGruppe = kontoGruppe;
        this.buchungsperiode = buchungsperiode;
        this.saldo = DatabaseReader.getSaldoKontoGruppe(this);
    }

    public Konto(String bilanz, Buchungsperiode buchungsperiode) {
        this.kontoNummer = 0;
        this.kontoBezeichnung = bilanz;
        this.buchungsperiode = buchungsperiode;
        this.saldo = DatabaseReader.getSaldoBilanz(this);
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

    public int getKontoKlasse() {
        return this.kontoKlasse;
    }

    public int getKontoHauptgruppe() {
        return this.kontoHauptgruppe;
    }
    public int getKontoGruppe() {
        return this.kontoGruppe;
    }

    public String getBetragBilanzTreeView() {
        if(saldo != null) {
            return saldo.getBetragToTableViewString();
        } else {
            return "-";
        }
    }

    public String getBezeichnungTreeView() {
        if(kontoNummer>0) {
            return kontoNummer + " " + kontoBezeichnung;
        } else {
            return kontoBezeichnung;
        }
    }

    public Buchungsperiode getBuchungsperiode() {
        return this.buchungsperiode;
    }
}
