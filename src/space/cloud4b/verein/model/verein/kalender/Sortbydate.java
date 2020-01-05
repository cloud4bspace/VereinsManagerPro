package space.cloud4b.verein.model.verein.kalender;

import java.time.Period;
import java.util.Comparator;


/**
 * Die Klasse dient dazu, die Jubiläumsliste nach Datum zu sortieren
 *
 * @author Bernhard Kämpf und Serge Kaulitz
 * @version 2019-12-17
 * @see space.cloud4b.verein.controller.KalenderController
 */
public class Sortbydate implements Comparator<Jubilaeum> {
    public int compare(Jubilaeum a, Jubilaeum b) {
        return Period.between(b.getDatum(), a.getDatum()).getDays();
    }
}

