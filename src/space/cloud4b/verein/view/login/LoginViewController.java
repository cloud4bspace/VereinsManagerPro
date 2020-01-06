package space.cloud4b.verein.view.login;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import space.cloud4b.verein.MainApp;
import space.cloud4b.verein.model.verein.user.User;
import space.cloud4b.verein.services.DatabaseOperation;
import space.cloud4b.verein.services.DatabaseReader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Die Klasse LoginViewController ist verknüpft mit dem JavaFX-Unserinterface LoginView.fxml
 * Sie ermöglicht einem registrierten Benutzer/User, sich anzumelden und die Applikation zu starten.
 *
 * @author Bernhard Kämpf und Serge Kaulitz
 * @version 2019-01-03
 */
public class LoginViewController {

    // allgemeine Instanzvariabeln
    private MainApp mainApp;
    private Stage dialogStage;

    // UI-Variabeln (Verknüpfung mit Elementen des Userinterfaces)
    @FXML
    private PasswordField pwFeld;
    @FXML
    private TextField userNameFeld;
    @FXML
    private Label feedbackLabel;
    @FXML
    private Label loginTitleLabel;
    @FXML
    private ImageView clubLogoImage;

    /**
     * Standardkonstruktor (wird nicht benötigt)
     */
    public LoginViewController() {
    }

    /**
     * Initialisieirung der Controller-Klasse. Diese Methode wird automatisch aufgerufen,
     * nachdem das fxml-File geladen wurde.
     */
    @FXML
    private void initialize() {
        feedbackLabel.setText("Bitte melde dich mit deinem Benutzernamen (E-Mail) und Passwort an..");
        loginTitleLabel.setText("Login VereinsManager");
        try {
            FileInputStream inputStream = new FileInputStream("ressources/images/logo/ClubLogo01.png");
            Image image = new Image(inputStream);
            clubLogoImage.setImage(image);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * setzt die Referenz zur MainApp
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    /**
     * Setzt die Referenz zum aktuellen Stage
     *
     * @param dialogStage die Stage-Referenz des UI
     */
    public void setStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * Überprüft beim Drücken des Login-Buttons die User-Eingaben und gibt true zurück,
     * wenn der User sich korrekt angemeldet hat
     */
    public void handleLogin() {
        /* Die User-Eingaben werden mit der Datenbank abgeglichen und wenn Username und Passwort korrekt sind,
           wird true zurückgegeben
        */
        if (DatabaseReader.checkUserCredentials(userNameFeld.getText(), pwFeld.getText())) {

            // die Mitglieder-Id wird aus der Datenbank ermittelt
            int mitgliedId = DatabaseReader.getMitgliedId(userNameFeld.getText());

            // der Klasse MainApp wird ein neuer User übergeben
            mainApp.setUser(new User(mitgliedId, DatabaseReader.getUserNameVorname(mitgliedId),
                    DatabaseReader.getMitgliederKategorie(mitgliedId), DatabaseReader.getVorstandsStatus(mitgliedId)));

            // der Zähler für die Anzahl Zugriffe wird beim User hochgezählt
            DatabaseOperation.incrementLoginCounter(mitgliedId);

            // TODO: Feld wird nicht aktualisiert...
            feedbackLabel.setText("Login erfolgreich - Applikation wird gestartet");
            dialogStage.close();
            mainApp.start(dialogStage);
        } else {
            feedbackLabel.setText("Login fehlgeschlagen");
            feedbackLabel.setStyle("-fx-text-fill: red;");
        }
    }

    /**
     * Methode wird ausgeführt, wenn der User den Button anklickt, um sich neu zu registrieren.
     * Das aktuelle UI wird geschlossen und das UI für die Registrierung als neuer Benutzer
     * wird angezeigt.
     */
    public void handleSignInButton() {
        dialogStage.close();
        mainApp.showSignupView();
    }
}
