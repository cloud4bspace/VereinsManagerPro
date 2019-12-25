package space.cloud4b.verein.einstellungen;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

public abstract class Einstellung {


    public static String getdbURL() {
        try {
            // Load Settings
            Properties loadProps = new Properties();
            //  loadProps.loadFromXML(new FileInputStream("../VereinsManager/src/space/cloud4b/verein/einstellungen/Systemeinstellungen.xml"));
            loadProps.loadFromXML(new FileInputStream(
                    "../VereinsManager/src/space/cloud4b/verein/einstellungen/Mandanteneinstellungen.xml"));
            String propValue = loadProps.getProperty("dbURL");
            return propValue;
        }
        catch (Exception e ) {
            System.out.println("dbURL konnte nicht ermittelt werden.");
            e.printStackTrace();
        }
        return null;
    }

    public static String getdbPW() {
        try {
            // Load Settings
            Properties loadProps = new Properties();
            // loadProps.loadFromXML(new FileInputStream("../VereinsManager/src/space/cloud4b/verein/einstellungen/Systemeinstellungen.xml"));
            loadProps.loadFromXML(new FileInputStream(
                    "../VereinsManager/src/space/cloud4b/verein/einstellungen/Mandanteneinstellungen.xml"));
            String propValue = loadProps.getProperty("mysqlPW");
            return propValue;
        } catch (Exception e) {
            System.out.println("dbPW konnte nicht ermittelt werden.");
            e.printStackTrace();
        }
        return null;
    }

    public static String getdbUser() {
        try {
            // Load Settings
            Properties loadProps = new Properties();
            // loadProps.loadFromXML(new FileInputStream(
            //        "../VereinsManager/src/space/cloud4b/verein/einstellungen/Systemeinstellungen.xml"));
            loadProps.loadFromXML(new FileInputStream(
                    "../VereinsManager/src/space/cloud4b/verein/einstellungen/Mandanteneinstellungen.xml"));
            String propValue = loadProps.getProperty("dbUser");
            return propValue;
        } catch (Exception e) {
            System.out.println("dbPW konnte nicht ermittelt werden.");
            e.printStackTrace();
        }
        return null;
    }

    public static String getVereinsName() {
        try {
            Properties loadProps = new Properties();
            loadProps.loadFromXML(new FileInputStream(
                    "../VereinsManager/src/space/cloud4b/verein/einstellungen/Mandanteneinstellungen.xml"));
            String propValue = loadProps.getProperty("VereinsName");
            return propValue;
        } catch (IOException e) {
            return "Verein ohne Namen";
        }
    }

    public static void setProperties(String vereinsName, String dbHost, String dbPort, String dbDatabase,
                                     String dbUser, String mysqlPW) {

//    <entry key="dbURL">
        OutputStream os = null;
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
                    "../VereinsManager/src/space/cloud4b/verein/einstellungen/Mandanteneinstellungen.xml");
            prop.storeToXML(os, "Update durch Methode setVereinsName");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getdbHost() {
        try {
            // Load Settings
            Properties loadProps = new Properties();
            //  loadProps.loadFromXML(new FileInputStream(
            //     "../VereinsManager/src/space/cloud4b/verein/einstellungen/Systemeinstellungen.xml"));
            loadProps.loadFromXML(new FileInputStream(
                    "../VereinsManager/src/space/cloud4b/verein/einstellungen/Mandanteneinstellungen.xml"));
            String propValue = loadProps.getProperty("dbHost");
            return propValue;
        } catch (Exception e) {
            System.out.println("dbHost konnte nicht ermittelt werden.");
            e.printStackTrace();
        }
        return null;
    }

    public static String getdbPort() {
        try {
            // Load Settings
            Properties loadProps = new Properties();
            // loadProps.loadFromXML(new FileInputStream(
            //       "../VereinsManager/src/space/cloud4b/verein/einstellungen/Systemeinstellungen.xml"));
            loadProps.loadFromXML(new FileInputStream(
                    "../VereinsManager/src/space/cloud4b/verein/einstellungen/Mandanteneinstellungen.xml"));
            String propValue = loadProps.getProperty("dbPort");
            return propValue;
        } catch (Exception e) {
            System.out.println("dbPort konnte nicht ermittelt werden.");
            e.printStackTrace();
        }
        return null;
    }

    public static String getdbDatenbank() {
        try {
            // Load Settings
            Properties loadProps = new Properties();
            // loadProps.loadFromXML(new FileInputStream(
            //        "../VereinsManager/src/space/cloud4b/verein/einstellungen/Systemeinstellungen.xml"));
            loadProps.loadFromXML(new FileInputStream(
                    "../VereinsManager/src/space/cloud4b/verein/einstellungen/Mandanteneinstellungen.xml"));
            String propValue = loadProps.getProperty("dbDatabase");
            return propValue;
        } catch (Exception e) {
            System.out.println("dbDatenbank konnte nicht ermittelt werden.");
            e.printStackTrace();
        }
        return null;
    }
}
