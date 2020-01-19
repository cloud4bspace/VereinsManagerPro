package space.cloud4b.verein.services;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import org.apache.commons.codec.digest.DigestUtils;
import space.cloud4b.verein.model.verein.adressbuch.Kontakt;
import space.cloud4b.verein.model.verein.adressbuch.Mitglied;
import space.cloud4b.verein.model.verein.kalender.Jubilaeum;
import space.cloud4b.verein.model.verein.kalender.Teilnehmer;
import space.cloud4b.verein.model.verein.kalender.Termin;
import space.cloud4b.verein.model.verein.kontrolle.rangliste.Position;
import space.cloud4b.verein.model.verein.status.Status;
import space.cloud4b.verein.model.verein.status.StatusElement;
import space.cloud4b.verein.model.verein.task.Task;
import space.cloud4b.verein.model.verein.user.User;
import space.cloud4b.verein.services.connection.MysqlConnection;

import java.sql.Date;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.*;

public abstract class DatabaseReader {

    /**
     * ermittelt die Anzahl der Mitglieder aus der Datenbank und gibt diese als int zurück
     *
     * @return die Anzahl der Mitglieder als int
     */
    public static int readAnzahlMitglieder() {
        try (Connection conn = new MysqlConnection().getConnection();
             Statement st = conn.createStatement()) {
            String query = "SELECT COUNT(*) AS AnzahlMitglieder FROM kontakt WHERE KontaktIstMitglied = 1";
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                return rs.getInt("AnzahlMitglieder");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * ermittelt die Anzahl Einträge in der Tabelle "terminkontrolle" für die Erfassung
     * der Anwesenheiten (Präsenzkontrolle) und gibt diese als int zurück
     * @return die Anzahl der Anwesenheiten als int
     */
    public static int getAnzAnwesenheiten() {
        try (Connection conn = new MysqlConnection().getConnection();
             Statement st = conn.createStatement()) {
            String query = "SELECT COUNT(*) Anzahl FROM terminkontrolle WHERE KontrolleArt = 'Anwesenheit'";
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                return rs.getInt("Anzahl");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * ermittelt für einen bestimmten Termin die Anzahl der anwesenden Personen gemäss
     * der Präsenzkontrolle (Tabelle "terminkontrolle")
     */
    public static int getAnzAnwesenheiten(Termin termin) {
        try (Connection conn = new MysqlConnection().getConnection();
             Statement st = conn.createStatement()) {
            String query = "SELECT COUNT(*) Anzahl FROM terminkontrolle WHERE KontrolleArt = 'Anwesenheit' " +
                    "AND KontrolleWert= 1 AND KontrolleTerminId=" + termin.getTerminId();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                return rs.getInt("Anzahl");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * ermittelt für einen bestimmten Termin die Anzahl der abwesenden (entschuldigten) Personen gemäss
     * der Präsenzkontrolle (Tabelle "terminkontrolle")
     */
    public static int getAnzEntschuldigteAbwesenheiten(Termin termin) {
        try (Connection conn = new MysqlConnection().getConnection();
             Statement st = conn.createStatement()) {
            String query = "SELECT COUNT(*) Anzahl FROM terminkontrolle WHERE KontrolleArt = 'Anwesenheit' " +
                    "AND KontrolleWert= 2 AND KontrolleTerminId=" + termin.getTerminId();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                return rs.getInt("Anzahl");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * ermittelt für einen bestimmten Termin die Anzahl der abwesenden (unentschuldigten) Personen gemäss
     * der Präsenzkontrolle (Tabelle "terminkontrolle")
     */
    public static int getAnzUnentschuldigteAbwesenheiten(Termin termin) {
        try (Connection conn = new MysqlConnection().getConnection();
             Statement st = conn.createStatement()) {
            String query = "SELECT COUNT(*) Anzahl FROM terminkontrolle WHERE KontrolleArt = 'Anwesenheit' " +
                    "AND KontrolleWert= 3 AND KontrolleTerminId=" + termin.getTerminId();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                return rs.getInt("Anzahl");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * ermittelt die anzahl Mitglieder, die sich zu einem Termin eingetragen haben (an-/abmeldung/vielleicht)
     */
    public static int getAnzMeldungen() {
        try (Connection conn = new MysqlConnection().getConnection();
             Statement st = conn.createStatement()) {
            //String query = "SELECT COUNT(*) Anzahl FROM terminkontrolle WHERE KontrolleArt = 'Anmeldung'";
            String query = "SELECT COUNT(*) Anzahl FROM terminkontrolle";
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                return rs.getInt("Anzahl");
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return 0;
    }

    /**
     * ermittelt die anzahl angemeldete Teilnehmer zu einem Termin
     */
    public static int getAnzAnmeldungen(Termin termin) {
        try (Connection conn = new MysqlConnection().getConnection();
             Statement st = conn.createStatement()) {
            String query = "SELECT COUNT(*) Anzahl FROM terminkontrolle WHERE KontrolleTerminId = "
                    + termin.getTerminId() +  " AND KontrolleArt = 'Anmeldung' AND KontrolleWert = 1";
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                return rs.getInt("Anzahl");
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return 0;
    }

    /**
     * ermittelt die Anzahl der provisorisch angemeldeten Teilnehmer zu einem Termin
     */
    public static int getAnzVielleicht(Termin termin) {
        try (Connection conn = new MysqlConnection().getConnection();
             Statement st = conn.createStatement()) {
            String query = "SELECT COUNT(*) Anzahl FROM terminkontrolle WHERE KontrolleTerminId = "
                    + termin.getTerminId() +  " AND KontrolleArt = 'Anmeldung' AND KontrolleWert = 3";
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                return rs.getInt("Anzahl");
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return 0;
    }

    /**
     * ermittelt die Anzahl der provisorisch angemeldeten Teilnehmer zu einem Termin
     */
    public static int getAnzNein(Termin termin) {
        try (Connection conn = new MysqlConnection().getConnection();
             Statement st = conn.createStatement()) {
            String query = "SELECT COUNT(*) Anzahl FROM terminkontrolle WHERE KontrolleTerminId = "
                    + termin.getTerminId() +  " AND KontrolleArt = 'Anmeldung' AND KontrolleWert = 2";
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                return rs.getInt("Anzahl");
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return 0;
    }

    /**
     * Verantwortliche ermitteln für einen Task
     */
    public static ArrayList<Integer> getVerantwortlicheId(Task task) {
        ArrayList<Integer> verantwortliche = new ArrayList<>();
        try (Connection conn = new MysqlConnection().getConnection();
             Statement st = conn.createStatement()) {
            String query = "SELECT * FROM taskZuordnung WHERE TaskId=" + task.getTaskId();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                verantwortliche.add(rs.getInt("KontaktId"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return verantwortliche;

    }

    /**
     * Ermittelt die Teilnehmer zu einem bestimmten Termin
     *
     * @param termin
     * @param mitgliederListe
     * @return Teilnehmer als ArrayList
     */
    public static ArrayList<Teilnehmer> getTeilnehmer(Termin termin, ArrayList<Mitglied> mitgliederListe) {
        ArrayList<Teilnehmer> teilnehmerListe = new ArrayList<>();
        Status anmeldung = new Status(5);
        Status teilnahme = new Status(6);
        int terminId = termin.getTerminId();
        try (Connection conn = new MysqlConnection().getConnection();
             Statement st = conn.createStatement()) {
            String query = "SELECT kontakt.KontaktId, kontakt.KontaktNachname, kontakt.KontaktVorname " +
                    "FROM terminkontrolle LEFT JOIN kontakt ON kontakt.KontaktId = KontrolleMitgliedId " +
                    "WHERE KontrolleArt = 'Anmeldung' AND KontrolleTerminId = " + terminId +
                    " GROUP BY KontrolleMitgliedId";

            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                for (int i = 0; i < mitgliederListe.size(); i++) {
                    if (rs.getInt("KontaktId") == mitgliederListe.get(i).getId()) {
                        teilnehmerListe.add(new Teilnehmer(mitgliederListe.get(i)));
                    }
                }
            }
            int i = 0;
            while (teilnehmerListe.size() > i) {
                int teilId = teilnehmerListe.get(i).getMitglied().getId();
                query = "SELECT `KontrolleWert` AS AnmeldeStatus, KontrolleBemerkungen FROM `terminkontrolle` " +
                        "WHERE `KontrolleTerminId` = " + terminId + " " +
                        "AND `KontrolleMitgliedId` = " + teilId +
                        " AND `KontrolleArt`='Anmeldung'";
                rs = st.executeQuery(query);
                while (rs.next()) {
                    // anredeStatus.getStatusElemente().get(rs.getInt("KontaktAnredeStatus"))
                    teilnehmerListe.get(i).setAnmeldeStatus(anmeldung.getStatusElemente().get(rs
                            .getInt("AnmeldeStatus")));
                    teilnehmerListe.get(i).setAnmeldungText(rs.getString("KontrolleBemerkungen"));
                }
                i++;

            }
            // Infos zum Teilnahmestatus hinzufügen
            i = 0;
            while (teilnehmerListe.size() > i) {
                int teilId = teilnehmerListe.get(i).getMitglied().getId();
                query = "SELECT `KontrolleWert` AS TeilnahmeStatus, KontrolleBemerkungen FROM `terminkontrolle` " +
                        "WHERE `KontrolleTerminId` = " + terminId + " " +
                        "AND `KontrolleMitgliedId` = " + teilId +
                        " AND `KontrolleArt`='Anwesenheit'";
                rs = st.executeQuery(query);
                while (rs.next()) {
                    // anredeStatus.getStatusElemente().get(rs.getInt("KontaktAnredeStatus"))
                    teilnehmerListe.get(i).setTeilnahmeStatus(teilnahme.getStatusElemente().get(rs
                            .getInt("TeilnahmeStatus")));
                    teilnehmerListe.get(i).setTeilnahmeText(rs.getString("KontrolleBemerkungen"));
                }
                i++;

            }
            return teilnehmerListe;
        } catch (SQLException e) {
            e.printStackTrace();
            return teilnehmerListe;
        }
    }
    /**
     * ermittelt die Teilnehmerliste zu einem Termin
     */
    public static ArrayList<Teilnehmer> getTeilnehmer(Termin termin) {
        ArrayList<Teilnehmer> teilnehmerListe = new ArrayList<>();
        Status anmeldung = new Status(5);
        Status teilnahme = new Status(6);
        int terminId = termin.getTerminId();
        try (Connection conn = new MysqlConnection().getConnection();
             Statement st = conn.createStatement()) {
            String query = "SELECT kontakt.KontaktId, kontakt.KontaktNachname, kontakt.KontaktVorname " +
                    "FROM terminkontrolle LEFT JOIN kontakt ON kontakt.KontaktId = KontrolleMitgliedId " +
                    "WHERE KontrolleArt = 'Anmeldung' AND KontrolleTerminId = " + terminId +
                    " GROUP BY KontrolleMitgliedId";

            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                Mitglied mitglied = new Mitglied(rs.getInt("KontaktId"),
                        rs.getString("KontaktNachname"), rs.getString("KontaktVorname"));
                DatabaseReader.completeMitglied(mitglied);
                teilnehmerListe.add(
                        new Teilnehmer(mitglied));
            }
            int i = 0;
            while(teilnehmerListe.size() > i){
                int teilId = teilnehmerListe.get(i).getMitglied().getId();
                query = "SELECT `KontrolleWert` AS AnmeldeStatus, KontrolleBemerkungen FROM `terminkontrolle` " +
                        "WHERE `KontrolleTerminId` = " + terminId + " " +
                        "AND `KontrolleMitgliedId` = " + teilId +
                        " AND `KontrolleArt`='Anmeldung'";
                rs = st.executeQuery(query);
                while (rs.next()) {
                    // anredeStatus.getStatusElemente().get(rs.getInt("KontaktAnredeStatus"))
                    teilnehmerListe.get(i).setAnmeldeStatus(anmeldung.getStatusElemente()
                            .get(rs.getInt("AnmeldeStatus")));
                    teilnehmerListe.get(i).setAnmeldungText(rs.getString("KontrolleBemerkungen"));
                }
                i++;

            }
            // Infos zum Teilnahmestatus hinzufügen
            i = 0;
            while(teilnehmerListe.size() > i){
                int teilId = teilnehmerListe.get(i).getMitglied().getId();
                query = "SELECT `KontrolleWert` AS TeilnahmeStatus, KontrolleBemerkungen FROM `terminkontrolle` " +
                        "WHERE `KontrolleTerminId` = " + terminId + " " +
                        "AND `KontrolleMitgliedId` = " + teilId +
                        " AND `KontrolleArt`='Anwesenheit'";
                rs = st.executeQuery(query);
                while (rs.next()) {
                    // anredeStatus.getStatusElemente().get(rs.getInt("KontaktAnredeStatus"))
                    teilnehmerListe.get(i).setTeilnahmeStatus(teilnahme.getStatusElemente().get(rs
                            .getInt("TeilnahmeStatus")));
                    teilnehmerListe.get(i).setTeilnahmeText(rs.getString("KontrolleBemerkungen"));
                }
                i++;

            }
            return teilnehmerListe;
        } catch (SQLException e) {
            e.printStackTrace();
            return teilnehmerListe;
        }
    }

    /**
     * Ist eMail eines Mitglieds?
     *
     * @return gibt die Mitglieder-Nr zurück
     */
    public static int isMitgliedEmail(String eMail) {
        try (Connection conn = new MysqlConnection().getConnection();
             Statement st = conn.createStatement()) {
            String query = "SELECT * FROM kontakt WHERE (KontaktEMail = '" + eMail + "' OR KontaktEMailII = '" + eMail + "')" +
                    " AND KontaktIstMitglied = 1";
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                return rs.getInt("KontaktId");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Ermittelt die Mitgliederkategorie
     *
     * @return gibt die Mitglieder-Nr zurück
     */
    public static String getMitgliederKategorie(int mitgliedId) {
        String userKat = null;
        try (Connection conn = new MysqlConnection().getConnection();
             Statement st = conn.createStatement()) {
            String query = "SELECT StatusElementNameLong FROM kontakt LEFT JOIN statusElement ON KontaktKategorieA" +
                    " = StatusElementKey WHERE StatusId = 2 AND KontaktId = " + mitgliedId;
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                userKat = rs.getString("StatusElementNameLong");
            }
            return userKat;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userKat;
    }

    /**
     * Ermittelt Vorstandsstatus
     *
     * @return gibt die Mitglieder-Nr zurück
     */
    public static boolean getVorstandsStatus(int mitgliedId) {
        try (Connection conn = new MysqlConnection().getConnection();
             Statement st = conn.createStatement()) {
            String query = "SELECT KontaktIstVorstandsmitglied FROM kontakt WHERE KontaktId = " + mitgliedId;
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                return rs.getBoolean("KontaktIstVorstandsmitglied");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * ermittelt Vor- und Nachname des Mitglieds
     *
     * @return gibt die Mitglieder-Nr zurück
     */
    public static String getUserNameVorname(int kontaktId) {
        String userTxt = null;
        try (Connection conn = new MysqlConnection().getConnection();
             Statement st = conn.createStatement()) {
            String query = "SELECT * FROM kontakt WHERE KontaktId=" + kontaktId;
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                userTxt = rs.getString("KontaktNachname");
                userTxt += " " + rs.getString("KontaktVorname");
            }
            return userTxt;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userTxt;
    }

    /**
     * Ist eMail eines Mitglieds?
     *
     * @return gibt die Mitglieder-Nr zurück
     */
    public static boolean isUser(int mitgliedId) {
        try (Connection conn = new MysqlConnection().getConnection();
             Statement st = conn.createStatement()) {
            String query = "SELECT COUNT(*) AS Treffer FROM benutzer WHERE KontaktId = '" + mitgliedId + "' AND BenutzerSperrcode = 0";
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                return rs.getInt("Treffer") == 1;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * check User credentials
     */
    public static boolean checkUserCredentials(String eMail, String pw) {
        try (Connection conn = new MysqlConnection().getConnection();
             Statement st = conn.createStatement()) {
            String query = "SELECT COUNT(*) Treffer FROM benutzer WHERE BenutzerName = '" + eMail + "' AND BenutzerPW=" +
                    "'" + DigestUtils.sha1Hex(pw + eMail) + "' AND BenutzerSperrcode=0";
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                return rs.getInt("Treffer") == 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    /**
     * Zählt die Anzahl Termine im laufenden Jahr
     * @return
     */
    public static int readAnzahlTermine() {
        try (Connection conn = new MysqlConnection().getConnection();
             Statement st = conn.createStatement()) {
            String query = "SELECT COUNT(*) AS AnzahlTermine FROM termin WHERE YEAR(TerminDatum) = YEAR(CURRENT_DATE)";
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                return rs.getInt("AnzahlTermine");
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return 0;
    }

    /**
     * Ermittelt den Zeitstempel der letzen Änderung in der Tabelle termin
     *
     * @return Timestamp der letzten Änderung
     */
    public static Timestamp readLetzteAenderung() {
        try (Connection conn = new MysqlConnection().getConnection();
             Statement st = conn.createStatement()) {
            String query = "SELECT MAX(KontaktTrackChangeTimestamp) AS LetzteAenderung FROM usr_web116_5.kontakt";
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                return Timestamp.valueOf(rs.getString("LetzteAenderung"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Ermittelt den Zeitstempel der letzen Änderung in der Tabelle terminkontrolle
     *
     * @return Timestamp der letzten Änderung
     */
    public static Timestamp readLetzteAnmeldungAenderung() {
        try (Connection conn = new MysqlConnection().getConnection();
             Statement st = conn.createStatement()) {
            String query = "SELECT MAX(KontrolleZeitstempel) AS LetzteAenderung " +
                    "FROM `terminkontrolle` WHERE KontrolleArt = 'Anmeldung'";
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                return Timestamp.valueOf(rs.getString("LetzteAenderung"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Ermittelt den Zeitstempel der letzen Änderung in der Tabelle task
     *
     * @return Timestamp der letzten Änderung
     */
    public static Timestamp readLetzteAenderungTask() {
        try (Connection conn = new MysqlConnection().getConnection();
             Statement st = conn.createStatement()) {
            String query = "SELECT MAX(TaskTrackChangeTimestamp) AS LetzteAenderung FROM `task`";
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                return Timestamp.valueOf(rs.getString("LetzteAenderung"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void completeMitglied(Mitglied mitglied) {
        Status anredeStatus = new Status(1);
        Status kategorieIStatus = new Status(2);
        Status kategorieIIStatus = new Status(4);
        try (Connection conn = new MysqlConnection().getConnection();
             Statement st = conn.createStatement()) {
            String query = "SELECT * from usr_web116_5.kontakt WHERE KontaktID=" + mitglied.getId();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                mitglied.setAdresse(rs.getString("KontaktAdresse"));
                mitglied.setAdresszusatz(rs.getString("KontaktAdresszusatz"));

                mitglied.setPlz(rs.getInt("KontaktPLZ"));
                mitglied.setOrt(rs.getString("KontaktOrt"));

                mitglied.setBemerkungen(rs.getString("KontaktBemerkungen"));
                mitglied.setMobile(rs.getString("KontaktMobile"));
                mitglied.setTelefon(rs.getString("KontaktTelefon"));
                mitglied.setEmail(rs.getString("KontaktEMail"));
                mitglied.setEmailII(rs.getString("KontaktEMailII"));
                mitglied.setLetzteAenderungUser(rs.getString("KontaktTrackChangeUsr"));
                mitglied.setLetzteAenderungTimestamp(rs.getTimestamp("KontaktTrackChangeTimestamp"));

                String kontaktGeburtsdatumString = rs.getString("KontaktGeburtsdatum");
                String kontaktAustrittsdatumString = rs.getString("KontaktAustrittsdatum");
                LocalDate kontaktGeburtsdatum;
                LocalDate kontaktAustrittsdatum = null;
                if (kontaktGeburtsdatumString != null && kontaktGeburtsdatumString != "0000-00-00"
                        && !kontaktGeburtsdatumString.isEmpty()) {
                    kontaktGeburtsdatum = Date.valueOf(kontaktGeburtsdatumString).toLocalDate();
                    mitglied.setGeburtsdatum(kontaktGeburtsdatum);
                }


                if (kontaktAustrittsdatumString != null && kontaktAustrittsdatumString != "0000-00-00"
                        && !kontaktAustrittsdatumString.isEmpty()) {
                    kontaktAustrittsdatum = Date.valueOf(kontaktAustrittsdatumString).toLocalDate();
                    mitglied.setAustrittsDatum(kontaktAustrittsdatum);
                }

                //kontakt.setAnredeStatus(rs.getInt("KontaktAnredeStatus"));
                mitglied.setAnredeStatus(anredeStatus.getStatusElemente().get(rs
                        .getInt("KontaktAnredeStatus")));

                if (mitglied instanceof Mitglied) {
                    String kontaktEintrittsdatumString = rs.getString("KontaktEintrittsdatum");
                    LocalDate kontaktEintrittsdatum = null;
                    if (kontaktEintrittsdatumString != null && kontaktEintrittsdatumString != "0000-00-00") {
                        kontaktEintrittsdatum = Date.valueOf(kontaktEintrittsdatumString).toLocalDate();
                    }

                    mitglied.setEintrittsDatum(kontaktEintrittsdatum);
                    mitglied.setKategorieIStatus(kategorieIStatus.getStatusElemente()
                            .get(rs.getInt("KontaktKategorieA")));
                    mitglied.setKategorieIIStatus(kategorieIIStatus.getStatusElemente()
                            .get(rs.getInt("KontaktKategorieB")));
                    mitglied.setIstVorstandsmitglied(rs.getBoolean("KontaktIstVorstandsmitglied"));
                }

            }

        } catch (SQLException e) {
            e.printStackTrace();

        }

    }
    public static ArrayList<Mitglied> getMitgliederAsArrayList() {
        Status anredeStatus = new Status(1);
        Status kategorieIStatus = new Status(2);
        Status kategorieIIStatus = new Status(4);
        ArrayList<Mitglied> mitgliederListe = new ArrayList<>();
        ArrayList<Kontakt> kontaktListe = new ArrayList<>();
        LocalDate heute = LocalDate.now();
        try (Connection conn = new MysqlConnection().getConnection();
             Statement st = conn.createStatement()) {
            String query = "SELECT * from usr_web116_5.kontakt ORDER BY KontaktNachname, KontaktVorname";
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                Kontakt kontakt;
                int kontaktId = rs.getInt("KontaktId");
                String kontaktNachname = rs.getString("KontaktNachname");
                String kontaktVorname = rs.getString("KontaktVorname");

                // Objekte werden erzeugt
                if (rs.getBoolean("KontaktIstMitglied")) {
                    kontakt = new Mitglied(kontaktId, kontaktNachname, kontaktVorname);
                } else {
                    kontakt = new Kontakt(kontaktId, kontaktNachname, kontaktVorname);
                }

                // Instanzvariabeln der neuen Objekte werden vervollständigt
                kontakt.setAdresse(rs.getString("KontaktAdresse"));
                kontakt.setAdresszusatz(rs.getString("KontaktAdresszusatz"));

                kontakt.setPlz(rs.getInt("KontaktPLZ"));
                kontakt.setOrt(rs.getString("KontaktOrt"));

                kontakt.setBemerkungen(rs.getString("KontaktBemerkungen"));
                kontakt.setMobile(rs.getString("KontaktMobile"));
                kontakt.setTelefon(rs.getString("KontaktTelefon"));
                kontakt.setEmail(rs.getString("KontaktEMail"));
                kontakt.setEmailII(rs.getString("KontaktEMailII"));
                kontakt.setLetzteAenderungUser(rs.getString("KontaktTrackChangeUsr"));
                kontakt.setLetzteAenderungTimestamp(rs.getTimestamp("KontaktTrackChangeTimestamp"));

                String kontaktGeburtsdatumString = rs.getString("KontaktGeburtsdatum");
                String kontaktAustrittsdatumString = rs.getString("KontaktAustrittsdatum");
                LocalDate kontaktGeburtsdatum = null;
                LocalDate kontaktAustrittsdatum = null;
                if (kontaktGeburtsdatumString != null && kontaktGeburtsdatumString != "0000-00-00"
                        && !kontaktGeburtsdatumString.isEmpty()) {
                    kontaktGeburtsdatum = Date.valueOf(kontaktGeburtsdatumString).toLocalDate();
                    kontakt.setGeburtsdatum(kontaktGeburtsdatum);
                }


                if (kontaktAustrittsdatumString != null && kontaktAustrittsdatumString != "0000-00-00"
                        && !kontaktAustrittsdatumString.isEmpty()) {
                    kontaktAustrittsdatum = Date.valueOf(kontaktAustrittsdatumString).toLocalDate();
                    kontakt.setAustrittsDatum(kontaktAustrittsdatum);
                }

                kontakt.setAnredeStatus(anredeStatus.getStatusElemente()
                        .get(rs.getInt("KontaktAnredeStatus")));

                if (kontakt instanceof Mitglied) {
                    String kontaktEintrittsdatumString = rs.getString("KontaktEintrittsdatum");
                    LocalDate kontaktEintrittsdatum = null;
                    if (kontaktEintrittsdatumString != null && kontaktEintrittsdatumString != "0000-00-00") {
                        kontaktEintrittsdatum = Date.valueOf(kontaktEintrittsdatumString).toLocalDate();
                    }

                    ((Mitglied) kontakt).setEintrittsDatum(kontaktEintrittsdatum);
                    ((Mitglied) kontakt).setKategorieIStatus(kategorieIStatus
                            .getStatusElemente().get(rs.getInt("KontaktKategorieA")));
                    ((Mitglied) kontakt).setKategorieIIStatus(kategorieIIStatus
                            .getStatusElemente().get(rs.getInt("KontaktKategorieB")));
                    ((Mitglied) kontakt).setIstVorstandsmitglied(rs
                            .getBoolean("KontaktIstVorstandsmitglied"));
                }

                /**
                 * Kontakt wird zur Liste hinzugefügt
                 */
                kontaktListe.add(kontakt);
                if(kontakt instanceof Mitglied) {
                    mitgliederListe.add((Mitglied) kontakt);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mitgliederListe;
    }

    /**
     *
     */
    public static ObservableList<PieChart.Data> getDataForPieChart01() {
        try (Connection conn = new MysqlConnection().getConnection();
             Statement st = conn.createStatement()) {

            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

            String query = (
                    "SELECT COUNT(*) AS MembersCount, statusElement.StatusElementNameLong AS StatusLong, statusElement.StatusElementNameShort AS StatusShort FROM kontakt LEFT JOIN statusElement ON statusElement.StatusElementKey = kontakt.KontaktKategorieA WHERE statusElement.StatusId=2 GROUP BY statusElement.StatusElementKey"
            );
            ResultSet result = st.executeQuery(query);
            while (result.next()) {

                pieChartData.add(new PieChart.Data(result.getString("StatusLong"),
                        result.getInt("MembersCount")));

            }
            return pieChartData;
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return null;
    }

    /**
     *
     */
    public static ObservableList<PieChart.Data> getDataForTaskPieChart01() {
        try (Connection conn = new MysqlConnection().getConnection();
             Statement st = conn.createStatement()) {

            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

            String query = (
                    "SELECT COUNT(*) AS TasksCount, statusElement.StatusElementNameLong AS StatusLong, statusElement.StatusElementNameShort AS StatusShort FROM task LEFT JOIN statusElement ON statusElement.StatusElementKey = task.TaskStatus WHERE statusElement.StatusId=8 GROUP BY statusElement.StatusElementKey"
            );
            ResultSet result = st.executeQuery(query);
            while (result.next()) {

                pieChartData.add(new PieChart.Data(result.getString("StatusLong")
                        , result.getInt("TasksCount")));

            }
            return pieChartData;
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return null;
    }

    public static ArrayList<Position> fuelleRangliste(ArrayList<Mitglied> mitgliederListe
            , ArrayList<Termin> terminListe) {
        ArrayList<Position> rangliste = new ArrayList<>();
        // ArrayList<Mitglied> mitgliederListe = DatabaseReader.getMitgliederAsArrayList();
        // ArrayList<Termin> terminListe = DatabaseReader.getTermineAsArrayList();

        Iterator<Mitglied> i = mitgliederListe.iterator();

        while (i.hasNext()) {
            String query;
            int anzTermineAktuellesJahr = 0;
            int anzAnwesenheiten = 0;
            double anwesenheitenAnteil; //Anteil in Prozent
            // Fokus 1 = dieses Jahr
            Mitglied mitglied = i.next();



            try (Connection conn = new MysqlConnection().getConnection(); Connection conn2 = new MysqlConnection().getConnection(); Statement st = conn.createStatement(); Statement st2 = conn2.createStatement()) {
                query = "SELECT COUNT(*) AS anzahlTermine FROM `termin` WHERE YEAR(TerminDatum) " +
                        "= YEAR(CURRENT_DATE) AND `TerminDatum` >= '" + mitglied.getEintrittsDatumAsISOString() + "'";


                ResultSet rs = st.executeQuery(query);
                while (rs.next()) {
                    anzTermineAktuellesJahr = rs.getInt("anzahlTermine");
                }

                // Anzahl der Anwesenheiten ermitteln für dieses Mitglied
                query = "SELECT COUNT(*) AS anzAnwesenheiten FROM `terminkontrolle` LEFT JOIN kontakt " +
                        "ON kontakt.KontaktId = `KontrolleMitgliedId` LEFT JOIN termin ON termin.TerminId " +
                        "= `KontrolleTerminId`WHERE `KontrolleMitgliedId`=" + mitglied.getId() +
                        " AND `KontrolleArt`='Anwesenheit' " +
                        "AND KontrolleWert = 1 AND YEAR(termin.TerminDatum) = YEAR(CURRENT_DATE)";


                ResultSet rs2 = st2.executeQuery(query);
                while (rs2.next()) {
                    anzAnwesenheiten = rs2.getInt("anzAnwesenheiten");
                }
                anwesenheitenAnteil = (anzAnwesenheiten * 100.0) / anzTermineAktuellesJahr;
                anwesenheitenAnteil = Math.round(anwesenheitenAnteil * 100.0) / 100.0;

                rangliste.add(new Position(mitglied, mitglied.getKurzbezeichnung(), anzTermineAktuellesJahr, anzAnwesenheiten, anwesenheitenAnteil));
                // rangliste sortieren nach Anteilen...
                //Collections.sort(rangliste, (a,b)->a.getAnwesenheitsAnteil().compareTo(b.getAnwesenheitsAnteil()));


            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // Rangliste sortieren, absteigend nach Anwesenheitsanteil
        rangliste.sort(Comparator.comparingDouble(Position::getAnwesenheitsAnteil).reversed());


        // Ränge setzen
        Iterator<Position> p = rangliste.iterator();
        int rangZaehler = 0;
        double anteilVorgaenger = 0;
        while (p.hasNext()) {
            Position position = p.next();

            // behandle die erste Position in der Rangliste
            if (rangZaehler == 0) {
                position.setRangYTD(++rangZaehler);
            } else {
                if (position.getAnwesenheitsAnteil() < anteilVorgaenger) {
                    rangZaehler++;
                }
                position.setRangYTD(rangZaehler);
            }
            anteilVorgaenger = position.getAnwesenheitsAnteil();
        }

        return rangliste;

    }

    public static ArrayList<Termin> termineLaden() {
        ArrayList<Termin> terminListe = new ArrayList<>();
        try (Connection conn = new MysqlConnection().getConnection(); Statement st = conn.createStatement()) {
            String query = "SELECT * from usr_web116_5.termin ORDER BY TerminDatum ASC";
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                Termin termin;
                LocalDateTime terminZeit;
                LocalDateTime terminZeitBis = null;
                int terminId = rs.getInt("TerminId");
                LocalDate terminDatum = Date.valueOf(rs.getString("TerminDatum")).toLocalDate();

                String terminText = rs.getString("TerminText");

                /**
                 * Objekte werden erzeugt und der Terminliste hinzugefügt
                 */
                termin = new Termin(terminId, terminDatum, terminText);
                if (rs.getString("TerminZeit") != null) {
                    terminZeit = LocalDateTime.of(terminDatum, Time.valueOf(rs.getString("TerminZeit")).toLocalTime());
                    termin.setZeit(terminZeit);
                }
                if (rs.getString("TerminZeitBis") != null) {
                    terminZeitBis = LocalDateTime.of(terminDatum, Time.valueOf(rs.getString("TerminZeitBis")).toLocalTime());
                    termin.setZeitBis(terminZeitBis);
                }

                terminListe.add(termin);

            }

        } catch (SQLException e) {
            e.printStackTrace();

        }
        return terminListe;
    }


    /**
     * Ermittelt verschiedene Daten zum übergebenen Status-Objekt und
     * ergänzt die entsprechenden Instanzvariabeln
     *
     * @param status
     */
    public static void statusInfosSetzen(Status status) {
        try (Connection conn = new MysqlConnection().getConnection();
             Statement st = conn.createStatement()) {
            String query = "SELECT * FROM status WHERE StatusId=" + status.getStatusId();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                status.setStatusTextLang(rs.getString("StatusNameLong"));
                status.setStatusTextKurz(rs.getString("StatusNameShort"));
                status.setStatusSymbol(rs.getString("StatusSymbol"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Ermittelt die Status-Elemente zur übergebenen Status-ID und
     * instanziert die einzelnen Status-Elemente und fügt diese zur
     * HashMap hinzu und gibt diese zurück zum Status-Objekt
     *
     * @param statusId eindeutige ID-Nummer des Status-Objekts
     * @return HashMap mit den einzelnen Status-Elementen
     */
    public static HashMap<Integer, StatusElement> statusHashMapLaden(int statusId) {
        HashMap<Integer, StatusElement> statusHashMap = new HashMap<>();

        try (Connection conn = new MysqlConnection().getConnection();
             Statement st = conn.createStatement()) {
            String query = "SELECT status.StatusId AS StatusId, StatusNameLong, StatusNameShort, StatusElementKey, "
                    + "StatusElementNameLong, StatusElementNameShort, StatusElementUnicodeChar FROM usr_web116_5.status" +
                    " LEFT JOIN usr_web116_5.statusElement ON status.StatusId = statusElement.StatusId" +
                    " WHERE status.statusId=" + statusId + " ORDER BY `statusElement`.`StatusElementKey` ASC;";

            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                statusHashMap.put(rs.getInt("StatusElementKey")
                        , new StatusElement(rs.getInt("StatusElementKey")
                                , rs.getString("StatusElementNameLong")
                                , rs.getString("StatusElementNameShort")
                                , rs.getString("StatusElementUnicodeChar")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return statusHashMap;
    }

    /**
     * Gibt die Statuselemente aus der Datenbank als ArrayList<StatusElement> zurück
     *
     * @return ArrayList der Statuselemente
     */
    public static ArrayList<StatusElement> getStatusElementeAsArrayList() {
        ArrayList<StatusElement> statusElementListe = new ArrayList<>();
        try (Connection conn = new MysqlConnection().getConnection();
             Statement st = conn.createStatement()) {
            String query = "SELECT status.StatusId AS StatusId, StatusNameLong, StatusElementId, StatusElementKey," +
                    " StatusElementNameLong, StatusElementNameShort, StatusElementUnicodeChar FROM statusElement " +
                    "LEFT JOIN status ON status.StatusId = statusElement.StatusId ORDER BY StatusId, StatusElementKey";

            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                statusElementListe.add(new StatusElement(
                        rs.getInt("StatusId"),
                        rs.getString("StatusNameLong"),
                        rs.getInt("StatusElementId"),
                        rs.getInt("StatusElementKey"),
                        rs.getString("StatusElementNameLong"),
                        rs.getString("StatusElementNameShort"),
                        rs.getString("StatusElementUnicodeChar")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return statusElementListe;
    }

    /**
     * ermittelt die Anzahl der Statuselemente aus der Datenbank und gibt diese zurück
     *
     * @return Anzahl der Statuselemente als int
     */
    public static int getAnzahlStatusElemente() {
        int anzahlStatusElemente = 0;
        try (Connection conn = new MysqlConnection().getConnection();
             Statement st = conn.createStatement()) {
            String query = "SELECT COUNT(*) AS Anzahl FROM statusElement";
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                anzahlStatusElemente = rs.getInt("Anzahl");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return anzahlStatusElemente;
    }

    /**
     * Ermittelt die künftigen Termine aus der Datenbank und gibt diese als ArrayList zurück
     *
     * @return Terminliste als ArrayList<Termin>
     */
    public static ArrayList<Termin> getKommendeTermineAsArrayList() {
        ArrayList<Termin> terminListe = new ArrayList<>();
        try (Connection conn = new MysqlConnection().getConnection(); Statement st = conn.createStatement()) {
            String query =
                    "SELECT * from usr_web116_5.termin WHERE TerminDatum >= CURRENT_DATE() ORDER BY TerminDatum ASC";
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                Termin termin;
                LocalDateTime terminZeit = null;
                LocalDateTime terminZeitBis = null;
                int terminId = rs.getInt("TerminId");
                LocalDate terminDatum = Date.valueOf(rs.getString("TerminDatum")).toLocalDate();
                String terminText = rs.getString("TerminText");
                String terminOrt = rs.getString("TerminOrt");

                // Objekte werden erzeugt und der Terminliste hinzugefügt
                termin = new Termin(terminId, terminDatum, terminText, terminOrt);
                if(rs.getString("TerminZeit") != null) {
                    terminZeit = LocalDateTime.of(terminDatum, Time.valueOf(rs.getString("TerminZeit"))
                            .toLocalTime());
                    termin.setZeit(terminZeit);
                }
                if (rs.getString("TerminZeitBis") != null) {
                    terminZeitBis = LocalDateTime.of(terminDatum,
                            Time.valueOf(rs.getString("TerminZeitBis")).toLocalTime());
                    termin.setZeitBis(terminZeitBis);
                }
                terminListe.add(termin);
            }
        } catch (SQLException e) {
            e.printStackTrace(); }
        return terminListe;
    }

    /**
     * Ermittelt die Benutzer aus der Datenbank und gibt diese als ArrayList<User> zurück
     *
     * @return die Benuterliste als ArrayList<User>
     */
    public static ArrayList<User> getBenutzerAsArrayList() {
        ArrayList<User> userListe = new ArrayList<>();
        try (Connection conn = new MysqlConnection().getConnection(); Statement st = conn.createStatement()) {
            String query = "SELECT * from usr_web116_5.benutzer ORDER BY BenutzerName ASC";
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                userListe.add(new User(rs.getInt("BenutzerId"), rs.getInt("KontaktId"),
                        rs.getString("BenutzerName"), rs.getString("BenutzerPw"),
                        Date.valueOf(rs.getString("BenutzerLastlogin")).toLocalDate(),
                        rs.getInt("BenutzerNumberLogins"), rs.getInt("BenutzerSperrcode")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userListe;
    }

    /**
     * ermittelt die Anzahl der Termine aus der Datenbank und gibt diese als int zurück
     *
     * @return die Anzahl der Termin als int
     */
    public static ArrayList<Termin> getTermineAsArrayList() {
        Status kategorieIStatus = new Status(2);
        Status kategorieIIStatus = new Status(4);
        ArrayList<Termin> terminListe = new ArrayList<>();
        try (Connection conn = new MysqlConnection().getConnection(); Statement st = conn.createStatement()) {
            String query = "SELECT * from usr_web116_5.termin ORDER BY TerminDatum ASC";
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                Termin termin;
                LocalDateTime terminZeit = null;
                LocalDateTime terminZeitBis = null;
                int terminId = rs.getInt("TerminId");
                LocalDate terminDatum = Date.valueOf(rs.getString("TerminDatum")).toLocalDate();

                String terminText = rs.getString("TerminText");

                // Objekte werden erzeugt und der Terminliste hinzugefügt
                termin = new Termin(terminId, terminDatum, terminText);
                if(rs.getString("TerminZeit") != null) {
                    terminZeit = LocalDateTime.of(terminDatum, Time.valueOf(rs.getString("TerminZeit")).toLocalTime());
                    termin.setZeit(terminZeit);
                }
                if(rs.getString("TerminZeitBis") != null) {
                    terminZeitBis = LocalDateTime.of(terminDatum, Time.valueOf(rs.getString("TerminZeitBis")).toLocalTime());
                    termin.setZeitBis(terminZeitBis);
                }
                if(rs.getString("TerminOrt") != null){
                    termin.setOrt(rs.getString("TerminOrt"));
                }
                if(rs.getString("TerminDetails") != null){
                    termin.setDetails(rs.getString("TerminDetails"));
                }
                if(rs.getInt("TerminTeilnehmerKatA") >= 0) {
                    termin.setTeilnehmerKatI(kategorieIStatus.getStatusElemente()
                            .get(rs.getInt("TerminTeilnehmerKatA")));
                }
                if(rs.getInt("TerminTeilnehmerKatB") >= 0) {
                    termin.setTeilnehmerKatII(kategorieIIStatus.getStatusElemente()
                            .get(rs.getInt("TerminTeilnehmerKatB")));
                }
                if(rs.getString("TerminTrackChangeUsr") != null){
                    termin.setTrackChangeUsr(rs.getString("TerminTrackChangeUsr"));
                }
                if(rs.getString("TerminTrackChangeTimestamp") != null) {
                    termin.setTrackChangeTimestamp(rs.getTimestamp("TerminTrackChangeTimestamp"));
                }
                terminListe.add(termin);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return terminListe;
    }

    /**
     * Ermittelt anhand der Geburtstage und der Eintrittsdaten pro Mitglied
     * das nächste Geburtsdatum und das nächste Jubiläum.
     * Runde Geburtstage = Alter % 10 = 0 && Alter != 0
     * Jubiläum = Vereinszugehörigkeit % 5 = 0  && Vereinszugehörigkeit !=0
     * @return die Jubiläumsliste als ArrayList<Jubilaeum>
     */
    public static ArrayList<Jubilaeum> getJubilaeenAsArrayList() {
        int jahr = Year.now().getValue();
        // Geburtstage
        ArrayList<Jubilaeum> jubilaeumsListe = new ArrayList<>();
        try (Connection conn = new MysqlConnection().getConnection(); Statement st = conn.createStatement()) {
            String query = "SELECT KontaktId, KontaktGeburtsdatum, KontaktNachname, KontaktVorname " +
                    "FROM usr_web116_5.kontakt WHERE KontaktIstMitglied = 1 AND KontaktGeburtsdatum IS NOT NULL" +
                    " AND KontaktGeburtsdatum NOT LIKE '0000-%'";

            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                String geburtsDatum = rs.getString("KontaktGeburtsdatum");
                if (geburtsDatum != null) {
                    int geburtsJahr = Integer.parseInt(geburtsDatum.substring(0, 4));
                    // Alter in diesem Jahr
                    int alter = jahr - geburtsJahr;
                    geburtsDatum = jahr + geburtsDatum.substring(4, 10);
                    LocalDate geburtsDatumLD = Date.valueOf(geburtsDatum).toLocalDate();

                    // Wenn der nächste Geburtstag grösser ist als heute
                    if (geburtsDatumLD.isAfter(LocalDate.now().minusDays(1))) {
                        if (alter % 10 == 0 && alter > 0) {
                            jubilaeumsListe.add(new Jubilaeum(999, geburtsDatumLD
                                    , alter + ". Geburtstag von "
                                    + rs.getString("KontaktVorname")
                                    + " " + rs.getString("KontaktNachname")));
                        }
                    } else {
                        // nächster Geburtstag ist erst im nächsten Jahr
                        // dann ist das Mitglied ein Jahr älter..
                        if ((alter + 1) % 10 == 0 && (alter + 1) > 0) {
                            jubilaeumsListe.add(new Jubilaeum(999, geburtsDatumLD.plusYears(1)
                                    , (alter + 1) + ". Geburtstag von "
                                    + rs.getString("KontaktVorname") + " "
                                    + rs.getString("KontaktNachname")));
                        }
                    }
                }
            }

            // Vereinsmitgliedschafts-Jubiläen ermittelnt
            query = "SELECT KontaktId, KontaktEintrittsdatum, KontaktNachname, KontaktVorname FROM usr_web116_5.kontakt WHERE KontaktIstMitglied = 1 AND KontaktGeburtsdatum IS NOT NULL AND KontaktGeburtsdatum NOT LIKE '0000-%'";
            rs = st.executeQuery(query);
            int i = 20000;
            while (rs.next()) {
                String eintrittsDatum = rs.getString("KontaktEintrittsdatum");
                int eintrittsJahr = Integer.parseInt(eintrittsDatum.substring(0, 4));
                // Jubilaeum in diesem Jahr
                int anzahlJahre = jahr - eintrittsJahr;
                eintrittsDatum = jahr + eintrittsDatum.substring(4,10);

                LocalDate eintrittsDatumLD = Date.valueOf(eintrittsDatum).toLocalDate();

                // Wenn das nächste Jubiläumsdatum grösser ist als heute
                if(eintrittsDatumLD.isAfter(LocalDate.now().minusDays(1))) {
                    if (anzahlJahre % 5 == 0 && anzahlJahre > 0) {
                        jubilaeumsListe.add(new Jubilaeum(i, eintrittsDatumLD, "Jubiläum: " + anzahlJahre + " Jahr(e) " + rs.getString("KontaktVorname") + " " + rs.getString("KontaktNachname")));
                    }
                } else {
                    if ((anzahlJahre + 1) % 5 == 0 && (anzahlJahre + 1) > 0) {
                        // nächstes Jubilaeum ist erst im nächsten Jahr
                        jubilaeumsListe.add(new Jubilaeum(i, eintrittsDatumLD.plusYears(1), "Jubiläum: " + (anzahlJahre + 1) + " Jahr(e) " + rs.getString("KontaktVorname") + " " + rs.getString("KontaktNachname")));
                    }
                }
                i++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //Liste sortieren nach Datum...
        Collections.sort(jubilaeumsListe, (a, b) -> a.getDatum().compareTo(b.getDatum()));
        return jubilaeumsListe;
    }


    /**
     * ermittelt die Mitglied-ID aufgrund der übergebenen E-Mail-Adresse und gibt diese zurück
     *
     * @param eMail die E-Mail-Adresse des Mitglieds/Users
     * @return die MitgliederId aus der Datenbank
     */
    public static int getMitgliedId(String eMail) {
        try (Connection conn = new MysqlConnection().getConnection();
             Statement st = conn.createStatement()) {
            String query = "SELECT KontaktId FROM benutzer WHERE BenutzerName='" + eMail + "'";
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                return rs.getInt("KontaktId");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * ermittelt alle Termine für ein bestimmtes Datum und gibt diese als ArrayList<Termin> zurück
     *
     * @param datum das Termindatum
     * @return die Termine als ArrayList<Termin>
     */
    public static ArrayList<Termin> getTermineFromLocalDate(LocalDate datum) {
        Status kategorieIStatus = new Status(2);
        Status kategorieIIStatus = new Status(4);
        ArrayList<Termin> terminListe = new ArrayList<>();
        try (Connection conn = new MysqlConnection().getConnection(); Statement st = conn.createStatement()) {
            String query = "SELECT * from usr_web116_5.termin WHERE TerminDatum ='" + datum + "' ORDER BY TerminDatum ASC";
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                Termin termin;
                LocalDateTime terminZeit = null;
                LocalDateTime terminZeitBis = null;
                int terminId = rs.getInt("TerminId");
                LocalDate terminDatum = Date.valueOf(rs.getString("TerminDatum")).toLocalDate();

                String terminText = rs.getString("TerminText");

                // Objekte werden erzeugt und der Terminliste hinzugefügt
                termin = new Termin(terminId, terminDatum, terminText);
                if (rs.getString("TerminZeit") != null) {
                    terminZeit = LocalDateTime.of(terminDatum, Time.valueOf(rs.getString("TerminZeit"))
                            .toLocalTime());
                    termin.setZeit(terminZeit);
                }
                if (rs.getString("TerminZeitBis") != null) {
                    terminZeitBis = LocalDateTime.of(terminDatum, Time.valueOf(rs
                            .getString("TerminZeitBis")).toLocalTime());
                    termin.setZeitBis(terminZeitBis);
                }
                if (rs.getString("TerminOrt") != null) {
                    termin.setOrt(rs.getString("TerminOrt"));
                }
                if (rs.getString("TerminDetails") != null) {
                    termin.setDetails(rs.getString("TerminDetails"));
                }
                if (rs.getInt("TerminTeilnehmerKatA") >= 0) {
                    termin.setTeilnehmerKatI(kategorieIStatus.getStatusElemente().get(rs
                            .getInt("TerminTeilnehmerKatA")));
                }
                if (rs.getInt("TerminTeilnehmerKatB") >= 0) {
                    termin.setTeilnehmerKatII(kategorieIIStatus.getStatusElemente().get(rs
                            .getInt("TerminTeilnehmerKatB")));
                }
                if (rs.getString("TerminTrackChangeUsr") != null) {
                    termin.setTrackChangeUsr(rs.getString("TerminTrackChangeUsr"));
                }
                if (rs.getString("TerminTrackChangeTimestamp") != null) {
                    termin.setTrackChangeTimestamp(rs.getTimestamp("TerminTrackChangeTimestamp"));
                }
                terminListe.add(termin);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return terminListe;
    }

    /**
     * ermittelt die Tasks aus der Datenbank und gibt diese als ArrayList<Task> zurück
     *
     * @return die Taskliste als ArrayList<Task>
     */
    public static ArrayList<Task> getTaskList() {
        ArrayList<Task> taskListe = new ArrayList<>();
        Status prioStatus = new Status(7);
        Status statusStatus = new Status(8);

        try (Connection conn = new MysqlConnection().getConnection(); Statement st = conn.createStatement()) {
            String query = "SELECT * from usr_web116_5.task LEFT JOIN kontakt" +
                    " ON kontakt.KontaktId = task.TaskMitgliedId ORDER BY TaskTerminBis ASC";
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                int terminId = rs.getInt("TaskId");
                Mitglied mitglied = new Mitglied(rs.getInt("KontaktId"),
                        rs.getString("KontaktNachname"), rs.getString("KontaktVorname"));
                DatabaseReader.completeMitglied(mitglied);

                // Objekte werden erzeugt und der Taskliste hinzugefügt
                Task task = new Task(rs.getInt("TaskId"),
                        rs.getString("TaskBezeichnung"),
                        rs.getString("TaskDetails"),
                        mitglied,
                        rs.getDate("TaskTerminBis").toLocalDate());
                task.setPrioStatus(prioStatus.getStatusElemente().get(rs.getInt("TaskPrio")));
                task.setStatusStatus(statusStatus.getStatusElemente().get(rs.getInt("TaskStatus")));
                taskListe.add(task);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return taskListe;
    }

    /**
     * ermittelt die Anzahl der pendenten/unerledigten Task aus der Datenbank und gibt diese als int zurück
     *
     * @return die Anzahl der pendenten Tasks als int
     */
    public static int readAnzahlOpenTasks() {
        int anzTasks = 0;
        try (Connection conn = new MysqlConnection().getConnection(); Statement st = conn.createStatement()) {
            String query = "SELECT COUNT(*) AS ANZTASKS from usr_web116_5.task WHERE TaskStatus < 3";
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                anzTasks = rs.getInt("ANZTASKS");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return anzTasks;
    }

    /**
     * ermittelt die Anzahl der Tasks aus der Datenbank und gibt dies als int zurück.
     *
     * @return die Anzahl der Tasks als int
     */
    public static int readTotalAnzahlTasks() {
        int anzTasks = 0;
        try (Connection conn = new MysqlConnection().getConnection(); Statement st = conn.createStatement()) {
            String query = "SELECT COUNT(*) AS ANZTASKS from usr_web116_5.task";
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                anzTasks = rs.getInt("ANZTASKS");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return anzTasks;
    }

    /**
     * ermittelt die Anzahl der Benutzer aus der Datenbank und gibt diese als int zurück
     *
     * @return die Anzahl Benutzer als int
     */
    public static int readAnzahlBenutzer() {
        int anzBenutzer = 0;
        try (Connection conn = new MysqlConnection().getConnection(); Statement st = conn.createStatement()) {
            String query = "SELECT COUNT(*) AS ANZUSERS from usr_web116_5.benutzer";
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                anzBenutzer = rs.getInt("ANZUSERS");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return anzBenutzer;
    }

    /**
     * ermittelt den Timestamp der letzten Änderung in der Benutzer-Tabelle und gibt diesen
     * als Timestamp zurück
     *
     * @return den Timestamp der letzten Änderung in der Benutzer-Tabelle
     */
    public static Timestamp readLetzteBenutzerAenderung() {
        try (Connection conn = new MysqlConnection().getConnection();
             Statement st = conn.createStatement()) {
            String query = "SELECT MAX(BenutzerTrackChangeTimestamp) AS LetzteAenderung FROM usr_web116_5.benutzer";
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                return Timestamp.valueOf(rs.getString("LetzteAenderung"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * ermittelt die Mitgliederkategorie zur übergebenen MitgliedId und gibt diese als
     * String zurück
     *
     * @param mitgliedId die Id des Mitglieds
     * @return die Mitgliederkategorie als String
     */
    public static String getUserKatString(int mitgliedId) {
        String userKat = null;
        try (Connection conn = new MysqlConnection().getConnection(); Statement st = conn.createStatement()) {
            String query = "SELECT statusElement.StatusElementNameLong AS Kat " +
                    "FROM kontakt LEFT JOIN statusElement ON kontakt.KontaktKategorieA = statusElement.StatusElementKey " +
                    "WHERE statusElement.StatusId = 2 AND kontakt.KontaktId = " + mitgliedId;
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                userKat = rs.getString("Kat");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userKat;
    }

    /**
     * ermittelt den Mitgliedernamen der übergebenen MitgliederId und gibt den Namen als String zurück
     *
     * @param mitgliedId die Id des übergebenen Mitglieds
     * @return der Name des Mitglieds als String
     */
    public static String getMitgliedName(int mitgliedId) {
        String mitgliedName = null;
        try (Connection conn = new MysqlConnection().getConnection(); Statement st = conn.createStatement()) {
            String query = "SELECT * FROM kontakt WHERE KontaktID = " + mitgliedId;
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                mitgliedName = rs.getString("KontaktNachname")
                        + " " + rs.getString("KontaktVorname");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mitgliedName;
    }


}
