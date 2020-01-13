package space.cloud4b.verein.model.verein.meldung;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Objekte der Klasse Meldung dienen dazu, dem User auf dem UI-MainFrame.xmls
 * Rückmeldungen auszugeben. Entsprechende Meldungen werden im UI in einer ListView im
 * linken Fensterbereich dargestellt.
 *
 * @author Bernhard Kämpf und Serge Kaulitz
 * @version 2019-01-03
 */
public class Meldung {

    private String meldungText;
    private Timestamp meldungTimestamp;
    private String meldungType;

    public Meldung(String meldungText, String meldungType) {
        this.meldungText = meldungText;
        this.meldungType = meldungType;
        this.meldungTimestamp = new Timestamp(new Date().getTime());
    }

    public String getMeldungType() {
        return this.meldungType;
    }

    /**
     * Gibt für die Ausgabe der Meldung in der ListView des UI-MainFrame.xmls
     * den Inhalt der Meldung als formatierten String zurück (inkl. Timestamp)
     *
     * @return Inhalt der Meldung als formatierter String (inkl. Timestamp)
     */
    public String getMeldungOutputString() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return "»» " + sdf.format(this.meldungTimestamp) + "\n" + meldungText;
    }
}
