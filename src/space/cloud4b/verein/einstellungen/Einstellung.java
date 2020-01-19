package space.cloud4b.verein.einstellungen;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

/**
 * Die abstrakte Klasse Einstellung biete Methode, um die Mandanteneinstellungen im XML-File zu lesen und zu
 * bewirtschaften.
 * File: VereinsManager/src/space/cloud4b/verein/einstellungen/Mandanteneinstellungen.xml
 *
 * @author Bernhard Kämpf und Serge Kaulitz
 * @version 2019-12-17
 */
public abstract class Einstellung {

    /**
     * Liest die URL der Datenbank in den Mandateneinstellungen und gibt diese
     * als Strin gzurück
     *
     * @return die Datenbank-URL als String
     */
    public static String getdbURL() {
        try {
            // Load Settings
            Properties loadProps = new Properties();
            loadProps.loadFromXML(new FileInputStream(
                //    "../VereinsManager/src/space/cloud4b/verein/einstellungen/Mandanteneinstellungen.xml"));
            "src/space/cloud4b/verein/einstellungen/Mandanteneinstellungen.xml"));
            return loadProps.getProperty("dbURL");
        } catch (Exception e) {
            System.out.println("dbURL konnte nicht ermittelt werden.");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Liest das Passwort für den Datenbank-Zugriff in den Mandateneinstellungen und gibt diese als String zurück
     *
     * @return die Datenbank-URL als String
     */
    public static String getdbPW() {
        try {
            // Load Settings
            Properties loadProps = new Properties();
            loadProps.loadFromXML(new FileInputStream(
                    "src/space/cloud4b/verein/einstellungen/Mandanteneinstellungen.xml"));
            return loadProps.getProperty("mysqlPW");
        } catch (Exception e) {
            System.out.println("dbPW konnte nicht ermittelt werden.");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Liest den User für den Datenbank-Zugriff in den Mandateneinstellungen und gibt diesen als String zurück
     *
     * @return die Datenbank-URL als String
     */
    public static String getdbUser() {
        try {
            // Load Settings
            Properties loadProps = new Properties();
            loadProps.loadFromXML(new FileInputStream(
                    "src/space/cloud4b/verein/einstellungen/Mandanteneinstellungen.xml"));
            return loadProps.getProperty("dbUser");
        } catch (Exception e) {
            System.out.println("dbPW konnte nicht ermittelt werden.");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Liest den Vereinsnamen den Mandateneinstellungen und gibt diesen als String zurück
     *
     * @return den Vereinsnamen
     */
    public static String getVereinsName() {
        try {
            Properties loadProps = new Properties();
            loadProps.loadFromXML(new FileInputStream(
                   // "../VereinsManager/src/space/cloud4b/verein/einstellungen/Mandanteneinstellungen.xml"));
                    "src/space/cloud4b/verein/einstellungen/Mandanteneinstellungen.xml"));
            return loadProps.getProperty("VereinsName");
        } catch (IOException e) {
            return "Verein ohne Namen";
        }
    }

    /**
     * speichert die übergebenen Daten in den Mandanteneinstellungen
     * File-Pfad: (../VereinsManager/src/space/cloud4b/verein/einstellungen/Mandanteneinstellungen.xml)
     *
     * @param vereinsName der Name des Verein
     * @param dbHost      Host der MYSQL-Datenbank
     * @param dbPort      Port für den DB-Zugriff
     * @param dbDatabase  die Datenbank
     * @param dbUser      der User für den DB-Zugriff
     * @param mysqlPW     das Passwort für den DB-Zugriff
     */
    public static void setProperties(String vereinsName, String dbHost, String dbPort, String dbDatabase,
                                     String dbUser, String mysqlPW) {
        OutputStream os;
        Properties prop = new Properties();
        prop.setProperty("VereinsName", vereinsName);
        prop.setProperty("dbHost", dbHost);
        prop.setProperty("dbPort", dbPort);
        prop.setProperty("dbDatabase", dbDatabase);
        prop.setProperty("dbUser", dbUser);
        prop.setProperty("mysqlPW", mysqlPW);
        String dbUrl = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbDatabase
                + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
        prop.setProperty("dbURL", dbUrl);
        try {
            os = new FileOutputStream(
                    "src/space/cloud4b/verein/einstellungen/Mandanteneinstellungen.xml");
            prop.storeToXML(os, "Update durch Methode setVereinsName");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Liest den Datenbankhost aus den Mandanteneinstellungen und gibt diesen als String zurück
     *
     * @return der Host für den DB-Zugang
     */
    public static String getdbHost() {
        try {
            // Load Settings
            Properties loadProps = new Properties();
            loadProps.loadFromXML(new FileInputStream(
                    "src/space/cloud4b/verein/einstellungen/Mandanteneinstellungen.xml"));
            return loadProps.getProperty("dbHost");
        } catch (Exception e) {
            System.out.println("dbHost konnte nicht ermittelt werden.");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Liest den Datenbank-Port aus den Mandanteneinstellungen und gibt diesen als String zurück
     *
     * @return der Port für den DB-Zugang
     */
    public static String getdbPort() {
        try {
            // Load Settings
            Properties loadProps = new Properties();
            loadProps.loadFromXML(new FileInputStream(
                    "src/space/cloud4b/verein/einstellungen/Mandanteneinstellungen.xml"));
            return loadProps.getProperty("dbPort");
        } catch (Exception e) {
            System.out.println("dbPort konnte nicht ermittelt werden.");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Liest die Datenbank aus den Mandanteneinstellungen und gibt diese als String zurück
     *
     * @return Datenbank für den MYSQL-Zugriff
     */
    public static String getdbDatenbank() {
        try {
            // Load Settings
            Properties loadProps = new Properties();
            loadProps.loadFromXML(new FileInputStream(
                    "src/space/cloud4b/verein/einstellungen/Mandanteneinstellungen.xml"));
            return loadProps.getProperty("dbDatabase");
        } catch (Exception e) {
            System.out.println("dbDatenbank konnte nicht ermittelt werden.");
            e.printStackTrace();
        }
        return null;
    }
}
