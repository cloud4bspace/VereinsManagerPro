package space.cloud4b.verein.model.verein.finanzen;

import space.cloud4b.verein.services.DatabaseReader;

import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;

public class Buchungsperiode {

    private Year periode;
    private ArrayList<Belegkopf> hauptJournal;
    private HashMap<Integer, Konto> kontenPlanHashMap;
    private ArrayList<Konto> kontenPlan;

    public Buchungsperiode(Year periode) {
        System.out.println(">> neue Buchungsperiode wird erstellt *** (" + periode + ")");
        this.periode = periode;
        hauptJournal = new ArrayList<>();
        kontenPlan = new ArrayList<>();
        fillKontenPlan();
        fillBelegliste();
    }

    public String toString() {
        return "Periode " + periode.getValue();
    }
    public void fillBelegliste() {
        this.hauptJournal = DatabaseReader.getBelegliste(this, kontenPlanHashMap);
    }

    public void fillKontenPlan() {
        this.kontenPlan = DatabaseReader.getKontenplan(this);
        kontenPlanHashMap = new HashMap<>();
        for(Konto konto : kontenPlan) {
            kontenPlanHashMap.put(konto.getKontoNummer(), konto);
        }
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

    public HashMap<Integer, Konto> getKontenPlanHashMap() {
        return this.kontenPlanHashMap;
    }

}
