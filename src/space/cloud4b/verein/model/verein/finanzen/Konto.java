package space.cloud4b.verein.model.verein.finanzen;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import space.cloud4b.verein.services.DatabaseReader;

public class Konto {
    private int kontoId;
    private int kontoNummer;
    private String kontoBezeichnung;

    public Konto(int kontoNummer, Buchungsperiode periode) {
        System.out.println(">>>> neues Konto wird erstellt *** (" + kontoNummer + ")");
        this.kontoId = DatabaseReader.getKontoId(kontoNummer, periode);
        this.kontoNummer = kontoNummer;
        this.kontoBezeichnung = DatabaseReader.getKontobezeichnung(this.kontoNummer, periode);
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
}
