package space.cloud4b.verein.services.output;

import space.cloud4b.verein.model.verein.adressbuch.Mitglied;
import space.cloud4b.verein.model.verein.kalender.Termin;
import space.cloud4b.verein.model.verein.task.Task;
import space.cloud4b.verein.model.verein.user.User;
import space.cloud4b.verein.services.connection.MysqlConnection;

import java.io.*;
import java.sql.*;
import java.util.Date;
import java.util.Properties;

/**
 * Die abstrakte Klasse LogWriter bietet Methoden an, um Vorgänge in ein Logfile zu schreiben.
 * Vorgänge sind z.B. Änderungen an Datensätzen, Löschungen oder neue Datensätze.
 *
 * @author Bernhard Kämpf und Serge Kaulitz
 * @version 2019-01-06
 */
public abstract class LogWriter {

    /**
     * Liest den letzten Zählerwert aus dem File LogCounter.xml und gibt diesen zurück.
     *
     * @return der letzte Zählerwert
     */
    public static int getLastLogCounterValue() {
        try {
            // Load Settings
            Properties loadProps = new Properties();
            loadProps.loadFromXML(new FileInputStream(
                    "../VereinsManager/src/space/cloud4b/verein/services/output/LogCounter.xml"));
            String propValue = loadProps.getProperty("logCounter");
            return Integer.parseInt(propValue);
        } catch (Exception e) {
            System.out.println("Counterwert konnte nicht ermittelt werden.");
            e.printStackTrace();
        }
        // falls kein Wert gelesen werden konnte, wird 99999 zurückgegeben
        return 99999;
    }

    /**
     * Erhöht den Zählerwert im File LogCounter.xml um 1.
     */
    public static void setNextLogCounterValue(int lastCounterValue) {
        OutputStream os = null;
        Properties prop = new Properties();
        prop.setProperty("logCounter", Integer.toString(lastCounterValue));
        try {
            os = new FileOutputStream(
                    "../VereinsManager/src/space/cloud4b/verein/services/output/LogCounter.xml");
            prop.storeToXML(os, "Zähler für Log-Einträge (letzter Counterwert wird hier gespeichert)");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Die Methode wird aufgerufen bevor ein Datensatz in der MYSQL-Tabelle geändert wird und schreibt
     * den alten Datensatz in das Logfile (ressources/files/logfiles/logfile.txt)
     *
     * @param object      das Objekt, dessen Inhalte in das Logfile geschrieben werden sollen
     * @param currentUser der angemeldete User, welcher die Änderung vornimmt
     * @param logCounter  fortlaufender Zählerwert aus dem File space/cloud4b/verein/services/output/LogCounter.xml
     */
    public static void writePreUpdateLog(Object object, User currentUser, int logCounter) {
        Mitglied mitglied = null;
        Termin termin = null;
        Task task = null;
        String query = null;
        try (FileWriter fw = new FileWriter("ressources/files/logfiles/logfile.txt", true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println();
            out.print("***** Log#" + logCounter + " --> ");
            out.print(new Timestamp(new Date().getTime()));
            out.print(" durch User: ");
            out.print(currentUser.getUserTxt() + " | ");
            out.print(currentUser.getSessionId() + " | ");
            out.println(System.getProperty("user.timezone") + " | ");
            if (object instanceof Mitglied) {
                mitglied = (Mitglied) object;
                query = "SELECT * FROM usr_web116_5.kontakt WHERE KontaktId=" + mitglied.getId();
                out.println("Vorgang: Mitglied #" + mitglied.getId() + " UPDATE");
            }
            if (object instanceof Termin) {
                termin = (Termin) object;
                query = "SELECT * FROM usr_web116_5.termin WHERE TerminId=" + termin.getTerminId();
                out.println("Vorgang: Termin #" + termin.getTerminId() + " UPDATE");
            }
            if (object instanceof Task) {
                task = (Task) object;
                query = "SELECT * FROM usr_web116_5.task WHERE TaskId=" + task.getTaskId();
                out.println("Vorgang: Task #" + task.getTaskId() + " UPDATE");
            }
            out.print("old values in mysql-db: | ");

            try (Connection conn = new MysqlConnection().getConnection();
                 Statement st = conn.createStatement()) {
                ResultSet result = st.executeQuery(query);
                ResultSetMetaData rsmd = result.getMetaData();
                int columnsNumber = rsmd.getColumnCount();
                while (result.next()) {
                    for (int i = 1; i <= columnsNumber; i++) {
                        out.print(rsmd.getColumnName(i) + "=");
                        out.print(result.getString(i) + " | ");
                    }
                    out.println();
                }
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Die Methode wird aufgerufen nachdem ein Datensatz in der MYSQL-Tabelle geändert wurde und schreibt
     * den neuen Datensatz in das Logfile (ressources/files/logfiles/logfile.txt)
     *
     * @param object      das Objekt, dessen neue Inhalte in das Logfile geschrieben werden sollen
     * @param currentUser der angemeldete User, welcher die Änderung vorgenommen hat
     * @param logCounter  fortlaufender Zählerwert aus dem File space/cloud4b/verein/services/output/LogCounter.xml
     */
    public static void writePostUpdateLog(Object object, User currentUser, int logCounter) {
        Mitglied mitglied = null;
        Termin termin = null;
        Task task = null;
        String query = null;
        try (FileWriter fw = new FileWriter("ressources/files/logfiles/logfile.txt", true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            if (object instanceof Mitglied) {
                mitglied = (Mitglied) object;
                query = "SELECT * FROM usr_web116_5.kontakt WHERE KontaktId=" + mitglied.getId();
            }
            if (object instanceof Termin) {
                termin = (Termin) object;
                query = "SELECT * FROM usr_web116_5.termin WHERE TerminId=" + termin.getTerminId();
            }
            if (object instanceof Task) {
                task = (Task) object;
                query = "SELECT * FROM usr_web116_5.task WHERE TaskId=" + task.getTaskId();
            }
            out.print("new values in mysql-db: | ");

            try (Connection conn = new MysqlConnection().getConnection();
                 Statement st = conn.createStatement()) {
                ResultSet result = st.executeQuery(query);
                ResultSetMetaData rsmd = result.getMetaData();
                int columnsNumber = rsmd.getColumnCount();
                while (result.next()) {
                    for (int i = 1; i <= columnsNumber; i++) {
                        out.print(rsmd.getColumnName(i) + "=");
                        out.print(result.getString(i) + " | ");
                    }
                }
                out.println();
                out.println("***** Log#" + logCounter + " --> E N D E");
            }
        } catch (IOException | SQLException e) {
            //exception handling left as an exercise for the reader
        }
    }

    /**
     * Die Methode wird aufgerufen bevor ein Datensatz in der MYSQL-Tabelle gelöscht wird und schreibt
     * den alten Datensatz in das Logfile (ressources/files/logfiles/logfile.txt)
     *
     * @param object      das Objekt, dessen Inhalte in das Logfile geschrieben werden sollen
     * @param currentUser der angemeldete User, welcher die Änderung vornimmt
     * @param logCounter  fortlaufender Zählerwert aus dem File space/cloud4b/verein/services/output/LogCounter.xml
     */
    public static void writePreDeleteLog(Object object, User currentUser, int logCounter) {
        Mitglied mitglied = null;
        Termin termin = null;
        Task task = null;
        String query = null;
        try (FileWriter fw = new FileWriter("ressources/files/logfiles/logfile.txt", true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println();
            out.print("***** Log#" + logCounter + " --> ");
            out.print(new Timestamp(new Date().getTime()));
            out.print(" durch User: ");
            out.print(currentUser.getUserTxt() + " | ");
            out.print(currentUser.getSessionId() + " | ");
            out.println(System.getProperty("user.timezone") + " | ");
            if (object instanceof Mitglied) {
                mitglied = (Mitglied) object;
                query = "SELECT * FROM usr_web116_5.kontakt WHERE KontaktId=" + mitglied.getId();
                out.println("Vorgang: Mitglied #" + mitglied.getId() + " LOESCHEN");
            }
            if (object instanceof Termin) {
                termin = (Termin) object;
                query = "SELECT * FROM usr_web116_5.termin WHERE TerminId=" + termin.getTerminId();
                out.println("Vorgang: Termin #" + termin.getTerminId() + " LOESCHEN");
            }
            if (object instanceof Task) {
                task = (Task) object;
                query = "SELECT * FROM usr_web116_5.task WHERE TaskId=" + task.getTaskId();
                out.println("Vorgang: Task #" + task.getTaskId() + " LOESCHEN");
            }
            out.print("old values in mysql-db: | ");

            try (Connection conn = new MysqlConnection().getConnection();
                 Statement st = conn.createStatement()) {
                ResultSet result = st.executeQuery(query);
                ResultSetMetaData rsmd = result.getMetaData();
                int columnsNumber = rsmd.getColumnCount();
                while (result.next()) {
                    for (int i = 1; i <= columnsNumber; i++) {
                        out.print(rsmd.getColumnName(i) + "=");
                        out.print(result.getString(i) + " | ");
                    }
                }
                out.println();
                out.println("***** Log#" + logCounter + " --> E N D E");
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Die Methode wird aufgerufen bevor ein Datensatz in der MYSQL-Tabelle gelöscht wird und schreibt
     * den alten Datensatz in das Logfile (ressources/files/logfiles/logfile.txt)
     *
     * @param klasse      die Klasse des neuen Objekts als String
     * @param objectKey   PrimaryKey des neuen Objekts in der MYSQL-DB
     * @param currentUser der angemeldete User, welcher die Änderung vornimmt
     * @param logCounter  fortlaufender Zählerwert aus dem File space/cloud4b/verein/services/output/LogCounter.xml
     */
    public static void writePostNewLog(String klasse, int objectKey, User currentUser, int logCounter) {
        Mitglied mitglied = null;
        Termin termin = null;
        Task task = null;
        String query = null;
        try (FileWriter fw = new FileWriter("ressources/files/logfiles/logfile.txt", true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println();
            out.print("***** Log#" + logCounter + " --> ");
            out.print(new Timestamp(new Date().getTime()));
            out.print(" durch User: ");
            out.print(currentUser.getUserTxt() + " | ");
            out.print(currentUser.getSessionId() + " | ");
            out.println(System.getProperty("user.timezone") + " | ");
            if (klasse == "Mitglied") {
                query = "SELECT * FROM usr_web116_5.kontakt WHERE KontaktId=" + objectKey;
                out.println("Vorgang: Mitglied #" + objectKey + " NEU");
            }
            if (klasse == "Termin") {
                query = "SELECT * FROM usr_web116_5.termin WHERE TerminId=" + objectKey;
                out.println("Vorgang: Termin #" + objectKey + " NEU");
            }
            if (klasse == "Task") {
                query = "SELECT * FROM usr_web116_5.task WHERE TaskId=" + objectKey;
                out.println("Vorgang: Task #" + objectKey + " NEU");
            }
            out.print("neu values in mysql-db: | ");

            try (Connection conn = new MysqlConnection().getConnection();
                 Statement st = conn.createStatement()) {
                ResultSet result = st.executeQuery(query);
                ResultSetMetaData rsmd = result.getMetaData();
                int columnsNumber = rsmd.getColumnCount();
                while (result.next()) {
                    for (int i = 1; i <= columnsNumber; i++) {
                        out.print(rsmd.getColumnName(i) + "=");
                        out.print(result.getString(i) + " | ");
                    }
                }
                out.println();
                out.println("***** Log#" + logCounter + " --> E N D E");
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

}
