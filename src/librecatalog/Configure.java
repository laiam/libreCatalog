/*
 * Name:       Team Innovation
 * Course:     CS225
 * Program:    Project Library
 * Problem:    Create a system for storing library books and patrons, provide methods
 *             for checking out books, and other library related tasks.
 * Class:      Configure
 */
package librecatalog;

import java.awt.GraphicsEnvironment;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import javax.swing.*;

/**
 * The configuration storage class.
 * Class is in final stage. checking for bugs.
 * @author van
 */
public class Configure
{
    static LinkedList<Setting> settings = new LinkedList<Setting>();
    static FileOps<Setting> SettingDB;
    
    static void main(String[] args)
    {
        boolean nogui=false;
        String filename = "config.properties";
        if (args.length>0)
            for (int idx = 0; idx < args.length;idx++)
                if (args[idx].startsWith("--config")) {
                    filename = args[idx].split("=")[1];
                } else if (args[idx].equals("--no-gui"))
                    nogui = true;
        String path = getPath(filename);
        SettingDB = new FileOps<Setting>(path);
        SettingDB.load(settings);
        if (settings.size()==0)
            loadDefaults();
        System.out.println(settings.size() + " settings loaded.");
        if (nogui || GraphicsEnvironment.isHeadless())
            addSetting("no-gui","true");
        else
            addSetting("no-gui","false");
    }
    
    static void unload() {
        SettingDB.save(settings);
    }
    
    static void loadDefaults() {
        addSetting("first-run","true");
        addSetting("PatronDB","Patrons.dbflat");
        addSetting("ItemDB","Items.dbflat");
        addSetting("FineDB","Fines.dbflat");
        addSetting("AvailabilityDB","ItemAvailability.dbflat");
        addSetting("Fine", ".10");
        addSetting("AgeRestricted", "18");
        addSetting("library","0061");
        unload();
    }
    
    static void addSetting (String key, String token) {
        Setting temp;
        Iterator conf = settings.iterator();
        while (conf.hasNext()) {
            temp = (Setting) conf.next();
            if (temp.getKey().equals(key)) {
                settings.remove(temp);
                break;
            }
        }
        settings.add(new Setting(key, token));
    }
    
    static String getSetting (String key) {
        Setting temp;
        Iterator conf = settings.iterator();
        while (conf.hasNext()) {
            temp = (Setting) conf.next();
            if (temp.getKey().equals(key)) {
                return temp.getToken();
            }
        }
        return "";
    }
    
    static boolean removeSetting (String key) {
        Setting temp;
        Iterator conf = settings.iterator();
        while (conf.hasNext()) {
            temp = (Setting) conf.next();
            if (temp.getKey().equals(key)) {
                return settings.remove(temp);
            }
        }
        return false;
    }
    
    /**
     * Get an absolute path for a file.
     * Calculate an absolute path relative to the location of the .jar or class
     * files.
     * 
     * @param filename the name of the file to access absolutely.
     * @return a full absolute path.
     */
    static String getPath(String filename)
    {
        String path = "";
        if (
                   !filename.startsWith("/")
                && !filename.startsWith(".")
                && !filename.startsWith("file: ")
                && !filename.startsWith(":\\",1)
                ||  filename.startsWith("..") // true or true
            ) {
            path = Main.class.getProtectionDomain().getCodeSource().getLocation().toString();
            path = path.substring(path.indexOf("/"));
            if (path.endsWith(".jar"))
            {
                int lastSlash = path.lastIndexOf("/");
                System.out.println(lastSlash);
                path = path.substring(0, lastSlash);
                System.out.println(path);
            }
            //harden the files by moving them to the project folder
            //to prevent removal on recompile....
            if (path.contains("dist") ) {
                path = path.split("dist")[0];
            }
            if (path.contains("build") ) {
                path = path.split("build")[0];
            }
            if (!path.endsWith("/"))
                path += "/";
            if (path.contains(":"))
                path = path.substring(1);
            path = path.replaceAll("%20", " ");
        }
                
        path+=filename;
        return path;
    }

    static JPanel configPanel()
    {
        return new ConfigPanel();
    }
    
    static class ConfigPanel extends JPanel {
        
        JPanel config;
        //db file names
        JLabel patronFileLabel = new JLabel("Patron File: ", JLabel.TRAILING);
        JLabel itemsFileLabel = new JLabel("Items File: ", JLabel.TRAILING);
        JLabel availFileLabel = new JLabel("Availability File: ", JLabel.TRAILING);
        JLabel finesFileLabel = new JLabel("Fines File: ", JLabel.TRAILING);
        JTextField patronFile = new JTextField(Configure.getSetting("PatronDB"),50);
        JTextField itemsFile = new JTextField(Configure.getSetting("ItemsDB"),50);
        JTextField availFile = new JTextField(Configure.getSetting("AvailabilityDB"),50);
        JTextField finesFile = new JTextField(Configure.getSetting("FineDB"),50);
        
        //fine per day
        JLabel finePerDayLabel = new JLabel("Overdue Fine Per Day: ", JLabel.TRAILING);
        JTextField finePerDay = new JTextField(Configure.getSetting("Fine"),4);
        
        //age restriction
        JLabel ageRestrictLabel = new JLabel("Age Restriction: ", JLabel.TRAILING);
        JTextField ageRestriction = new JTextField(Configure.getSetting("AgeRestricted"),2);
        
        //buttons
        JButton saveChanges = new JButton();
        JButton reset = new JButton();
        
        SpringLayout layout = new SpringLayout();
        
        ConfigPanel() {
            config.setLayout(layout);
            config.add(patronFileLabel);
            config.add(patronFile);
            config.add(itemsFileLabel);
            config.add(itemsFile);
            config.add(availFileLabel);
            config.add(availFile);
            config.add(finesFileLabel);
            config.add(finesFile);
            config.add(finePerDayLabel);
            config.add(finePerDay);
            config.add(ageRestrictLabel);
            config.add(ageRestriction);
            
            saveChanges.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    addSetting("PatronDB",patronFile.getText());
                    addSetting("ItemDB",itemsFile.getText());
                    addSetting("FineDB",finesFile.getText());
                    addSetting("AvailabilityDB",availFile.getText());
                    addSetting("Fine", finePerDay.getText());
                    addSetting("AgeRestricted", ageRestriction.getText());
                }
            });
            reset.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    patronFile.setText(getSetting("PatronDB"));
                    itemsFile.setText(getSetting("ItemDB"));
                    finesFile.setText(getSetting("FineDB"));
                    availFile.setText(getSetting("AvailabilityDB"));
                    finePerDay.setText(getSetting("Fine"));
                    ageRestriction.setText(getSetting("AgeRestricted"));
                }
            });
            
            config.add(saveChanges);
        }
        
        
    }
    
    
    static class Setting implements Serializable {
        private String key;
        private String token;

        Setting () {
            key = "";
            token = "";
        }

        public Setting (String key, String token) {
            this.key = key;
            this.token = token;
        }

        public void getToken(String newToken) {
            token = newToken;
        }

        public String getKey() {
            return key;
        }
        public String getToken() {
            return token;
        }
    }
}