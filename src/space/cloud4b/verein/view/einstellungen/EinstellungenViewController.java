package space.cloud4b.verein.view.einstellungen;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import space.cloud4b.verein.MainApp;
import space.cloud4b.verein.einstellungen.Einstellung;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

/**
 * Controller zum JavaFX-UI EinstellungenView.fxml (Mandanteneinstellungen anzeigen und ändern)
 * Versorgt die FXML-Objekte (Felder und Tabellen) mit Daten und behandelt die Action-Events
 *
 * @author Bernhard Kämpf und Serge Kaulitz
 * @version 2020-01
 */
public class EinstellungenViewController {
    private MainApp mainApp;
    private Stage dialogStage;

    // UI-Variabeln (Verknüpfung mit Elementen des Userinterfaces)
    @FXML
    private TextField vereinsNameFeld;
    @FXML
    private ImageView clubLogoImage;
    @FXML
    private ImageView clubLogoImageSmall;
    @FXML
    private TextField dbHostFeld;
    @FXML
    private TextField dbPortFeld;
    @FXML
    private TextField dbUserFeld;
    @FXML
    private TextField dbDatabaseFeld;
    @FXML
    private PasswordField dbPasswortFeld;
    @FXML
    private VBox einstellungenVbox01;
    @FXML
    private Label feedbackLabel;

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        // feedbackTxt = new SimpleStringProperty("Bitte melde dich mit deinem Benutzernamen (E-Mail) und Passwort an..");
        vereinsNameFeld.setText(Einstellung.getVereinsName());
        dbUserFeld.setText(Einstellung.getdbUser());
        dbHostFeld.setText(Einstellung.getdbHost());
        dbPortFeld.setText(Einstellung.getdbPort());
        dbDatabaseFeld.setText(Einstellung.getdbDatenbank());
        dbPasswortFeld.setText(Einstellung.getdbPW());

        try (FileInputStream inputStream = new FileInputStream("ressources/images/logo/ClubLogo01.png");) {
            Image image = new Image(inputStream);
            clubLogoImage.setImage(image);
            clubLogoImageSmall.setImage(image);

        } catch (IOException e) {
        }
    }

    public void handleAbbrechen() {
        dialogStage.close();
    }

    /**
     * Wird ausgeführt, wenn der User den Button "speichern" betätigt.
     * Es erfolgt eine Gültigkeitsprüfung der eingegebenen Werte.
     * Wenn die Werte gültig sind, werden diese in den Properties gespeichert.
     */
    public void handleSpeichern() {
        if (isValid()) {
            Einstellung.setProperties(vereinsNameFeld.getText(), dbHostFeld.getText(), dbPortFeld.getText(),
                    dbDatabaseFeld.getText(), dbUserFeld.getText(), dbPasswortFeld.getText());
            feedbackLabel.setText("Änderungen gespeichert und wirksam nach nächstem Login");
        }
    }

    /**
     * Wird ausgeführt, wenn der User den Button betätigt, um das Logo zu ändern.
     * Speichert das übergebene File als neues Logo ab.
     */
    public void handelLogoButton() {
        File selectedFile = mainApp.chooseImageFile();
        if (selectedFile != null) {
            saveNeuesLogo(selectedFile);
            initialize();
        } else {
            System.out.println("File selection cancelled.");
        }
    }

    /**
     * speichert die übergebene Bilddatei als Profilbild des Users im dafür
     * vorgesehenen Ordner ab
     *
     * @param file das übergebene neue Logo-File
     */
    public void saveNeuesLogo(File file) {
        // die File-Extension des übergebenen Filenamens wird extrahiert
        Optional<String> ext = mainApp.getExtensionByStringHandling(file.getName());
        String extStr = ext.get();
        // die Pfade der Ursprungsdatei und der Zieldatei werden gelesen/generiert
        Path src = Paths.get(file.getAbsolutePath());
        Path dst = Paths.get("../VereinsManager/ressources/images/logo/ClubLogo01." + extStr);
        try {
            // das übergebene File wird mit dem neuen Namen in den Zielordner kopiert
            java.nio.file.Files.copy(
                    src, dst, StandardCopyOption.COPY_ATTRIBUTES,
                    StandardCopyOption.REPLACE_EXISTING
            );
        } catch (IOException e) {
            System.out.println("Neues Logo konnte nicht gespeicher twerden (" + e + ")");
        }
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public void setStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }


    /**
     * Überprüft die Gültigkeit der vom User eingegebenen Werte und gibt true zurück, wenn
     * alles i.O. ist. Ansonsten wird eine Fehlermeldung ausgegeben und false zurückgegeben.
     *
     * @return das Resultat der Überprüfung als boolean-Wert.
     */
    public boolean isValid() {
        boolean isValid = true;
        String errorMeldung = null;
        if (vereinsNameFeld.getText().length() < 1 || vereinsNameFeld.getText() == null) {
            isValid = false;
            errorMeldung = "- Vereinsname ist ungültig.";
        }
        if (dbHostFeld.getText().length() < 1 || vereinsNameFeld.getText() == null) {
            isValid = false;
            if (errorMeldung.length() > 0) {
                errorMeldung += "\n";}
            errorMeldung += "Host ist ungültig.";
        }

        if (dbPortFeld.getText().length() < 1 || dbPortFeld.getText() == null) {
            isValid = false;
            if(errorMeldung.length()>0) {errorMeldung += "\n";}
            errorMeldung += "Port ist ungültig.";
        } else {
            try {
                Integer.parseInt(dbPortFeld.getText());
            } catch (NumberFormatException e) {
                isValid = false;
                if(errorMeldung.length()>0) {errorMeldung += "\n";}
                errorMeldung += "Port muss eine Zahl sein!";
            }
        }

        if (dbUserFeld.getText().length() < 1 || dbUserFeld.getText() == null) {
            isValid = false;
            if(errorMeldung.length()>0) {errorMeldung += "\n";}
            errorMeldung += "User ist ungültig";
        }

        if (dbDatabaseFeld.getText().length() < 1 || dbDatabaseFeld.getText() == null) {
            isValid = false;
            if(errorMeldung.length()>0) {errorMeldung += "\n";}
            errorMeldung += "Datenbank ist ungültig.";
        }

        if (dbPasswortFeld.getText().length() < 1 || dbPasswortFeld.getText() == null) {
            isValid = false;
            if (errorMeldung.length() > 0) {
                errorMeldung += "\n";
            }
            errorMeldung += "Passwort ist ungültig.";
        }
        if (!isValid) {
            showWarning(errorMeldung);
        }
        return isValid;
    }

    /**
     * zeigt dem User eine Warnmeldung an mit der übergebenen Fehlermeldung
     *
     * @param errorMeldung die anzuzeigende Fehlermeldung
     */
    private void showWarning(String errorMeldung) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Ungültige Eingaben..");
        alert.setHeaderText("Folgende Eingaben sind ungültig:");
        //System.getProperty("user.name");
        alert.setContentText(errorMeldung);
        alert.showAndWait();
    }
}
