package space.cloud4b.verein.view.login;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import space.cloud4b.verein.MainApp;
import space.cloud4b.verein.services.DatabaseOperation;
import space.cloud4b.verein.services.DatabaseReader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Die Klasse SignupViewController ist verknüpft mit dem JavaFX-Unserinterface SignupView.fxml
 * Sie ermöglicht es einem User, sich mit seiner E-Mail-Adresse und einem Passwort
 * als neuer Benutzer anzumelden.
 *
 * @author Bernhard Kämpf und Serge Kaulitz
 * @version 2019-01-03
 */
public class SignupViewController {

    // allgemeine Instanzvariabeln
    private MainApp mainApp;
    private Stage dialogStage;

    // UI-Variabeln (Verknüpfung mit Elementen des Userinterfaces)
    @FXML
    private PasswordField pwFeld;
    @FXML
    private PasswordField pwCheckFeld;
    @FXML
    private TextField userNameFeld;
    @FXML
    private Label feedbackLabel;
    @FXML
    private Button saveButton;
    @FXML
    private Button gotoLoginButton;
    @FXML
    private Button goBackButton;
    @FXML
    private ImageView clubLogoImage;


    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public void setStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * Initialisieirung der Controller-Klasse. Diese Methode wird automatisch aufgerufen,
     * nachdem das fxml-File geladen wurde.
     */
    @FXML
    private void initialize() {
        saveButton.setVisible(true);
        gotoLoginButton.setVisible(false);
        try {
            FileInputStream inputStream = new FileInputStream("ressources/images/logo/ClubLogo01.png");
            Image image = new Image(inputStream);
            clubLogoImage.setImage(image);
        } catch (FileNotFoundException e) {
        }
    }

    public void handleGotoLogin() {
        dialogStage.close();
        mainApp.showLoginView();
    }

    public void handleBackToLogin() {
        dialogStage.close();
        mainApp.showLoginView();
    }

    public void handleSignIn() {
        if (pwFeld.getText().length() < 8 || !pwFeld.getText().equals(pwCheckFeld.getText())) {
            feedbackLabel.setText("Passwörter nicht identisch oder zu kurz (mind. 8 Zeichen)");
            feedbackLabel.setStyle("-fx-text-fill: white;");
        } else {
            if (DatabaseReader.isMitgliedEmail(userNameFeld.getText()) > 0) {
                // es handelt sich um die E-Mail eines bekannten Kontakts (bereits in der MYSQL-Tabelle vorhanden)
                if (!DatabaseReader.isUser(DatabaseReader.isMitgliedEmail(userNameFeld.getText()))) {
                    // der User existiert noch nicht als Benutzer in der Datenbank
                    DatabaseOperation.saveUserCredentials(userNameFeld.getText(), pwFeld.getText());
                    feedbackLabel.setText("Der User wurde gespeichert");
                    feedbackLabel.setStyle("-fx-text-fill: green;");
                    saveButton.setVisible(false);
                    goBackButton.setVisible(false);
                    gotoLoginButton.setVisible(true);
                } else {
                    feedbackLabel.setText("Es existiert bereits ein User mit dieser E-Mail-Adresse");
                    feedbackLabel.setStyle("-fx-text-fill: white;");
                }
            } else {
                feedbackLabel.setText("Diese E-Mail-Adresse ist nicht berechtigt (kein Mitglied)");
                feedbackLabel.setStyle("-fx-text-fill: red;");
            }
        }
    }


}
