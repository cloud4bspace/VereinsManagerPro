package space.cloud4b.verein.model.verein.user;

import org.apache.commons.codec.digest.DigestUtils;

import java.sql.Timestamp;

public class User {

    private int userId;
    private String userName;
    private String userKat;
    private boolean userIstVorstand;
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
}
