package space.cloud4b.verein.model.verein.kalender;

import java.time.LocalDate;

public class KalenderDatum {

    private LocalDate datum;
    private int kalenderWoche;
    private int positionInMatrix;

    public KalenderDatum(LocalDate datum, int kalenderWoche, int positionInMatrix) {
        this.datum = datum;
        this.positionInMatrix = positionInMatrix;
    }
}
