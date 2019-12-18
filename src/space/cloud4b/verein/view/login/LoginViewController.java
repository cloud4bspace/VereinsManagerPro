package space.cloud4b.verein.view.login;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import space.cloud4b.verein.MainApp;
import space.cloud4b.verein.model.verein.user.User;
import space.cloud4b.verein.services.DatabaseOperation;
import space.cloud4b.verein.services.DatabaseReader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class LoginViewController {
    private MainApp mainApp;
    private Stage dialogStage;

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
    @FXML
    private VBox loginVbox01;

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        // feedbackTxt = new SimpleStringProperty("Bitte melde dich mit deinem Benutzernamen (E-Mail) und Passwort an..");
        feedbackLabel.setText("Bitte melde dich mit deinem Benutzernamen (E-Mail) und Passwort an..");
        loginTitleLabel.setText("Login VereinsManager");
        try {
            FileInputStream inputStream = new FileInputStream("ressources/images/logo/ClubLogo01.png");
            Image image = new Image(inputStream);
            clubLogoImage.setImage(image);
        } catch (FileNotFoundException e) {
        }
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;


    }
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
            feedbackLabel.setText("Applikation wird geladen...");
            // der Zähler für die Anzahl Zugriffe wird beim User hochgezählt
            DatabaseOperation.incrementLoginCounter(mitgliedId);
            dialogStage.close();
            mainApp.start(dialogStage);

        } else {
            System.out.println("Login fehlgeschlagen");
            feedbackLabel.setText("Login fehlgeschlagen");
            feedbackLabel.setStyle("-fx-text-fill: red;");
        }
    }


    /**
     * handleSignInButton()
     */
    public void handleSignInButton() {
        dialogStage.close();
        mainApp.showSignupView();
    }


}
