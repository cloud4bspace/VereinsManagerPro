package space.cloud4b.verein.model.verein.kalender;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import space.cloud4b.verein.model.verein.status.StatusElement;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

/**
 * Die Objekte der Klasse Termin bildet die Basis für die Termine des Vereins.
 *
 * @author Bernhard Kämpf und Serge Kaulitz
 * @version 2019-12-17
 */
public class Termin {

    private int terminId;
    private LocalDate terminDatum;
    private LocalDateTime terminZeit;
    private LocalDateTime terminZeitBis;
    private StatusElement teilnehmerKatI;
    private StatusElement teilnehmerKatII;
    private String terminOrt;
    private String terminText;
    private String terminDetails;
    private String zeitText;
    private int projektId;
    private String trackChangeUsr;
    private Timestamp trackChangeTimestamp;

    public Termin(int terminId, LocalDate terminDatum, String terminText, int projektId) {
        this.terminId = terminId;
        this.terminDatum = terminDatum;
        this.terminText = terminText;
        this.projektId = projektId;
    }

    public Termin(int terminId, LocalDate terminDatum, String terminText, String terminOrt) {
        this.terminId = terminId;
        this.terminDatum = terminDatum;
        this.terminText = terminText;
        this.terminOrt = terminOrt;
    }

    public String toString() {
        String string = getDateAsNiceString().getValue() + " " + this.terminText;
        if (this.terminZeit != null) {
            string += " | " + terminZeit.format(DateTimeFormatter.ofPattern("HH:mm"));
        }
        if (this.terminZeitBis != null) {
            string += "-" + terminZeitBis.format(DateTimeFormatter.ofPattern("HH:mm"));
        }
        return string;
    }

    // Termin-Id
    public int getTerminId() {
        return this.terminId;
    }

    // Termindatum
    public void setDatum(LocalDate datum) {
        this.terminDatum = datum;
    }

    public LocalDate getDatum() {
        return terminDatum;
    }

    public StringProperty getDateAsLocalStringMedium() {
        return new SimpleStringProperty(terminDatum.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));
    }

    public StringProperty getDateAsLocalStringLong() {
        return new SimpleStringProperty(terminDatum.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)));
    }

    public StringProperty getDateAsNiceString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E. dd.MM.yyyy");
        return new SimpleStringProperty(terminDatum.format(formatter));
    }

    public ObjectProperty<LocalDate> getDatumProperty() {
        return new SimpleObjectProperty<LocalDate>(this.terminDatum);
    }

    // Zeit von
    public void setZeit(LocalDateTime terminZeit) {
        this.terminZeit = terminZeit;
        if (terminZeit != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            this.zeitText = terminZeit.format(formatter);
            if (this.terminZeitBis != null) {
                this.zeitText += ":" + terminZeitBis.format(formatter);
            }
        } else {
            this.zeitText = null;
        }
    }

    public LocalDateTime getTerminZeitVon(){ return this.terminZeit; }

    // Zeit bis
    public void setZeitBis(LocalDateTime terminZeitBis) {
        this.terminZeitBis = terminZeitBis;
        if (terminZeitBis != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            this.zeitText = terminZeit.format(formatter) + "-" + terminZeitBis.format(formatter);
        } else {
            this.zeitText = null;
        }
    }

    public LocalDateTime getTerminZeitBis() {
        return this.terminZeitBis;
    }

    // Zeitangabe als Text
    public String getZeitText() {
        return zeitText;
    }

    public StringProperty getZeitTextProperty() {
        return new SimpleStringProperty(zeitText);
    }

    // Text
    public StringProperty getTerminText() {
        return new SimpleStringProperty(terminText);
    }

    public ObservableValue<String> getTextProperty() {
        return new SimpleStringProperty(this.terminText);
    }

    public void setTerminText(String terminText) {
        this.terminText = terminText;
    }

    // Teilnehmer Kat A/B / Kat I/II
    public void setTeilnehmerKatI(StatusElement teilnehmerKatI) {
        this.teilnehmerKatI = teilnehmerKatI;
    }

    public void setTeilnehmerKatII(StatusElement teilnehmerKatII) { this.teilnehmerKatII = teilnehmerKatII; }

    public StatusElement getKatIElement() { return teilnehmerKatI; }

    public StatusElement getKatIIElement() { return teilnehmerKatII; }

    // Ort
    public void setOrt(String terminOrt) { this.terminOrt = terminOrt; }

    public String getOrt() { return this.terminOrt; }

    public ObservableValue<String> getOrtProperty() {
        return new SimpleStringProperty(this.terminOrt);
    }

    // Details
    public void setDetails(String terminDetails) {
        this.terminDetails = terminDetails;
    }

    public String getDetails() {
        return this.terminDetails;
    }

    public ObservableValue<String> getDetailsProperty() {
        return new SimpleStringProperty(this.terminDetails);
    }

    // TrackChanges
    public void setTrackChangeUsr(String trackChangeUsr) {
        this.trackChangeUsr = trackChangeUsr;
    }

    public String getTrackChangeUsr() {
        return this.trackChangeUsr;
    }

    public void setTrackChangeTimestamp(Timestamp trackChangeTimestamp) {
        this.trackChangeTimestamp = trackChangeTimestamp;
    }

    public String getLetzteAenderung() {
        return "letzte Änderung: " + trackChangeTimestamp + " (" + trackChangeUsr + ")";
    }

    // Kalender Output
    public String getKalenderText() {
        if (this.zeitText != null) {
            return this.terminText + "\n" + this.zeitText;
        } else {
            return this.terminText;
        }
    }


    public int getProjektId() {
        return projektId;
    }
}
