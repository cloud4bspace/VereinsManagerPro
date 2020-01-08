package space.cloud4b.verein.view.logging;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import space.cloud4b.verein.MainApp;

import java.io.*;
import java.util.ArrayList;

public class LogViewController {

    private MainApp mainApp;
    private Stage dialogStage;
    private ArrayList<Text> textLines;

    @FXML
    private VBox logViewVBox;
    @FXML
    private TextFlow logTextFlow;
    @FXML
    private Label loggingTitleLabel;

    public LogViewController() {

    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    public void initialize() {
        File file = new File("ressources/files/logfiles/logfile.txt");
        ObservableList list = logTextFlow.getChildren();
        ArrayList textLines = new ArrayList<>();
        loggingTitleLabel.setText("Auszug aus dem Log-File (" + file.getName() + ")");
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String st;
        while (true) {
            try {
                if (!((st = br.readLine()) != null)) break;
                Text text = new Text(st + "\n");
                System.out.println(st.startsWith("*"));
                if (st.length() > 1 && st.startsWith("*")) {
                    text.setFont(Font.font(java.awt.Font.MONOSPACED, 14));
                    text.setFill(Color.RED);
                } else if (st.startsWith("Vorgang")) {
                    text.setFont(Font.font(java.awt.Font.MONOSPACED, 14));
                    text.setFill(Color.DARKRED);
                    text.setStyle("-fx-background-color: #C3C3C3;");
                } else {
                    text.setFont(Font.font(java.awt.Font.MONOSPACED, 14));
                    text.setFill(Color.DARKBLUE);
                }


                textLines.add(text);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        list.addAll(textLines);
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public void setStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }


}
