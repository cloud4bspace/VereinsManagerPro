package space.cloud4b.verein.model.verein.kontrolle.rangliste;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import space.cloud4b.verein.controller.MainController;
import space.cloud4b.verein.services.DatabaseReader;

import java.util.ArrayList;

public class Rangliste {

    private String bezeichnung;
    private ArrayList<Position> rangliste;

    private MainController mainController;


    public Rangliste(String bezeichnung, MainController mainController) {
        this.mainController = mainController;
        this.bezeichnung = bezeichnung;
        //this.rangliste = new ArrayList<>();
        System.out.println("neue Rangliste erstellt: " + this.bezeichnung);
        this.rangliste = DatabaseReader.fuelleRangliste(this.mainController);
    }


    public ObservableList<Position> getRanglistenListe() {
        return FXCollections.observableArrayList(rangliste);
    }

}
