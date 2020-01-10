package space.cloud4b.verein.services;

public interface Subject {

    /**
     * Methode fügt das übergebene Objekt zur Observer-Liste hinzu
     */
    void Attach(Observer o);


    void Dettach(Observer o);


    /**
     * Methode durchläuft die in der Observerliste eingetragenen Klassen und ruft dort die
     * update-Methode auf.
     */
    void Notify();
}
