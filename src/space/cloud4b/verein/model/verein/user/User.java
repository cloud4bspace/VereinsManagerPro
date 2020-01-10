package space.cloud4b.verein.model.verein.user;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import org.apache.commons.codec.digest.DigestUtils;
import space.cloud4b.verein.services.DatabaseOperation;
import space.cloud4b.verein.services.DatabaseReader;

import java.sql.Timestamp;
import java.time.LocalDate;

public class User {

    private int userId;
    private int mitgliedId;
    private String mitgliedName;
    private String userName;
    private String userPw;
    private String userKat;
    private boolean userIstVorstand;
    private LocalDate userLastLogin;
    private int userLoginCount;
    private int userSperrcode;
    private String sessionId;

    public User(int userId, String userName, String userKat, boolean istVorstand) {
        this.userId = userId;
        this.userName = userName;
        this.userKat = userKat;
        this.userIstVorstand = istVorstand;
        this.sessionId = DigestUtils.sha256(userName + new Timestamp(System.currentTimeMillis())).toString();
        System.out.println("SessionId: " + this.sessionId);
        System.out.println(this);
    }

    public User(int userId, int kontaktId, String userName, String userPw, LocalDate userLastLogin,
                int userLoginCount, int userSperrcode) {
        this.userId = userId;
        this.mitgliedId = kontaktId;
        this.userName = userName;
        this.userPw = userPw;
        this.userLastLogin = userLastLogin;
        this.userLoginCount = userLoginCount;
        this.userSperrcode = userSperrcode;
        this.userKat = DatabaseReader.getUserKatString(this.mitgliedId);
        this.userIstVorstand = DatabaseReader.getVorstandsStatus(this.mitgliedId);
        this.mitgliedName = DatabaseReader.getMitgliedName(this.mitgliedId);
    }

    public String toString() {
        String string = "#" + userId + " " + userName + " (" + userKat;
        if (userIstVorstand) {
            string += "|*Vorstand*";
        }
        string += ")";
        return string;
    }

    public int getUserId() {
        return this.userId;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getUserTxt() {
        return userId + "-" + userName + "-" + userKat;
    }

    public String getSessionId() {
        return sessionId;
    }

    public ObservableValue<Number> getUserIdProperty() {
        return new SimpleIntegerProperty(this.userId);
    }

    public ObservableValue<Number> getMitgliedIdProperty() {
        return new SimpleIntegerProperty(this.mitgliedId);
    }

    public ObservableValue<String> getUserNameProperty() {
        return new SimpleStringProperty(this.userName + "\n" + this.mitgliedName);
    }

    public ObservableValue<LocalDate> getUserLastLogin() {
        return new SimpleObjectProperty<LocalDate>(this.userLastLogin);
    }

    public ObservableValue<Number> getUserLoginsCountProperty() {
        return new SimpleIntegerProperty(this.userLoginCount);
    }

    public ObservableValue<Number> getUserSperrcodeProperty() {
        return new SimpleIntegerProperty(this.userSperrcode);
    }

    public ObservableValue<String> getUserKatProperty() {
        return new SimpleStringProperty(this.userKat);
    }

    public ObservableValue<String> getUserPWProperty() {
        return new SimpleStringProperty(this.userPw);
    }

    public void setSperrCode(Number newValue, User currentUser) {
        this.userSperrcode = newValue.intValue();
        DatabaseOperation.setSperrCode(this.userSperrcode, this, currentUser);
    }

    public boolean isUserGesperrt() {
        if (this.userSperrcode > 0) {
            return true;
        } else {
            return false;
        }
    }
}
