package space.cloud4b.verein.model.verein.projekt;

import javafx.beans.property.*;
import space.cloud4b.verein.model.verein.kalender.Termin;
import space.cloud4b.verein.model.verein.status.StatusElement;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Die Objekte der Klasse Projekt repräsentieren Projekte und Events
 *
 * @author Bernhard Kämpf
 * @version 2020-04-04
 */
public class Projekt {

    private int projektId;
    private ArrayList<Termin> projektTermine;

    private String projektTitel;
    private String projektText;
    private LocalDate projektStart;
    private LocalDate projektEnde;
    private ArrayList<Termin> terminListe;
    private StatusElement projektKategorie;
    private StatusElement projektPhase;
    private String trackChangeUsr;
    private Timestamp trackChangeTimestamp;

    public Projekt(int projektId, String projektTitel, String projektText, LocalDate projektStart, LocalDate projektEnde
            , StatusElement projektKategorie, StatusElement projektPhase, ArrayList<Termin> terminListe) {
        this.projektId = projektId;
        this.projektTitel = projektTitel;
        this.projektText = projektText;
        this.projektStart = projektStart;
        this.projektEnde = projektEnde;
        this.projektKategorie = projektKategorie;
        this.projektPhase = projektPhase;
        this.terminListe = terminListe;
    }

    // Projekt-Id
    public int getProjektId() {
        return this.projektId;
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

    public IntegerProperty getProjektIdProperty() {
        return new SimpleIntegerProperty(projektId);
    }

    public StringProperty getTitelProperty() {
        return new SimpleStringProperty(projektTitel);
    }

    public StringProperty getProjektDetailsProperty() {
        return new SimpleStringProperty(projektText);
    }

    public ObjectProperty<LocalDate> getProjektStartDatumProperty() {
        return new SimpleObjectProperty<>(projektStart);
    }

    public ObjectProperty<LocalDate> getProjektEndeDatumProperty() {
        return new SimpleObjectProperty<>(projektEnde);
    }

    public ObjectProperty<StatusElement> getProjektPhaseProperty() {
        return new SimpleObjectProperty<>(projektPhase);
    }

    public String getDetailsText() {
        return projektText;
    }

    public ObjectProperty<StatusElement> getProjektKategorieProperty() {
        return new SimpleObjectProperty<>(projektKategorie);
    }
}
