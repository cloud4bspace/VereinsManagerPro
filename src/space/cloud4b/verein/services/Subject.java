package space.cloud4b.verein.services;

/**
 * Interface wird von den Controller-Klassen auf der Model-Ebene implementiert und sorgt seinerseits
 * für die Implementierung er notwendigen Methoden.
 *
 * @author Bernhard Kämpf & Serge Kaulitz
 * @version 2019-11
 */
public interface Subject {

    /**
     * Methode fügt das übergebene Objekt zur Observer-Liste hinzu
     *
     * @param o das Objekt, welches in die Observerliste aufzunehmen ist
     */
    void Attach(Observer o);

    /**
     * Methode löscht ein Observer-Objekt aus der Observerliste
     *
     * @param o das zu entfernende Observer-Objekt
     */
    void Dettach(Observer o);


    /**
     * Methode durchläuft die in der Observerliste eingetragenen Klassen und ruft dort die
     * update-Methode auf.
     */
    void Notify();
}
