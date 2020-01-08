package space.cloud4b.verein;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import space.cloud4b.verein.controller.*;
import space.cloud4b.verein.einstellungen.Einstellung;
import space.cloud4b.verein.model.verein.adressbuch.Mitglied;
import space.cloud4b.verein.model.verein.task.Task;
import space.cloud4b.verein.model.verein.user.User;
import space.cloud4b.verein.services.DatabaseReader;
import space.cloud4b.verein.view.benutzer.BenutzerViewController;
import space.cloud4b.verein.view.chart.BirthdayStatisticsController;
import space.cloud4b.verein.view.chart.MemberStatistics01Controller;
import space.cloud4b.verein.view.chart.TaskStatistics01Controller;
import space.cloud4b.verein.view.dashboard.DashBoardController;
import space.cloud4b.verein.view.einstellungen.EinstellungenViewController;
import space.cloud4b.verein.view.logging.LogViewController;
import space.cloud4b.verein.view.login.LoginViewController;
import space.cloud4b.verein.view.login.SignupViewController;
import space.cloud4b.verein.view.mainframe.MainFrameController;
import space.cloud4b.verein.view.mitglieder.MitgliedNeuViewController;
import space.cloud4b.verein.view.mitglieder.MitgliedViewController;
import space.cloud4b.verein.view.tasks.TaskEditViewController;
import space.cloud4b.verein.view.tasks.TaskListViewController;
import space.cloud4b.verein.view.tasks.TaskNeuViewController;
import space.cloud4b.verein.view.tasks.TaskViewController;
import space.cloud4b.verein.view.termine.KalenderViewController;
import space.cloud4b.verein.view.termine.TerminNeuViewController;
import space.cloud4b.verein.view.termine.TerminViewController;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

public class MainApp extends Application {
    private User currentUser = null;
    private Stage primaryStage;
    private BorderPane mainFrame;
    private MainFrameController mainFrameController;
    private MitgliedViewController mitgliedViewController;
    private RanglisteController ranglisteController;
    private KalenderController kalenderController;
    private AdressController adressController;
    private TaskController taskController;
    private BenutzerController benutzerController;

    public MainApp() {
       // DatabaseOperation.createDatabaseFromTemplate();
        // TODO Restore Database from Template (beim Testen wichtig)
        // verein = new Verein(Einstellung.getVereinsName());

    }
    @Override
    public void start(Stage primaryStage) {

        this.primaryStage = primaryStage;
        this.primaryStage.setTitle(Einstellung.getVereinsName());
        this.primaryStage.getIcons().add(new Image("file:ressources/images/address_book_32.png"));
        this.primaryStage.setMaximized(true);

        if (currentUser != null) {
            this.kalenderController = new KalenderController();
            this.adressController = new AdressController();
            this.taskController = new TaskController();
            this.benutzerController = new BenutzerController();
            this.ranglisteController = new RanglisteController(this);

            initMainFrame();
            showDashboard();
        } else {
            showLoginView();
        }
    }

    public void setMitgliedViewController(MitgliedViewController mitgliedViewController){
        this.mitgliedViewController = mitgliedViewController;
    }

    public MitgliedViewController getMitgliedViewController(){
        return mitgliedViewController;
    }

    /**
     * Initialisiert das Login-Fenster
     */
    public void showLoginView() {
        try {
            // Load the fxml file and create a new stage for the popup.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/login/LoginView.fxml"));
            AnchorPane page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Login");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the persons into the controller.
            LoginViewController controller = loader.getController();
            controller.setMainApp(this);
            controller.setStage(dialogStage);
            // controller.setPersonData(contactData);
            //controller.setPersonData(verein.getAdressBuch().getMitgliederListe());

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Initialisiert das Sign-up Fenster
     */
    public void showSignupView() {
        try {
            // Load the fxml file and create a new stage for the popup.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/login/SignupView.fxml"));
            AnchorPane page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Signup");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the persons into the controller.
            SignupViewController controller = loader.getController();
            controller.setMainApp(this);
            controller.setStage(dialogStage);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Initialisiert das Hauptfenster
     */
    public void initMainFrame() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class
                    .getResource("view/mainframe/MainFrame.fxml"));
            mainFrame = loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(mainFrame);
            primaryStage.setScene(scene);
            // Give the controller access to the main app.
            MainFrameController controller = loader.getController();
            controller.setMainApp(this);
            this.mainFrameController = controller;
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initialisiert das Sign-up Fenster
     */
    public void showEinstellungenView() {
        try {
            // Load the fxml file and create a new stage for the popup.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/einstellungen/EinstellungenView.fxml"));
            AnchorPane page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Einstellungen");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the persons into the controller.
            EinstellungenViewController controller = loader.getController();
            controller.setMainApp(this);
            controller.setStage(dialogStage);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Zeigt die Hauptübersicht (Dashboard/Cockpit) in der Mitte des Hauptfensters
     */
    public void showDashboard() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/dashboard/DashBoard.fxml"));
            AnchorPane dashBoard = loader.load();
            dashBoard.setPadding(new Insets(10,10,10,10));
            // Set person overview into the center of root layout.
            mainFrame.setCenter(dashBoard);

            // Give the controller access to the main app.
            DashBoardController controller = loader.getController();
            controller.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Zeigt den Mitgliederbereich in der Mitte des Hauptfensters
     */
    public boolean showMitgliedView(Mitglied mitglied) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/mitglieder/MitgliedView.fxml"));
            AnchorPane page = loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Person");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            mainFrame.setCenter(page);
            MitgliedViewController controller = loader.getController();
            controller.setMainApp(this);
            controller.setStage(dialogStage);

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public AdressController getAdressController() {
        return adressController;
    }

    public KalenderController getKalenderController() {
        return kalenderController;
    }

    public TaskController getTaskController() {
        return taskController;
    }

    public BenutzerController getBenutzerController() {
        return this.benutzerController;
    }

    /**
     * Zeigt den Termin-Bereich in der Mitte des Hauptfensters
     * wenn der TerminViewController erzeugt wird, übergebe ich ihm auch den
     * KalenderController, weil er daraus später Daten bezieht.
     */
    public boolean showTerminEditDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/termine/TerminView.fxml"));
            AnchorPane page = loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Termine bewirtschaften");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            mainFrame.setCenter(page);
            TerminViewController controller = loader.getController();
            controller.setMainApp(this);
            // controller.setMainFrameController(mainFrameController);
            //   controller.setKalenderController(kalenderController);


        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Öffnet ein Fenster und zeigt eine Geburtstags-Statistik
     */
    public void showBirthdayStatistics() {
        try {
            // Load the fxml file and create a new stage for the popup.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/chart/BirthdayStatistics.fxml"));
            AnchorPane page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Birthday Statistics");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the persons into the controller.
            BirthdayStatisticsController controller = loader.getController();
            controller.setPersonData(DatabaseReader.getMitgliederAsArrayList());

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Öffnet ein Fenster und zeigt die Mitglieder-Kategorien als Kuchendiagramm
     */
    public void showMemberKatIStatistics() {
        try {
            // Load the fxml file and create a new stage for the popup.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/chart/MemberStatistics01.fxml"));
            AnchorPane page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Auswertung Mitglieder nach Kat I");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the persons into the controller.
            MemberStatistics01Controller controller = loader.getController();
         //   controller.setPersonData(verein.getAdressBuch().getMitgliederListe());

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Öffnet einen Dialog zum Erfassen eines neuen Mitglieds
     */
    public void showMitgliedErfassen() {
        try {
            // Load the fxml file and create a new stage for the popup.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/mitglieder/MitgliedNeuView.fxml"));
            AnchorPane page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Mitglied erfassen");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the persons into the controller.
            MitgliedNeuViewController controller = loader.getController();
            controller.setMainApp(this);
            controller.setStage(dialogStage);
            // controller.setPersonData(contactData);
            //controller.setPersonData(verein.getAdressBuch().getMitgliederListe());

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Öffnet einen Dialog zum Erfassen eines neuen Termins
     */
    public void showTerminErfassen() {
        try {
            // Load the fxml file and create a new stage for the popup.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/termine/TerminNeuView.fxml"));
            AnchorPane page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Termin erfassen");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the persons into the controller.
            TerminNeuViewController controller = loader.getController();
            controller.setMainApp(this);
            controller.setStage(dialogStage);
            // controller.setPersonData(contactData);
            //controller.setPersonData(verein.getAdressBuch().getMitgliederListe());

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Öffnet einen Dialog zum Erfassen eines neuen Tasks
     */
    public void showTaskErfassen() {
        try {
            // Load the fxml file and create a new stage for the popup.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/tasks/TaskNeuView.fxml"));
            AnchorPane page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Task erfassen");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the persons into the controller.
            TaskNeuViewController controller = loader.getController();
            controller.setMainApp(this);
            controller.setStage(dialogStage);
            // controller.setPersonData(contactData);
            //controller.setPersonData(verein.getAdressBuch().getMitgliederListe());

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Öffnet einen Dialog zum Erfassen eines neuen Tasks
     */
    public void showTaskEdit(Task task) {
        try {
            // Load the fxml file and create a new stage for the popup.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/tasks/TaskEditView.fxml"));
            AnchorPane page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Task ändern");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the persons into the controller.
            TaskEditViewController controller = loader.getController();
            controller.setMainApp(this);
            controller.setTask(task);
            controller.setStage(dialogStage);
            // controller.setPersonData(contactData);
            //controller.setPersonData(verein.getAdressBuch().getMitgliederListe());

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Gibt den Haupt-Stage (primaryStage) zurück
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public RanglisteController getRanglisteController() {
        return ranglisteController;
    }

    public MainFrameController getMainFrameController() {
        return mainFrameController;
    }

    public void setUser(User user) {
        this.currentUser = user;
    }
    public static void main(String[] args) {
        launch(args);
    }


    public void showTerminOverview() {
        try {
            // Load the fxml file and create a new stage for the popup.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/termine/KalenderView.fxml"));
            AnchorPane page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Kalenderansicht");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the persons into the controller.
            KalenderViewController controller = loader.getController();
            controller.setMainApp(this);
            controller.setStage(dialogStage);
            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showTaskOverview() {
        try {
            // Load the fxml file and create a new stage for the popup.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/tasks/TaskListView.fxml"));
            AnchorPane page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Taskliste");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the persons into the controller.
            TaskListViewController controller = loader.getController();
            controller.setMainApp(this);
            controller.setStage(dialogStage);
            dialogStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void showTask() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/tasks/TaskView.fxml"));
            AnchorPane page = loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Tasks");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            mainFrame.setCenter(page);
            TaskViewController controller = loader.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showTaskStatistics() {
        try {
            // Load the fxml file and create a new stage for the popup.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/chart/TaskStatistics01.fxml"));
            AnchorPane page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Auswertung Tasks nach Status");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the persons into the controller.
            TaskStatistics01Controller controller = loader.getController();
            //   controller.setPersonData(verein.getAdressBuch().getMitgliederListe());

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Zeigt eine Warnmeldung an mit dem übergebenen String
     */
    public void showAlert(String meldung, Stage dialogStage) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.initOwner(dialogStage);
        alert.setTitle("Eingaben sind ungültig");
        alert.setHeaderText("Folgende Fehler bei der Dateneingabe müssen bereinigt werden:");
        alert.setContentText(meldung);
        Optional<ButtonType> result = alert.showAndWait();
    }

    public void showBenutzerView() {
        try {
            // Load the fxml file and create a new stage for the popup.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/benutzer/BenutzerView.fxml"));
            AnchorPane page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Benutzerliste");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the persons into the controller.
            BenutzerViewController controller = loader.getController();
            controller.setMainApp(this);
            controller.setStage(dialogStage);
            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * öffnet einen "Save as"-Dialog
     *
     * @return
     */
    public File chooseImageFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        Window stage = new Stage();
        fileChooser.setTitle("Bilddatei auswählen..");
        //fileChooser.setInitialFileName(fileName + LocalDate.now() + ".xlsx");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG-Images", "*.png"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JPG-Images", "*.jpg"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JPEG-Images", "*.jpeg"));
        File file = fileChooser.showOpenDialog(stage);
        return file;
    }

    /**
     * Methode extrahiert aus dem übergebenen Filenamen die Erweiterung (Extension)
     * und gibt diese als String zurück
     *
     * @param filename
     * @return
     */
    public Optional<String> getExtensionByStringHandling(String filename) {
        return Optional.ofNullable(filename).filter(f -> f.contains(".")).map(f
                -> f.substring(filename.lastIndexOf(".") + 1));
    }

    /**
     * öffnet einen "Save as"-Dialog
     *
     * @param fileName gibt den vom
     * @return
     */
    public File saveAsFile(String fileName) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        Window stage = new Stage();
        fileChooser.setTitle("Speichern unter..");
        fileChooser.setInitialFileName(fileName + LocalDate.now() + ".xlsx");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Excel File", "*.xlsx"));
        File file = fileChooser.showSaveDialog(stage);
        return file;
    }

    public void showLogFileView() {
        try {
            // Load the fxml file and create a new stage for the popup.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/logging/LogView.fxml"));
            AnchorPane page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("LogFile-Einträge");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the persons into the controller.
            LogViewController controller = loader.getController();
            controller.setMainApp(this);
            controller.setStage(dialogStage);
            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
