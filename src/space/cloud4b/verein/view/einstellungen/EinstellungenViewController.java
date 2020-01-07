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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

public class EinstellungenViewController {
    MainApp mainApp;
    Stage dialogStage;

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

        try {
            FileInputStream inputStream = new FileInputStream("ressources/images/logo/ClubLogo01.png");
            Image image = new Image(inputStream);
            clubLogoImage.setImage(image);
            clubLogoImageSmall.setImage(image);
        } catch (FileNotFoundException e) {
        }
    }

    public void handleAbbrechen() {
        dialogStage.close();
    }

    public void handleSpeichern() {
        if(isValid()) {
            Einstellung.setProperties(vereinsNameFeld.getText(), dbHostFeld.getText(), dbPortFeld.getText(),
                    dbDatabaseFeld.getText(), dbUserFeld.getText(), dbPasswortFeld.getText());

            feedbackLabel.setText("Änderungen gespeichert und wirksam nach nächstem Login");

        }
    }

    public void handelLogoButton() {
        File selectedFile = mainApp.getMainController().chooseImageFile();
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
     * @param file
     */
    public void saveNeuesLogo(File file) {
        // TODO Bild kann jpg oder png sein... Entweder umwandeln oder dann immer beim Auslesen des Bilds beide Varianten in Betracht ziehen..
        // die File-Extension des übergebenen Filenamens wird extrahiert
        Optional<String> ext = mainApp.getMainController().getExtensionByStringHandling(file.getName());
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

    public boolean isValid() {
        boolean isValid = true;
        String errorMeldung = null;
        if (vereinsNameFeld.getText().length() < 1 || vereinsNameFeld.getText() == null) {
            isValid = false;
            errorMeldung = "- Vereinsname ist ungültig.";
        }
        if (dbHostFeld.getText().length() < 1 || vereinsNameFeld.getText() == null) {
            isValid = false;
            if(errorMeldung.length()>0) {errorMeldung += "\n";}
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
            if(errorMeldung.length()>0) {errorMeldung += "\n";}
            errorMeldung += "Passwort ist ungültig.";
        }
        if(!isValid) { showWarning(errorMeldung);}
        return isValid;
    }

    private void showWarning(String errorMeldung) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Ungültige Eingaben..");
        alert.setHeaderText("Folgende Eingaben sind ungültig:");
        //System.getProperty("user.name");
        alert.setContentText(errorMeldung);
        alert.showAndWait();
    }
}
