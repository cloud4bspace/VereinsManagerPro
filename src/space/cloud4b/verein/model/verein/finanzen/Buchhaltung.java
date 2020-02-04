package space.cloud4b.verein.model.verein.finanzen;

import space.cloud4b.verein.services.DatabaseReader;

import java.time.Year;
import java.util.ArrayList;

public class Buchhaltung {
    ArrayList<Buchungsperiode> buchungsPerioden;

    public Buchhaltung(){
        System.out.println("> neue Buchhaltung wird erstellt **");
        buchungsPerioden = new ArrayList<>();
        setupBuchungsperioden();

    }

    public void setupBuchungsperioden() {
        ArrayList<Year> jahre = DatabaseReader.getBuchungsperioden();
        for(Year jahr : jahre) {
            buchungsPerioden.add(new Buchungsperiode(jahr));
        }
    }

    public Buchungsperiode getBuchungsperiode(int jahr) {
        for(Buchungsperiode buchungsPeriode : buchungsPerioden) {
            if(buchungsPeriode.getJahr() == jahr) {
                return buchungsPeriode;
            }
        }
        return null;
    }


}
