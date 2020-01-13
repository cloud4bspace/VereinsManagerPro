package space.cloud4b.verein.services;

import org.apache.commons.codec.digest.DigestUtils;
import space.cloud4b.verein.model.verein.adressbuch.Mitglied;
import space.cloud4b.verein.model.verein.kalender.Termin;
import space.cloud4b.verein.model.verein.task.Task;
import space.cloud4b.verein.model.verein.user.User;
import space.cloud4b.verein.services.connection.MysqlConnection;
import space.cloud4b.verein.services.output.LogWriter;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Die abstrakte Klasse DataBaseOperation stellt Methoden zur Verfügung, um in den Tabellen der
 * Datenbank Änderungen an Datensätzen vorzunehmen.
 *
 * @author Bernhard Kämpf & Serge Kaulitz
 * @version 2019-11
 */
public abstract class DatabaseOperation {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm");

    /**
     * Erstellt die erforderlichen Tabellen für Adressen und Termine aus Tabellen-Vorlagen
     * und füllt sie mit initialen Datensätzen
     */
    public static void createDatabaseFromTemplate() {
        try (Connection conn = new MysqlConnection().getConnection();
             Statement st = conn.createStatement()) {
            String query = "TRUNCATE usr_web116_5.kontakt;";
            st.executeUpdate(query);
            query = "CREATE TABLE IF NOT EXISTS kontakt LIKE kontakt_template;";
            st.executeUpdate(query);
            query = "INSERT kontakt SELECT * FROM kontakt_template;";
            st.executeUpdate(query);
            System.out.println("Datenbank für Kontakte erstellt und mit Beispieldaten gefüllt");

            query = "TRUNCATE usr_web116_5.termin;";
            st.executeUpdate(query);
            query = "CREATE TABLE IF NOT EXISTS usr_web116_5.termin LIKE usr_web116_5.termin_template;";
            st.executeUpdate(query);
            query = "INSERT usr_web116_5.termin SELECT * FROM usr_web116_5.termin_template;";
            st.executeUpdate(query);
            System.out.println("Datenbank für Termine erstellt und mit Beispieldaten gefüllt");
        } catch (SQLException e) {
            System.out.println("Datenbanken konnten nicht bereitgestellt werden (" + e + ")");
        }
    }

    /**
     * legt mit den übergebenen Basisdaten ein neues Mitglied in der Datenbank an.
     * @param currentUser der angemeldete User
     * @param nachname der Nachname des Mitglieds
     * @param vorname der Vorname des Mitglieds
     * @param eintrittsDatum das Eintrittsdatum des Mitglieds
     * @return die MitgliederId des neuen Tabelleneintrags als int
     */
    public static int saveNewMember(String nachname, String vorname, String eintrittsDatum, User currentUser) {

        String query = "INSERT INTO usr_web116_5.kontakt (KontaktId, KontaktNachname, " +
                "KontaktVorname, KontaktEintrittsdatum, KontaktIstMitglied, KontaktTrackChangeUsr, " +
                "KontaktTrackChangeTimestamp) VALUES (NULL, ?, ?, ?, '1', ?, CURRENT_TIMESTAMP)";
        MysqlConnection conn = new MysqlConnection();
        PreparedStatement ps = null;
        try {
            ps = conn.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, nachname);
            ps.setString(2, vorname);
            ps.setString(3, eintrittsDatum);
            ps.setString(4, currentUser.getUserName());
            System.out.println("neues Mitglied hinzugefügt: " + ps.executeUpdate());
            System.out.println(ps.getGeneratedKeys());
            ResultSet keys = null;
            keys = ps.getGeneratedKeys();
            keys.next();
            int newKey = keys.getInt(1);
            int logCounter = LogWriter.getLastLogCounterValue();
            LogWriter.setNextLogCounterValue(++logCounter);
            LogWriter.writePostNewLog("Mitglied", newKey, currentUser, logCounter);
            return newKey;
        } catch (SQLException e) {
            System.out.println("Fehler " + e);
        }
        return 0;
    }

    /**
     * legt in der Datenbank einen neuen Task mit den übergebenen Basisdaten an
     * @param bezeichnung die Kurzbezeichhnung des Tasks
     * @param details weitere Detailangaben zum Task
     * @param terminDatum das Fälligkeitsdatum des Tasks
     * @param verantwortlich das verantwortliche Mitglied
     * @param user der angemeldete User
     * @return die TaskId des neu eingefügten Tasks aus der Datenbank-Tabelle
     */
    public static int saveNewTask(String bezeichnung, String details, String terminDatum, Mitglied verantwortlich, User user) {

        String query = "INSERT INTO task (TaskId, TaskBezeichnung, TaskPrio, TaskStatus, TaskDetails, TaskTerminBis" +
                ", TaskMitgliedId, TaskTrackChangeUser, TaskTrackChangeTimestamp) VALUES (NULL, ?, '1', '1', ?, ?, ?, ?" +
                ", CURRENT_TIMESTAMP)";
        MysqlConnection conn = new MysqlConnection();
        PreparedStatement ps = null;
        try {
            ps = conn.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, bezeichnung);
            ps.setString(2, details);
            ps.setString(3, terminDatum);
            ps.setInt(4, verantwortlich.getId());
            ps.setString(5, user.getUserName());
            System.out.println("neues Mitglied hinzugefügt: " + ps.executeUpdate());
            // System.out.println(ps.getGeneratedKeys());
            ResultSet keys = null;
            keys = ps.getGeneratedKeys();
            keys.next();
            int newKey = keys.getInt(1);
            System.out.println("KEy: " + newKey + "/" + verantwortlich.getKurzbezeichnung());
            int logCounter = LogWriter.getLastLogCounterValue();
            LogWriter.setNextLogCounterValue(++logCounter);
            LogWriter.writePostNewLog("Task", newKey, user, logCounter);

            return newKey;

        } catch (SQLException e) {
            System.out.println("Fehler " + e);
        }
        return 0;
    }


    /**
     * löscht das übergebene Mitglied aus den relevanten Tabellen der Datenbank
     * @param mitglied das zu löschende Mitglied
     * @param currentUser der angemeldete User
     */
    public static void deleteMitglied(Mitglied mitglied, User currentUser) {

        // Schritt 1: alte DB-Werte in Logfile schreiben
        int logCounter = LogWriter.getLastLogCounterValue();
        LogWriter.setNextLogCounterValue(++logCounter);
        LogWriter.writePreDeleteLog(mitglied, currentUser, logCounter);

        try (Connection conn = new MysqlConnection().getConnection();
             Statement st = conn.createStatement()) {
            String query = "DELETE FROM kontakt WHERE KontaktId=" + mitglied.getId();
            st.execute(query);

            query = "DELETE FROM `terminkontrolle` WHERE `KontrolleMitgliedId` = " + mitglied.getId();
            st.execute(query);

        } catch (SQLException e) {
            System.out.println("Mitglied konnte nicht gelöscht werden (" + e + ")");
        }
    }

    /**
     * löscht den übergebenen Termin in den relevanten Tabellen der Datenbank
     *
     * @param termin der zu löschende Termin
     * @param currentUser der angemeldete User
     */
    public static void deleteTermin(Termin termin, User currentUser) {
        if (termin != null) {

            // Schritt 1: alte DB-Werte in Logfile schreiben
            int logCounter = LogWriter.getLastLogCounterValue();
            LogWriter.setNextLogCounterValue(++logCounter);
            LogWriter.writePreDeleteLog(termin, currentUser, logCounter);

            try (Connection conn = new MysqlConnection().getConnection();
                 // Termin in der Tabelle termin löschen
                 Statement st = conn.createStatement()) {
                String query = "DELETE FROM termin WHERE TerminId=" + termin.getTerminId();
                st.execute(query);

                // alle Anmeldungen und Teilnahmen in der Tabelle terminkontrolle löschen
                query = "DELETE FROM `terminkontrolle` WHERE `KontrolleTerminId` = " + termin.getTerminId();
                st.execute(query);
            } catch (SQLException e) {
                System.out.println("Termin konnte nicht gelöscht werden (" + e + ")");
            }
        }

    }

    /**
     * löscht den übergebenen Task aus den relevanten Datenbanken
     *
     * @param task die zu löschende Task
     * @param currentUser der angemeldete User
     */
    public static void deleteTask(Task task, User currentUser) {
        if (task != null) {

            // Schritt 1: alte DB-Werte in Logfile schreiben
            int logCounter = LogWriter.getLastLogCounterValue();
            LogWriter.setNextLogCounterValue(++logCounter);
            LogWriter.writePreDeleteLog(task, currentUser, logCounter);

            try (Connection conn = new MysqlConnection().getConnection();
                 // Task in der Tabelle termin löschen
                 Statement st = conn.createStatement()) {
                String query = "DELETE FROM task WHERE TaskId=" + task.getTaskId();
                st.execute(query);
            } catch (SQLException e) {
                System.out.println("Task konnte nicht gelöscht werden (" + e + ")");
            }
        }
    }

    /**
     * prüft und ändert gegebenenfalls den Mitgliederstatus anhand des gesetzten Austrittsdatums
     */
    public static void checkMitgliederStatus() {
        try (Connection conn = new MysqlConnection().getConnection();
             Statement st = conn.createStatement()) {
            String query = "UPDATE usr_web116_5.kontakt SET `KontaktIstMitglied` = 0 " +
                    "WHERE KontaktAustrittsdatum < CURRENT_DATE AND KontaktAustrittsdatum NOT LIKE '0000-00-00'";
            st.executeUpdate(query);

        } catch(SQLException e) {

        }
    }

    /**
     * Das Übergebene Objekt vom Typ Mitglied wird in der Kontakt-Tabelle aktualisiert
     * Zuvor werden die alten Werte aus der DB in ein Logfile geschrieben.
     * Nach dem Update werden die neuen Werte ebenfalls in das Logfile geschrieben.
     *
     * @param mitglied das geänderte und zu aktualisierende Mitlied
     * @param currentUser der angemeldete User
     */
    public static void updateMitglied(Mitglied mitglied, User currentUser) {

        // Schritt 1: alte DB-Werte in Logfile schreiben
        int logCounter = LogWriter.getLastLogCounterValue();
        LogWriter.setNextLogCounterValue(++logCounter);
        LogWriter.writePreUpdateLog(mitglied, currentUser, logCounter);


        // Schritt 2: Update in DB ausführen
        MysqlConnection conn = new MysqlConnection();
        boolean istMitglied = false;

        // create the java mysql update preparedstatement
        String query = "UPDATE usr_web116_5.kontakt SET KontaktNachname = ?,"
                + " KontaktVorname = ?,"
                + " KontaktAdresse = ?,"
                + " KontaktAdresszusatz = ?,"
                + " KontaktPLZ = ?,"
                + " KontaktOrt = ?," // 6
                + " KontaktGeburtsdatum = ?, "
                + " KontaktAnredeStatus = ?, "
                + " KontaktEintrittsdatum = ?,"
                + " KontaktAustrittsdatum = ?," // 10
                + " KontaktKategorieA = ?,"
                + " KontaktKategorieB = ?,"
                + " KontaktIstMitglied = ?,"
                + " KontaktIstVorstandsmitglied = ?,"
                + " KontaktBemerkungen = ?,"
                + " KontaktMobile = ?,"
                + " KontaktTelefon = ?,"
                + " KontaktEMail = ?,"
                + " KontaktEMailII = ?,"
                + " KontaktTrackChangeUsr = ?,"
                + " KontaktTrackChangeTimestamp = CURRENT_TIMESTAMP"
                + " WHERE KontaktId = ?";
        //einzelne Änderungen abfragen und dann Wert alt und neu, User und Timestamp in logdatei schreiben
        PreparedStatement preparedStmt = null;
        try {
            preparedStmt = conn.getConnection().prepareStatement(query);
            preparedStmt.setString(1, mitglied.getNachName());
            preparedStmt.setString(2, mitglied.getVorname());
            preparedStmt.setString(3, mitglied.getAdresse());
            preparedStmt.setString(4, mitglied.getAdresszusatz());
            preparedStmt.setInt(5, mitglied.getPlz());
            preparedStmt.setString(6, mitglied.getOrt());
            preparedStmt.setString(7, mitglied.getGeburtsdatum().toString());
            preparedStmt.setInt(8, mitglied.getAnredeElement().getStatusElementKey());
            preparedStmt.setString(9, mitglied.getEintrittsdatum().toString());
            if (mitglied.getAustrittsDatum() != null) {
                istMitglied = mitglied.getAustrittsDatum().isAfter(LocalDate.now());
                preparedStmt.setString(10, mitglied.getAustrittsDatum().toString());
            } else {
                istMitglied = true;
                preparedStmt.setString(10, null);
            }
            preparedStmt.setInt(11, mitglied.getKategorieIElement().getStatusElementKey());
            preparedStmt.setInt(12, mitglied.getKategorieIIElement().getStatusElementKey());

            preparedStmt.setBoolean(13, istMitglied);
            preparedStmt.setBoolean(14, mitglied.getIstVorstandsmitglied().getValue());
            preparedStmt.setString(15, mitglied.getBemerkungen());
            preparedStmt.setString(16, mitglied.getMobile());
            preparedStmt.setString(17, mitglied.getTelefon());
            preparedStmt.setString(18, mitglied.getEmail());
            preparedStmt.setString(19, mitglied.getEmailII());
            preparedStmt.setString(20, currentUser.toString());
            preparedStmt.setInt(21, mitglied.getId());

            // prepared Statement ausführen
            System.out.println("Rückmeldung preparedStmt: " + preparedStmt.executeUpdate());

            // Schritt 3: neue DB-Werte in Logilfe schreiben
            LogWriter.writePostUpdateLog(mitglied, currentUser, logCounter);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Das Übergebene Objekt vom Typ Termin wird in der Termin-Tabelle aktualisiert
     *
     * @param termin der geänderte Termin
     * @param user der angemeldete User
     */
    public static void updateTermin(Termin termin, User user) {
        // Schritt 1: alte DB-Werte in Logfile schreiben
        int logCounter = LogWriter.getLastLogCounterValue();
        LogWriter.setNextLogCounterValue(++logCounter);
        LogWriter.writePreUpdateLog(termin, user, logCounter);

        // Schritt 2: DB-Tabelle aktualisieren
        MysqlConnection conn = new MysqlConnection();

        // create the java mysql update preparedstatement
        String query = "UPDATE usr_web116_5.termin SET TerminDatum = ?,"
                + " TerminText = ?,"
                + " TerminOrt = ?,"
                + " TerminDetails = ?,"
                + " TerminZeit = ?,"
                + " TerminZeitBis = ?," // 6
                + " TerminTeilnehmerKatA = ?, "
                + " TerminTeilnehmerKatB = ?, "
                + " TerminTrackChangeUsr = ?,"
                + " TerminTrackChangeTimestamp = CURRENT_TIMESTAMP"
                + " WHERE TerminId = ?";
        //einzelne Änderungen abfragen und dann Wert alt und neu, User und Timestamp in logdatei schreiben
        PreparedStatement preparedStmt = null;
        try {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            preparedStmt = conn.getConnection().prepareStatement(query);
            preparedStmt.setString(1, termin.getDatum().toString());
            preparedStmt.setString(2, termin.getTerminText().getValue());
            preparedStmt.setString(3, termin.getOrt());
            preparedStmt.setString(4, termin.getDetails());
            if(termin.getTerminZeitVon() == null) {
                preparedStmt.setString(5, "");
            } else {
                preparedStmt.setString(5, termin.getTerminZeitVon().format(formatter));
            }
            if(termin.getTerminZeitBis() == null) {
                preparedStmt.setString(6, "");
            } else {
                preparedStmt.setString(6, termin.getTerminZeitBis().format(formatter));
            }
            preparedStmt.setInt(7, termin.getKatIElement().getStatusElementKey());
            preparedStmt.setInt(8, termin.getKatIIElement().getStatusElementKey());
            preparedStmt.setString(9, user.getUserTxt());
            preparedStmt.setInt(10, termin.getTerminId());
            // execute the java preparedstatement
            System.out.println("Rückmeldung preparedStmt: " + preparedStmt.executeUpdate());

            // Schritt 3: neue DB-Werte in Logilfe schreiben
            LogWriter.writePostUpdateLog(termin, user, logCounter);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * aktualisiert den übergebenen Task in der entsprechenden Datenbanktabelle
     *
     * @param task        der zu aktualisierende Task
     * @param currentUser der angemeldete User
     */
    public static void updateTask(Task task, User currentUser) {

        // Schritt 1: alte DB-Werte in Logfile schreiben
        int logCounter = LogWriter.getLastLogCounterValue();
        LogWriter.setNextLogCounterValue(++logCounter);
        LogWriter.writePreUpdateLog(task, currentUser, logCounter);

        // Schritt 2: DB-Tabelle aktualisieren
        MysqlConnection conn = new MysqlConnection();

        // create the java mysql update preparedstatement
        String query = "UPDATE usr_web116_5.task SET TaskBezeichnung = ?,"
                + " TaskPrio = ?,"
                + " TaskStatus = ?,"
                + " TaskDetails = ?,"
                + " TaskTerminBis = ?,"
                + " TaskMitgliedId = ?,"
                + " TaskTrackChangeUser = ?,"
                + " TaskTrackChangeTimestamp = CURRENT_TIMESTAMP"
                + " WHERE TaskId = ?";
        //einzelne Änderungen abfragen und dann Wert alt und neu, User und Timestamp in logdatei schreiben
        PreparedStatement preparedStmt = null;
        try {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            preparedStmt = conn.getConnection().prepareStatement(query);
            preparedStmt.setString(1, task.getTaskTitel());
            preparedStmt.setInt(2, task.getPrioStatus().getStatusElementKey());
            preparedStmt.setInt(3, task.getStatusStatus().getStatusElementKey());
            preparedStmt.setString(4, task.getTaskText());
            preparedStmt.setString(5, task.getTaskDatum().toString());
            preparedStmt.setInt(6, task.getVerantwortlichesMitglied().getId());
            preparedStmt.setString(7, currentUser.getUserTxt());
            preparedStmt.setInt(8, task.getTaskId());
            // execute the java preparedstatement
            System.out.println("Rückmeldung preparedStmt (updateTask): " + preparedStmt.executeUpdate());

            // Schritt 3: neue DB-Werte in Logilfe schreiben
            LogWriter.writePostUpdateLog(task, currentUser, logCounter);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * trägt einen neuen Benutzer/User in der Benutzertabelle ein
     * @param eMail die E-Mail-Adresse des neuen Users
     * @param pw das Passwort des neuen Users
     */
    public static void saveUserCredentials(String eMail, String pw) {

        // Check I: gehört die E-Mail-Adresse zu einem aktiven Mitglied?
        int mitgliedId = DatabaseReader.isMitgliedEmail(eMail);
        if (mitgliedId > 0) {

            // Check II: gibt es den User bereits?
            boolean isUser = DatabaseReader.isUser(mitgliedId);
            MysqlConnection conn = new MysqlConnection();
            String query = "INSERT INTO benutzer (BenutzerId, KontaktId, BenutzerName, BenutzerPw," +
                    " BenutzerLastlogin, BenutzerNumberLogins, BenutzerSperrcode, BenutzerTrackChangeUsr," +
                    " BenutzerTrackChangeTimestamp) VALUES (NULL, ?, ?, ?, CURRENT_DATE, ?, '0', ?, CURRENT_TIMESTAMP );";
            try (PreparedStatement ps = conn.getConnection().prepareStatement(query)) {
                ps.setInt(1, mitgliedId);//KontaktID
                ps.setString(2, eMail);
                ps.setString(3, DigestUtils.sha1Hex(String.valueOf(pw) + eMail));
                ps.setInt(4, 0);
                ps.setString(5, "neuer User #" + mitgliedId);
                System.out.println("Rückmeldung preparedStmt: " + ps.executeUpdate());
            } catch (SQLException e) {
                System.out.println("Fehler: " + e);
            }
            ;
        } else {
            System.out.println("Keine gültige E-Mail-Adresse eines Mitglieds");
        }
    }


    /**
     * legt in der MySql-Datenbank einen neuen Termin an mit den übergebenen Werten
     *
     * @param terminText  Bezeichnung des Termins
     * @param terminDatum Datum des Termins
     * @return gibt die Id des neuen Datensatzes zurück
     */
    public static int saveNewTermin(String terminText, String terminDatum, User currentUser) {

        String query = "INSERT INTO usr_web116_5.termin (TerminId, TerminDatum, TerminText, " +
                "TerminTrackChangeUsr, " +
                "TerminTrackChangeTimestamp) VALUES (NULL, ?, ?, ?, CURRENT_TIMESTAMP)";
        MysqlConnection conn = new MysqlConnection();
        PreparedStatement ps = null;
        try {
            ps = conn.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, terminDatum);
            ps.setString(2, terminText);
            ps.setString(3, currentUser.getUserName());
            System.out.println("neuer Temin hinzugefügt: " + ps.executeUpdate());
            // System.out.println(ps.getGeneratedKeys());
            ResultSet keys = null;
            keys = ps.getGeneratedKeys();
            keys.next();
            int newKey = keys.getInt(1);
            int logCounter = LogWriter.getLastLogCounterValue();
            LogWriter.setNextLogCounterValue(++logCounter);
            LogWriter.writePostNewLog("Termin", newKey, currentUser, logCounter);
            return newKey;

        } catch(SQLException e) {
            System.out.println("Fehler " + e);
        }
        return 0;
    }

    /**
     * erhöht in der Benutzer-Datenbank die Anzahl der Zugriffe um einen Zählerwert und
     * trägt beim entsprechenden Benutzer das Datum der letzten Anmeldung ein
     *
     * @param mitgliedId die Id des Mitglieds
     */
    public static void incrementLoginCounter(int mitgliedId) {
        try (Connection conn = new MysqlConnection().getConnection();
             Statement st = conn.createStatement()) {
            String query = "UPDATE benutzer SET BenutzerLastlogin= CURRENT_DATE(), BenutzerNumberLogins = " +
                    "BenutzerNumberLogins +1 WHERE KontaktId=" + mitgliedId;
            st.execute(query);

        } catch (SQLException e) {
            System.out.println("Mitglied konnte nicht gelöscht werden (" + e + ")");
        }
    }

    /**
     * setzt beim übergebenen User einen Sperrcode
     *
     * @param sperrCode   der zu setzende Sperrcode
     * @param user        der von der Sperrung betroffene User
     * @param currentUser der angemeldete Benutzer
     */
    public static void setSperrCode(int sperrCode, User user, User currentUser) {
        try (Connection conn = new MysqlConnection().getConnection();
             Statement st = conn.createStatement()) {
            String query = "UPDATE benutzer SET BenutzerSperrcode = '" + sperrCode +
                    "',  BenutzerTrackChangeUsr = '" + currentUser.getUserName() +
                    "', BenutzerTrackChangeTimestamp = CURRENT_TIMESTAMP " +
                    "WHERE BenutzerId=" + user.getUserId();
            st.execute(query);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
