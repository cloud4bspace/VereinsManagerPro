package space.cloud4b.verein.model.verein.finanzen;

import space.cloud4b.verein.services.DatabaseReader;

import java.time.Year;
import java.util.ArrayList;

public class Buchungsperiode {

    private Year periode;
    private ArrayList<Belegkopf> hauptJournal;
    private ArrayList<Konto> kontenPlan;

    public Buchungsperiode(Year periode) {
        System.out.println(">> neue Buchungsperiode wird erstellt *** (" + periode + ")");
        this.periode = periode;
        hauptJournal = new ArrayList<>();
        kontenPlan = new ArrayList<>();
        fillBelegliste();
        fillKontenPlan();
    }

    public String toString() {
        return "Periode " + periode.getValue();
    }
    public void fillBelegliste() {
        this.hauptJournal = DatabaseReader.getBelegliste(this);
    }

    public void fillKontenPlan() {
        this.kontenPlan = DatabaseReader.getKontenplan(this);
    }

    public int getJahr() {
        return this.periode.getValue();
    }

    public void listHauptjournal() {
        for(Belegkopf belegkopf : hauptJournal) {
            System.out.println(belegkopf.toString());
        }
    }


    public ArrayList<Belegkopf> getHauptjournal() {
        return hauptJournal;
    }

    public ArrayList<Konto> getKontenplan() {
        return this.kontenPlan;
    }
}
