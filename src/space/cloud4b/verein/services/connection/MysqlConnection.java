package space.cloud4b.verein.services.connection;

import space.cloud4b.verein.einstellungen.Einstellung;

import java.sql.DriverManager;
import java.sql.SQLException;

public class MysqlConnection {

        private java.sql.Connection conn = null;
        private String dbURL;
        private String user;
        private String pw;

        public MysqlConnection() {
            dbURL = Einstellung.getdbURL();
            user = Einstellung.getdbUser();
            pw = Einstellung.getdbPW();
            try {
                conn = DriverManager.getConnection(dbURL, user, pw);
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        public java.sql.Connection getConnection() {
            return conn;
        }

}
