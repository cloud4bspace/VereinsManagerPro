package space.cloud4b.verein.einstellungen;

import java.io.FileInputStream;
import java.util.Properties;

public abstract class Einstellung {


    public static String getdbURL() {
        try {
            // Load Settings
            Properties loadProps = new Properties();
            loadProps.loadFromXML(new FileInputStream("../VereinsManager/src/space/cloud4b/verein/einstellungen/Systemeinstellungen.xml"));
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
            loadProps.loadFromXML(new FileInputStream("../VereinsManager/src/space/cloud4b/verein/einstellungen/Systemeinstellungen.xml"));
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
            loadProps.loadFromXML(new FileInputStream(
                    "../VereinsManager/src/space/cloud4b/verein/einstellungen/Systemeinstellungen.xml"));
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
            // Load Settings
            Properties loadProps = new Properties();
            loadProps.loadFromXML(new FileInputStream(
                    "../VereinsManager/src/space/cloud4b/verein/einstellungen/Mandanteneinstellungen.xml"));
            String propValue = loadProps.getProperty("VereinsName");
            return propValue;
        }
        catch (Exception e ) {
            return "Verein ohne Namen";
            //e.printStackTrace();
        }


    }

    public static String getdbHost() {
        try {
            // Load Settings
            Properties loadProps = new Properties();
            loadProps.loadFromXML(new FileInputStream(
                    "../VereinsManager/src/space/cloud4b/verein/einstellungen/Systemeinstellungen.xml"));
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
            loadProps.loadFromXML(new FileInputStream(
                    "../VereinsManager/src/space/cloud4b/verein/einstellungen/Systemeinstellungen.xml"));
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
            loadProps.loadFromXML(new FileInputStream(
                    "../VereinsManager/src/space/cloud4b/verein/einstellungen/Systemeinstellungen.xml"));
            String propValue = loadProps.getProperty("dbDatabase");
            return propValue;
        } catch (Exception e) {
            System.out.println("dbDatenbank konnte nicht ermittelt werden.");
            e.printStackTrace();
        }
        return null;
    }
}
