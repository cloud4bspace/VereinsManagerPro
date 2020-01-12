package space.cloud4b.verein.model.verein.adressbuch;

import javafx.beans.property.*;
import space.cloud4b.verein.model.verein.status.StatusElement;

import java.sql.Timestamp;
import java.time.LocalDate;

/**
 * Die Objekte der Klasse Kontakt bilden die Basis für die Adress- bzw. Mitgliederliste.
 * Sie wird erweitert durch die Klasse Mitglieder, welche zusätzliche, mitgliederspezifische
 * Daten bereitstellt.
 *
 * @author Bernhard Kämpf und Serge Kaulitz
 * @version 2019-12-17
 */
public class Kontakt {

    private int kontaktId;
    private String nachName;
    private String vorName;
    private String adresse;
    private String adresszusatz;
    private int plz;
    private String ort;
    private LocalDate geburtsdatum;
    private StatusElement anredeStatus;
    private String bemerkungen;
    private String mobile;
    private String telefon;
    private String eMail;
    private String eMailII;
    private LocalDate austrittsDatum;
    private String letzteAenderungUser;
    private Timestamp letzteAenderungTimestamp;

    public Kontakt(int kontaktId, String nachName, String vorName) {
        this.kontaktId = kontaktId;
        this.nachName = nachName;
        this.vorName = vorName;
    }

    public String toString() {
        String string = "#" + kontaktId + ": " + nachName + " " + vorName;
        if (this.plz > 0) {
            string += " | " + plz + " " + ort;
        }
        return string;
    }

    /**
     * Gibt die Kurzbezeichnung des Kontakts zurück
     * @return Kurzbezeichnung des Kontakts
     */
    public String getKurzbezeichnung() {
        return this.nachName + " " + this.vorName;
    }

    /**
     * Gibt die Kurzbezeichnung des Kontakts als SimpleStringProperty zurück
     *
     * @return Kurzbezeichnung des Kontakts als SimpleStringProperty zurück
     */
    public SimpleStringProperty getKurzbezeichnungProperty() {
        return new SimpleStringProperty(nachName + " " + vorName);
    }

    /**
     * gibt die KontaktId als SimpleIntegerProperty zurück
     *
     * @return kontaktId als SimpleIntegerProperty
     */
    public IntegerProperty getIdProperty() {
        return new SimpleIntegerProperty(kontaktId);
    }

    /**
     * gibt die KontaktId als int zurück
     *
     * @return die KontaktId als int
     */
    public int getId() {
        return kontaktId;
    }


    /**
     * setzt den übergebenen Anrede-Status
     *
     * @param anredeStatus der Status der Anrede
     */
    public void setAnredeStatus(StatusElement anredeStatus) {
        this.anredeStatus = anredeStatus;
    }

    /**
     * gibt die Anrede als StatusElement zurück
     *
     * @return die Anrede als Statuselement
     */
    public StatusElement getAnredeElement() {
        return anredeStatus;
    }

    /**
     * gibt die Anrede als ObjektProperty zurück
     *
     * @return StatusElemnt der Anrede als ObjectProperty
     */
    public ObjectProperty<StatusElement> getAnredeProperty() {
        return new SimpleObjectProperty<StatusElement>(this.anredeStatus);
    }

    /**
     * gibt den Nachnamen als NachnameProperty zurück
     *
     * @return der Nachname als StringProperty
     */
    public StringProperty getNachnameProperty() {
        return new SimpleStringProperty(nachName);
    }

    /**
     * gibt den Nachnamen als String zurück
     *
     * @return der Nachname als String
     */
    public String getNachName() {
        return this.nachName;
    }

    /**
     * setzt den übergebenen Nachnamen
     *
     * @param nachname der Nachname des Kontakts
     */
    public void setNachName(String nachname) {
        this.nachName = nachname;
    }

    /**
     * gibt den Vornamen als String zurück
     *
     * @param vorName der Vorname als String
     */
    public void setVorName(String vorName) {
        this.vorName = vorName;
    }

    /**
     * gibt den Vornamen als StringProperty zurück
     *
     * @return Vorname als StringProperty
     */
    public StringProperty getVornameProperty() {
        return new SimpleStringProperty(vorName);
    }

    /**
     * gibt den Vornamen als String zurück
     *
     * @return der Vorname als String
     */
    public String getVorname() {
        return this.vorName;
    }

    /**
     * gibt die Adresse als String zurück
     *
     * @return die Adresse als String
     */
    public String getAdresse() {
        return this.adresse;
    }

    /**
     * setzt die übergebene Adresse (Strasse)
     *
     * @param adresse die Adresse (Strasse)
     */
    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    /**
     * gibt den Adresszusatz (z.B. Postfach) als String zurück
     *
     * @return die Adresse als String
     */
    public String getAdresszusatz() {
        return this.adresszusatz;
    }

    /**
     * setzt den Adresszusatz
     *
     * @param adresszusatz der übergebene Adresszusatz
     */
    public void setAdresszusatz(String adresszusatz) {
        this.adresszusatz = adresszusatz;
    }

    /**
     * gibt die Postleitzahl als int zurück
     *
     * @return die Postleitzahl als int
     */
    public int getPlz() {
        return this.plz;
    }

    /**
     * setzt die übergebene PLZ
     *
     * @param plz
     */
    public void setPlz(int plz) {
        this.plz = plz;
    }

    /**
     * gibt den Ort (Wohnort) als String zurück
     *
     * @return Wohnort als String
     */
    public String getOrt() {
        return this.ort;
    }

    /**
     * setzt den übergebenen Ort
     *
     * @param ort der Ort
     */
    public void setOrt(String ort) {
        this.ort = ort;
    }

    /**
     * gibt den Ort als StringProperty zurück
     *
     * @return Ort als StringProperty
     */
    public StringProperty getOrtProperty() {
        return new SimpleStringProperty(ort);
    }

    /**
     * gibt die Bemerkungen als String zurück
     *
     * @return die Bemerkungen als String
     */
    public String getBemerkungen() {
        return this.bemerkungen;
    }

    /**
     * setzt die übergebenen Bemerkungen
     *
     * @param bemerkungen die übergebenen Bemerkungen
     */
    public void setBemerkungen(String bemerkungen) {
        this.bemerkungen = bemerkungen;
    }

    /**
     * gibt die Mobile-Telefonnummer als String zurück
     *
     * @return die Mobiltelefon-Nummer
     */
    public String getMobile() {
        return this.mobile;
    }

    /**
     * setzt die Mobile-Telefonnummer
     *
     * @param mobile die Mobilfunk-Telefonnummer
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * gibt die Mobile-Telefonnummer als StringProperty zurück
     *
     * @return
     */
    public StringProperty getMobileProperty() {
        return new SimpleStringProperty(mobile);
    }

    /**
     * gibt die Telefonnummer als String zurück
     *
     * @return die Telefonnummer
     */
    public String getTelefon() {
        return this.telefon;
    }

    /**
     * setzt die Telefonnummer
     *
     * @param telefon die Telefonnummer des Kontakts
     */
    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    /**
     * gibt die Telefonnummer als String zurück
     *
     * @return Telefonnummer als StringProperty
     */
    public StringProperty getTelefonProperty() {
        return new SimpleStringProperty(telefon);
    }

    /**
     * gibt die E-Mail-Adresse als String zurück
     *
     * @return E-Mail-Adresse als String
     */
    public String getEmail() {
        return this.eMail;
    }

    /**
     * setzt die übergebene E-Mail-Adresse
     *
     * @param eMail die zu setzende E-Mail-Adresse
     */
    public void setEmail(String eMail) {
        this.eMail = eMail;
    }

    /**
     * gibt die E-Mail-Adresse als StringProperty zurück
     *
     * @return die E-Mail-Adresse als StringProperty
     */
    public StringProperty getEmailProperty() {
        return new SimpleStringProperty(eMail);
    }

    /**
     * gibt die alternative E-Mail-Adresse als String zurück
     *
     * @return die alternative E-Mail-Adresse
     */
    public String getEmailII() {
        return this.eMailII;
    }

    /**
     * setzt die alternative E-Mail-Adresse
     *
     * @param eMailII die alternative E-Mail-Adresse
     */
    public void setEmailII(String eMailII) {
        this.eMailII = eMailII;
    }

    /**
     * gibt die alternative E-Mail-Adresse als StringProperty zurück
     *
     * @return die alternative E-Mail-Adresse als StringProperty
     */
    public StringProperty getEmailIIProperty() {
        return new SimpleStringProperty(eMailII);
    }

    /**
     * gibt das Geburtstag als LocalDate zurück
     *
     * @return das Geburtsdatum als LocalDate
     */
    public LocalDate getGeburtsdatum() {
        return this.geburtsdatum;
    }

    /**
     * setht das Geburtsdatum
     *
     * @param geburtsdatum das Geburtsdatum
     */
    public void setGeburtsdatum(LocalDate geburtsdatum) {
        this.geburtsdatum = geburtsdatum;
    }

    /**
     * gibt das Austrittsdatum als LocalDate zurück
     *
     * @return
     */
    public LocalDate getAustrittsDatum() {
        return this.austrittsDatum;
    }

    /**
     * setzt das Austrittsdatum
     *
     * @param austrittsDatum das Austrittsdatum
     */
    public void setAustrittsDatum(LocalDate austrittsDatum) {
        this.austrittsDatum = austrittsDatum;
    }

    /**
     * setzt den Zeitstempel der letzten Änderung
     *
     * @param letzteAenderungUser der Zeitstempel der letzten Änderung
     */
    public void setLetzteAenderungUser(String letzteAenderungUser) {
        this.letzteAenderungUser = letzteAenderungUser;
    }

    /**
     * setzt den Zeitstempel der letzten Änderung
     *
     * @param letzteAenderungTimestamp der Zeitstempel der letzten Änderung
     */
    public void setLetzteAenderungTimestamp(Timestamp letzteAenderungTimestamp) {
        this.letzteAenderungTimestamp = letzteAenderungTimestamp;
    }

    /**
     * gibt die letzte Änderung als String zurück
     *
     * @return die letzte Änderung als String
     */
    public String getLetzteAenderung() {
        return "letzte Änderung: " + letzteAenderungTimestamp + " (" + letzteAenderungUser + ")";
    }
}


