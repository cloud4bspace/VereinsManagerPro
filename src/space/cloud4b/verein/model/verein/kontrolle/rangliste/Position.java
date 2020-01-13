package space.cloud4b.verein.model.verein.kontrolle.rangliste;

import javafx.beans.property.*;
import space.cloud4b.verein.model.verein.adressbuch.Mitglied;

/**
 * Objekte der Klasse sind Positionen innerhalb der Rangliste, welche über alle Mitglieder und den Anteil
 * ihrer Anwesenheiten an den gesamten Vereinsterminen erstellt wird.
 *
 * @author Bernhard Kämpf & Serge Kaulitz
 * @version 2019-12
 */
public class Position {

    private Mitglied mitglied;
    private String mitgliedKurzbezeichnung;
    private int anzahlTermine;
    private int anzahlAnwesenheiten;
    private double anwesenheitsAnteil;
    private int rangYTD;

    public Position(Mitglied mitglied, String mitgliedKurzbezeichnung, int anzahlTermine, int anzahlAnwesenheiten, double anwesenheitsAnteil) {
        this.mitglied = mitglied;
        this.mitgliedKurzbezeichnung = mitgliedKurzbezeichnung;
        this.anzahlTermine = anzahlTermine;
        this.anzahlAnwesenheiten = anzahlAnwesenheiten;
        this.anwesenheitsAnteil = anwesenheitsAnteil;
        this.rangYTD = 9999;
    }

    /**
     * gibt den aktuellen Rang der Position zurück
     *
     * @return der aktuelle Rang der Position als SimpleIntegerProperty
     */
    public IntegerProperty getRangProperty() {
        return new SimpleIntegerProperty(this.rangYTD);
    }

    /**
     * gibt die Angaben zum Mitglied als Kurzbezeichnung zurück
     *
     * @return die Bezeichnung des Mitglieds
     */
    public StringProperty getKurzbezeichnungProperty() {
        return new SimpleStringProperty(this.mitgliedKurzbezeichnung);
    }

    /**
     * gibt die Anzahl der Vereinstermine für eine Position bzw. ein Mitglied zurück
     *
     * @return die Anzahl Termine
     */
    public IntegerProperty getAnzahlTermineProperty() {
        return new SimpleIntegerProperty(this.anzahlTermine);
    }

    /**
     * gibt die Anzahl der Termine zurück, an denen ein Mitglied effektiv anwesend war
     *
     * @return die Anzahl der Präsenztermine
     */
    public IntegerProperty getAnzahlAnwesenheitenProperty() {
        return new SimpleIntegerProperty(this.anzahlAnwesenheiten);
    }

    /**
     * Das Verhältnis der wahrgenommenen im Verhältnis zur Gesamtanzahl der Vereinstermine
     *
     * @return die Anwesenheiten in Prozent als SimpleDoubleProperty
     */
    public DoubleProperty getAnwesenheitsAnteilProperty() {
        return new SimpleDoubleProperty(this.anwesenheitsAnteil);
    }

    /**
     * Das Verhältnis der wahrgenommenen im Verhältnis zur Gesamtanzahl der Vereinstermine
     *
     * @return die Anwesenheiten in Prozent als double
     */
    public double getAnwesenheitsAnteil() {
        return this.anwesenheitsAnteil;
    }

    /**
     * setzt den Rang innerhalb der Rangliste
     *
     * @param rangYTD die Position (Rang) innerhalb der Rangliste
     */
    public void setRangYTD(int rangYTD) {
        this.rangYTD = rangYTD;
    }
}
