package space.cloud4b.verein.view.login;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import space.cloud4b.verein.MainApp;
import space.cloud4b.verein.services.DatabaseOperation;
import space.cloud4b.verein.services.DatabaseReader;

public class SignupViewController {
    private MainApp mainApp;
    private Stage dialogStage;

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



    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public void setStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        saveButton.setVisible(true);
        gotoLoginButton.setVisible(false);
    }

    public void handleGotoLogin() {
        dialogStage.close();
        mainApp.showLoginView();
    }

    public void handleSignIn() {
        if (pwFeld.getText().length() < 8 || !pwFeld.getText().equals(pwCheckFeld.getText())) {
            feedbackLabel.setText("Passwörter nicht identisch oder zu kurz (mind. 8 Zeichen)");
            feedbackLabel.setStyle("-fx-text-fill: red;");
        } else {
            if (DatabaseReader.isMitgliedEmail(userNameFeld.getText()) > 0) {
                // es handelt sich um eine E-Mail eines Users
                if (!DatabaseReader.isUser(DatabaseReader.isMitgliedEmail(userNameFeld.getText()))) {
                    // der User existiert noch nicht in der Datenbank
                    DatabaseOperation.saveUserCredentials(userNameFeld.getText(), pwFeld.getText());
                    feedbackLabel.setText("Der User wurde gespeichert");
                    feedbackLabel.setStyle("-fx-text-fill: green;");
                    saveButton.setVisible(false);
                    gotoLoginButton.setVisible(true);
                } else {
                    feedbackLabel.setText("Es existiert bereits ein User mit dieser E-Mail-Adresse");
                    feedbackLabel.setStyle("-fx-text-fill: red;");

                }

            } else {
                feedbackLabel.setText("Diese E-Mail-Adresse ist nicht berechtigt (kein Mitglied)");
                feedbackLabel.setStyle("-fx-text-fill: red;");
            }

        }
    }


}
