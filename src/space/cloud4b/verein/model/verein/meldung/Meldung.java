package space.cloud4b.verein.model.verein.meldung;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    public String getMeldungOutputString() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        System.out.println("Meldung: " + this.meldungTimestamp + " " + meldungText);
        return "»» " + sdf.format(this.meldungTimestamp) + "\n" + meldungText;
    }
}
