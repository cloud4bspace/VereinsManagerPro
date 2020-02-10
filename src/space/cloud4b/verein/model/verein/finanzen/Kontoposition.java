package space.cloud4b.verein.model.verein.finanzen;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;

public class Kontoposition {
    private Konto konto;
    private Belegkopf belegkopf;
    private Belegposition belegposition;
    private double saldo;

    public Kontoposition(Belegkopf belegkopf, Belegposition belegposition, Konto konto, double saldo) {
        this.belegposition = belegposition;
        this.konto = konto;
        this.belegkopf = belegkopf;
        this.saldo = saldo;
    }

    public Belegkopf getBelegkopf(){
        return belegkopf;
    }

    public Belegposition getBelegposition() {
        return belegposition;
    }

    public Konto getKonto() {
        return konto;
    }

    public ObservableValue<String> getSaldo() {
       // NumberFormat formatter = NumberFormat.getInstance(new Locale("de-CH"));
      //  return new SimpleStringProperty(formatter.format(saldo));
        return new SimpleStringProperty(String.format("%,.2f", this.saldo));
    }
}
