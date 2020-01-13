package space.cloud4b.verein.services;

/**
 * Interface kann von Observer-Klassen implementiert werden und stellt wiederum die
 * Implementierung der update-Methode sicher.
 */
public interface Observer {

    void update(Object o);
}
