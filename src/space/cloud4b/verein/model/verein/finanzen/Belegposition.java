package space.cloud4b.verein.model.verein.finanzen;

public class Belegposition {
    private int belegPositionId;
    private int positionNummer;
    private char sollHaben;
    private Konto konto;
    private Betrag betrag;
    private String positionsText;

    public Belegposition(int belegPositionId, int positionNummer, char sollHaben, Konto konto, Betrag betrag
            , String positionsText) {
        System.out.println(">>>> neue Belegposition wird erstellt ***");
        this.belegPositionId = belegPositionId;
        this.positionNummer = positionNummer;
        this.sollHaben = sollHaben;
        this.betrag = betrag;
        this.positionsText = positionsText;
        this.konto = konto;
    }
    public String toHauptjournalString() {
        return "#" + positionNummer + " " + sollHaben + " " + konto.toString()
                + " " + betrag.getBetragToShortString() + " " + this.positionsText;
    }
    public String toString() {
            return "#" + positionNummer + " " + sollHaben + " " + konto.toString() + " " + betrag.toString();
    }
}
